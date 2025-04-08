package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Complaint;
import com.heng.property_manager.pojo.vo.ComplaintVo;

import java.util.List;

public interface ComplaintService {
    List<ComplaintVo> searchComplaints(Complaint complaint);

    List<ComplaintVo>  myComplaints(Complaint complaint);

    boolean addComplaint(Complaint complaint);

    boolean editComplaint(Complaint complaint);

    boolean conductComplaint(Complaint complaint);

    boolean deleteComplaint(Complaint complaint);
}
