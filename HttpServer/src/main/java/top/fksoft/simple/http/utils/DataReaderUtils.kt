package top.fksoft.simple.http.utils

import jdkUtils.logcat.Logger
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

/**
 * @author ExplodingDragon
 * @version 1.1
 *
 * bug 修复：
 * v1.1 修复在慢速连接下发生错误的问题
 *
 */
@Deprecated(message = "对于不同编码存在大量问题，即将替代")
class DataReaderUtils(private val inputStream: InputStream, private val charset: Charset = Charsets.UTF_8) {
    private val logger = Logger.getLogger(DataReaderUtils::class)

    /**
     * # 读取一行数据
     * @return String
     */
    fun readLine(): String {
        val defaultResult = ""
        var charLine = 0
        val outputStream = ByteArrayOutputStream()
        var byteChar: Char
        var read: Int
        while (true) {
            read = inputStream.read()
            if (read == -1) {
                break
            }
            byteChar = read.toChar()
            if (byteChar == '\r' || byteChar == '\n') {
                charLine++
                if (charLine > 2) {
                    outputStream.reset()
                    break
                }
                if (outputStream.size() == 0) {
                    continue
                } else {
                    break
                }
            }
            outputStream.write(read)
        }
        val result = when (outputStream.size()) {
            0 -> defaultResult
            else -> {
                val byteArray = outputStream.toByteArray()
                byteArray?.let {
                    return String(it, charset)
                }
                return defaultResult
            }
        }
        outputStream.reset()
        outputStream.close()
        return result
    }



    fun copy(output: OutputStream, length: Int): Boolean{
        var  result: Boolean
        var len2 = length
        try {
            val bytes = ByteArray(2048)
                while (true){
                    if (inputStream.available() == 0) {
                        return len2 >=length
                    }
                    val readSize = inputStream.read(bytes, 0, bytes.size)
                    if (len2 < bytes.size){
                        output.write(bytes,0,len2)
                        break
                    }else{
                        output.write(bytes,0,readSize)
                    }
                    output.flush()
                    len2 -= readSize
                }
            result = true
        }catch (e:Exception){
            logger.debug("copy error.",e)
            result = false
        }
        return result
    }

}
