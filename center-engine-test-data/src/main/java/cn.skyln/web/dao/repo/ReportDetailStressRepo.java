package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.ReportDetailStressDO;
import org.springframework.data.repository.Repository;

/**
 * @author lamella
 * @description ReportDetailStressRepo TODO
 * @since 2024-02-10 14:15
 */
public interface ReportDetailStressRepo extends Repository<ReportDetailStressDO, String> {

    ReportDetailStressDO getReportDetailStressDOById(String id);

}
