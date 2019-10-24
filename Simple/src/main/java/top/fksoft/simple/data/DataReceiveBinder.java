package top.fksoft.simple.data;

import org.jetbrains.annotations.NotNull;
import top.fksoft.server.udp.bean.Packet;
import top.fksoft.server.udp.callback.PacketListener;
import top.fksoft.server.udp.callback.ReceiveBinder;


public class DataReceiveBinder<T extends Packet> implements ReceiveBinder<T> {

    private T packet;
    private PacketListener<T> t;


    public DataReceiveBinder(PacketListener<T> t, T packet) {
        this.t = t;
        this.packet = packet;
    }

    @NotNull
    @Override
    public PacketListener<? extends Packet> getListener() {
        return t;
    }

    @Override
    public boolean create(@NotNull byte[] byteArray, int offset, int length) {
        return packet.decode(byteArray, offset, length);
    }

    @NotNull
    @Override
    public Packet packet() {
        return packet;
    }

    @NotNull
    @Override
    public String getHashSrc() {
        return packet().getHashSrc();
    }
}
