package top.fksoft.server.udp

import jdkUtils.data.AtomicUtils
import jdkUtils.logcat.Logger
import top.fksoft.bean.NetworkInfo
import top.fksoft.server.udp.bean.Packet
import top.fksoft.server.udp.callback.ReceiveBinder
import top.fksoft.server.udp.factory.HashFactory
import java.io.Closeable
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.util.concurrent.*

/**
 * # 建立一个 UDP 管理服务器
 * > 封装 的DatagramSocket
 * 此udp传输无加密
 * 在指定 mtu大小时，要保证小于网络传输层最小 MTU 大小（保证数据包不分片）
 *
 *  datagramSocket DatagramSocket 已经初始化的 ``` DatagramSocket ```
 *
 *  mtuSize Int 网络MTU大小
 *
 *  threadPool ExecutorService 非线性线程池
 *
 *  hashFactory HashFactory 哈希生成工厂类
 *
 * 默认已经全部实现，绑定了 ```31412``` 端口，使用标准互联网MTU大小，依赖 CRC32 来校验
 *
 */
class UdpServer @JvmOverloads constructor(
        private val datagramSocket: DatagramSocket = DatagramSocket(31412),
        private val mtuSize: Int = InternetMTU,
        private val threadPool: ExecutorService = ThreadPoolExecutor(0, Integer.MAX_VALUE,
                5L, TimeUnit.SECONDS,
                SynchronousQueue()),
        private val hashFactory: HashFactory = HashFactory.default
) : Closeable {
    private val logger = Logger.getLogger(this)
    /**
     * # 实际数据包大小
     *  INTERNET UDP 减去报文和包头大小
     */
    val packetSize: Int = mtuSize - 20 - 8

    /**
     *  监听二级维护列表
     */
    private val receiveMap = ConcurrentHashMap<String, ReceiveBinder<*>>()
    /**
     * # 数据包校验类型
     * 如果 UDP 包头不符合则丢弃
     */
    private val tag = ByteArray(3)

    fun setTAG(zero: Byte = 32, first: Byte = 16, second: Byte = 23) {
        tag[0] = zero
        tag[1] = first
        tag[2] = second

    }


    /**
     *  进行 UDP 数据包监听的线程
     */
    private val receiveListenRunnable = Runnable {
        val receiveBytes = ByteArray(packetSize)
        val receivePacket = DatagramPacket(receiveBytes, receiveBytes.size)
        val offset = tag.size + hashFactory.hashByteSize + Short.SIZE_BYTES
        while (isClosed.not()) {
            try {
                //监 听
                datagramSocket.receive(receivePacket)
                val remoteInfo = NetworkInfo(receivePacket.address.hostAddress, receivePacket.port)
                // 远程服务器
                val remoteTypeHash = hashFactory.decodeHashStr(receiveBytes, tag.size)
                if (receiveMap.containsKey(remoteTypeHash)) {
                    val binder = receiveMap[remoteTypeHash]!!
                    val length = AtomicUtils.byteToShort(receiveBytes, tag.size + hashFactory.hashByteSize).toInt()
                    if (binder.create(receiveBytes, offset, length)) {
                        val runnable = Runnable {
                            try {
                                binder.listener.onReceive(remoteInfo, binder.packet())
                            } catch (e: Exception) {
                                logger.warn("处理数据包时发生问题")
                            }
                        }
                        threadPool.submit(runnable)
                    }else{
                        logger.info("解析数据包未通过.")
                    }
                }else{
                    logger.info("新接受数据包但无法解析，原因：$remoteTypeHash")

                }
            } catch (e: Exception) {
                val contains = e.message?.contains("socket closed")?:false
                if ((e is SocketException && contains).not()) {
                    logger.error("在解析数据包时发生问题！", e)
                }
            }

        }
        logger.info("数据包监听结束。")
    }

    /**
     * 服务器是否关闭
     */
    val isClosed: Boolean
        get() = datagramSocket.isBound.not() || datagramSocket.isClosed


    private val sendPacketData = ByteArray(packetSize)
    //发送数据包时使用中转


    init {
        if (isClosed) {
            throw SocketException("连接存在问题，无法初始化.")
        }
        setTAG() //默认 TAG
        Thread(receiveListenRunnable,"#UDP_RECEIVE@${Integer.toHexString(hashCode())}").start()
    }


    /**
     * 得到本地端口号
     */
    val localPort by lazy {
        datagramSocket.localPort
    }


    /**
     * # 发送数据包
     *
     * > 将封装好的数据包发送到目标服务器
     *
     * @param packet Packet 数据包
     * @param info NetworkInfo 远程服务器ip + 端口
     * @return Boolean 是否由系统发送
     *
     *
     */
    fun sendPacket(packet: Packet, info: NetworkInfo): Boolean {
        try {
            synchronized(sendPacketData) {
                val dataSize = packet.encode(sendPacketData, tag.size + hashFactory.hashByteSize + Short.SIZE_BYTES)
                //写入数据
                if (dataSize == -1) {
                    throw RuntimeException("在写入时发生错误 dataSize = -1 .")
                }
                hashFactory.createToByteArray(packet.hashSrc, sendPacketData, tag.size)
                //定义数据类型（伪）
                System.arraycopy(
                        AtomicUtils.shortToBytes(dataSize.toShort()),
                        0,
                        sendPacketData,
                        tag.size + hashFactory.hashByteSize,
                        Short.SIZE_BYTES
                )
                //定义数据实际长度
                if (isClosed) {
                    throw IOException("服务器已经关闭.")
                }
                datagramSocket.send(
                        DatagramPacket(
                                sendPacketData,
                                sendPacketData.size,
                                InetAddress.getByName(info.ip),
                                info.port
                        )
                )
                //进行数据包发送
            }
        } catch (e: Exception) {
            logger.error("此实例在发送数据到$info 时出现问题.", e)
            return false
        }
        logger.info("发送数据包到$info 完成.")
        return true
    }

    /**
     * # UDP 包接受的回调监听事件
     * > 注意，添加重复的会发生覆盖！
     * @param listener PacketListener
     */
    @Synchronized
    fun bindReceive(binder: ReceiveBinder<*>) {
        val key = hashFactory.create(binder.hashSrc)
        logger.info("添加新的监听器，代号：$key")
        receiveMap[key] = binder
    }


    override fun close() {
        if (!isClosed) {
            receiveMap.clear()
            datagramSocket.close()
            logger.info("已发送销毁服务器请求.")
        }else{
            logger.debug("服务器已关闭，无需再次关闭.")

        }

    }


    companion object {
        /**
         * 标准 Internet 下 MTU 大小
         */
        const val InternetMTU = 576
        /**
         * 一般情况下 局域网下最大MTU
         */
        const val LocalMTU = 1480

    }
}