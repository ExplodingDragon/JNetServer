package top.fksoft.simple.http.utils

import jdkUtils.data.StringUtils
import org.json.JSONArray
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * @author ExplodingDragon
 * @version 1.0
 */
object ContentTypeUtils {
    private val contentTypes = ConcurrentHashMap<String, String>()

    init {
        val string = StringUtils.readInputStream(javaClass.getResource("/res/application.json").openStream())
        val array = JSONArray(string)
        val length = array.length()
        contentTypes.clear()
        for (i in 0 until length) {
            val jsonObject = array.getJSONObject(i)
            val fileType = jsonObject.getString("fileType").trim { it <= ' ' }.toUpperCase()
            val application = jsonObject.getString("application").trim { it <= ' ' }
            contentTypes[fileType] = application
        }
    }

    /**
     *
     * 将 Html 头中的 application 转换成对应的后缀
     *
     *
     * @param application `Content-Type `
     * @return 文件后缀名，如果未发现则返回 空 ：`“” `
     */
    fun application2Extension(application: String): String {
        for ((key, value) in contentTypes) {
            if (value == application) {
                return key
            }
        }
        return ""
    }
    fun file2Application(file: File) = extension2Application(file.absolutePath)

    fun extension2Application(externsion: String): String {
        var newExternsion = externsion
        val i = newExternsion.lastIndexOf(".")
        if (i != -1) {
            newExternsion = newExternsion.substring(i).toUpperCase().trim { it <= ' ' }
            if (contentTypes.containsKey(newExternsion)) {
                return contentTypes[newExternsion]!!
            }
        }
        return contentTypes["Unknown".toUpperCase()]!!
    }

}
