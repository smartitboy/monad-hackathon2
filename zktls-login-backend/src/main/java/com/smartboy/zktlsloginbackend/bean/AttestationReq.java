package com.smartboy.zktlsloginbackend.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @Author xuda
 * @Date 2025/7/13 15:32
 */
@lombok.NoArgsConstructor
@lombok.Data
public class AttestationReq {

  @JsonProperty("recipient")
  private String recipient;
  @JsonProperty("request")
  private RequestDTO request;
  @JsonProperty("reponseResolve")
  private List<ReponseResolveDTO> reponseResolve;
  @JsonProperty("data")
  private String data;
  @JsonProperty("attConditions")
  private String attConditions;
  @JsonProperty("timestamp")
  private Long timestamp;
  @JsonProperty("additionParams")
  private String additionParams;
  @JsonProperty("attestors")
  private List<AttestorsDTO> attestors;
  @JsonProperty("signatures")
  private List<String> signatures;

  @lombok.NoArgsConstructor
  @lombok.Data
  public static class RequestDTO {

    @JsonProperty("url")
    private String url;
    @JsonProperty("header")
    private String header;
    @JsonProperty("method")
    private String method;
    @JsonProperty("body")
    private String body;
  }

  @lombok.NoArgsConstructor
  @lombok.Data
  public static class ReponseResolveDTO {

    @JsonProperty("keyName")
    private String keyName;
    @JsonProperty("parseType")
    private String parseType;
    @JsonProperty("parsePath")
    private String parsePath;
  }

  @lombok.NoArgsConstructor
  @lombok.Data
  public static class AttestorsDTO {

    @JsonProperty("attestorAddr")
    private String attestorAddr;
    @JsonProperty("url")
    private String url;
  }
}
