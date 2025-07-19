package com.smartboy.zktlsloginbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.smartboy.zktlsloginbackend.bean.CommonResponse;
import com.smartboy.zktlsloginbackend.bean.entity.AppLoginStatus;
import com.smartboy.zktlsloginbackend.bean.entity.LoginRecord;
import com.smartboy.zktlsloginbackend.bean.req.AppLoginReq;
import com.smartboy.zktlsloginbackend.bean.req.AppUpdateStatus;
import com.smartboy.zktlsloginbackend.bean.req.LoginReq;
import com.smartboy.zktlsloginbackend.bean.vo.LoginVO;
import com.smartboy.zktlsloginbackend.service.AppLoginServiceImpl;
import com.smartboy.zktlsloginbackend.service.LoginServiceImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xuda
 * @Date 2025/7/13 15:31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppLoginController {
    private final AppLoginServiceImpl appLoginService;


    @PostMapping("/updateStatus")
    public CommonResponse<String> updateStatus(@RequestBody AppUpdateStatus appUpdateStatus){
        return CommonResponse.successOf(appLoginService.updateStatus(appUpdateStatus));
    }

    @PostMapping("/login")
    public CommonResponse<String> login(@RequestBody AppLoginReq loginReq) {
        return CommonResponse.successOf(appLoginService.login(loginReq));
    }

    @GetMapping("/getStatus")
    public CommonResponse<AppLoginStatus> getStatus(@RequestParam("loginId") String loginId) {
        return CommonResponse.successOf(appLoginService.getStatus(loginId));
    }

}
