package cn.skyln.web.service.common.impl;

import cn.skyln.web.dao.mapper.EnvironmentMapper;
import cn.skyln.web.dao.repo.EnvironmentRepo;
import cn.skyln.web.model.DO.EnvironmentDO;
import cn.skyln.web.model.DTO.EnvironmentDTO;
import cn.skyln.web.model.REQ.EnvironmentSaveReq;
import cn.skyln.web.model.REQ.EnvironmentUpdateReq;
import cn.skyln.web.service.common.EnvironmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * @author lamella
 * @description EnvironmentServiceImpl TODO
 * @since 2024-04-18 22:18
 */
public class EnvironmentServiceImpl implements EnvironmentService {

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private EnvironmentMapper environmentMapper;

    @Override
    public List<EnvironmentDTO> list(String projectId) {
        return environmentRepo.findAllByProjectId(projectId);
    }

    @Override
    public int save(EnvironmentSaveReq req) {
        EnvironmentDO environmentDO = new EnvironmentDO();
        BeanUtils.copyProperties(req, environmentDO);
        return environmentMapper.insert(environmentDO);
    }

    @Override
    public int update(EnvironmentUpdateReq req) {
        EnvironmentDO environmentDO = environmentMapper.selectById(req.getId());
        if (Objects.isNull(environmentDO)) {
            environmentDO = new EnvironmentDO();
            BeanUtils.copyProperties(req, environmentDO);
            return environmentMapper.insert(environmentDO);
        } else {
            BeanUtils.copyProperties(req, environmentDO);
            return environmentMapper.updateById(environmentDO);
        }
    }

    @Override
    public int delete(String projectId, String id) {
        return 0;
    }
}
