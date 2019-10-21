package top.fksoft.simple.http.server.serverIO.responseData.impl.raw

import top.fksoft.simple.http.config.HttpConstant
import top.fksoft.simple.http.config.ResponseCode
import top.fksoft.simple.http.config.ResponseCode.Companion.HTTP_NOT_MODIFIED
import top.fksoft.simple.http.config.ResponseCode.Companion.HTTP_OK
import top.fksoft.simple.http.config.ResponseCode.Companion.HTTP_PARTIAL
import top.fksoft.simple.http.server.serverIO.HttpHeaderInfo
import top.fksoft.simple.http.server.serverIO.responseData.BaseResponseData
import top.fksoft.simple.http.utils.CalculateUtils
import top.fksoft.simple.http.utils.ContentTypeUtils
import java.io.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */

/**
 *  文件断点续传
 *
 * @property file File 解析的文件
 * @property breakpoint 开启断点续传（实际情况受内部解析而定）
 * @property length Long Content-Length 长度
 * @property responseCode ResponseCode 请求ID
 * @property contentType String 文件类型
 * @constructor
 */
class FileResponseData(private val httpHeaderInfo: HttpHeaderInfo, private val file: File, private val breakpoint: Boolean = false) : BaseResponseData() {
    override var length: Long = 0

    override var responseCode: ResponseCode = ResponseCode.HTTP_OK

    override var contentType: String = ContentTypeUtils.file2Application(file)

    data class Range(val start: Long, val end: Long) {
        val size = end - start + 1
    }

    private val rangeFormatList = ArrayList<Range>()

    private val eTag by lazy {
        "\"${getFileETag(file)}\""
    }

    private val lastModified by lazy {
        webDateFormat.format(file.lastModified())
    }

    init {
        if (file.isFile.not()) {
            throw FileNotFoundException("${file.absolutePath} exists.");
        }
        val clientModified = httpHeaderInfo.getHeader("If-Modified-Since")
        //客户端下文件记录的修改时间
        val clientETag = httpHeaderInfo.getHeader("If-None-Match", HttpConstant.UNKNOWN_VALUE)

        val fileLength = file.length()
        if (clientETag == eTag && clientModified != "") {
            if (lastModified.trim().toLowerCase() == clientModified.trim().toLowerCase()) {
                // 确定本地文件与网络文件为同一个, 发送 304 ，同时不发送 body
                responseCode = HTTP_NOT_MODIFIED
                length = 0
                addHeader("Last-Modified", lastModified)
                addHeader("ETag", eTag)
            } else {
                // 表示本地文件与服务器文件并不相同
                responseCode = HTTP_OK
                length = fileLength
                addHeader("Last-Modified", lastModified)
                addHeader("ETag", eTag)
            }
        }else{
            responseCode = HTTP_OK
            length = fileLength
            addHeader("Last-Modified", lastModified)
            addHeader("ETag", eTag)
        }
        val clientRange = httpHeaderInfo.getHeader("Range", "")

        if (breakpoint && responseCode == HTTP_NOT_MODIFIED && clientRange != "") {
            val rangeData = clientRange.split("=")[1]
            val rangeArray = rangeData.split(",")
            rangeArray.forEach {
                val trim = it.trim()
                val i = trim.indexOf("-")
                val split = trim.split('-')
                length += when (i) {
                    0 -> {
                        val l = split[0].toLong()
                        val element = Range(fileLength - l, fileLength - 1)
                        rangeFormatList.add(element)
                        element.size
                    }
                    trim.length - 1 -> {
                        val l = split[0].toLong()
                        val element = Range(l, fileLength - 1)
                        rangeFormatList.add(element)
                        element.size
                    }
                    else -> {
                        val element = Range(split[0].toLong(), split[1].toLong())
                        rangeFormatList.add(element)
                        element.size
                    }
                }
            }
            addHeader("Content-Range", "bytes $rangeData/$fileLength")
            responseCode = HTTP_PARTIAL
        }

    }


    override fun writeBody(output: OutputStream): Boolean {
        val b = ByteArray(4096)
        if (responseCode == HTTP_OK) {
            val inputStream = file.inputStream()
            while (true) {
                val i = inputStream.read(b)
                if (i == -1) {
                    break
                }
                output.write(b, 0, i)
                output.flush()
            }
            inputStream.close()
        } else if (responseCode == HTTP_PARTIAL) {
            val accessFile = RandomAccessFile(file, "r")
            rangeFormatList.forEach {
                accessFile.seek(it.start)
                var size = it.size
                while (true) {
                    if (size >= b.size){
                        output.write(b,0,accessFile.read(b))
                        size-=b.size
                    }else{
                        output.write(b,0,accessFile.read(b,0,size.toInt()))
                        output.flush()
                        break
                    }
                }
            }
        }
        return true
    }

    override fun close() {
    }


    /**
     * # 尝试计算文件ETag
     *
     *
     *
     * @param file File
     * @return String
     */
    private fun getFileETag(file: File): String {
        val absolutePath = file.absolutePath
        if (!absolutePath.endsWith(".md5") && !absolutePath.endsWith(".sha1")) {
            val md5File = File("$absolutePath.md5")
            val sha1File = File("$absolutePath.sha1")
            if (md5File.isFile) {
                return getFileETag(md5File)
            }
            if (sha1File.isFile) {
                return getFileETag(sha1File)
            }
        }
        if (file.length() > 8192) {
            var b = ByteArray(8192)
            val inputStream = file.inputStream()
            inputStream.read(b)
            inputStream.close()
            val byteArrayOutputStream = ByteArrayOutputStream()
            byteArrayOutputStream.write(b)
            byteArrayOutputStream.write(file.lastModified().toString(16).toByteArray())
            val crC32 = CalculateUtils.getCRC32(byteArrayOutputStream.toByteArray().inputStream())
            byteArrayOutputStream.close()
            return "$crC32-c"
        } else {
            val crC32 = CalculateUtils.getCRC32(file.inputStream())
            return "$crC32-c"
        }
    }


}