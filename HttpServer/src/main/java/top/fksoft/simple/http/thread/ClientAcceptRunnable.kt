package top.fksoft.simple.http.thread

import jdkUtils.logcat.Logger
import top.fksoft.bean.NetworkInfo
import top.fksoft.simple.http.HttpServer
import top.fksoft.simple.http.config.ResponseCode.Companion.HTTP_OK
import top.fksoft.simple.http.server.factory.HeaderReaderFactory
import top.fksoft.simple.http.server.serverIO.ClientResponse
import top.fksoft.simple.http.server.serverIO.HttpHeaderInfo
import top.fksoft.simple.http.server.serverIO.base.BaseResponse
import top.fksoft.simple.http.thread.base.BaseClientRunnable
import java.net.Socket

/**
 *
 * HTTP 異步處理客戶端發送的請求
 *
 *
 * @version 1.0
 * @author ExplodingDragon
 */
class ClientAcceptRunnable(httpServer: HttpServer, client: Socket, private val networkInfo: NetworkInfo) : BaseClientRunnable(httpServer, client, networkInfo) {
    private val httpHeaderInfo = HttpHeaderInfo(networkInfo, serverConfig)
    //读取Header 头
    private val headerReader: HeaderReaderFactory by lazy {
        HeaderReaderFactory.createHttpHeaderReader(super.httpServer.httpHeaderReader, httpServer.serverConfig, client.getInputStream())
    }

    private val logger = Logger.getLogger(ClientAcceptRunnable::class)
    @Throws(Exception::class)
    override fun execute() {
        bindAutoCloseables(headerReader,httpHeaderInfo)
        //初始化 HTTP 解析类
        var responseCode = headerReader.readHeaderInfo(httpHeaderInfo.edit)
        if ((responseCode == HTTP_OK)) {
            responseCode  = headerReader.readHeaderBody(httpHeaderInfo.edit)
        }else{
            logger.warn("在读取来自[$networkInfo]的Header中失败了.原因：[${responseCode }].")
        }
        if (responseCode  == HTTP_OK) {
            //协议识别 再放行tcp 连接维持时间
            try {
                val httpServlet = bindAutoCloseable(httpServer.findHttpServletFactory.findHttpServlet(httpHeaderInfo))
                val responseData = bindAutoCloseable(httpServlet.execute())
                val clientResponse:BaseResponse = bindAutoCloseable(ClientResponse(httpHeaderInfo,bindAutoCloseable(client.getOutputStream()!!),responseData))
                // 建立销毁索引方便结束后销毁
                if (clientResponse.flashResponse()) {
                    if (logger.debug){
                        httpHeaderInfo.printDebug()
                    }
                    logger.info("请求类型:[${httpHeaderInfo.method}]; 请求路径:[${httpHeaderInfo.path}]; 返回状态码:[${responseData.responseCode}]")
                }else{
                    logger.warn("在回馈来自[$networkInfo]的连接中失败了.")
                }
            } catch (e: Exception) {
                logger.error("在$remoteAddress 的Servlet处理中发生不可预知的异常！", e)
            }
        }else{
            logger.warn("在读取来自[$networkInfo]的POST中失败了.原因：[${responseCode }]  .")

        }

    }




}
