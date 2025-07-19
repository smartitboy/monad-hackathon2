package com.smartboy.zktlsloginbackend.bean.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2025/7/15 15:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRecord {
    private String attestationId;
    private String userName;
    private String dataSource;
    private LocalDateTime loginDate;
}
