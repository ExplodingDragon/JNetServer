package top.fksoft.simple.http.servlet.impl

import jdkUtils.data.StringUtils
import top.fksoft.simple.http.server.serverIO.HttpHeaderInfo
import top.fksoft.simple.http.server.serverIO.responseData.impl.TextResponseData
import top.fksoft.simple.http.servlet.BaseHttpServlet
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class TextHtmlServlet(headerInfo: HttpHeaderInfo) : BaseHttpServlet(headerInfo) {
    init {
        responseData = TextResponseData()
        hasPost = true
    }

    var contentType:String
    set(value) {
        responseData.contentType = value
    }
    get() = responseData.contentType

    fun replace(old: String, newValues: String) {
            (responseData as TextResponseData).replace(old,newValues)
    }
    fun replaceNode(old: String, newValues: String) {
        replace("<$old/>",newValues)
    }

    fun println(str: String) {
        (responseData as TextResponseData).println(str)
    }

    fun print(str: String) {
        (responseData as TextResponseData).print(str)
    }

    fun printf(format: String, vararg args: Any) {
        (responseData as TextResponseData).printf(format,args)
    }

    fun printAsResource(path:String,charset: Charset = Charsets.UTF_8){
        print(StringUtils.readInputStream(javaClass.getResourceAsStream(path),charset))
    }

    override fun doGet(headerInfo: HttpHeaderInfo) {
    }

    @Throws(IOException::class)
    override fun close() {
    }
}
