package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.StressCaseModuleDO;
import org.springframework.data.repository.Repository;

/**
 * @author lamella
 * @description StressCaseModuleRepo TODO
 * @since 2024-02-10 14:15
 */
public interface StressCaseModuleRepo extends Repository<StressCaseModuleDO, String> {

    StressCaseModuleDO getStressCaseModuleById(String id);
}
