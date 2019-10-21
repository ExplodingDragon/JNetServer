package top.fksoft.simple.http.server.serverIO.responseData.impl.text

import top.fksoft.simple.http.server.serverIO.responseData.impl.TextResponseData

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class HtmlResponseData : TextResponseData(){

    override var contentType: String = "text/html; charset=utf-8"
}