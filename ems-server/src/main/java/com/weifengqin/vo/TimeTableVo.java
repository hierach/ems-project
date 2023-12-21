package com.weifengqin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author qin start
 * @create 2023-12-20-11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableVo {

    //返回星期几
    private Integer xq;

    //课程名
    private String title;

    //拼凑的内容  开始时间 + 结课时间 + 上课地点
    private String content;

    //开始时间
    private LocalDateTime startTime;

    //结课时间
    private LocalDateTime endTime;

    //上课地点
    private String place;

    // 开始节数
    private Integer start;

    // 结束节数
    private Integer end;

}
