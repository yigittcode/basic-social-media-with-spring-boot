package com.yigit.social_media.controller.admin;

import org.springframework.web.bind.annotation.*;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/admin/images")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Images", description = "Operations related to image management by administrators")
public class AdminImageController {

    private final AdminService adminService;

    @DeleteMapping("/{imageId}")
    @Operation(summary = "Remove an image", description = "Deletes an image from the system by its ID. Only accessible to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid image ID supplied"),
            @ApiResponse(responseCode = "404", description = "Image not found") // Although not explicitly handled, it's good to document potential 404
    })
    public ResponseDTO<Void> removeImage(@Parameter(description = "ID of the image to be deleted") @PathVariable Long imageId) {
        adminService.removeImage(imageId);
        return ResponseDTO.success("Image deleted successfully from the system", null);
    }
}