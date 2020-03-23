package filter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author hzq
 * @Date 2019-11-21 0:23
 * @Description 计算起始ip和终止ip下的所有ip地址
 */

public class IpParser {
    public static long getIP(InetAddress ip) {
        byte[] b = ip.getAddress();
        long l = b[0] << 24L & 0xff000000L |
                b[1] << 16L & 0xff0000L |
                b[2] << 8L & 0xff00L |
                b[3] << 0L & 0xffL;
        return l;
    }

    //由低32位二进制数构成InetAddress对象
    public static InetAddress toIP(long ip) throws UnknownHostException {
        byte[] b = new byte[4];
        int i = (int) ip;//低32位
        b[0] = (byte) ((i >> 24) & 0x000000ff);
        b[1] = (byte) ((i >> 16) & 0x000000ff);
        b[2] = (byte) ((i >> 8) & 0x000000ff);
        b[3] = (byte) ((i >> 0) & 0x000000ff);
        return InetAddress.getByAddress(b);
    }
}
