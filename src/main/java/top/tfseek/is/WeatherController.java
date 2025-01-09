package top.tfseek.is;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    private static final String DEFAULT_CITY_CODE = "101010100"; // 默认城市代码（北京）

    @GetMapping("/weather")
    public ResponseEntity<Map<String, Object>> getWeather(@RequestParam(value = "city", required = false) String city) {
        String cityCode = getCityCodeByName(city != null ? city : "北京"); // 默认为北京
        Map<String, Object> response = new HashMap<>();

        if (cityCode != null) {
            String url = "http://t.weather.itboy.net/api/weather/city/" + cityCode;
            String weatherData = fetchUrlData(url);
            if (weatherData != null) {
                Map<String, Object> weatherResponse = parseJsonString(weatherData);
                if (weatherResponse != null) {
                    response.put("status", "success");
                    response.put("data", weatherResponse);
                } else {
                    response.put("status", "error");
                    response.put("message", "无法解析天气数据");
                }
            } else {
                response.put("status", "error");
                response.put("message", "无法获取天气数据");
            }
        } else {
            response.put("status", "error");
            response.put("message", "城市代码无效");
        }

        // 返回通用的 JSON 格式错误消息
        response.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 显式设置 Content-Type 为 JSON
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(response);
    }

    private String getCityCodeByName(String cityName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 使用 ClassPathResource 来加载资源文件
            ClassPathResource resource = new ClassPathResource("assets/json/city.json");
            JsonNode rootNode = objectMapper.readTree(resource.getInputStream());
            JsonNode cityArray = rootNode.get("city");

            if (cityArray != null && cityArray.isArray()) {
                for (JsonNode cityNode : cityArray) {
                    if (cityName.equals(cityNode.get("name").asText())) {
                        return cityNode.get("city_code").asText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DEFAULT_CITY_CODE; // 默认值
    }

    private String fetchUrlData(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> parseJsonString(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
