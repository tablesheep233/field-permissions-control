package com.github.tablesheep233.permission.control.field.fortest.vo;

import com.github.tablesheep233.permission.control.field.jackson.annotation.JsonFieldControl;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonFieldControl(name = "Report")
public class ReportVO implements Serializable {

    private String title;

    private LocalDateTime createTime;

    private Integer status;

    private Item<BigDecimal> today;

    private Item<Double> last;

    @Data
    @Accessors(chain = true)
    @JsonFieldControl
    public static class Item<T extends Number> implements Serializable {

        private LocalDateTime recordTime;

        private BigDecimal max;

        private BigDecimal min;

        private List<T> data;
    }
}
