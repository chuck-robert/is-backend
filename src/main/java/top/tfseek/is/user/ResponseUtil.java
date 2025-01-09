package top.tfseek.is.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity<String> generateResponse(int code, String msg) {
        return generateResponse(code, msg, null);
    }

    public static ResponseEntity<String> generateResponse(int code, String msg, Map<String, Object> data) {
        try {
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // 将数据放入一个 Map 中，包括 code, msg, time 和 data
            Map<String, Object> response = Map.of(
                    "code", code,
                    "msg", msg,
                    "time", currentTime,
                    "data", data != null ? data : Map.of() // 使用空 Map 来避免 null 或 Object
            );

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);

            return ResponseEntity.status(code)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = String.format("{\"code\": 500, \"msg\": \"服务器错误\", \"time\": \"%s\"}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return ResponseEntity.status(500)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(errorResponse);
        }
    }
}
