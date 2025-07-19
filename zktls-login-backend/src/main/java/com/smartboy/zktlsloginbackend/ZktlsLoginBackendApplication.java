package com.smartboy.zktlsloginbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xuda
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.smartboy"})
@ConfigurationPropertiesScan(basePackages = "com.smartboy")
public class ZktlsLoginBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZktlsLoginBackendApplication.class, args);
  }

}
