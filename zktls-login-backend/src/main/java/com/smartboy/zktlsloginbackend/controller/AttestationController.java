package com.smartboy.zktlsloginbackend.controller;

import com.smartboy.zktlsloginbackend.bean.CommonResponse;
import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;
import com.smartboy.zktlsloginbackend.bean.req.AttestationUploadReq;
import com.smartboy.zktlsloginbackend.bean.vo.AttestationWithDataSourceVO;
import com.smartboy.zktlsloginbackend.service.AttestationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.signers.ISOTrailers;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xuda
 * @Date 2025/7/15 11:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AttestationController {

    private final AttestationServiceImpl attestationService;

    @PostMapping("/uploadAttestation")
    public String uploadAttestation(@RequestBody AttestationUploadReq attestationUploadReq) {
        attestationService.saveAttestation(attestationUploadReq);
        return "success";
    }

    @GetMapping("/getAttestation")
    public CommonResponse<AttestationWithDataSourceVO> getAttestation(@RequestParam("attId") String attId) {
        return CommonResponse.successOf(attestationService.getAttestation(attId));
    }

}
