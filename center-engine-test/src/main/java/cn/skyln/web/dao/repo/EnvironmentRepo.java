package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.EnvironmentDO;
import org.springframework.data.repository.Repository;

/**
 * @author lamella
 * @description EnvironmentRepo TODO
 * @since 2024-02-10 14:15
 */
public interface EnvironmentRepo extends Repository<EnvironmentDO, String> {

    EnvironmentDO getEnvironmentById(String id);
}
