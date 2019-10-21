package top.fksoft.server.http.server.factory

import top.fksoft.server.http.config.ResponseCode
import top.fksoft.server.http.config.ServerConfig
import top.fksoft.server.http.server.factory.impl.DefaultHeaderReader
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import java.io.Closeable
import java.io.InputStream
import kotlin.reflect.KClass

/**
 *
 * 解析 http header 的抽象方法
 *
 *
 * 用于解析http header的抽象方法，用于 http 头的所有内容，
 * 包括 HTTP 协议、请求的类型、请求头的所有信息，以及包括POST请求的
 * 表单信息，如果是文件上传则保存到指定的临时目录
 *
 * @see DefaultHeaderReaderFactory 默认实现
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class HeaderReaderFactory(protected val config: ServerConfig, protected val inputStream:InputStream) : Closeable {


    /**
     *
     * # 解析HTTP Header 信息并归档
     *
     *
     * 解析HTTP Header 中的信息，并将信息归档到 [HttpHeaderInfo] 中;
     * 在此方法中针对HTTP 1.1 头中的配置信息进行处理，但不处理POST表单
     * 信息（如果有）
     *
     * example:
     * ``` html
     * POST http://www.example.com HTTP/1.1
     * Content-Type: application/x-www-form-urlencoded;charset=utf-8
     *
     * ```
     *
     * @param edit 有效信息存放的位置
     * @return 解析是否存在问题
     */
    @Throws(Exception::class)
    abstract fun readHeaderInfo(edit: HttpHeaderInfo.Edit): ResponseCode


    /**
     * # 解析HTTP POST 下发送的表单数据
     *
     * 此方法只会在 POST  请求的情况下被调用，
     *
     *
     * @param httpHeader Edit
     * @throws Exception
     */
    @Throws(Exception::class)
    abstract fun readHeaderPostData(httpHeader: HttpHeaderInfo.Edit): ResponseCode

    /**
     * # 调用此方法读取 POST 数据
     * @param edit Edit
     */
    fun readHeaderBody(edit: HttpHeaderInfo.Edit): ResponseCode {
        if (edit.reader.isPost()){
            return readHeaderPostData(edit)
        }else
            return ResponseCode.HTTP_OK
    }


    companion object {
        @JvmStatic
        @Throws(IllegalAccessException::class, InstantiationException::class)
        fun createHttpHeaderReader(headerFactory: KClass<out HeaderReaderFactory>, config: ServerConfig, inputStream:InputStream): HeaderReaderFactory {
            val clazz = headerFactory.java
            val constructor = clazz.getDeclaredConstructor(ServerConfig::class.java, InputStream::class.java)
            constructor.isAccessible = true
            return constructor.newInstance(config,inputStream)
        }
        @JvmStatic
        fun getDefault() = DefaultHeaderReader::class

    }
}
