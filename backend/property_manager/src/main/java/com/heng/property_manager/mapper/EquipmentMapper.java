package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Equipment;
import com.heng.property_manager.pojo.entity.Maintenance;
import com.heng.property_manager.pojo.vo.EquipmentVo;
import com.heng.property_manager.pojo.vo.MaintenanceVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface EquipmentMapper {
    List<EquipmentVo> searchEquipments(Equipment equipment);

    List<MaintenanceVo> searchMaintenances(MaintenanceVo maintenanceVo);

    @Insert("insert into equipment(name,type,model,deployPlace,department_id,status,deploy_time,purchasing_time,repairsPeriod,replacePeriod,producer,remark,create_time,create_by) " +
            "values(#{name},#{type},#{model},#{deployPlace},#{departmentId},#{status},#{deployTime},#{purchasingTime},#{repairsPeriod},#{replacePeriod},#{producer},#{remark},#{createTime},#{createBy})")
    boolean addEquipment(Equipment equipment);

    @Update("update equipment set name=#{name},type=#{type},model=#{model},deployPlace=#{deployPlace},department_id=#{departmentId},status=#{status},deploy_time=#{deployTime},purchasing_time=#{purchasingTime},repairsPeriod=#{repairsPeriod},replacePeriod=#{replacePeriod},producer=#{producer},remark=#{remark},update_time=#{updateTime},update_by=#{updateBy} where id=#{id}")
    boolean editEquipment(Equipment equipment);

    @Update("update equipment set is_deleted = 1,update_time=#{updateTime},update_by=#{updateBy} where id = #{id}")
    boolean deleteEquipment(Equipment equipment);

    @Insert("insert into maintenance(equipment_id,status,maintainer_id,maintain_date,maintain_target_date,remark) " +
            "values(#{equipmentId},#{status},#{maintainerId},#{maintainDate},#{maintainTargetDate},#{remark})")
    boolean addMaintenance(Maintenance maintenance);

    @Update("update maintenance set status=#{status},maintainer_id=#{maintainerId},maintain_date=#{maintainDate},remark=#{remark} where id=#{id}")
    boolean editMaintenance(Maintenance maintenance);

    @Update("update maintenance set is_deleted = 1 where id = #{id}")
    boolean deleteMaintenance(Maintenance maintenance);

    @Select("select maintain_date from maintenance where is_deleted = 0 and  equipment_id = #{equipmentId} order by maintain_date desc limit 1")
    Date getLastMaintainDateByEquipmentId(Long id);

    @Select("select repairsPeriod from equipment where is_deleted = 0 and  id = #{equipmentId} limit 1")
    Integer getEquipmentRepairsPeriodById(Long equipmentId);

    @Select("select * from equipment where id=#{equipmentId}")
    Equipment getEquipmentById(Long equipmentId);

    @Insert("insert into maintenance(equipment_id,maintain_target_date) " +
            "values(#{equipmentId},#{maintainTargetDate})")
    boolean autoAddMaintenance(Maintenance maintenance);

    @Select("SELECT e.* from equipment e\n" +
            "LEFT JOIN maintenance m ON e.id = m.equipment_id AND m.is_deleted = 0\n" +
            "WHERE e.is_deleted = 0 AND e.repairsPeriod > 0 AND m.id IS NULL \n")
    List<Equipment> findUnMaintainEquipment();

    @Select("select  * from maintenance where is_deleted = 0 and  equipment_id = #{id} order by maintain_date desc limit 1")
    Maintenance getEquipmentLastMaintainMsg(Long id);

    @Select("select * from equipment where is_deleted = 0")
    List<Equipment> listEquipments();

    @Update("update maintenance set status=#{status} where id=#{id}")
    boolean updateStatus(Maintenance maintenance);
}
