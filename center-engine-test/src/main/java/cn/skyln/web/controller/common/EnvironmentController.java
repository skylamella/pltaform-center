package cn.skyln.web.controller.common;

import cn.skyln.util.JsonData;
import cn.skyln.web.model.REQ.EnvironmentOperationReq;
import cn.skyln.web.service.common.EnvironmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author lamella
 * @description EnvironmentController TODO
 * @since 2024-04-18 22:14
 */
@RestController
@RequestMapping("/api/v1/env")
public class EnvironmentController {

    @Resource
    private EnvironmentService environmentService;


    @GetMapping("/list")
    public JsonData list(@RequestParam("projectId") String projectId) {
        return JsonData.buildSuccess(environmentService.list(projectId));
    }


    @PostMapping("/save")
    public JsonData save(@RequestBody EnvironmentOperationReq req) {
        return JsonData.buildSuccess(environmentService.save(req));
    }


    @PostMapping("/update")
    public JsonData update(@RequestBody EnvironmentOperationReq req) {
        return JsonData.buildSuccess(environmentService.update(req));
    }

    @PostMapping("/del")
    public JsonData delete(@RequestBody EnvironmentOperationReq req) {
        return JsonData.buildSuccess(environmentService.delete(req.getProjectId(), req.getId()));
    }
}
