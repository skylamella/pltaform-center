package cn.skyln.web.service.common.impl;

import cn.skyln.web.model.DTO.ProjectDTO;
import cn.skyln.web.model.REQ.ProjectSaveReq;
import cn.skyln.web.model.REQ.ProjectUpdateReq;
import cn.skyln.web.service.common.ProjectService;

import java.util.List;

/**
 * @author lamella
 * @description ProjectServiceImpl TODO
 * @since 2024-04-18 22:19
 */
public class ProjectServiceImpl implements ProjectService {
    @Override
    public List<ProjectDTO> list() {
        return null;
    }

    @Override
    public int save(ProjectSaveReq projectSaveReq) {
        return 0;
    }

    @Override
    public int update(ProjectUpdateReq projectUpdateReq) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }
}
