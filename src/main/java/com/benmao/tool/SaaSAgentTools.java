package com.benmao.tool;

import com.benmao.service.SaaSResourceService;
import dev.langchain4j.agent.tool.Tool;
import lombok.val;
import org.springframework.stereotype.Component;

@Component //将本类交由 Spring 管理
public class SaaSAgentTools {
//TODO    @Autowired
//TODO    private SaaSResourceService saasResourceService;

    private final SaaSResourceService saasResourceService;

    // 采用构造器注入（比 @Autowired 更规范，Spring 官方推荐，上面注入的方式也可以但不推荐）
    public SaaSAgentTools(SaaSResourceService saasResourceService) {
        this.saasResourceService = saasResourceService;
    }

    @Tool("当用户遭遇系统故障时，为指定账户补偿 API 额度。需要传入账户ID（accountId）和补偿的额度数量（amount）。")
    public String compensateUserQuota(Integer accountId, Integer amount) {
        try {
            boolean result = saasResourceService.userQuotaCompensate(accountId, amount);
            if (result) {
                return "补偿成功：已为账户 " + accountId + " 增加额度 " + amount;
            } else {
                return "补偿失败：系统未能完成额度更新。";
            }
        }catch (IllegalArgumentException e) {
            return "补偿失败，参数校验未通过：" + e.getMessage();
        } catch (Exception e) {
            return "补偿失败，系统内部错误：" + e.getMessage();
        }
    }

    @Tool("在为用户发放补偿前，必须优先调用此工具，查询该账户近期是否真的发生了系统故障（如 500 或 429 错误）。如果没有查到错误日志，则必须拒绝补偿诉求。")
    public String analyzeFailureLogs(Integer accountId, Integer hours) {
        try {
            // 没有异常代表以及查出流水
            saasResourceService.getUsageLog(accountId, hours);
            return "业务核实完毕：该账户 " + accountId + " 在最近 " + hours + " 小时内确实存在 API 调用报错记录，符合补偿的前提条件。";

        } catch (IllegalArgumentException e) {
            return "探针调用失败，参数不合法：" + e.getMessage();

        } catch (RuntimeException e) {
            return "业务核实完毕：" + e.getMessage();

        } catch (Exception e) {
            return "探针执行失败，发生了未知的系统内部异常：" + e.getMessage();
        }
    }
}
