package cn.skyln.web.controller;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.DTO.CartDTO;
import cn.skyln.web.model.REQ.CartItemRequest;
import cn.skyln.web.model.VO.CartItemVO;
import cn.skyln.web.model.VO.CartVO;
import cn.skyln.web.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: lamella
 * @Date: 2022/09/12/15:31
 * @Description:
 */
@RestController
@RequestMapping("/api/v1/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("add")
    public JsonData addProductToCart(@RequestBody CartItemRequest cartItemRequest) {
        cartService.addToCart(cartItemRequest);
        return JsonData.buildResult(BizCodeEnum.OPERATE_SUCCESS);
    }

    @PostMapping("clear")
    public JsonData clearCart() {
        cartService.clear();
        return JsonData.buildResult(BizCodeEnum.OPERATE_SUCCESS);
    }

    @GetMapping("my_cart")
    public JsonData findMyCart() {
        CartVO cartVO = cartService.getMyCart();
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, cartVO);
    }

    @PostMapping("delete/{product_id}")
    public JsonData deleteItem(@PathVariable("product_id") String productId) {
        cartService.deleteItem(productId);
        return JsonData.buildResult(BizCodeEnum.OPERATE_SUCCESS);
    }

    @PostMapping("change")
    public JsonData changeItem(@RequestBody CartItemRequest cartItemRequest) {
        cartService.changeItem(cartItemRequest);
        return JsonData.buildResult(BizCodeEnum.OPERATE_SUCCESS);
    }

    /**
     * 用于订单服务，确认订单，获取对应的商品详情信息
     * 会清空对应的购物车商品数据
     *
     * @param cartDTO CartDTO
     * @return JsonData
     */
    @PostMapping("confirm_order_cart_items")
    public JsonData confirmOrderCartItems(@RequestBody CartDTO cartDTO) {
        List<CartItemVO> cartItemVOList = cartService.confirmOrderCartItems(cartDTO);
        if (cartItemVOList.isEmpty()) {
            return JsonData.buildResult(BizCodeEnum.CART_NOT_EXIT);
        }
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, cartItemVOList);
    }

    @PostMapping("redis_cart_to_mysql")
    public Integer redisCart2MysqlCart(){
        return cartService.redisCart2MysqlCart();
    }

}
