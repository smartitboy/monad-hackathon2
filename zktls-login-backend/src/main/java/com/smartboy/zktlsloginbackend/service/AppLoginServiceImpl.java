package com.smartboy.zktlsloginbackend.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.smartboy.zktlsloginbackend.bean.entity.AppLoginStatus;
import com.smartboy.zktlsloginbackend.bean.entity.LoginRecord;
import com.smartboy.zktlsloginbackend.bean.req.AppUpdateStatus;
import com.smartboy.zktlsloginbackend.cache.AppLoginStatusCache;
import com.smartboy.zktlsloginbackend.cache.LoginRecordCache;
import com.smartboy.zktlsloginbackend.cache.SessionCache;
import com.smartboy.zktlsloginbackend.util.AttVerifyUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.smartboy.zktlsloginbackend.bean.req.AppLoginReq;

/**
 * @Author xuda
 * @Date 2025/7/16 11:36
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AppLoginServiceImpl {
    private final AttVerifyUtil attVerifyUtil;
    private final AppLoginStatusCache appLoginStatusCache;
    private final SessionCache sessionCache;
    private final LoginRecordCache loginRecordCache;



    public String updateStatus(AppUpdateStatus updateStatus) {
        if(appLoginStatusCache.getStatus(updateStatus.getLoginId()) != null) {
            AppLoginStatus status = appLoginStatusCache.getStatus(updateStatus.getLoginId());
            String status1 = status.getStatus();
            if(StrUtil.equals(status1, "success")){
                log.info("login id:{}, hash login", updateStatus.getLoginId());
                return "success";
            }
        }
        AppLoginStatus appLoginStatus = new AppLoginStatus();
        appLoginStatus.setStatus(updateStatus.getStatus());
        appLoginStatusCache.setStatus(updateStatus.getLoginId(), appLoginStatus);
        log.info("login id:{}, update status:{}", updateStatus.getLoginId(),appLoginStatus.getStatus());
        return "success";
    }

    public String login(AppLoginReq appLoginReq) {
        String userName = attVerifyUtil.verifyAttestation(appLoginReq.getDataSource(),
                                                   appLoginReq.getAttestation());
        String token = RandomUtil.randomString(20);
        sessionCache.addSession(token, userName);
        AppLoginStatus status = appLoginStatusCache.getStatus(appLoginReq.getLoginId());
        if(status == null){
            status = new AppLoginStatus();
        }
        status.setStatus("success");
        status.setToken(token);
        appLoginStatusCache.setStatus(appLoginReq.getLoginId(), status);
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setAttestationId(appLoginReq.getAttestation().getRecipient() + "-" + appLoginReq.getAttestation().getTimestamp());
        loginRecord.setUserName(userName);
        loginRecord.setDataSource(appLoginReq.getDataSource());
        loginRecord.setLoginDate(LocalDateTime.now());
        loginRecordCache.addRecord(appLoginReq.getAttestation().getRecipient() + "-" + appLoginReq.getAttestation().getTimestamp(),loginRecord);
        log.info("login id:{},login success with attestation:{}", appLoginReq.getLoginId(), appLoginReq.getAttestation());
        return "success";
    }

    public AppLoginStatus getStatus(String loginId) {
        return appLoginStatusCache.getStatus(loginId);
    }
}
