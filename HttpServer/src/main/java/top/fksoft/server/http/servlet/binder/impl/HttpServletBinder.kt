package top.fksoft.server.http.servlet.binder.impl

import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.servlet.BaseHttpServlet
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder.Companion.getPath
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder.Companion.isBindDirectory
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder.Companion.pathFormat


class HttpServletBinder(private val servletClass:Class<out BaseHttpServlet>, private var ignorePath:String = getPath(servletClass), override var bindDirectory: Boolean = isBindDirectory(servletClass)):BaseHttpServletBinder{
    override fun createNewHttpServlet(info: HttpHeaderInfo): BaseHttpServlet {
        return BaseHttpServlet.newInstance(servletClass, info)
    }

    override val path:String
        get() = ignorePath

    init {
        ignorePath = pathFormat(ignorePath)
    }



}