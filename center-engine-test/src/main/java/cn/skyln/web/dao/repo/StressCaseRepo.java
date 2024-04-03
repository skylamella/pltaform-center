package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.StressCaseDO;
import org.springframework.data.repository.Repository;

/**
 * @author lamella
 * @description StressCaseRepo TODO
 * @since 2024-02-10 14:15
 */
public interface StressCaseRepo extends Repository<StressCaseDO, String> {

    StressCaseDO getStressCaseDOById(String id);
}
