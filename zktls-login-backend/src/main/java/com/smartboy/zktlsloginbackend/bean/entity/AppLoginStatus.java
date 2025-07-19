package com.smartboy.zktlsloginbackend.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2025/7/16 11:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppLoginStatus {
  private String status;
  private String token;
}
