package top.fksoft.simple.http.server.serverIO.responseData.impl.text

import jdkUtils.data.StringUtils
import top.fksoft.simple.http.config.ResponseCode
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class PkgHtmlResponseData(override var responseCode: ResponseCode = ResponseCode.HTTP_OK, packagePath: String, charset: Charset = UTF_8): HtmlResponseData(){
    init {
        println(StringUtils.readInputStream(javaClass.getResourceAsStream(packagePath),charset))
    }
}
