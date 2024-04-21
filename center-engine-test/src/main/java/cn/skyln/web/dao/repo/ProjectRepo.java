package cn.skyln.web.dao.repo;

import cn.skyln.web.model.DO.ProjectDO;
import org.springframework.data.repository.Repository;

/**
 * @author lamella
 * @description ProjectRepo TODO
 * @since 2024-02-10 14:15
 */
public interface ProjectRepo extends Repository<ProjectDO, String> {

    ProjectDO getProjectById(String id);

    ProjectDO getProjectByIdAndProjectAdminId(String id, String projectAdminId);
}
