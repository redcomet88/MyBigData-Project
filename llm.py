import dashscope
from dashscope import Generation

# 设置你的 API Key（建议用环境变量）
dashscope.api_key = "YOUR_DASHSCOPE_API_KEY"

def call_qwen(prompt: str):
    try:
        response = Generation.call(
            model="qwen-plus",  # 可选: qwen-turbo, qwen-max, qwen-plus
            prompt=prompt,
            max_tokens=500,
            temperature=0.7
        )
        if response.status_code == 200:
            return response.output.text.strip()
        else:
            return f"❌ 请求失败: {response.code} - {response.message}"
    except Exception as e:
        return f"❌ 异常: {str(e)}"

# 使用示例
if __name__ == "__main__":
    user_input = "请用一句话解释什么是人工智能？"
    answer = call_qwen(user_input)
    print("👤 用户:", user_input)
    print("🤖 Qwen:", answer)