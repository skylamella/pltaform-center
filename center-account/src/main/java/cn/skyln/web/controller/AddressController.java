package cn.skyln.web.controller;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.REQ.AddressAddREQ;
import cn.skyln.web.model.VO.AddressVO;
import cn.skyln.web.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author lamella
 * @description AddressController TODO
 * @since 2024-04-03 19:09
 */
@RestController
@RequestMapping("/api/v1/address")
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/query/{address_id}")
    public JsonData getOneAddress(@PathVariable("address_id") String addressId) {
        AddressVO addressVO = addressService.getOneById(addressId);
        if (Objects.isNull(addressVO)) {
            return JsonData.buildResult(BizCodeEnum.ADDRESS_NOT_EXIT);
        }
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, addressVO);
    }

    @PostMapping("add")
    public JsonData addAddress(@RequestBody AddressAddREQ addressAddREQ) {
        return addressService.add(addressAddREQ);
    }

    @PostMapping("/del/{address_id}")
    public JsonData delAddress(@PathVariable("address_id") String addressId) {
        return addressService.del(addressId);
    }

    @PostMapping("/list")
    public JsonData findUserAllAddress() {
        List<AddressVO> list = addressService.listUserAllAddress();
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, list);
    }
}
