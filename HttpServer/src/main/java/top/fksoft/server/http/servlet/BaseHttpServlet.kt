package top.fksoft.server.http.servlet

import jdkUtils.logcat.Logger
import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.server.serverIO.responseData.BaseResponseData
import top.fksoft.server.http.server.serverIO.responseData.SimpleResponseData
import top.fksoft.server.http.server.serverIO.responseData.SimpleResponseData.WELCOME
import java.io.Closeable

/**
 * @author ExplodingDragon
 * @version 1.0
 */

abstract class BaseHttpServlet constructor(private val headerInfo: HttpHeaderInfo) : Closeable {
    protected val logger = Logger.getLogger(javaClass.kotlin)

    protected var hasPost:Boolean = false

    protected var responseData: BaseResponseData = WELCOME


    fun execute(): BaseResponseData {
        try {
            if (hasPost && headerInfo.isPost()){
                 doPost(headerInfo)
            }else if(headerInfo.method == HttpConstant.METHOD_GET){
                 doGet(headerInfo)
            }else{
                throw RuntimeException("请求为 POST 但此 Servlet 无法处理POST 请求.")
            }
        }catch (e:Exception){
            responseData = SimpleResponseData.BAD_REQUEST
            logger.warn("处理请求出现问题.",e)
        }
        return responseData
    }

    @Throws(Exception::class)
    abstract fun doGet(headerInfo: HttpHeaderInfo)

    @Throws(Exception::class)
    open fun doPost(headerInfo: HttpHeaderInfo){

    }

    companion object{
        fun newInstance(clazz:Class<out BaseHttpServlet>, headerInfo: HttpHeaderInfo): BaseHttpServlet {
            val declaredConstructor = clazz.getDeclaredConstructor(HttpHeaderInfo::class.java)
            declaredConstructor.isAccessible = true
            return declaredConstructor.newInstance(headerInfo)!!
        }
    }
}
