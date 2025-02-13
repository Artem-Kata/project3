package com.bank.authorization.Mapper;

import com.bank.authorization.DTO.AuditDTO;
import com.bank.authorization.DTO.UserDTO;
import com.bank.authorization.Entities.Audit;
import com.bank.authorization.Entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface EntityMapper {

    public static UserDTO toUserDTO(User user) {
        if(user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setRole(user.getRole());
        dto.setProfileId(user.getProfileId());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public static User toUser(UserDTO dto) {
        if(dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setRole(dto.getRole());
        user.setProfileId(dto.getProfileId());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static AuditDTO toAuditDTO(Audit audit) {
        if(audit == null) return null;
        AuditDTO dto = new AuditDTO();
        dto.setId(audit.getId());
        dto.setEntityType(audit.getEntityType());
        dto.setOperationType(audit.getOperationType());
        dto.setCreatedBy(audit.getCreatedBy());
        dto.setModifiedBy(audit.getModifiedBy());
        dto.setCreatedAt(audit.getCreatedAt());
        dto.setModifiedAt(audit.getModifiedAt());
        dto.setNewEntityJson(audit.getNewEntityJson());
        dto.setEntityJson(audit.getEntityJson());
        return dto;
    }

    public static Audit toAudit(AuditDTO dto) {
        if(dto == null) return null;
        Audit audit = new Audit();
        audit.setId(dto.getId());
        audit.setEntityType(dto.getEntityType());
        audit.setOperationType(dto.getOperationType());
        audit.setCreatedBy(dto.getCreatedBy());
        audit.setModifiedBy(dto.getModifiedBy());
        audit.setCreatedAt(dto.getCreatedAt());
        audit.setModifiedAt(dto.getModifiedAt());
        audit.setNewEntityJson(dto.getNewEntityJson());
        audit.setEntityJson(dto.getEntityJson());
        return audit;
    }
}
