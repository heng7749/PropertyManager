package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.ComplaintMapper;
import com.heng.property_manager.pojo.entity.Complaint;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.ComplaintVo;
import com.heng.property_manager.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Autowired
    private ComplaintMapper complaintMapper;

    @Override
    public List<ComplaintVo> searchComplaints(Complaint complaint) {
        return complaintMapper.searchComplaints(complaint);
    }

    @Override
    public List<ComplaintVo> myComplaints(Complaint complaint) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        complaint.setOwnerId(principal.getUser().getId());
        return complaintMapper.listUserComplaints(complaint);
    }

    @Override
    public boolean addComplaint(Complaint complaint) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取业主id
        complaint.setOwnerId(principal.getUser().getBindId());
        complaint.setCreateTime(new Date());
        return complaintMapper.addComplaint(complaint);
    }

    @Override
    public boolean editComplaint(Complaint complaint) {
        return complaintMapper.editComplaint(complaint);
    }

    @Override
    public boolean conductComplaint(Complaint complaint) {
         if (complaint.getStatus() == 1) {
             LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
             // 获取操作者id
            complaint.setUpdateBy(principal.getUser().getBindId());
            complaint.setUpdateTime(new Date());
        } else {
             complaint.setFeedback("");
             complaint.setUpdateTime(null);
         }
        return complaintMapper.conductComplaint(complaint);
    }

    @Override
    public boolean deleteComplaint(Complaint complaint) {
        return complaintMapper.deleteComplaint(complaint);
    }
}
