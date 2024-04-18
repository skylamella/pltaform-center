package cn.skyln.web.model.REQ;

import lombok.Data;

/**
 * @author lamella
 * @description ProjectUpdateReq TODO
 * @since 2024-04-18 22:17
 */
@Data
public class ProjectUpdateReq {
    private String id;

    private String name;

    private String description;
}
