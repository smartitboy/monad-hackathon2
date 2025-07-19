package com.smartboy.zktlsloginbackend.bean.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2025/7/16 11:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUpdateStatus {
  private String loginId;
  private String status;
}
