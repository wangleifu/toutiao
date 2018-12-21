package com.nowcoder;

import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * @author: wang
 * @Desciption: 自动扫描Spring 上下文，构建 war 程序
 * @Date: Created in 10:44 2018/12/21
 * @Modified By:
 **/
public class SpringApplication extends SpringBootServletInitializer {

    @Override
    protected org.springframework.boot.builder.SpringApplicationBuilder configure(org.springframework.boot.builder.SpringApplicationBuilder builder) {
        return builder.sources(ToutiaoApplication.class);
    }
}
