package cn.skyln.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lamella
 * @description SpringBeanUtil TODO
 * @since 2024-02-10 15:25
 */
public class SpringBeanUtil {
    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            T t = target.getConstructor().newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> copyProperties(List<?> sourceList, Class<T> target) {
        return sourceList.stream().map(item -> copyProperties(item, target)).collect(Collectors.toList());
    }
}
