package top.fksoft.server.http.server.serverIO.responseData.impl.raw

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.server.serverIO.responseData.BaseResponseData
import top.fksoft.server.http.utils.ContentTypeUtils
import java.io.OutputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class ResRawResponseData (private val path:String,private val reuse: Boolean = false): BaseResponseData(){
    override var contentType: String = ContentTypeUtils.extension2Application(path)
    private val inputStream = ResRawResponseData::class.java.getResourceAsStream(path)
    private var bytes:ByteArray = inputStream.readBytes()

    init {
        inputStream.close()
    }

    override val length by lazy {
        bytes.size.toLong()
    }
    override val responseCode: ResponseCode = ResponseCode.HTTP_OK



    override fun writeBody(output: OutputStream): Boolean {
        output.write(bytes)
        return true
    }

    override fun close() {
        if (!reuse){
            bytes = ByteArray(0)
        }
    }

}
