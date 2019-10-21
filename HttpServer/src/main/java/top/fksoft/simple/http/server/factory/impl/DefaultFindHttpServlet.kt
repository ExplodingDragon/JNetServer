package top.fksoft.simple.http.server.factory.impl

import top.fksoft.simple.http.config.ServerConfig
import top.fksoft.simple.http.server.factory.FindHttpServletFactory
import top.fksoft.simple.http.server.serverIO.HttpHeaderInfo
import top.fksoft.simple.http.servlet.BaseHttpServlet
import top.fksoft.simple.http.servlet.impl.FileHttpServlet

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultFindHttpServlet(private val config: ServerConfig) : FindHttpServletFactory(config) {

    override fun findHttpServlet(info: HttpHeaderInfo): BaseHttpServlet {
        val path = info.path.trim().toLowerCase()
        val httpExecuteBinder = config.httpServletMap[path]
        httpExecuteBinder?.let {
            if (path.endsWith('/')) {
                if (it.bindDirectory) {
                    return it.createNewHttpServlet(info)
                }
            } else {
                if (!it.bindDirectory) {
                    return it.createNewHttpServlet(info)
                }
            }
        }
        return BaseHttpServlet.newInstance(FileHttpServlet::class.java, info)

    }
}
