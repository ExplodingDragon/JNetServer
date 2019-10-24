package top.fksoft.simple.data;

import com.google.gson.Gson;
import jdkUtils.logcat.Logger;
import org.jetbrains.annotations.NotNull;
import top.fksoft.server.udp.bean.StringPacket;

/**
 * @author Explo
 */
public class UserPacket extends StringPacket {
    private Logger logger = Logger.getLogger(this);
    public User user = new User();

    public class User {
        public String name = "Unknown";
        public String devicesName = "Unknown";

        @Override
        public String toString() {
            return String.format("%s - %s", name, devicesName);
        }
    }

    @Override
    public boolean decodeStr(@NotNull String data) {
        try {
            user = new Gson().fromJson(data, User.class);
            return true;
        } catch (Exception e) {
            logger.error(data,e);
            return false;
        }
    }

    @NotNull
    @Override
    public String encodeStr() {
        return new Gson().toJson(user);
    }

    @NotNull
    @Override
    public String getHashSrc() {
        return "UserClass";
    }
}
