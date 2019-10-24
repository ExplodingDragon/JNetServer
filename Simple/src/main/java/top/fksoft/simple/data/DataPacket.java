package top.fksoft.simple.data;

import jdkUtils.logcat.Logger;
import org.jetbrains.annotations.NotNull;
import top.fksoft.server.udp.bean.StringPacket;

/**
 * @author Explo
 */
public class DataPacket extends StringPacket {
    private Logger logger = Logger.getLogger(this);

    public String data = "";

    @Override
    public boolean decodeStr(@NotNull String data) {
        this.data = data;
        return true;
    }

    @NotNull
    @Override
    public String encodeStr() {
        return data;
    }

    @NotNull
    @Override
    public String getHashSrc() {
        return "DataClass";
    }
}
