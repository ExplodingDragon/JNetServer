package top.fksoft.server.udp.bean


/**
 *  数据包实体封装
 */
interface Packet {
    /**
     * # 将RAW 数据解析
     * @param bytes ByteArray 解析元数据
     * @param offset Int 元数据偏移
     * @param len Int 实际长度
     * @return Boolean 是否解析成功
     */
    fun decode(bytes: ByteArray, offset: Int, len: Int): Boolean

    /**
     * #将 数据还原成 RAW 数据
     *
     * 将数据还原成原始byteArray，
     * 注意，可用的实际大小为 array.size - offset，
     * 如果数据放不下了，那么返回 -1 ，最好不要
     * 在此抛出异常
     *
     * @param output ByteArray 填充的数组
     * @param offset Int 填充偏移
     * @return Int 实际填充的长度
     */
    fun encode(output: ByteArray, offset: Int): Int

    /**
     * # 由这个值确定一个 HASH 值，方便确定数据包的唯一性
     *  生成hash 的方案由 ``` UdpServer ```决定
     */
    val hashSrc: String
}