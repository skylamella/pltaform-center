package cn.skyln.web.service.impl;

import cn.skyln.config.RabbitMQConfig;
import cn.skyln.constant.CacheKey;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.enums.order.ProductOrderStateEnum;
import cn.skyln.exception.BizException;
import cn.skyln.interceptor.LoginInterceptor;
import cn.skyln.model.CartMessage;
import cn.skyln.model.LoginUser;
import cn.skyln.util.CommonUtils;
import cn.skyln.util.JsonData;
import cn.skyln.web.dao.mapper.CartItemMapper;
import cn.skyln.web.dao.mapper.CartMapper;
import cn.skyln.web.feignClient.ProductOrderFeignService;
import cn.skyln.web.model.DO.CartDO;
import cn.skyln.web.model.DO.CartItemDO;
import cn.skyln.web.model.DTO.CartDTO;
import cn.skyln.web.model.REQ.CartItemRequest;
import cn.skyln.web.model.VO.CartItemVO;
import cn.skyln.web.model.VO.CartVO;
import cn.skyln.web.model.VO.ProductDetailVO;
import cn.skyln.web.service.CartService;
import cn.skyln.web.service.ProductService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: lamella
 * @Date: 2022/09/12/15:33
 * @Description:
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductOrderFeignService productOrderFeignService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    /**
     * 添加商品到购物车
     *
     * @param cartItemRequest 加入购物车商品对象
     */
    @Override
    public void addToCart(CartItemRequest cartItemRequest) {
        String productId = cartItemRequest.getProductId();
        int buyNum = cartItemRequest.getBuyNum();
        // 获取购物车
        BoundHashOperations<String, Object, Object> myCart = getMyCartOps();
        Object cacheObj = myCart.get(String.valueOf(productId));
        String result = "";
        if (Objects.nonNull(cacheObj)) {
            result = (String) cacheObj;
        }
        if (StringUtils.isBlank(result)) {
            // 不存在商品，新增购物项
            CartItemVO cartItemVO = new CartItemVO();
            ProductDetailVO productDetail = productService.findDetailById(productId);
            if (Objects.isNull(productDetail)) {
                throw new BizException(BizCodeEnum.PRODUCT_NOT_EXIT);
            }
            cartItemVO.setProductId(productId);
            cartItemVO.setBuyNum(buyNum);
            cartItemVO.setProductImg(productDetail.getCoverImg());
            cartItemVO.setProductTitle(productDetail.getTitle());
            cartItemVO.setAmount(productDetail.getAmount());
            cartItemVO.setDelStatue(false);
            myCart.put(String.valueOf(productId), JSON.toJSONString(cartItemVO));
        } else {
            // 存在商品，修改数量
            CartItemVO cartItemVO = JSON.parseObject(result, CartItemVO.class);
            cartItemVO.setBuyNum(cartItemVO.getBuyNum() + buyNum);
            myCart.put(String.valueOf(productId), JSON.toJSONString(cartItemVO));
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void clear() {
        String cartKey = getCartKey();
        if (redisTemplate.hasKey(cartKey)) {
            BoundHashOperations<String, Object, Object> myCart = getMyCartOps();
            List<Object> itemList = myCart.values();
            if (Objects.nonNull(itemList) && !itemList.isEmpty()) {
                itemList.forEach(item -> deleteItem((String) item, myCart));
            }
        } else {
            throw new BizException(BizCodeEnum.CART_NOT_EXIT);
        }
    }

    /**
     * 查看我的购物车
     *
     * @return CartVO
     */
    @Override
    public CartVO getMyCart() {
        // 获取全部购物项
        List<CartItemVO> cartItemVOList = buildCartItemList(false);

        // 封装成CartVO
        CartVO cartVO = new CartVO();
        cartVO.setCartItemVOList(cartItemVOList);
        return cartVO;
    }

    /**
     * 删除购物项
     *
     * @param productId 商品ID
     */
    @Override
    public void deleteItem(String productId) {
        // 获取购物车
        BoundHashOperations<String, Object, Object> myCart = getMyCartOps();
        deleteItem(productId, myCart);
    }

    private void deleteItem(String productId, String userId) {
        // 获取购物车
        BoundHashOperations<String, Object, Object> myCart = getMyCartOps(userId);
        deleteItem(productId, myCart);
    }

    private void deleteItem(String productId, BoundHashOperations<String, Object, Object> myCart) {
        String result = (String) myCart.get(String.valueOf(productId));
        if (StringUtils.isNotBlank(result)) {
            CartItemVO cartItemVO = JSON.parseObject(result, CartItemVO.class);
            cartItemVO.setDelStatue(true);
            myCart.put(String.valueOf(cartItemVO.getProductId()), JSON.toJSONString(cartItemVO));
        }
    }

    /**
     * 修改购物项
     *
     * @param cartItemRequest 购物车商品对象
     */
    @Override
    public void changeItem(CartItemRequest cartItemRequest) {
        // 获取购物车
        BoundHashOperations<String, Object, Object> myCart = getMyCartOps();
        Object object = myCart.get(String.valueOf(cartItemRequest.getProductId()));
        if (Objects.isNull(object)) {
            throw new BizException(BizCodeEnum.CART_NOT_EXIT);
        }
        CartItemVO cartItemVO = JSON.parseObject((String) object, CartItemVO.class);
        if (cartItemRequest.getBuyNum() < 0) {
            throw new BizException(BizCodeEnum.CART_UPD_NUM_FAIL);
        } else if (cartItemRequest.getBuyNum() == 0) {
            deleteItem(cartItemRequest.getProductId(), myCart);
        }
        cartItemVO.setBuyNum(cartItemRequest.getBuyNum());
        myCart.put(String.valueOf(cartItemRequest.getProductId()), JSON.toJSONString(cartItemVO));
    }

    @Override
    public List<CartItemVO> confirmOrderCartItems(CartDTO cartDTO) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        // 获取购物车的全部购物项
        List<CartItemVO> cartItemVOList = buildCartItemList(true);
        List<String> productIdList = cartDTO.getProductIdList();
        // 根据需要的商品id
        return cartItemVOList.stream().filter(obj -> {
            if (productIdList.contains(obj.getProductId())) {
                CartMessage cartMessage = new CartMessage();
                cartMessage.setOutTradeNo(cartDTO.getOrderOutTradeNo());
                cartMessage.setProductId(obj.getProductId());
                cartMessage.setUserId(loginUser.getId());
                rabbitTemplate.convertAndSend(rabbitMQConfig.getCartEventExchange(),
                        rabbitMQConfig.getCartReleaseDelayRoutingKey(),
                        cartMessage);
                log.info("清空购物车-延迟消息发送成功：{}", cartMessage);
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 清空MQ中的购物车项
     *
     * @param cartMessage MQ消息体
     * @return 清理结果
     */
    @Override
    public boolean cleanCartRecord(CartMessage cartMessage) {
        String productId = cartMessage.getProductId();
        String outTradeNo = cartMessage.getOutTradeNo();
        JsonData jsonData = productOrderFeignService.queryProductOrderState(outTradeNo);
        if (jsonData.getCode() == 0) {
            // 正常响应，判断订单状态
            String state = jsonData.getData().toString();
            // 返回的数据不存在，返回给消息队列，重新投递
            if (StringUtils.isBlank(state)) {
                log.warn("订单状态不存在，返回给消息队列，重新投递：{}", cartMessage);
                return false;
            }
            // 状态是NEW新建状态，返回给消息队列，重新投递
            if (StringUtils.equalsIgnoreCase(ProductOrderStateEnum.NEW.name(), state)) {
                log.warn("订单状态是NEW，返回给消息队列，重新投递：{}", cartMessage);
                return false;
            }
            // 状态是已经支付
            if (StringUtils.equalsIgnoreCase(ProductOrderStateEnum.PAY.name(), state)) {
                // 清空购物车
                this.deleteItem(productId, cartMessage.getUserId());
                log.info("订单已经支付，清空购物车对应购物项：{}", cartMessage);
                return true;
            }
        }
        log.warn("订单不存在，或者订单被取消，outTradeNo={}，消息：{}", outTradeNo, cartMessage);
        return true;
    }

    /**
     * 获取最新的购物项
     *
     * @param latestAmount 是否获取最新的价格
     * @return 购物项列表
     */
    private List<CartItemVO> buildCartItemList(boolean latestAmount) {
        // 获取购物车
        BoundHashOperations<String, Object, Object> myCart = getMyCartOps();
        List<Object> itemList = myCart.values();
        List<CartItemVO> cartItemVOList = new ArrayList<>();
        // 拼接ID列表查询最新价格
        List<String> productIdList = new ArrayList<>();
        if (Objects.nonNull(itemList) && !itemList.isEmpty()) {
            itemList.forEach(item -> {
                CartItemVO cartItemVO = JSON.parseObject((String) item, CartItemVO.class);
                if (!cartItemVO.isDelStatue()) {
                    cartItemVOList.add(cartItemVO);
                    productIdList.add(cartItemVO.getProductId());
                }
            });
        }
        if (latestAmount && !cartItemVOList.isEmpty() && !productIdList.isEmpty()) {
            setProductLatestAmount(cartItemVOList, productIdList);
        }
        return cartItemVOList;
    }

    /**
     * 设置商品最新价格
     *
     * @param cartItemVOList 购物项列表
     * @param productIdList  ID列表
     */
    private void setProductLatestAmount(List<CartItemVO> cartItemVOList, List<String> productIdList) {
        // 批量查询
        List<ProductDetailVO> productDetailVOList = productService.findProductsByIdBatch(productIdList);
        // 根据ID分组
        Map<String, ProductDetailVO> productDetailVOMap = productDetailVOList.stream().collect(Collectors.toMap(ProductDetailVO::getId, Function.identity()));

        cartItemVOList.forEach(item -> {
            ProductDetailVO productDetailVO = productDetailVOMap.get(item.getProductId());
            item.setAmount(productDetailVO.getAmount());
            item.setProductTitle(productDetailVO.getTitle());
            item.setProductImg(productDetailVO.getCoverImg());
        });
    }

    /**
     * redis中的购物车数据转移到mysql中
     *
     * @return 购物车数据转移结果
     */
    @Override
    public Integer redisCart2MysqlCart() {
        // 批量获取购物车的key
        Set<String> keys = redisTemplate.keys("cart:*");
        if (Objects.nonNull(keys) && !keys.isEmpty()) {
            keys.forEach(key -> {
                // 根据redis_cart_key获取用户ID
                String userId = key.split(":")[1];
                // 根据用户ID获取mysql购物车
                CartDO cartDO = cartMapper.selectOne(new QueryWrapper<CartDO>().eq("user_id", userId));
                if (Objects.isNull(cartDO)) {
                    // 用户不存在购物车时将自动创建mysql购物车
                    cartDO = new CartDO();
                    cartDO.setUserId(userId);
                    cartMapper.insert(cartDO);
                }
                // 根据用户ID获取redis购物车
                BoundHashOperations<String, Object, Object> cart = getMyCartOps(userId);
                // 获取redis购物项
                List<Object> itemList = cart.values();
                if (Objects.nonNull(itemList) && !itemList.isEmpty()) {
                    CartDO finalCartDO = cartDO;
                    List<CartItemDO> cartItemList = new ArrayList<>();
                    // 循环处理redis中的购物项
                    itemList.forEach(item -> {
                        CartItemVO cartItemVO = JSON.parseObject((String) item, CartItemVO.class);
                        CartItemDO cartItemDO = cartItemMapper.selectOne(new QueryWrapper<CartItemDO>()
                                .eq("product_id", cartItemVO.getProductId())
                                .eq("cart_id", finalCartDO.getId()));
                        // 判断是否为删除掉的购物车
                        if (cartItemVO.isDelStatue()) {
                            // 如果mysql中存在该购物项则删除mysql中的记录
                            if (Objects.nonNull(cartItemDO)) {
                                cartItemMapper.deleteById(cartItemDO.getId());
                            }
                            // 从redis中彻底移除该购物项
                            cart.delete(String.valueOf(cartItemVO.getProductId()));
                        } else {
                            ProductDetailVO productDetailVO = productService.findDetailById(cartItemVO.getProductId());
                            cartItemVO.setAmount(productDetailVO.getAmount());
                            cartItemVO.setProductTitle(productDetailVO.getTitle());
                            cartItemVO.setProductImg(productDetailVO.getCoverImg());
                            cart.put(String.valueOf(cartItemVO.getProductId()), JSON.toJSONString(cartItemVO));
                            if (Objects.isNull(cartItemDO)) {
                                // 数据库中不存在该购物项，则创建一个购物项
                                cartItemDO = (CartItemDO) CommonUtils.beanProcess(cartItemVO, new CartItemDO());
                                cartItemDO.setCartId(finalCartDO.getId());
                                cartItemList.add(cartItemDO);
                            } else {
                                BeanUtils.copyProperties(cartItemVO, cartItemDO);
                                cartItemMapper.updateById(cartItemDO);
                            }
                        }
                    });
                    // 将购物车数据批量插入mysql
                    cartItemMapper.insertBatch(cartItemList);
                }
            });
            return 1;
        }
        return 0;
    }

    /**
     * 获取购物车redis的key
     *
     * @return
     */
    private String getCartKey() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        return String.format(CacheKey.CART_KEY, loginUser.getId());
    }

    /**
     * 我的购物车
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getMyCartOps() {
        String cartKey = getCartKey();
        return redisTemplate.boundHashOps(cartKey);
    }

    private BoundHashOperations<String, Object, Object> getMyCartOps(String userId) {
        String cartKey = String.format(CacheKey.CART_KEY, userId);
        return redisTemplate.boundHashOps(cartKey);
    }
}
