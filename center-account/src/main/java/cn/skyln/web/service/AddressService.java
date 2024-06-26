package cn.skyln.web.service;

import cn.skyln.util.JsonData;
import cn.skyln.web.model.REQ.AddressAddREQ;
import cn.skyln.web.model.VO.AddressVO;

import java.util.List;

/**
 * @author lamella
 * @description AddressService TODO
 * @since 2024-04-02 22:59
 */
public interface AddressService {

    AddressVO getOneById(String addressId);

    JsonData add(AddressAddREQ addressAddREQ);

    JsonData del(String addressId);

    List<AddressVO> listUserAllAddress();
}
