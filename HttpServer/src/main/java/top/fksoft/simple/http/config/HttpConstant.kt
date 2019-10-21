package top.fksoft.simple.http.config


/**
 *
 * 一些属性的预设封装
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
object HttpConstant {


    enum class PROPERTIES_KEY {
        /**
         * 服务器端口号
         */
        SERVER_PORT

    }

    /**
     * # 最大内存下保存的文件
     */
    const val MAX_RAM_CLIENT_SIZE: Int = 64 * 1024
    const val HEADER_VALUE_TEXT_HTML: String = "text/html;charset=utf-8"
    const val UNKNOWN_VALUE: String = "_ExplodingFKL_By_unknown"
    /**
     * Http 下的GET 请求表示方法
     */
    const val METHOD_GET = "GET"


    /**
     * Http 下 POST 请求的表示方法
     */
    const val METHOD_POST = "POST"

    /**
     * Http 下未知请求的表示方法
     */
    const val METHOD_UNKNOWN = "UNKNOWN"

    /**
     *  一个系统属性值，可得到系统临时目录位置
     */
    var PROPERTY_SYSTEM_TEMP_DIR: String = System.getProperty("java.io.tmpdir")


    /**
     * 项目名称，随意啦
     */
    var LIB_NAME = "MiniHttpServer"




    /**
     *  浏览器类型
     */
    const val HEADER_KEY_USER_AGENT = "User-Agent"


    /**
     * 请求的网址
     */
    const val HEADER_KEY_HOST = "Host"
    /**
     * 浏览器语言
     */
    const val HEADER_KEY_ACCEPT_LANGUAGE = "Accept-Language"
    /**
     * body 的长度
     */
    const val HEADER_KEY_CONTENT_LENGTH = "Content-Length"

    /**
     * body 类型
     */
    const val HEADER_KEY_CONTENT_TYPE = "Content-Type"
    /**
     * # 最普通的POST请求表单方式
     */
    const val HEADER_CONTENT_TYPE_URLENCODED: String = "application/x-www-form-urlencoded"
    /**
     * # 文件上传的POST请求表单方式
     */
    const val HEADER_CONTENT_TYPE_FORM_DATA: String = "multipart/form-data"


    /**
     * 在集合下得到
     */
    fun getValue(src: String, key: String, spit: String = ";", defaultResult:String = ""): String {
        for (line in src.split(spit.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (line.contains(key)) {
                return line.substring(line.indexOf(key) + key.length)
            }
        }
        return defaultResult
    }

}
