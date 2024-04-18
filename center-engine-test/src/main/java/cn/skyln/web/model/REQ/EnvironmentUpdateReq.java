package cn.skyln.web.model.REQ;

import lombok.Data;

/**
 * @author lamella
 * @description EnvironmentUpdateReq TODO
 * @since 2024-04-18 22:17
 */
@Data
public class EnvironmentUpdateReq {
    private String id;

    private String projectId;

    private String name;

    private String protocol;

    private String domain;

    private Integer port;

    private String description;
}
