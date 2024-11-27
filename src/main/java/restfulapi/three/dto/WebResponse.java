package restfulapi.three.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebResponse<T> {
    private int statusCode;
    private String message;
    private T data;
}

