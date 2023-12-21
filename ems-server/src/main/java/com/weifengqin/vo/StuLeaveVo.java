package com.weifengqin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author qin start
 * @create 2023-12-19-19:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuLeaveVo {


    private String courseNum;
    private String courseName;
    private String techNum;
    private String techName;
    private Integer selWeek;
    private Integer selStart;
    private Integer selEnd;
    private Integer leaveStatus;

    private LocalDateTime leaveStart;
    private LocalDateTime leaveEnd;
}
