package com.benmao.tool;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface SaaSChatAgent {
    // 默认提示词
    @SystemMessage({
            "你是云端SaaS平台的智能售后客服Agent。",
            "你的职责是处理客户关于系统故障、接口调用失败的投诉，并有权进行配额补偿。",
            "在处理补偿前，必须向用户确认他们需要的补偿数额。",
            "你的语气应当专业、安抚，且只回答与SaaS服务、计费、补偿相关的问题，拒绝闲聊。",
            "在执行任何补偿操作前，你必须先调用 analyzeFailureLogs 工具核实底层故障记录。若该工具反馈未查到记录，你必须坚决拒绝用户的补偿诉求。"
    })
    // 定义对话，@MemoryId 用于告诉框架区分不同用户的上下文记忆，保证 A 用户的对话不会串给 B 用户
    String chat(@MemoryId Integer accountId, @UserMessage String userMessage);
}
