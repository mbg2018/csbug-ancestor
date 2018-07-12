package com.csbug.ancestor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Author ： Martin
 * Date : 18/1/5
 * Description : 编码配置文件解析类
 * Version : 1.0
 */
@ConfigurationProperties(prefix = "spring.http.encoding")
public class HttpEncodingProperties {
    private Charset charset = StandardCharsets.UTF_8; //默认为utf-8编码模式
    private boolean force = true; //默认为强制使用

    public Charset getCharset() {
        return charset;
    }

    public boolean isForce() {
        return force;
    }
}
