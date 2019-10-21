package top.fksoft.simple.http.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class CalculateUtils {
    public static String getCalculate(InputStream is, String method) throws NoSuchAlgorithmException, IOException {
        StringBuffer md5 = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance(method);
        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();

        for (int i = 0; i < mdbytes.length; i++) {
            md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        is.close();
        return md5.toString();
    }
    public static String getCRC32(InputStream inputStream) throws IOException {
        CRC32 crc32 = new CRC32();
        CheckedInputStream checkedinputstream = new CheckedInputStream(inputStream, crc32);
        while (checkedinputstream.read() != -1) {
        }
        checkedinputstream.close();
        inputStream.close();
        return   Long.toHexString(crc32.getValue()).toUpperCase();
    }
}
