package top.fksoft.server.http.servlet.binder

import top.fksoft.server.http.server.serverIO.HttpHeaderInfo
import top.fksoft.server.http.servlet.BaseHttpServlet
import top.fksoft.server.http.servlet.annotation.ServletBinder
import java.io.IOException
import java.lang.annotation.AnnotationFormatError

/**
 * @author ExplodingDragon
 * @version 1.0
 */

interface BaseHttpServletBinder {

    val path:String

    val bindDirectory:Boolean

    fun createNewHttpServlet(info: HttpHeaderInfo): BaseHttpServlet



    companion object{
        fun getPath(clazz: Class<out BaseHttpServlet>): String{
            val annotation = clazz.getAnnotation(ServletBinder::class.java) ?: throw AnnotationFormatError("未标记注解.")
            return annotation.path
        }
        fun isBindDirectory(clazz: Class<out BaseHttpServlet>): Boolean{
            val annotation = clazz.getAnnotation(ServletBinder::class.java) ?: throw AnnotationFormatError("未标记注解.")
            return annotation.bindDirectory
        }

        fun pathFormat(path: String): String {
            var trim = path.trim()
            if (!trim.startsWith("/")){
                throw IOException("不是合法的绑定路径结构.")
            }
            return if (trim.endsWith('/')&& trim.length > 1){
                trim.substring(0,trim.lastIndexOf('/')).trim()
            }else{
                trim
            }
        }
    }
}