package top.fksoft.server.udp.bean

import jdkUtils.logcat.Logger

/**
 * @author Explo
 */

abstract class StringPacket:Packet {
    private val logger = Logger.getLogger(StringPacket::class)
    override fun decode(bytes: ByteArray, offset: Int, len: Int): Boolean {
        val decodeResult = decodeStr(String(bytes, offset, len, Charsets.UTF_8))
        if (decodeResult.not()){
            logger.warn("无法解析通过，建议检查日志.")
        }
        return decodeResult
    }

    abstract fun decodeStr(data: String):Boolean


    override fun encode(output: ByteArray, offset: Int): Int {
        val encodeStr = encodeStr()
        val array = encodeStr.toByteArray(Charsets.UTF_8)
        if (array.size > output.size - offset){
            logger.error("文字$encodeStr 过长！无法转化")
            return -1
        }else{
            System.arraycopy(array,0,output,offset,array.size)
            return array.size
        }
    }

    abstract fun encodeStr():String
}