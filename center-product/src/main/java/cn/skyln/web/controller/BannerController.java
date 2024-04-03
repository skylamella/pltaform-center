package cn.skyln.web.controller;


import cn.skyln.util.JsonData;
import cn.skyln.web.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author skylamella
 * @since 2022-09-10
 */
@RestController
@RequestMapping("/api/v1/banner/")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("banner_list")
    public JsonData bannerList() {
        return bannerService.findAllBanner();
    }

}

