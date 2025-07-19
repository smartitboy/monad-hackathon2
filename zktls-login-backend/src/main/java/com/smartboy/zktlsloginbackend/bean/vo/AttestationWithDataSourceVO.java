package com.smartboy.zktlsloginbackend.bean.vo;

import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2025/7/15 11:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttestationWithDataSourceVO {
    private String dataSource;
    private Attestation attestation;
}
