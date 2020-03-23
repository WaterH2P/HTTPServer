package filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ConfigUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author hzq
 * @Date 2019-11-20 14:03
 * @Description Ip访问控制, 白名单方式，只允许ipConfig.properties内的ip地址进行访问
 */

public class IpFilter {
    private static final Logger log = LogManager.getLogger(IpFilter.class);
    private List<String> allowList = new ArrayList();

    public void initConfig() {
        String allowIP = ConfigUtil.getConfig("ALLOW_IP");
        String allowIPRange = ConfigUtil.getConfig("ALLOW_IP_RANGE");
        if (!validate(allowIP, allowIPRange)) {
            throw new RuntimeException("配置文件内容书写有误，请检查！");
        }
        /* * 将每一种配置方法的ip地址解析出来，放置到allowList中 */
        if (null != allowIP && !"".equals(allowIP.trim())) {
            String[] allowIPs = allowIP.split(",|;");
            for (String ip : allowIPs) {
                allowList.add(ip);
            }
        }
        if (null != allowIPRange && !"".equals(allowIPRange.trim())) {
            String[] allowIPRanges = allowIPRange.split(",|;");
            if (allowIPRanges.length > 0) {
                for (String allowRanges : allowIPRanges) {
                    if (allowRanges != null && !"".equals(allowRanges.trim())) {
                        String[] ips = allowRanges.split("-");
                        if (ips.length > 0 && ips.length < 3) {
                            try {
                                long ip1 = IpParser.getIP(InetAddress.getByName(ips[0]));
                                long ip2 = IpParser.getIP(InetAddress.getByName(ips[1]));
                                for (long ip = ip1; ip <= ip2; ip++) {
                                    allowList.add(IpParser.toIP(ip).getHostAddress());
                                }
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                        } else {
                            throw new RuntimeException("配置文件内容书写有误，请检查！");
                        }
                    }
                }
            }
        }
    }

    public Boolean validate(String allowIP, String allowIPRange) {
        Boolean result;
        String regx = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String ipRegx = regx + "\\." + regx + "\\." + regx + "\\." + regx;
        Pattern pattern = Pattern.compile("(" + ipRegx + ")|(" + ipRegx + "(,|;))*");
        if (this.isNullorMatches(allowIP, pattern)) {
            result = true;
        } else {
            result = false;
        }
        pattern = Pattern.compile("(" + ipRegx + ")\\-(" + ipRegx + ")|" + "((" + ipRegx + ")\\-(" + ipRegx + ")(,|;))");
        if (this.isNullorMatches(allowIPRange, pattern)) {
            result = result && true;
        } else {
            result = false;
        }
        return result;
    }

    public Boolean isNullorMatches(String allow, Pattern pattern) {
        if (allow == null || "".equals(allow.trim())) {
            return true;
        } else {
            if (!allow.endsWith(";") && !allow.endsWith(",")) {
                allow += ";";
            }
            if (pattern.matcher(allow).matches()) {
                return true;
            }
        }
        return false;
    }

    public Boolean filterIp(String remoteAddr) {
        if (allowList.size() == 0 || allowList == null) {
            return true;
        } else {
            for (String regex : allowList) {
                if (remoteAddr.matches(regex)) {
                    return true;
                }
            }
            log.info("Refuse the visit of ip:" + remoteAddr);
            return false;
        }
    }
}
