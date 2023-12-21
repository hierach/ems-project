package com.weifengqin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qin start
 * @create 2023-12-14-14:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSelectCourseDto {
    private String userId;
    private String courseNum;
    private String techNum;
}
