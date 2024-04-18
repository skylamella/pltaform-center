package cn.skyln.web.controller.common;

import org.springframework.stereotype.Controller;

import java.io.File;

/**
 * @author lamella
 * @description TestController TODO
 * @since 2024-02-04 14:25
 */
@Controller
public class TestController {
    public static void main(String[] args) {
        String jmeterPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "LocalLow" + File.separator + "jmeter55";
        System.out.println(jmeterPath);
    }
}
