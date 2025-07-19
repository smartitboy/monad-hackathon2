package com.smartboy.zktlsloginbackend.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.smartboy.zktlsloginbackend.bean.CommonResponse;
import com.smartboy.zktlsloginbackend.bean.entity.LoginRecord;
import com.smartboy.zktlsloginbackend.bean.req.LoginReq;
import com.smartboy.zktlsloginbackend.bean.vo.LoginVO;
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
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {
    private final LoginServiceImpl loginService;

    @PostMapping("/login")
    public CommonResponse<LoginVO> login(@RequestBody LoginReq loginReq) {
        return CommonResponse.successOf(loginService.login(loginReq));
    }

    @GetMapping("/loginRecords")
    public CommonResponse<List<LoginRecord>> loginRecords(@RequestParam("attestationIds") String attestationIds){
        if(StrUtil.isNotEmpty(attestationIds)){
            return CommonResponse.successOf(loginService.getLoginRecords(StrUtil.split(attestationIds, ',')));
        }
        return CommonResponse.successOf(new ArrayList<>());
    }

    @GetMapping("/ping")
    public String check() {
        return "pong";
    }

}
