package com.smartboy.zktlsloginbackend.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.smartboy.zktlsloginbackend.bean.attestation.AttNetworkRequest;
import com.smartboy.zktlsloginbackend.bean.attestation.AttNetworkResponseResolve;
import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;

/**
 * @Author xuda
 * @Date 2025/7/16 11:37
 */
@Component
@RequiredArgsConstructor
public class AttVerifyUtil {

  private final AttSignUtil attSignUtil;

  private static final String ATTESTOR = "0xdb736b13e2f522dbe18b2015d0291e4b193d8ef6";



  public String verifyAttestation(String dataSource, Attestation attestation) {
    String encodeAttestation = attSignUtil.encodeAttestation(attestation);
    String recoverAddress = new HashUtil().recoverAddress(Numeric.hexStringToByteArray(encodeAttestation),
                                                          attestation.getSignatures()[0]);
    String userName = null;
    if (!StrUtil.equalsIgnoreCase(ATTESTOR, recoverAddress)) {
      return null;
    }
    if (StrUtil.equals(dataSource, "twitter-attestation")) {
      AttNetworkRequest request = attestation.getRequest();
      String url = request.getUrl();
      if (!StrUtil.startWith(url, "https://api.x.com/1.1/account/settings.json")) {
        return null;
      }
      String method = request.getMethod();
      if (!StrUtil.equals(method, "GET")) {
        return null;
      }
      AttNetworkResponseResolve[] reponseResolve = attestation.getReponseResolve();
      String parsePath = reponseResolve[0].getParsePath();
      if (!StrUtil.equals(parsePath, "$.screen_name")) {
        return null;
      }
      String data = attestation.getData();
      Map bean = JSONUtil.toBean(data, Map.class);
      return bean.get("screen_name").toString();
    } else if (StrUtil.equals(dataSource, "google-attestation")) {
      AttNetworkRequest request = attestation.getRequest();
      String url = request.getUrl();
      if (!StrUtil.equals(url, "https://developers.google.com/_d/profile/user")) {
        return null;
      }
      String method = request.getMethod();
      if (!StrUtil.equals(method, "POST")) {
        return null;
      }
      AttNetworkResponseResolve[] reponseResolve = attestation.getReponseResolve();
      String parsePath = reponseResolve[0].getParsePath();
      if (!StrUtil.equals(parsePath, "$[2]")) {
        return null;
      }
      String data = attestation.getData();
      Map bean = JSONUtil.toBean(data, Map.class);
      return bean.get("2").toString();
    } else if (StrUtil.equals(dataSource, "xiaohongshu-attestation")) {
      AttNetworkRequest request = attestation.getRequest();
      String url = request.getUrl();
      if (!StrUtil.equals(url, "https://edith.xiaohongshu.com/api/sns/web/v2/user/me")) {
        return null;
      }
      String method = request.getMethod();
      if (!StrUtil.equals(method, "GET")) {
        return null;
      }
      AttNetworkResponseResolve[] reponseResolve = attestation.getReponseResolve();
      String parsePath = reponseResolve[0].getParsePath();
      if (!StrUtil.equals(parsePath, "$.data.red_id")) {
        return null;
      }
      String data = attestation.getData();
      Map bean = JSONUtil.toBean(data, Map.class);
      return bean.get("red_id").toString();
    } else {
      throw new RuntimeException("Data source not support!");
    }
  }

}
