package com.smartboy.zktlsloginbackend.controller;

import com.smartboy.zktlsloginbackend.bean.CommonResponse;
import com.smartboy.zktlsloginbackend.bean.req.LoginReq;
import com.smartboy.zktlsloginbackend.bean.vo.LoginVO;
import com.smartboy.zktlsloginbackend.cache.SessionCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xuda
 * @Date 2025/7/13 19:43
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {

  private final SessionCache sessionCache;

  @GetMapping("/login/check")
  public CommonResponse<String> check( HttpServletRequest request) {
    String token = request.getHeader("token");
    String user = sessionCache.getSession(token);
    if(user == null){
      return CommonResponse.failure("NOT_LOGIN","请先登录！");
    }
    return CommonResponse.successOf(user);
  }

}
