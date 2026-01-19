import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class QwenClient {

    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    private static final String API_KEY = "YOUR_DASHSCOPE_API_KEY"; // 替换为你的 Key
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String callQwen(String prompt) {
        try {
            // 构建请求体
            String jsonBody = "{\n" +
                "  \"model\": \"qwen-plus\",\n" +
                "  \"input\": {\n" +
                "    \"messages\": [\n" +
                "      {\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}\n" +
                "    ]\n" +
                "  },\n" +
                "  \"parameters\": {\n" +
                "    \"max_tokens\": 500,\n" +
                "    \"temperature\": 0.7\n" +
                "  }\n" +
                "}";

            Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8")))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "❌ HTTP 错误: " + response.code();
                }

                String responseBody = response.body().string();
                JsonNode root = mapper.readTree(responseBody);
                JsonNode output = root.path("output").path("text");

                if (output.isMissingNode()) {
                    return "❌ 解析失败: " + responseBody;
                }
                return output.asText().trim();
            }
        } catch (IOException e) {
            return "❌ 网络异常: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        String prompt = "请用一句话解释什么是人工智能？";
        String answer = callQwen(prompt);
        System.out.println("👤 用户: " + prompt);
        System.out.println("🤖 Qwen: " + answer);
    }
}