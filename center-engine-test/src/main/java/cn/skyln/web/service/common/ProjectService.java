package cn.skyln.web.service.common;

import cn.skyln.web.model.DTO.ProjectDTO;
import cn.skyln.web.model.REQ.ProjectSaveReq;
import cn.skyln.web.model.REQ.ProjectUpdateReq;

import java.util.List;

/**
 * @author lamella
 * @description ProjectService TODO
 * @since 2024-04-18 22:16
 */
public interface ProjectService {
    /**
     * 获取用户的项目列表
     * @return
     */
    List<ProjectDTO> list();

    /**
     * 保存项目
     * @param projectSaveReq
     * @return
     */
    int save(ProjectSaveReq projectSaveReq);

    /**
     * 更新项目
     * @param projectUpdateReq
     * @return
     */
    int update(ProjectUpdateReq projectUpdateReq);

    /**
     * 删除项目
     * @param id
     * @return
     */
    int delete(String id);
}
