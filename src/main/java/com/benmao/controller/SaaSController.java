package com.benmao.controller;

import com.benmao.tool.SaaSChatAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class SaaSController {
    @Autowired
    private SaaSChatAgent saaSChatAgent;

    @PostMapping("/api/chat")
    public String chatRes(@RequestParam("accountId") Integer accountId,@RequestParam("message") String message) {
        String prompt = "用户账户 " + accountId + " 说：" + message;
        String result = saaSChatAgent.chat(accountId, prompt);
        return result;
    }
}
