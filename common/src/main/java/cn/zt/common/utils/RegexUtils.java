package cn.zt.common.utils;

import java.util.regex.Pattern;

/**
 * @author : ZhangTao on 2018/1/25 14:58
 */
public class RegexUtils {
    // 正则：IP地址
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    public static boolean isIP(final CharSequence input) {
        return isMatch(REGEX_IP, input);
    }

    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}
