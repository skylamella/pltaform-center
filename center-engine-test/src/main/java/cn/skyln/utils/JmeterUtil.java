package cn.skyln.utils;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.File;

/**
 * @author lamella
 * @description JmeterUtil TODO
 * @since 2024-04-18 21:56
 */
@Slf4j
public class JmeterUtil {

    public static void startJmeterTest(String jmxPath) {
        if (StringUtils.isBlank(jmxPath)) throw new BizException(BizCodeEnum.DATA_SPECIFICATION);
        try {
            // jmeter路径
            // C:\Users\lamel\AppData\LocalLow\jmeter55
            String jmeterPath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "LocalLow" + File.separator + "jmeter55";
            log.info("jmeter55 path:{}", jmeterPath);
            // jmeter根目录
            String jmeterHome = new File(jmeterPath).getPath();
            // JMX 文件
            File jmxFile = new File(jmxPath);
            // jmeter配置文件路径
            File jmeterProperties = new File(jmeterHome + File.separator + "bin" + File.separator + "jmeter.properties");
            // 设置JMeter根目录
            JMeterUtils.setJMeterHome(jmeterHome);
            // 加载jmeter配置文件
            JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
            // jmeter标准引擎
            StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();
            // 设置JMX脚本路径
            FileServer.getFileServer().setBaseForScript(jmxFile);
            // 加载测试计划树
            HashTree testPlanTree = SaveService.loadTree(jmxFile);
            // 转换测试计划
            JMeter.convertSubTree(testPlanTree,false);
        } catch (Exception e) {
            log.error("{}", e);
        }
    }
}
