package com.yigit.social_media.dto.user.request;

import com.yigit.social_media.enums.RoleTypes;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRoleDTO {
    @NotNull(message = "Role cannot be null")
    private RoleTypes role;
} 