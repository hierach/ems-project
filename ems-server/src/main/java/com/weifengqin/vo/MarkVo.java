package com.weifengqin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 打分表要存的字段
 * @author qin start
 * @create 2023-12-19-15:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkVo {

    private String courseNum;

    private String courseName;

    private String stuNum;

    private String stuName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer fullMark;

}
