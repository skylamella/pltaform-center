package cn.skyln.web.controller;


import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.DTO.LockProductDTO;
import cn.skyln.web.model.VO.ProductDetailVO;
import cn.skyln.web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author skylamella
 * @since 2022-09-10
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("page_product")
    public JsonData pageProduct(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "8") int size) {

        Map<String, Object> pageMap = productService.pageProductActivity(page, size);
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, pageMap);
    }

    @GetMapping("/detail/{product_id}")
    public JsonData detail(@PathVariable("product_id") String productId) {
        ProductDetailVO productDetailVO = productService.findDetailById(productId);
        if (Objects.isNull(productDetailVO)) {
            return JsonData.buildResult(BizCodeEnum.PRODUCT_NOT_EXIT);
        }
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, productDetailVO);
    }

    @PostMapping("lock_products")
    public JsonData lockProducts(@RequestBody LockProductDTO lockProductDTO) {
        return productService.lockProductStock(lockProductDTO);
    }

}

