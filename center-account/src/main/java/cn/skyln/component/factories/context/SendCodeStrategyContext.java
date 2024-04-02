package cn.skyln.component.factories.context;

import cn.skyln.component.factories.strategy.SendCodeStrategy;

public class SendCodeStrategyContext {

    private final SendCodeStrategy strategy;

    public SendCodeStrategyContext(SendCodeStrategy strategy) {
        this.strategy = strategy;
    }

    public void sendCode(String enumName, String to, String code) {
        this.strategy.sendCode(enumName, to, code);
    }
}
