package com.xixiy.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.format.datetime.standard.Jsr310DateTimeFormatAnnotationFormatterFactory;

@SpringBootApplication
@MapperScan("com.xixiy.seckill.mapper")
@EntityScan(basePackageClasses = {SeckillDemoApplication.class , Jsr310DateTimeFormatAnnotationFormatterFactory.class})
public class SeckillDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }

}
