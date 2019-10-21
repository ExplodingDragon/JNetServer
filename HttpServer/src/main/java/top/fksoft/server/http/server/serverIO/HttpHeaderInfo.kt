package top.fksoft.server.http.server.serverIO

import jdkUtils.data.StringUtils
import jdkUtils.io.autoByteArray.AutoByteArray
import jdkUtils.io.autoByteArray.DefaultAutoByteArray
import jdkUtils.logcat.Logger
import top.fksoft.bean.NetworkInfo
import top.fksoft.server.http.config.HttpConstant
import top.fksoft.server.http.config.ServerConfig
import java.io.Closeable
import java.nio.charset.Charset
import kotlin.random.Random

/**
 *
 * # HTTP 头信息
 *
 *
 * 用于保存HTTP Header 的所有信息
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class HttpHeaderInfo(val remoteInfo: NetworkInfo, val serverConfig: ServerConfig) : Closeable {
    var charset: Charset = Charsets.UTF_8
    private val logger = Logger.getLogger(this)
    val edit = Edit()

    /**
     * 服务器下的请求类型
     */
    var method = HttpConstant.METHOD_GET
        private set
    private val formArray = HashMap<String, String>()
    private val headerArray = HashMap<String, String>()
    private val postFileArray = HashMap<String, PostFileItem>()

    /**
     * 请求的服务器文件路径（去除GET后缀）
     */
    var path: String = "/"
        private set

    /**
     *  HTTP 协议的版本，默认 1.0
     */
    var httpVersion: Float = 1.0f
        private set

    /**
     * 当前实例的唯一 Token
     */
    val headerSession = StringUtils.stringSha1("$remoteInfo${System.currentTimeMillis()}${Random.nextDouble()}")

    /**
     * # 得到表单数据
     *  得到GET 或 POST 下
     * @param key String 键值对
     * @param defaultValue String
     * @return String
     */
    fun getForm(key: String, defaultValue: String = ""): String {
        return if (formArray.containsKey(key)) {
            formArray[key]!!
        } else {
            defaultValue
        }
    }


    var rawPostArray : AutoByteArray = DefaultAutoByteArray()
        private set


    /**
     * # 得到POST 指定的文件信息
     *
     * @param key String 键值对
     * @return PostFileItem? 数据
     */
    fun getFormFile(key: String): PostFileItem? {
        return postFileArray[key]
    }

    /**
     * # 判断是否为 POST 请求
     *
     * @return Boolean 判断是否为POST 请求
     */
    fun isPost() = method == HttpConstant.METHOD_POST

    /**
     * # 得到 header 键值对
     *
     * 得到 Http Header 下键值对，如果键值对不存在则返回``` ```；
     * 可自定义默认返回的字符串
     *
     * @param key String 键值对
     * @param defaultValue String 如果不存在返回的数据
     * @return String 返回的数据
     */
    fun getHeader(key: String, defaultValue: String = ""): String {
        return headerArray[key] ?: defaultValue
    }

    @Throws(Exception::class)
    override fun close() {
        synchronized(postFileArray) {
            for (value in postFileArray.values) {
                value.close()
            }
        }
        rawPostArray.close()
        postFileArray.clear()
        formArray.clear()
        headerArray.clear()
    }


    fun edit(): Edit {
        return edit
    }

    fun containsHeader(key: String) = headerArray.containsKey(key)


    fun printDebug() {
        for (key in headerArray.keys) {
            logger.debug("header's Key=$key,value=${headerArray[key]};")
        }
        for (key in formArray.keys) {
            logger.debug("form's Key=$key,value=${formArray[key]};")
        }
        for (key in postFileArray.keys) {
            val postFileItem = postFileArray[key]!!
            val search = postFileItem.autoByteArray.search
            val md5:String = search.calculate("MD5").toUpperCase()
            logger.debug("form's File Key=$key,MD5=$md5;")
        }
    }




    inner class Edit() {

        /**
         * # 指定请求的类型
         * @param method String
         */
        fun setMethod(method: String) {
            this@HttpHeaderInfo.method = method;
        }

        fun setHttpVersion(httpVersion: Float) {
            this@HttpHeaderInfo.httpVersion = httpVersion
        }
        /**
         *  # 添加表单
         *
         *
         * 将 GET 的表单和POST下以``` application/x-www-formArray-urlencoded```
         * 表单添加到MAP 中。
         *
         * 格式如下：
         * ``` bash
         * title=test&sub%5B%5D=1&sub%5B%5D=2&sub%5B%5D=3
         * ```
         * 但是需要将转义字符回转
         *
         * @param formArray String 原始表单数据
         * @param delimiter String 分隔符
         */
        fun addForms(formArray: String, delimiter: String = "&") {
            for (formStr in formArray.split(delimiter.toRegex()).filter { it != "" }) {
                var index = formStr.indexOf('=')
                if (index != -1) {
                    addForm(formStr.substring(0, index), formStr.substring(index + 1))
                } else {
                    logger.debug("无法格式化此字段：$formStr .")
                }

            }
        }

        /**
         * 添加一个表单到 map 中
         * @param key String 键值对
         * @param value String 数值
         */
        fun addForm(key: String, value: String) {
            if (key.trim().isEmpty())
                return
            formArray[key] = value.trim()
        }

        /**
         * # 添加一个 POST 上传文件的表单
         * @param key String POST 键值对
         * @param item PostFileItem 保存的临时文件位置
         */
        fun addFormFile(key: String, item: PostFileItem) {
            postFileArray[key] = item
        }

        /**
         * # 指定请求的路径
         * @param path String
         */
        fun setPath(path: String) {
            this@HttpHeaderInfo.path = path

        }

        /**
         * # 得到只读header对象
         * @return HttpHeaderInfo 绑定的 Header 对象
         */
        val reader = this@HttpHeaderInfo
        /**
         * # 添加一个Header Header 属性
         *
         * @param key String 键值对
         * @param value String 数值
         */
        fun addHeader(key: String, value: String) {
            headerArray[key] = value.trim()
        }



        /**
         * 指定原始post 数据
         * @param input File
         */
        fun setRawPostByteArray(array: AutoByteArray) {
            rawPostArray = array
        }


    }


    /**
     * # POST 上传文件的实体类
     *
     * @property key String 文件键值对
     * @property autoByteArray AutoByteArray 保存的位置
     * @property contentType String 文件类型
     * @constructor
     */
    data class PostFileItem(val key: String, val autoByteArray: AutoByteArray, val contentType: String, val fileName:String) : Closeable {
        override fun close() {
            autoByteArray.close()
        }



    }


}
