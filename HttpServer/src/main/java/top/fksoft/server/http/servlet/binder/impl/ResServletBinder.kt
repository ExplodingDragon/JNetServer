package top.fksoft.server.http.servlet.binder.impl

import jdkUtils.data.StringUtils
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.servlet.BaseHttpServlet
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder
import top.fksoft.server.http.servlet.binder.BaseHttpServletBinder.Companion.pathFormat
import top.fksoft.server.http.servlet.impl.TextHtmlServlet
import top.fksoft.server.http.utils.ContentTypeUtils
import java.nio.charset.Charset

/**
 * # 用于动态绑定
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class ResServletBinder (private var ignorePath: String, private val packagePath:String, private val charset: Charset = Charsets.UTF_8, private val contentType : String = ContentTypeUtils.extension2Application(packagePath)): BaseHttpServletBinder {
    init {
        ignorePath = pathFormat(ignorePath)
        javaClass.getResourceAsStream(packagePath)
        //测试pkg 是否存在

    }
    override val path by lazy {
        ignorePath
    }

    override val bindDirectory: Boolean by lazy {
        path.endsWith('/')
    }

    override fun createNewHttpServlet(info: HttpHeaderInfo): BaseHttpServlet {
        val textHtmlServlet = TextHtmlServlet(info)
        textHtmlServlet.contentType = contentType
        textHtmlServlet.print(StringUtils.readInputStream(javaClass.getResourceAsStream(packagePath),charset))
        return textHtmlServlet
    }

}