package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.ReportDO;
import org.springframework.data.repository.Repository;

/**
 * @author lamella
 * @description ReportRepo TODO
 * @since 2024-02-10 14:15
 */
public interface ReportRepo extends Repository<ReportDO, String> {
    ReportDO getReportById(String id);
}
