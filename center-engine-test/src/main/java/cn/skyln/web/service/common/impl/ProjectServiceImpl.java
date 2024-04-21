package cn.skyln.web.service.common.impl;

import cn.skyln.interceptor.LoginInterceptor;
import cn.skyln.model.LoginUser;
import cn.skyln.web.dao.mapper.ProjectMapper;
import cn.skyln.web.dao.repo.ProjectRepo;
import cn.skyln.web.model.DO.ProjectDO;
import cn.skyln.web.model.DTO.ProjectDTO;
import cn.skyln.web.model.REQ.ProjectOperationReq;
import cn.skyln.web.service.common.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lamella
 * @description ProjectServiceImpl TODO
 * @since 2024-04-18 22:19
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<ProjectDTO> list() {
        return null;
    }

    @Override
    public int save(ProjectOperationReq projectSaveReq) {
        return 0;
    }

    @Override
    public int update(ProjectOperationReq projectOperationReq) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public ProjectDO getById(String projectId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        ProjectDO project = projectRepo.getProjectByIdAndProjectAdminId(projectId, loginUser.getId());
        return null;
    }
}
