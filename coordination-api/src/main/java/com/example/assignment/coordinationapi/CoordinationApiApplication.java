package com.example.assignment.coordinationapi;

import com.example.assignment.Assignment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.time.ZoneOffset;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackageClasses = {Assignment.class})
public class CoordinationApiApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(CoordinationApiApplication.class, args);
    }

}