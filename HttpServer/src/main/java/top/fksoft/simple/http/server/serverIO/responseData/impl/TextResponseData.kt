package top.fksoft.simple.http.server.serverIO.responseData.impl

import top.fksoft.simple.http.config.HttpConstant
import top.fksoft.simple.http.config.ResponseCode
import top.fksoft.simple.http.server.serverIO.responseData.BaseResponseData
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * @author ExplodingDragon
 * @version 1.0
 */

open  class TextResponseData() : BaseResponseData() {
    override var responseCode: ResponseCode = ResponseCode.HTTP_OK

    override var contentType: String = HttpConstant.HEADER_VALUE_TEXT_HTML

    override val length: Long by lazy {
        builder.toString().toByteArray().size.toLong()
    }
    // 忽略长度测量
    private val builder = StringBuilder()



    override fun writeBody(output: OutputStream): Boolean {
        val writer = OutputStreamWriter(output, Charsets.UTF_8)
        writer.write(builder.toString())
        writer.flush()
        return true
    }


    fun replace(old: String, newValues: String) {
        synchronized(builder) {
            val size = old.length
            while (true) {
                val i = builder.indexOf(old)
                if (i == -1) {
                    break
                }
                builder.replace(i, i + size, newValues)
            }
        }
    }

    fun println(str: String) {
        synchronized(builder) {
            builder.append(str).append("\r\n")
        }
    }

    fun print(str: String) {
        synchronized(builder) {

            builder.append(str)
        }
    }

    fun printf(format: String, vararg args: Any) {
        synchronized(builder) {
            builder.append(String.format(format, args))
        }
    }

    override fun close() {
        builder.clear()
    }

}


