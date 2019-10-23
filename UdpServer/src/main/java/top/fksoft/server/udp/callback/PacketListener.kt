package top.fksoft.server.udp.callback

import top.fksoft.bean.NetworkInfo
import top.fksoft.server.udp.bean.Packet

/**
 * # 数据包监听回调方案
 *
 * @param T:Packet 自实现
 */
interface PacketListener<T:Packet>{

    /**
     * 数据包响应事件
     *
     *
     * @param networkInfo NetworkInfo 远程地址
     * @param packet T 数据包对象
     * @throws Exception 如果处理发生错误
     */
    @Throws(Exception::class)
    fun onReceive(networkInfo: NetworkInfo,packet: Packet)

}
