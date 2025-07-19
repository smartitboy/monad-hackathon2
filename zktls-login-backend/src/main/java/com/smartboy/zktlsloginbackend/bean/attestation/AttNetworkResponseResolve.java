package com.smartboy.zktlsloginbackend.bean.attestation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2024/12/2 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttNetworkResponseResolve {
    private String keyName;
    private String parseType;
    private String parsePath;
}
