package top.tfseek.is.user;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/v1/user")
public class LogoutController {

    @RequestMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // 获取当前时间
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 清除 cookie
        Cookie cookie = new Cookie("XnUserName", "0");
        cookie.setMaxAge(0);  // 设置过期时间为 0，表示删除 cookie
        cookie.setPath("/");  // 设置路径为根目录
        response.addCookie(cookie); // 添加 cookie 到响应中

        // 创建 JSON 响应
        String jsonResponse = String.format(
                "{\"code\": 200, \"msg\": \"成功\", \"time\": \"%s\"}",
                currentTime
        );

        // 返回 JSON 响应
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(jsonResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutWithGet(HttpServletResponse response) {
        // 获取当前时间
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 清除 cookie
        Cookie cookie = new Cookie("XnUserName", "0");
        cookie.setMaxAge(0);  // 设置过期时间为 0，表示删除 cookie
        cookie.setPath("/");  // 设置路径为根目录
        response.addCookie(cookie); // 添加 cookie 到响应中

        // 创建 JSON 响应
        String jsonResponse = String.format(
                "{\"code\": 200, \"msg\": \"成功\", \"time\": \"%s\"}",
                currentTime
        );

        // 返回 JSON 响应
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(jsonResponse);
    }
}
