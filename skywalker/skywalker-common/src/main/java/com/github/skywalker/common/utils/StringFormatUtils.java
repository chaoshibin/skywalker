package com.github.skywalker.common.utils;

import com.github.skywalker.common.constant.StrConst;
import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CHAO 2019/5/21 12:29
 */
@UtilityClass
public class StringFormatUtils {
    private static final Map<String, Joiner> JOINER_FACTORY = new ConcurrentHashMap<>();
    private static final String S_S = "%s_%s";

    public static String format(String splitter, String... args) {
        return Joiner.on(splitter).useForNull(StrConst.EMPTY).join(args);
    }

    /**
     * 下划线连接
     *
     * @param args 连接字符
     * @return 结果
     */
    public static String formatUnderline(String... args) {
        return get(StrConst.UNDERLINE).join(args);
    }

    /**
     * 斜线连接
     *
     * @param args 连接字符
     * @return 结果
     */
    public static String formatSlash(String... args) {
        return get(StrConst.SLASH).join(args);
    }

    public static String format(String prefix, String suffix) {
        return String.format(S_S, prefix, suffix);
    }

    public static Joiner get(String splitter) {
        Joiner instance = JOINER_FACTORY.get(splitter);
        if (instance == null) {
            synchronized (StringFormatUtils.class) {
                instance = JOINER_FACTORY.get(splitter);
                if (instance == null) {
                    instance = Joiner.on(splitter).useForNull("");
                    JOINER_FACTORY.put(splitter, instance);
                }
            }
        }
        return instance;
    }
}