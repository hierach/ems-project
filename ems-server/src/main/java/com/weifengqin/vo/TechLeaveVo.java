package com.weifengqin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author qin start
 * @create 2023-12-19-21:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechLeaveVo {

    private String techNum;

    private String courseNum;
    private String courseName;
    private String stuNum;
    private String stuName;

    private Integer selWeek;
    private Integer selStart;
    private Integer selEnd;

    private LocalDateTime leaveStart;
    private LocalDateTime leaveEnd;
}
