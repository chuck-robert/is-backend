package top.tfseek.is;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理 404 错误，返回 JSON 格式
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handle404(Exception ex) {
        return buildErrorResponse(404, "Unprocessable Entity",
                "The required version parameter of the API service is not provided");
    }

    // 自定义错误响应模板
    private ResponseEntity<Object> buildErrorResponse(int code, String msg, String description) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(code);
        errorResponse.setMsg(msg);
        errorResponse.setData(new ErrorData(description));
        errorResponse.setCallback(new Callback());
        return ResponseEntity.status(code).body(errorResponse);
    }

    // 错误响应模型
    public static class ErrorResponse {
        private int code;
        private String msg;
        private ErrorData data;
        private Callback callback;

        // Getters and Setters
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public ErrorData getData() {
            return data;
        }

        public void setData(ErrorData data) {
            this.data = data;
        }

        public Callback getCallback() {
            return callback;
        }

        public void setCallback(Callback callback) {
            this.callback = callback;
        }
    }

    // 错误数据模型
    public static class ErrorData {
        private String msg;

        public ErrorData(String msg) {
            this.msg = msg;
        }

        // Getter and Setter
        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    // Callback 模型
    public static class Callback {
        private String[] content = {
                "缺少必要参数",
                "参数格式错误",
                "字段值无效",
                "请求无法处理"
        };
        private int code = 422;

        // Getter and Setter
        public String[] getContent() {
            return content;
        }

        public void setContent(String[] content) {
            this.content = content;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
