package com.zepox.EcommerceWebApp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zepox.EcommerceWebApp.dto.response.ImageUploadServiceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {
    private final Cloudinary cloudinary;

    public ImageUploadServiceResponseDto uploadImage(MultipartFile file) throws IOException {
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "products")
        );
        return ImageUploadServiceResponseDto.builder()
                .imageUrl(result.get("secure_url").toString())
                .publicId(result.get("public_id").toString())
                .build();
    }

}
