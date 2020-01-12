package com.github.skywalker.common.utils;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 编码与解码操作工具类
 *
 * @author Chao Shibin
 */
public final class CodecUtils {

    public static final String UTF_8 = "UTF-8";

    private CodecUtils() {
    }

    /**
     * 将 URL 编码
     */
    @SneakyThrows
    public static String encodeUrl(String url) {
        return URLEncoder.encode(url, UTF_8);
    }

    /**
     * 将 URL 解码
     */
    @SneakyThrows
    public static String decodeUrl(String url) {
        return URLDecoder.decode(url, UTF_8);
    }

    /**
     * 将字符串 Base64 编码
     */
    public static String encodeBase64(String text) {
        return Base64.encodeBase64URLSafeString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将字符串 Base64 解码
     */
    public static String decodeBase64(String text) {
        return new String(Base64.decodeBase64(text), StandardCharsets.UTF_8);
    }

    /**
     * 将字符串 MD5 加密
     */
    public static String encryptMd5(String text) {
        return DigestUtils.md5Hex(text);
    }

    /**
     * 将字符串 SHA1 加密
     *
     * @param text text
     * @return 32位
     */
    public static String encryptSha1(String text) {
        return DigestUtils.sha1Hex(text);
    }


    /**
     * 将字符串 SHA256 加密
     *
     * @param text text
     * @return 64位
     */
    public static String encryptSha256(String text) {
        return DigestUtils.sha256Hex(text);
    }

    /**
     * 创建随机数
     */
    public static String createRandom(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    /**
     * 获取 UUID（32位）
     */
    public static String createUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
