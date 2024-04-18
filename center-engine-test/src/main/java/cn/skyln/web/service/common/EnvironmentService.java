package cn.skyln.web.service.common;

import cn.skyln.web.model.DTO.EnvironmentDTO;
import cn.skyln.web.model.REQ.EnvironmentSaveReq;
import cn.skyln.web.model.REQ.EnvironmentUpdateReq;

import java.util.List;

/**
 * @author lamella
 * @description EnvironmentServiceImpl TODO
 * @since 2024-04-18 22:16
 */
public interface EnvironmentService {
    List<EnvironmentDTO> list(String projectId);

    int save(EnvironmentSaveReq req);

    int update(EnvironmentUpdateReq req);

    int delete(String projectId, String id);
}
