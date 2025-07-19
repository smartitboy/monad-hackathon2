package com.smartboy.zktlsloginbackend.bean.req;

import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2025/7/15 11:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttestationUploadReq {
    private String attId;
    private String dataSource;
    private Attestation attestation;
}
