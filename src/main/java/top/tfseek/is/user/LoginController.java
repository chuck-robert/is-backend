package top.tfseek.is.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // POST 请求
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "password", required = false) String password) {

        // 如果没有传递 username 或 password 参数，返回 403
        if (username == null || password == null) {
            return ResponseUtil.generateResponse(403, "缺少必要参数");
        }

        // 将密码使用 sha1 加密
        String hashedPassword = org.apache.commons.codec.digest.DigestUtils.sha1Hex(password);

        // SQL 查询
        String query = "SELECT * FROM `xn_user` WHERE `username` = ? AND `password` = ?";

        try {
            // 执行查询
            List<Map<String, Object>> users = jdbcTemplate.queryForList(query, username, hashedPassword);

            if (users.isEmpty()) {
                // 返回账号或者密码错误的响应
                return ResponseUtil.generateResponse(403, "账号或者密码错误");
            }

            // 获取 token
            String token = (String) users.get(0).get("token");

            // 获取用户信息
            Map<String, Object> userData = Map.of(
                    "username", users.get(0).get("username"),
                    "nick", users.get(0).get("nick"),
                    "sex", users.get(0).get("sex"),
                    "avatar", users.get(0).get("avatar")
            );

            // 返回成功响应，并设置 cookie 和用户数据
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.SET_COOKIE, "XnUserName=" + token + "; Path=/; Max-Age=2592000")
                    .body(ResponseUtil.generateResponse(200, "成功", userData).getBody());
        } catch (Exception e) {
            // 服务器错误
            return ResponseUtil.generateResponse(500, "服务器错误");
        }
    }


    // 检查用户是否已经登录
    @RequestMapping("/info")
    public ResponseEntity<String> checkLogin(@CookieValue(value = "XnUserName", required = false) String token) {

        // 如果没有找到 token，返回正常访问
        if (token == null) {
            return ResponseUtil.generateResponse(200, "未登录，请先登录");
        }

        // 如果有 token，检查数据库
        String query = "SELECT * FROM `xn_user` WHERE `token` = ?";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(query, token);

        if (!users.isEmpty()) {
            // 如果数据库中找到了用户，返回已登录并包含用户信息
            Map<String, Object> userData = Map.of(
                    "username", users.get(0).get("username"),
                    "nick", users.get(0).get("nick"),
                    "sex", users.get(0).get("sex"),
                    "avatar", users.get(0).get("avatar")
            );
            return ResponseUtil.generateResponse(200, "已登录", userData);
        } else {
            // 如果数据库中找不到该 token，删除 cookie 并提示重新登录
            return ResponseEntity.status(401)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.SET_COOKIE, "XnUserName=; Path=/; Max-Age=0")
                    .body("{\"code\": 401, \"msg\": \"数据错误，请重新登录\", \"time\": \"" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"}");
        }
    }


    // 如果是 GET 请求，返回提示需要使用 POST 请求
    @GetMapping("/login")
    public ResponseEntity<String> getLogin() {
        return ResponseUtil.generateResponse(405, "请使用 POST 请求");
    }
}
