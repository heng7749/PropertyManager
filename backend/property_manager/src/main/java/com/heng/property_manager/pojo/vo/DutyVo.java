package com.heng.property_manager.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heng.property_manager.pojo.entity.BaseEntity;
import com.heng.property_manager.pojo.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DutyVo extends BaseEntity {
    private Long id;
    private Long employeeId;
    private String shift;
    private String job;
    private String attendedDate;

    private Long departmentId;
    private Employee employee;
    private String department;
    private String dutyMonth;
}
