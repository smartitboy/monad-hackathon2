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
public class Attestation {
    private String recipient;
    private AttNetworkRequest request;
    private AttNetworkResponseResolve[] reponseResolve;
    private String data;
    private String attConditions;
    private Long timestamp;
    private String additionParams;
    private Attestor[] attestors;
    private String[] signatures;
}
