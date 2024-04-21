package cn.skyln.web.service.common;

import cn.skyln.web.model.DO.ProjectDO;
import cn.skyln.web.model.DTO.ProjectDTO;
import cn.skyln.web.model.REQ.ProjectOperationReq;

import java.util.List;

/**
 * @author lamella
 * @description ProjectService TODO
 * @since 2024-04-18 22:16
 */
public interface ProjectService {
    /**
     * 获取用户的项目列表
     *
     * @return
     */
    List<ProjectDTO> list();

    /**
     * 保存项目
     *
     * @param projectSaveReq
     * @return
     */
    int save(ProjectOperationReq projectSaveReq);

    /**
     * 更新项目
     *
     * @param projectOperationReq
     * @return
     */
    int update(ProjectOperationReq projectOperationReq);

    /**
     * 删除项目
     *
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 查找项目
     *
     * @param projectId 项目ID
     * @return
     */
    ProjectDO getById(String projectId);
}
