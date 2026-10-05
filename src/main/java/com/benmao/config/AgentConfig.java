package com.benmao.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

    /**
     * 配置多用户的对话记忆提供者
     * 每个 accountId 都会获得一个独立的记忆空间，保留最近的 10 条对话
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId) // 传入 accountId，实现多用户隔离
                .maxMessages(10) // 每个用户只记住最近10句话，防止内存溢出
                .build();
    }
}