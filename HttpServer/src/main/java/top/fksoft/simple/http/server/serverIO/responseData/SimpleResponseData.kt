package top.fksoft.simple.http.server.serverIO.responseData

import top.fksoft.simple.http.config.ResponseCode
import top.fksoft.simple.http.server.serverIO.responseData.impl.raw.ResRawResponseData
import top.fksoft.simple.http.server.serverIO.responseData.impl.text.PkgHtmlResponseData

/**
 * @author ExplodingDragon
 * @version 1.0
 */
object SimpleResponseData {
    val NOT_FOUND
        get() = PkgHtmlResponseData(ResponseCode.HTTP_NOT_FOUND, "/res/resultHtml/404.html")
    val BAD_REQUEST
        get() = PkgHtmlResponseData(ResponseCode.HTTP_BAD_REQUEST, "/res/resultHtml/400.html")
    val WELCOME
        get() = PkgHtmlResponseData(packagePath = "/res/resultHtml/HelloWorld.html")
    val FAVICON = ResRawResponseData("/res/drawable/favicon.png",true)


}