package cn.skyln.web.service.impl;

import cn.skyln.web.dao.mapper.ReportMapper;
import cn.skyln.web.dao.repo.ReportRepo;
import cn.skyln.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lamella
 * @description ReportServiceImpl TODO
 * @since 2024-02-10 15:53
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private ReportMapper reportMapper;
}
