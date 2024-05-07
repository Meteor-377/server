package nulled

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.apollo.ServerContext
import org.apollo.ServiceManager
import org.apollo.cache.IndexedFileSystem
import org.apollo.game.model.World
import org.apollo.game.release.r377.Release377
import org.apollo.game.session.ApolloHandler
import org.apollo.net.HttpChannelInitializer
import org.apollo.net.JagGrabChannelInitializer
import org.apollo.net.NetworkConstants
import org.apollo.net.ServiceChannelInitializer
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.file.Paths
import java.util.logging.Level
import java.util.logging.Logger

object Main {
    val version = "377"
    private val httpBootstrap: ServerBootstrap = ServerBootstrap()
    private val jaggrabBootstrap: ServerBootstrap = ServerBootstrap()
    private val loopGroup: EventLoopGroup = NioEventLoopGroup()
    private val logger: Logger = Logger.getLogger(Main::class.java.getName())

    /**
     * The [ServerBootstrap] for the service listener.
     */
    private val serviceBootstrap: ServerBootstrap = ServerBootstrap()
    @JvmStatic
    fun main(args: Array<String>) {
        init()
        val service: SocketAddress = InetSocketAddress(NetworkConstants.SERVICE_PORT)
        val http: SocketAddress = InetSocketAddress(NetworkConstants.HTTP_PORT)
        val jaggrab: SocketAddress = InetSocketAddress(NetworkConstants.JAGGRAB_PORT)

        bind(service, http, jaggrab)
    }

    fun init() {
        serviceBootstrap.group(loopGroup)
        httpBootstrap.group(loopGroup)
        jaggrabBootstrap.group(loopGroup)

        val world = World()
        val services = ServiceManager(world)
        val fs = IndexedFileSystem(Paths.get("data/fs", version), true)
        val context = ServerContext(Release377(), services, fs)
        val handler = ApolloHandler(context)

        val service: ChannelInitializer<io.netty.channel.socket.SocketChannel> = ServiceChannelInitializer(handler)
        serviceBootstrap.channel(NioServerSocketChannel::class.java)
        serviceBootstrap.childHandler(service)

        val http: ChannelInitializer<io.netty.channel.socket.SocketChannel> = HttpChannelInitializer(handler)
        httpBootstrap.channel(NioServerSocketChannel::class.java)
        httpBootstrap.childHandler(http)

        val jaggrab: ChannelInitializer<io.netty.channel.socket.SocketChannel> = JagGrabChannelInitializer(handler)
        jaggrabBootstrap.channel(NioServerSocketChannel::class.java)
        jaggrabBootstrap.childHandler(jaggrab)

        services.startAll()

        world.init(fs)
    }

    @Throws(IOException::class)
    fun bind(service: SocketAddress, http: SocketAddress, jaggrab: SocketAddress) {
        logger.fine("Binding service listener to address: $service...")
        bind(serviceBootstrap, service)

        try {
            logger.fine("Binding HTTP listener to address: $http...")
            bind(httpBootstrap, http)
        } catch (cause: IOException) {
            logger.log(
                Level.WARNING,
                "Unable to bind to HTTP - JAGGRAB will be used as a fallback.",
                cause
            )
        }

        logger.fine("Binding JAGGRAB listener to address: $jaggrab...")
        bind(jaggrabBootstrap, jaggrab)

        logger.info("Ready for connections.")
    }

    @Throws(IOException::class)
    private fun bind(bootstrap: ServerBootstrap, address: SocketAddress) {
        try {
            bootstrap.bind(address).sync()
        } catch (cause: Exception) {
            throw IOException("Failed to bind to $address", cause)
        }
    }
}