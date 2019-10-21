package top.fksoft.server.http.server.serverIO.responseData.impl.raw

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.server.serverIO.responseData.BaseResponseData
import java.io.InputStream
import java.io.OutputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
open class RawResponseData (private val inputStream: InputStream, override var contentType: String): BaseResponseData(){

    override val length: Long = -1
    override val responseCode: ResponseCode = ResponseCode.HTTP_OK


    override fun writeBody(output: OutputStream): Boolean {
        val b = ByteArray(4096)
        while (true) {
            val i = inputStream.read(b)
            if (i == -1) {
                break
            }
            output.write(b, 0, i)
            output.flush()
        }
        return true
    }

    override fun close() {
        inputStream.close()
    }

}