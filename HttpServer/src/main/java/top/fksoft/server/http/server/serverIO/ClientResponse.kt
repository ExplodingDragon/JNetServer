package top.fksoft.server.http.server.serverIO

import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.server.serverIO.base.BaseResponse
import top.fksoft.server.http.server.serverIO.responseData.BaseResponseData
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.util.*
import kotlin.collections.HashMap

/**
 * # 响应客户端的信息
 *
 * @author ExplodingDragon
 * @version 1.0
 */


class ClientResponse(private val headerInfo: HttpHeaderInfo,private val outputStream: OutputStream,private val responseData: BaseResponseData): BaseResponse (outputStream,responseData){
    private val header = HashMap<String,String>()

    init {
        header["server"] = HttpConstant.LIB_NAME
        header["Content-Type"] = responseData.contentType
        if (responseData.length >= 0){
            header["Content-Length"] = responseData.length.toString()
        }

        // 为标识注明
    }

    override fun flashResponse(): Boolean {
        val dataHeader = responseData.header()
        header.putAll(dataHeader)
        dataHeader.clear()
        if (header.containsKey("Date").not()){
            header["Date"] = responseData.webDateFormat.format(Date())
        }
        // 读取所有的返回数据
        val printWriter = PrintWriter(OutputStreamWriter(outputStream, Charsets.UTF_8), true)
        printWriter.println("HTTP/1.1 ${responseData.responseCode}")
        synchronized(header){
            for (key in header.keys) {
                printWriter.println("$key: ${header[key]}")
            }
        }
        printWriter.println()
        printWriter.flush()
        val writeBody = responseData.writeBody(outputStream)
        outputStream.flush()
        return writeBody
    }

    override fun close() {
        header.clear()
    }

}