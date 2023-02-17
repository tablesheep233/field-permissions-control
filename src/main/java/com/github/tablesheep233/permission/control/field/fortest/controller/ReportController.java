package com.github.tablesheep233.permission.control.field.fortest.controller;

import com.github.tablesheep233.permission.control.field.fortest.vo.ReportVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("report")
public class ReportController {

    @RequestMapping("record")
    public ReportVO record() {
        return generateReport();
    }

    @RequestMapping("list")
    public List<ReportVO> list() {
        return List.of(generateReport());
    }

    @RequestMapping("array")
    public ReportVO[] array() {
        return new ReportVO[]{generateReport()};
    }

    private ReportVO generateReport() {
        ReportVO vo = new ReportVO();
        vo.setStatus(1);
        vo.setTitle("标题");
        vo.setLast(new ReportVO.Item<Double>().setMax(BigDecimal.ONE).setMin(BigDecimal.ZERO).setData(List.of(0d, 1d)).setRecordTime(LocalDateTime.now()));
        vo.setToday(new ReportVO.Item<BigDecimal>().setMax(BigDecimal.ONE).setMin(BigDecimal.ZERO)
                .setData(List.of(BigDecimal.valueOf(0.2d), BigDecimal.valueOf(0d), BigDecimal.valueOf(1d))).setRecordTime(LocalDateTime.now()));
        vo.setCreateTime(LocalDateTime.now());
        return vo;
    }
}
