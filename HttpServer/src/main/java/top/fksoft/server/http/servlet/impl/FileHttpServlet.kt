package top.fksoft.server.http.servlet.impl

import jdkUtils.data.StringUtils
import jdkUtils.io.FileUtils
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.server.serverIO.responseData.SimpleResponseData
import top.fksoft.server.http.server.serverIO.responseData.SimpleResponseData.FAVICON
import top.fksoft.server.http.server.serverIO.responseData.impl.raw.FileResponseData
import top.fksoft.server.http.server.serverIO.responseData.impl.text.HtmlResponseData
import top.fksoft.server.http.servlet.BaseHttpServlet
import java.io.File
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class FileHttpServlet (headerInfo: HttpHeaderInfo) : BaseHttpServlet(headerInfo) {
    override fun doGet(headerInfo: HttpHeaderInfo) {
        val workDirectory = headerInfo.serverConfig.workDirectory
        var acceptFile = File(workDirectory, headerInfo.path.substring(1))
        if (headerInfo.path.endsWith('/')) {
            var string = StringUtils.readInputStream(javaClass.getResourceAsStream("/res/resultHtml/ListFiles.html"))
            string = string.replace("%PATH%", headerInfo.path)
            var stringBuilder = StringBuilder()
            if (acceptFile.isDirectory) {
                var listFiles = acceptFile.listFiles().asList()
                FileUtils.fileSort(listFiles)
                for (file in listFiles) {
                    var name = file.name
                    var length = file.length()
                    var lengthStr = FileUtils.lengthFormat(length, 2)
                    var date = String.format("%20s", Date(file.lastModified()).toString())
                    if (file.isDirectory) {
                        name = "$name/"
                        lengthStr = "-"
                    }
                    stringBuilder.append("<tr><td><a href=\"$name\">$name</a><td>$date</td><td>$lengthStr</td></tr>")
                }
                string = string.replace("%TABLE%", stringBuilder.toString())
                val htmlResponseData = HtmlResponseData()
                responseData = htmlResponseData
                htmlResponseData.println(string)
            }
        }else{
            if (acceptFile.isFile) {
                responseData = FileResponseData(headerInfo, acceptFile, true)
            }else{
                responseData = SimpleResponseData.NOT_FOUND
                if (headerInfo.path.toLowerCase().contains( "/favicon.ico")){
                    responseData = FAVICON
                }
            }
        }
    }



    override fun close() {
    }



}
