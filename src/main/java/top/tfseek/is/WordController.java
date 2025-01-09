package top.tfseek.is;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1")
public class WordController {

    @GetMapping("/word")
    public ResponseEntity<String> getWord() {
        String url = "https://v1.hitokoto.cn/?c=i&encode=json";
        RestTemplate restTemplate = new RestTemplate();

        // 获取远程 API 数据
        String response = restTemplate.getForObject(url, String.class);

        // 显式设置响应的 Content-Type 为 JSON
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(response);
    }
}
