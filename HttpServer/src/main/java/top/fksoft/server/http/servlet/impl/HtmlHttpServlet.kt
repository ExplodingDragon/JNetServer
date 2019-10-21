package top.fksoft.server.http.servlet.impl

import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class HtmlHttpServlet(headerInfo: HttpHeaderInfo) : TextHtmlServlet(headerInfo) {
    init {
        contentType = HttpConstant.HEADER_VALUE_TEXT_HTML
    }

}
