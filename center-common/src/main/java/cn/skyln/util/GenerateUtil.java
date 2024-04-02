package cn.skyln.util;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lamella
 * @description GenerateUtil TODO
 * @since 2024-02-10 19:16
 */
public class GenerateUtil {
    private static final ClassLoader classLoader = GenerateUtil.class.getClassLoader();

    private static String getBasePath() {
        String path = classLoader.getResource("").getPath();
        String[] split = path.split("/");
        StringBuilder result = new StringBuilder();

        for (String s : split) {
            if (!s.isBlank()) {
                if (s.equals("platform-common")) {
                    break;
                }
                result.append(s).append("/");
            }
        }
        return result.toString();
    }

    private static List<Class<?>> getDoList(String modelName) throws ClassNotFoundException {
        String basePath = getBasePath() + modelName + "/src/main/java/cn/skyln/web/model/DO/";
        List<Class<?>> result = new ArrayList<>();
        for (File file : new File(basePath).listFiles()) {
            String className = file.getName().replace(".java", "");
            Class<?> aClass = classLoader.loadClass("cn.skyln.web.model.DO." + className);
            result.add(aClass);
        }
        return result;
    }

    private static Map<String, String> getRealPaths(String modelName) {
        String basePath = getBasePath() + modelName + "/src/main/";
        Map<String, String> result = new HashMap<>();
        result.put("mapperXml", basePath + "resources/mapper");
        basePath = basePath + "java/cn/skyln/web";
        result.put("mapper", basePath + "/dao/mapper");
        result.put("repo", basePath + "/dao/repo");
        result.put("service", basePath + "/service");
        result.put("serviceImpl", basePath + "/service/impl");
        return result;
    }

    public static void generateFiles(String modelName) throws ClassNotFoundException {
        List<Class<?>> classList = getDoList(modelName);
        for (Class<?> aClass : classList) {
            System.out.println(aClass.getName());
        }
        Map<String, String> realPaths = getRealPaths(modelName);
    }

    public static void main(String[] args) {
        try {
            List<Class<?>> classList = getDoList("platform-engine");
            for (Class<?> aClass : classList) {
                System.out.println(aClass.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(getRealPaths("platform-engine")));
    }

    private static void createFile(String path, String content) throws IOException {
        File dest = new File(path);
        if (!dest.exists()) {
            //dest.mkdirs();
            dest.createNewFile();
        }
        Files.writeString(dest.toPath(), content);
    }
}
