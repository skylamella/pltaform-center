package cn.skyln.web.service.impl;

import cn.skyln.web.dao.mapper.AddressMapper;
import cn.skyln.web.dao.repo.AddressRepo;
import cn.skyln.web.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lamella
 * @description AddressServiceImpl TODO
 * @since 2024-04-02 22:59
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AddressRepo addressRepo;
}
