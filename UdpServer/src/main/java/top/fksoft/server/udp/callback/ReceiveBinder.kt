package top.fksoft.server.udp.callback

import top.fksoft.server.udp.bean.Packet

/**
 *
 * 数据包后续处理的方案
 *
 */
interface ReceiveBinder<T:Packet>{

     val listener:PacketListener<out Packet>

    /**
     * 确定数据包的唯一标志
     */
    open val hashSrc: String
    get() = packet().hashSrc

    /**
     * 将数据包写入到数据接口下
     *
     * @param byteArray ByteArray
     * @param offset Int
     * @param length Int
     * @return Boolean  是否成功
     */
     fun create(byteArray: ByteArray,offset:Int = 0,length: Int = byteArray.size):Boolean

     fun packet():Packet
}