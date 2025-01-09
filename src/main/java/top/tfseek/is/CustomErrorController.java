package top.tfseek.is;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CustomErrorController {

    @GetMapping(value = "/error/404", produces = "application/json")
    public ResponseEntity<Map<String, Object>> error404Response() {
        // 构造 404 错误的 JSON 数据
        Map<String, Object> response = Map.of(
                "time", System.currentTimeMillis() / 1000, // 当前时间戳（秒）
                "code", 404,
                "msg", "Not Found",
                "callback", Map.of(
                        "code", 404,
                        "content", List.of(
                                "资源未找到",
                                "请求的页面不存在",
                                "未能找到该链接"
                        )
                ),
                "data", Map.of(
                        "state", "error",
                        "msg", "The requested resource could not be found"
                )
        );

        // 返回 ResponseEntity，设置状态码为 404，并保证返回的是 JSON 格式
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/error/403", produces = "application/json")
    public ResponseEntity<Map<String, Object>> error403Response() {
        // 构造 403 错误的 JSON 数据
        Map<String, Object> response = Map.of(
                "time", System.currentTimeMillis() / 1000, // 当前时间戳（秒）
                "code", 403,
                "msg", "Forbidden",
                "callback", Map.of(
                        "code", 403,
                        "content", List.of(
                                "没有权限访问此资源",
                                "禁止访问",
                                "您的请求没有访问权限"
                        )
                ),
                "data", Map.of(
                        "state", "error",
                        "msg", "Access to the requested resource is forbidden"
                )
        );

        // 返回 ResponseEntity，设置状态码为 403，并保证返回的是 JSON 格式
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/error/500", produces = "application/json")
    public ResponseEntity<Map<String, Object>> error500Response() {
        // 构造 500 错误的 JSON 数据
        Map<String, Object> response = Map.of(
                "time", System.currentTimeMillis() / 1000, // 当前时间戳（秒）
                "code", 500,
                "msg", "Internal Server Error",
                "callback", Map.of(
                        "code", 500,
                        "content", List.of(
                                "服务器内部错误",
                                "服务器遇到未知错误",
                                "无法处理请求"
                        )
                ),
                "data", Map.of(
                        "state", "error",
                        "msg", "An unexpected error occurred on the server"
                )
        );

        // 返回 ResponseEntity，设置状态码为 500，并保证返回的是 JSON 格式
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/error/502", produces = "application/json")
    public ResponseEntity<Map<String, Object>> error502Response() {
        // 构造 502 错误的 JSON 数据
        Map<String, Object> response = Map.of(
                "time", System.currentTimeMillis() / 1000, // 当前时间戳（秒）
                "code", 502,
                "msg", "Bad Gateway",
                "callback", Map.of(
                        "code", 502,
                        "content", List.of(
                                "网关错误",
                                "无效的响应",
                                "上游服务器无法响应"
                        )
                ),
                "data", Map.of(
                        "state", "error",
                        "msg", "The server received an invalid response from an upstream server"
                )
        );

        // 返回 ResponseEntity，设置状态码为 502，并保证返回的是 JSON 格式
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @GetMapping(value = "/error/429", produces = "application/json")
    public ResponseEntity<Map<String, Object>> error429Response() {
        // 构造 429 错误的 JSON 数据
        Map<String, Object> response = Map.of(
                "time", System.currentTimeMillis() / 1000, // 当前时间戳（秒）
                "code", 429,
                "msg", "Too Many Requests",
                "callback", Map.of(
                        "code", 429,
                        "content", List.of(
                                "请求过多",
                                "超出请求限制",
                                "请稍后再试"
                        )
                ),
                "data", Map.of(
                        "state", "error",
                        "msg", "You have sent too many requests in a given amount of time"
                )
        );

        // 返回 ResponseEntity，设置状态码为 429，并保证返回的是 JSON 格式
        return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
    }
}
