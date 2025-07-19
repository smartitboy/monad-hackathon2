package com.smartboy.zktlsloginbackend.bean.attestation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2024/12/2 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attestor {
    private String attestorAddr;
    private String url;
}
