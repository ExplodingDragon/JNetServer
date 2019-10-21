package top.fksoft.simple.http.servlet.impl

import top.fksoft.simple.http.config.HttpConstant
import top.fksoft.simple.http.server.serverIO.HttpHeaderInfo

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class HtmlHttpServlet(headerInfo: HttpHeaderInfo) : TextHtmlServlet(headerInfo) {
    init {
        contentType = HttpConstant.HEADER_VALUE_TEXT_HTML
    }

}
