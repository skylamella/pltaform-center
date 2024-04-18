package cn.skyln.web.model.REQ;

import lombok.Data;

/**
 * @author lamella
 * @description EnvironmentDelReq TODO
 * @since 2024-04-18 22:20
 */
@Data
public class EnvironmentDelReq {
    private String projectId;
    private String id;
}
