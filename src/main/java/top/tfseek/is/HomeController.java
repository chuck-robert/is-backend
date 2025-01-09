package top.tfseek.is;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Map<String, Object>> errorResponse() {
        // 构造 JSON 数据
        Map<String, Object> response = Map.of(
                "time", System.currentTimeMillis() / 1000, // 当前时间戳（秒）
                "code", 422,
                "msg", "Unprocessable Entity",
                "callback", Map.of(
                        "code", 422,
                        "content", List.of(
                                "缺少必要参数",
                                "参数格式错误",
                                "字段值无效",
                                "请求无法处理"
                        )
                ),
                "data", Map.of(
                        "state", "error",
                        "msg", "The required version parameter of the API service is not provided"
                )
        );

        // 返回 ResponseEntity，设置状态码为 403
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
