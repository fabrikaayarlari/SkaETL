package io.skalogs.skaetl.generator.credit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WebServiceData {
    private String type;
    private String microService;
    private String timestamp;
    private String timeRequestMs;
    private String requestId;
    private String uri;
    private String typeRequest;
    private String codeResponse;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String provider;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productName;
}
