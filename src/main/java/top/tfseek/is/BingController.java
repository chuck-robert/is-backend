package top.tfseek.is;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.StringReader;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@RestController
public class BingController {

    private static final String BING_URL = "https://cn.bing.com/HPImageArchive.aspx?idx=0&n=1";
    private static final String LOCAL_IMAGE_URL_BASE = "https://api.stear.cn/v1/bing";

    @GetMapping("/v1/bing")
    public ResponseEntity<byte[]> getBingImage(@RequestParam(value = "format", required = false) String format) {
        try {
            if (format == null) {
                // 获取 Bing 图片数据
                String xmlResponse = fetchBingImageData();

                String imageUrl = parseImageUrl(xmlResponse);
                String fullImageUrl = "https://cn.bing.com" + imageUrl;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(fullImageUrl))
                        .build();
                HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

                if (response.statusCode() == 200) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")  // 设置图片类型
                            .body(response.body());
                } else {
                    return ResponseEntity.status(response.statusCode()).build();
                }
            }

            if ("json".equals(format)) {
                String xmlResponse = fetchBingImageData();
                String jsonResponse = convertXmlToJson(xmlResponse);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                        .body(jsonResponse.getBytes());
            }

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // 捕获并打印详细的错误信息
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();  // 打印堆栈跟踪
            return ResponseEntity.status(500).body("Internal Server Error".getBytes());
        }
    }

    // 从 Bing API 获取 XML 数据
    private String fetchBingImageData() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(BING_URL, String.class);
    }

    // 解析 XML 数据，获取图片的 URL
    private String parseImageUrl(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        // 获取 XML 中的 URL 节点
        NodeList nodeList = doc.getElementsByTagName("url");
        if (nodeList.getLength() == 0) {
            throw new Exception("No image URL found in the response.");
        }

        // 取出图片的 URL
        return nodeList.item(0).getTextContent();
    }

    // 将 XML 数据转换为 JSON，并替换 URL

    private String convertXmlToJson(String xml) throws Exception {
        // 使用 org.json 库的 XML.toJSONObject() 方法将 XML 转换为 JSON
        JSONObject jsonObject = org.json.XML.toJSONObject(xml);

        // 获取 images 节点中的单一 image 对象
        JSONObject imageNode = jsonObject.getJSONObject("images").getJSONObject("image");

        // 创建一个新的 JSONObject 并将 imageNode 中的字段提取到这个新的 JSONObject 中
        JSONObject result = new JSONObject();
        result.put("copyright", imageNode.getString("copyright"));
        result.put("enddate", imageNode.getLong("enddate"));
        result.put("fullstartdate", imageNode.getLong("fullstartdate"));
        result.put("startdate", imageNode.getLong("startdate"));
        result.put("headline", imageNode.getString("headline"));
        result.put("hotspots", imageNode.getString("hotspots"));
        result.put("url", LOCAL_IMAGE_URL_BASE);  // 使用准备好的本地 URL

        // 获取并解码 copyrightlink
        String copyrightLink = imageNode.getString("copyrightlink");
        String decodedCopyrightLink = URLDecoder.decode(copyrightLink, StandardCharsets.UTF_8.name());
        result.put("searchURL", decodedCopyrightLink);

        // 返回更新后的 JSON 字符串
        return result.toString(4);  // 格式化输出 JSON
    }



}
