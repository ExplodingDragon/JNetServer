package top.fksoft.simple.http.server.serverIO.base

import jdkUtils.logcat.Logger
import top.fksoft.simple.http.server.serverIO.responseData.BaseResponseData
import java.io.Closeable
import java.io.OutputStream

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class BaseResponse( private val outputStream: OutputStream,private val responseData:  BaseResponseData):Closeable{
    val finalResponseCode by lazy {
        responseData.responseCode
    }

    open val logger = Logger.getLogger(javaClass)
    /**
     * # 推送缓冲区内容
     */
    abstract fun flashResponse():Boolean



}