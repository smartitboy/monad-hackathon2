package com.smartboy.zktlsloginbackend.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;
import com.smartboy.zktlsloginbackend.bean.req.AttestationUploadReq;
import com.smartboy.zktlsloginbackend.bean.vo.AttestationWithDataSourceVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author xuda
 * @Date 2025/7/15 11:03
 */
@Service
public class AttestationServiceImpl {

  @Value("${config.attestation-storage-path}")
  private String attestationPath;


  public void saveAttestation(AttestationUploadReq attestationUploadReq){
    Attestation attestation = attestationUploadReq.getAttestation();
    String attId = attestationUploadReq.getAttId();
    String dataSource = attestationUploadReq.getDataSource();
    AttestationWithDataSourceVO attestation1 = new AttestationWithDataSourceVO(dataSource, attestation);
    String path = attestationPath + attId + ".json";
    FileUtil.writeBytes(JSONUtil.toJsonStr(attestation1).getBytes(), path);
  }


  public AttestationWithDataSourceVO getAttestation(String attId) {
    String path = attestationPath + attId + ".json";
    return JSONUtil.toBean(FileUtil.readUtf8String(path), AttestationWithDataSourceVO.class);
  }

}
