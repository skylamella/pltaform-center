package cn.skyln.web.model.VO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author skylamella
 * @since 2022-09-10
 */
@Data
public class ProductListVO {

    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图
     */
    private String coverImg;

    /**
     * 老价格
     */
    private BigDecimal oldAmount;

    /**
     * 新价格
     */
    private BigDecimal amount;
}
