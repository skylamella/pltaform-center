package cn.skyln.model;

import lombok.Data;

@Data
public class SnowFlakeId {
    private String id;
    private boolean used;
}
