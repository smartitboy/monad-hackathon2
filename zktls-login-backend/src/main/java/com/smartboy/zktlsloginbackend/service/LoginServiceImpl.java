package com.smartboy.zktlsloginbackend.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.smartboy.zktlsloginbackend.bean.attestation.AttNetworkRequest;
import com.smartboy.zktlsloginbackend.bean.attestation.AttNetworkResponseResolve;
import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;
import com.smartboy.zktlsloginbackend.bean.entity.LoginRecord;
import com.smartboy.zktlsloginbackend.cache.LoginRecordCache;
import com.smartboy.zktlsloginbackend.cache.SessionCache;
import com.smartboy.zktlsloginbackend.util.AttSignUtil;
import com.smartboy.zktlsloginbackend.util.AttVerifyUtil;
import com.smartboy.zktlsloginbackend.util.HashUtil;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.smartboy.zktlsloginbackend.bean.req.LoginReq;
import com.smartboy.zktlsloginbackend.bean.vo.LoginVO;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.web3j.utils.Numeric;

/**
 * @Author xuda
 * @Date 2025/7/13 15:32
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl {
    private final SessionCache sessionCache;
    private final LoginRecordCache loginRecordCache;
    private final AttVerifyUtil attVerifyUtil;

    public LoginVO login(LoginReq loginReq) {
        try {
            LoginVO loginVO = new LoginVO();
            String userName = attVerifyUtil.verifyAttestation(loginReq.getDataSource(), loginReq.getAttestation());
            if (userName != null) {
                loginVO.setToken(RandomUtil.randomString(20));
                sessionCache.addSession(loginVO.getToken(), userName);
                Attestation attestation = loginReq.getAttestation();
                String attestationId = attestation.getRecipient() + "-" + attestation.getTimestamp();
                loginRecordCache.addRecord(attestationId,
                    new LoginRecord(attestationId, userName, loginReq.getDataSource(), LocalDateTime.now()));
                return loginVO;
            } else {
                throw new RuntimeException("Attestation verify failed!");
            }
        } catch (Exception e) {
            log.error("Login failed!", e);
            throw new RuntimeException(e);
        }
    }

    public List<LoginRecord> getLoginRecords(List<String> attestationIds){
        List<LoginRecord> loginRecords = new LinkedList<>();
        for (String attestationId : attestationIds) {
            if(loginRecordCache.getRecords(attestationId)!=null){
                loginRecords.addAll(loginRecordCache.getRecords(attestationId));
            }
        }
        return loginRecords;
    }



}
