package cn.skyln.component.factories.strategy;

public interface SendCodeStrategy {
    void sendCode(String enumName, String to, String code);
}
