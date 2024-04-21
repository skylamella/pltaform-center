package cn.skyln.web.service.common.impl;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.exception.BizException;
import cn.skyln.interceptor.LoginInterceptor;
import cn.skyln.model.LoginUser;
import cn.skyln.web.dao.mapper.EnvironmentMapper;
import cn.skyln.web.dao.repo.EnvironmentRepo;
import cn.skyln.web.model.DO.EnvironmentDO;
import cn.skyln.web.model.DO.ProjectDO;
import cn.skyln.web.model.DTO.EnvironmentDTO;
import cn.skyln.web.model.REQ.EnvironmentOperationReq;
import cn.skyln.web.service.common.EnvironmentService;
import cn.skyln.web.service.common.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author lamella
 * @description EnvironmentServiceImpl TODO
 * @since 2024-04-18 22:18
 */
@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private EnvironmentMapper environmentMapper;

    @Autowired
    private ProjectService projectService;

    @Override
    public List<EnvironmentDTO> list(String projectId) {
        return environmentRepo.findAllByProjectId(projectId);
    }

    @Override
    public int save(EnvironmentOperationReq req) {
        ProjectDO projectDO = projectService.getById(req.getProjectId());
        if (Objects.isNull(projectDO)) {
            throw new BizException(BizCodeEnum.PROJECT_NOT_EXIT);
        }
        EnvironmentDO environmentDO = new EnvironmentDO();
        BeanUtils.copyProperties(req, environmentDO);
        return environmentMapper.insert(environmentDO);
    }

    @Override
    public int update(EnvironmentOperationReq req) {
        EnvironmentDO environmentDO = environmentMapper.selectById(req.getId());
        if (Objects.isNull(environmentDO)) {
            return save(req);
        } else {
            BeanUtils.copyProperties(req, environmentDO);
            return environmentMapper.updateById(environmentDO);
        }
    }

    @Override
    public int delete(String projectId, String id) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        return 0;
    }
}
