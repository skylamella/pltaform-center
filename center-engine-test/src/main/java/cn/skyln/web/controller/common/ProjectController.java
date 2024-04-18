package cn.skyln.web.controller.common;

import cn.skyln.util.JsonData;
import cn.skyln.web.model.REQ.ProjectDelReq;
import cn.skyln.web.model.REQ.ProjectSaveReq;
import cn.skyln.web.model.REQ.ProjectUpdateReq;
import cn.skyln.web.service.common.ProjectService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author lamella
 * @description ProjectController TODO
 * @since 2024-04-18 22:21
 */
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    /**
     * 获取列表
     * 通过GET请求访问/list接口，获取项目列表
     *
     * @return 列表数据
     */
    @GetMapping("/list")
    public JsonData list(){
        return JsonData.buildSuccess(projectService.list());
    }

    /**
     * 保存项目
     * 使用POST方法请求/save接口，用于保存项目信息
     *
     * @param req 项目保存请求对象
     * @return 保存结果的JsonData对象
     */
    @PostMapping("/save")
    public JsonData save(@RequestBody ProjectSaveReq req){
        return JsonData.buildSuccess(projectService.save(req));
    }

    /**
     * 更新项目
     * 使用POST方法请求"/update"接口，用于更新项目信息
     *
     * @param req 项目更新请求对象
     * @return 更新结果的JsonData对象
     */
    @PostMapping("/update")
    public JsonData update(@RequestBody ProjectUpdateReq req){
        return JsonData.buildSuccess(projectService.update(req));
    }

    /**
     * 删除项目
     * 使用POST方法请求"/del"接口，接收一个ProjectDelReq对象作为参数
     * ProjectDelReq对象必须包含一个项目ID（id）
     * 调用projectService的delete方法，将项目ID作为参数传入
     * 返回删除结果的JsonData对象
     */
    @PostMapping("/del")
    public JsonData delete(@RequestBody ProjectDelReq req){
        return JsonData.buildSuccess(projectService.delete(req.getId()));
    }
}
