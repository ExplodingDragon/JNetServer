package top.fksoft.simple.http.thread.base

import jdkUtils.logcat.Logger
import top.fksoft.bean.NetworkInfo
import top.fksoft.simple.http.HttpServer
import top.fksoft.simple.http.config.ServerConfig
import java.io.Closeable
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.ConcurrentLinkedQueue

/**
 *
 * HTTP 異步處理客戶端發送的請求
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class BaseClientRunnable(protected val httpServer: HttpServer, protected val client: Socket, val remoteAddress: NetworkInfo)
    : Runnable, Closeable {
    private val closeableList = ConcurrentLinkedQueue<Closeable>()

    private val logger = Logger.getLogger(BaseClientRunnable::class)

    protected val serverConfig: ServerConfig = httpServer.serverConfig


    override fun run() {
        try {
            execute()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> logger.warn("$remoteAddress 发送未知请求，已强制断开！")
                is SocketException -> logger.warn("$remoteAddress 远程主机强制断开连接！", e)
                else -> logger.warn("在处理来自%s的Http请求中发生错误.", e, remoteAddress)
            }
        }

        try {
            //销毁
            this.close()
        } catch (e: Exception) {
            logger.warn("在销毁来自%s的Http请求中发生错误.", e, remoteAddress)
        }

    }

    /**
     *
     * 从TCP 连接中读取 http 头信息
     *
     *
     * @throws Exception 如果发生异常直接抛出，自动销毁实例
     */
    @Throws(Exception::class)
    protected abstract fun execute()

    @Throws(Exception::class)
    override fun close() {
        closeableList.add(client)
        synchronized(closeableList) {
            closeableList.forEach {
                try {
                    it.close()
                } catch (e: Exception) {
                    logger.debug("关闭${it.javaClass.name} 时错误！.", e)
                }
            }
        }
        closeableList.clear()
        logger.debug("已完全关闭$remoteAddress 的连接.")
    }


    /**
     * #绑定一个自动销毁事件
     *
     * 针对一些继承于 ``` java.io.Closeable``` 的类，
     * 需要在此连接完成后进行销毁，保证程序的健壮性，可通过此方法进行反向
     * 遍历销毁
     *
     * @param args T
     * @return T
     */
    fun <T : Closeable> bindAutoCloseable(args: T): T {
        synchronized(closeableList) {
            closeableList.add(args)
        }
        return args
    }

    fun <T : Closeable> bindAutoCloseables(vararg args: T) {
        synchronized(closeableList) {
            closeableList.addAll(args)
        }
    }

}
