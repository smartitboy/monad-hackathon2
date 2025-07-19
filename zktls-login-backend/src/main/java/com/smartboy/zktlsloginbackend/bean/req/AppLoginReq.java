package com.smartboy.zktlsloginbackend.bean.req;

import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2025/7/13 16:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppLoginReq {
    private String loginId;
    private String dataSource;
    private Attestation attestation;
}
