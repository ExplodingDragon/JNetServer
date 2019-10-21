package top.fksoft.server.http.config

import jdkUtils.logcat.Logger
import top.fksoft.server.http.config.HttpConstant.PROPERTIES_KEY.SERVER_PORT
import top.fksoft.server.http.servlet.BaseHttpServlet
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder
import top.fksoft.server.http.servlet.binder.impl.HttpServletBinder
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 *
 * simple 的配置文件
 *
 *
 * 在此 http simple 实例中，所有配置都会保存在此
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class ServerConfig(val serverPort :Int): Closeable {


    private val logger = Logger.getLogger(ServerConfig::class)
    private val httpPropertiesMap = ConcurrentHashMap<String, String>()
    val httpServletMap = ConcurrentHashMap<String, BaseHttpServletBinder>()



    /**
     *
     * HTTP 连接的维持时间
     *
     *
     *
     * 此时长为http 上游 tcp 维持的时间，保证在定长时间内断开连接，
     * 注意，此 HTTP simple 在此为遵循 标准HTTP ，在此，一条TCP连接
     * 只维护一次HTTP 交互，交互完成后 TCP 连接立即断开。
     *
     * @return 维持的时间，单位： ms
     */
    var socketTimeout:Int = 3000

    var tempDirectory = File(HttpConstant.PROPERTY_SYSTEM_TEMP_DIR, HttpConstant.LIB_NAME)
    var workDirectory = tempDirectory


    init {
        addHttpProperty(SERVER_PORT, serverPort.toString())
        //添加系统属性：端口号
    }

    @Throws(IOException::class)
    fun init(): Boolean {
        logger.debug("临时文件夹:[${tempDirectory.absolutePath}]")
        if (!tempDirectory.isDirectory) {
            tempDirectory.delete()
            if (!tempDirectory.mkdirs()) {
                logger.error("无法建立临时文件夹.")
                return false
            }else{
                logger.info("未发现临时文件夹，已重新建立.")

            }
        }
        return true
    }

    /**
     *
     * 得到此Server下的属性键值对
     *
     *
     * 在某些情况下默认的 HTTP 解析存在问题时则引入自定义实现，
     * 但是在某些情况下需要利用某些自定义配置时则可能会存在问题，
     * 利用此方法可将在程序初始化过程中的所有添加的配置读取出来然后进行
     * 利用.
     *
     * 注意：
     * 不要将此作为临时数据的中转！！！
     *
     * @see .addHttpProperty
     * @param key 键值对
     * @return 保存的配置，如果不存在这返回 `null `
     */
    fun getHttpProperty(key: String): String? {
        return httpPropertiesMap[key]
    }

    /**
     *
     * 得到标准属性
     *
     *
     * 通过此方法可以得到
     *
     * @param key 标准键值对
     * @return 数据
     */
    fun getHttpProperty(key: HttpConstant.PROPERTIES_KEY): String? {
        return httpPropertiesMap["_DONT_REMOVE_2_EDIT_${key.name}" ]
    }

    /**
     *
     * 添加配置
     *
     *
     * 添加配置到当前 Server实例，如果此`Key `存在这会替换旧的数据
     *
     * @see java.util.HashMap.put
     * @param key 键值对
     * @param value 数据
     */
    fun addHttpProperty(key: String, value: String) {
        httpPropertiesMap[key] = value
    }

    /**
     *
     * 添加标准属性
     *
     *
     * @param key 键值对
     * @param value 属性
     */
    fun addHttpProperty(key: HttpConstant.PROPERTIES_KEY, value: String) {
        val keyStr = "_DONT_REMOVE_2_EDIT_${key.name}"
        if (!httpPropertiesMap.containsKey(keyStr)) {
            httpPropertiesMap[keyStr] = value
        }
    }

    /**
     * # 添加一个注解的 HttpServlet
     *
     * 警告，如果未标记，则会抛出
     *
     * @param servletClass BaseHttpServlet
     */
    fun addAutoHttpServlet(servletClass:Class<out BaseHttpServlet>) :Boolean{
        return addHttpServletBinder(HttpServletBinder(servletClass))
    }
    fun addAutoHttpServlet(servletClass: KClass<out BaseHttpServlet>) :Boolean{
        return addHttpServletBinder(HttpServletBinder(servletClass.java))
    }
    /**
     * 添加一个监听方法
     * @param binder HttpServletBinder
     * @return Boolean
     */
    fun addHttpServletBinder(binder: BaseHttpServletBinder):Boolean{
        val path = binder.path.toLowerCase()
        return if (httpServletMap.contains(path)) {
            false
        }else{
            httpServletMap[path] = binder
            true
        }
    }

    override fun close() {
    }
}
