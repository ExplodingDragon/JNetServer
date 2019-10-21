package top.fksoft.server.http.thread

import jdkUtils.logcat.Logger
import top.fksoft.bean.NetworkInfo
import top.fksoft.server.http.HttpServer
import top.fksoft.server.http.config.ServerConfig
import java.io.Closeable
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketException
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *
 * HTTP 处理线程
 *
 * 处理每一条远程 TCP 连接并建立通道
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 * @see java.io.Closeable
 *
 * @see java.lang.Runnable
 */
class SocketListenerRunnable @Throws(IOException::class)

constructor(private val httpServer: HttpServer, private val serverSocket: ServerSocket) : Runnable, Closeable {

    @Volatile
    var accept: Boolean = false



    private val serverConfig: ServerConfig = httpServer.serverConfig
    private val cacheThreadPool: ExecutorService
    private val logger = Logger.getLogger(SocketListenerRunnable::class)

    init {
        if (serverSocket.isClosed || !serverSocket.isBound) {
            throw IOException("套接字错误！")
        }
        val timeout = (serverConfig.socketTimeout * 4).run { toLong() }
        cacheThreadPool = ThreadPoolExecutor(0, Integer.MAX_VALUE,
                if (timeout == 0L) Long.MAX_VALUE else timeout,
                TimeUnit.MILLISECONDS,
                SynchronousQueue())
    }

    override fun run() {
        logger.info("HTTP 服务器启动正常，绑定端口为：${serverSocket.localPort} .")
        while (!serverSocket.isClosed) {
            var remoteInfo : NetworkInfo? = null
            try {
                val client = serverSocket.accept()
                val remote = client.remoteSocketAddress as InetSocketAddress
                val remoteAddress = remote.address.hostAddress
                val remotePort = remote.port
                remoteInfo = NetworkInfo(remoteAddress, remotePort)
                remoteInfo.hostName = remote.hostName
                // 得到远程服务器信息
                val remoteUrl = "tcp${if (remoteInfo.isIpv6Host()) 6 else 4}://$remoteInfo/"
                logger.info(remoteUrl)
                client.soTimeout = serverConfig.socketTimeout
                if (accept) {
                    cacheThreadPool.execute(ClientAcceptRunnable(httpServer, client, remoteInfo))
                }else{
                    logger.info("服务器处于关闭状态，$remoteUrl 已被丢弃")
                    client.close()
                }
            } catch (e: Exception) {
                if (e !is SocketException || !e.message!!.contains("closed")) {
                    logger.error("在处理 $remoteInfo 的过程中出现异常.", e)
                }
            }
        }
    }

    /**
     * 关闭Server
     *
     * 不应该手动调用此方法，应该调用 HttpServer#close()
     * 来关闭
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    override fun close() {
        serverSocket.close()
    }


}
