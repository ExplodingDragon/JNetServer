package top.fksoft.server.http.server.factory

import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.server.factory.impl.DefaultFindHttpServlet
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.servlet.BaseHttpServlet

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class FindHttpServletFactory (config: ServerConfig) {
    protected val serverConfig = config
    /**
     * # 得到当前 HTTP  连接可用的 HTTP 解析类
     *
     * @param info
     * @return
     */
    abstract fun findHttpServlet(info: HttpHeaderInfo): BaseHttpServlet


    companion object{
        fun getDefault(config: ServerConfig): FindHttpServletFactory = default.getDeclaredConstructor(ServerConfig::class.java).newInstance(config)
        @JvmStatic
        val default = DefaultFindHttpServlet::class.java
    }
}
