package com.workmate.app.mail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workmate.app.mail.service.AttachmentVO;

public interface AttachmentMapper {
    void insertAttachment(AttachmentVO file);
    List<AttachmentVO> selectAttachmentsByMailId(@Param("mailId") int mailId);
    AttachmentVO selectAttachmentById(@Param("fileId") Long fileId);
}