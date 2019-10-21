package top.fksoft.server.http

import jdkUtils.logcat.Logger
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.server.factory.FindHttpServletFactory
import top.fksoft.server.http.server.factory.HeaderReaderFactory
import top.fksoft.server.http.thread.SocketListenerRunnable
import java.io.Closeable
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.ThreadFactory
import javax.net.ServerSocketFactory
import kotlin.reflect.KClass

/**
 * @author ExplodingDragon
 *
 * @version 1.0
 */
class HttpServer

/**
 *
 * 将 HttpServer通过自定义工厂类绑定到指定端口
 *
 *
 * 有时候需要对httpServer做一些特殊操作，
 * 例如积压 ... 则可通过此构造方法进行初始化参数
 *
 *
 * @param port 绑定的端口
 * @param factory 工厂类
 * @throws IOException 如果发生绑定错误
 */
@Throws(IOException::class)
@JvmOverloads constructor(private val serverPort:Int,private val factory: ServerSocketFactory = ServerSocketFactory.getDefault()) : Closeable {

    /**
     *
     * 服务器的所有配置信息,
     *
     */
    val serverConfig: ServerConfig = ServerConfig(serverPort)
    /**
     * 服务器路径查询方法
     */
    val findHttpServletFactory =  FindHttpServletFactory.getDefault(serverConfig)

    /**
     * 解析 HTTP请求头的处理类
     */
    var httpHeaderReader: KClass<out HeaderReaderFactory> = HeaderReaderFactory.getDefault()


    private val logger = Logger.getLogger(HttpServer::class)
    private val serverSocket = factory.createServerSocket(serverConfig.serverPort) ?: throw SocketException("端口无法绑定的问题")
    private val runnable = SocketListenerRunnable(this, serverSocket)
    private val httpThread: Thread = HttpThreadFactory().newThread(runnable)
    init {
        if (!serverConfig.init()) {
            throw IOException("在初始化过程发生问题.")
        }
        httpThread.start()
    }

    fun start() {
        runnable.accept = true
    }
    fun pause() {
        runnable.accept = false
    }


    private inner class HttpThreadFactory : ThreadFactory {
        override fun newThread(r: Runnable): Thread {
            val thread = Thread(r, "AcceptThread")
            thread.priority = Thread.MAX_PRIORITY
            thread.setUncaughtExceptionHandler { t, e ->
                Logger.getLogger(r)
                        .error("[ ${t.name} ] 发生未捕获的错误，可将错误信息通过issues告诉开发者!", e)
            }
            return thread
        }
    }

    override fun close() {
        serverSocket.close()
        runnable.close()
        logger.info("销毁服务器完成！")
    }


}
