package com.smartboy.zktlsloginbackend.bean.attestation.sign;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xuda
 * @Date 2024/12/2 17:09
 */
@NoArgsConstructor
@Data
public class ParseSchema {

    @JsonProperty("conditions")
    private ConditionsDTO conditions;

    @NoArgsConstructor
    @Data
    public static class ConditionsDTO {
        @JsonProperty("type")
        private String type;
        @JsonProperty("op")
        private String op;
        @JsonProperty("subconditions")
        private List<SubconditionsDTO> subconditions;

        @NoArgsConstructor
        @Data
        public static class SubconditionsDTO {
            @JsonProperty("field")
            private String field;
            @JsonProperty("op")
            private String op;
            @JsonProperty("reveal_id")
            private String revealId;
            @JsonProperty("type")
            private String type;
            @JsonProperty("value")
            private String value;
            @JsonProperty("subconditions")
            private List<SubconditionsDTO> subconditions;
        }
    }
}
