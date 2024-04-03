package cn.skyln.web.service.impl;

import cn.skyln.enums.AddressStatusEnum;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.exception.BizException;
import cn.skyln.interceptor.LoginInterceptor;
import cn.skyln.model.LoginUser;
import cn.skyln.util.JsonData;
import cn.skyln.web.dao.mapper.AddressMapper;
import cn.skyln.web.dao.repo.AddressRepo;
import cn.skyln.web.model.DO.AddressDO;
import cn.skyln.web.model.DTO.AddressAddRequest;
import cn.skyln.web.model.VO.AddressVO;
import cn.skyln.web.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public AddressVO getOneById(String addressId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressDO addressDO = addressRepo.getAddressDOByIdAndUserId(addressId, loginUser.getId());
        if (Objects.isNull(addressDO)) {
            return null;
        }
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(addressDO, addressVO);
        return addressVO;
    }

    @Override
    @Transactional
    public JsonData add(AddressAddRequest addressAddRequest) {
        AddressDO addressDO = new AddressDO();
        BeanUtils.copyProperties(addressAddRequest, addressDO);
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        addressDO.setUserId(loginUser.getId());
        // 判断是否有默认收货地址
        if (addressDO.getDefaultStatus() == AddressStatusEnum.DEFAULT_ADDRESS.getStatus()) {
            AddressDO defaultAddressDO = addressRepo.getAddressDOByUserIdAndDefaultStatus(loginUser.getId(), AddressStatusEnum.DEFAULT_ADDRESS.getStatus());
            if (Objects.nonNull(defaultAddressDO)) {
                defaultAddressDO.setDefaultStatus(AddressStatusEnum.COMMON_ADDRESS.getStatus());
                int rows = addressMapper.updateById(defaultAddressDO);
                if (rows != 1) {
                    throw new BizException(BizCodeEnum.ADDRESS_UPD_FAIL);
                }
            }
        }
        int rows = addressMapper.insert(addressDO);
        return rows == 1 ? JsonData.buildResult(BizCodeEnum.OPERATE_SUCCESS) : JsonData.buildResult(BizCodeEnum.ADDRESS_ADD_FAIL);
    }

    @Override
    public JsonData del(String addressId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressDO addressDO = addressRepo.getAddressDOByIdAndUserId(addressId, loginUser.getId());
        if (Objects.isNull(addressDO)) {
            return JsonData.buildResult(BizCodeEnum.ADDRESS_NOT_EXIT);
        }
        int rows = addressMapper.deleteById(addressDO);
        return rows == 1 ? JsonData.buildResult(BizCodeEnum.OPERATE_SUCCESS) : JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }

    @Override
    public List<AddressVO> listUserAllAddress() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        List<AddressDO> list = addressRepo.findAddressDOSByUserId(loginUser.getId());
        return list.stream().map(obj -> {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(obj, addressVO);
            return addressVO;
        }).collect(Collectors.toList());
    }
}
