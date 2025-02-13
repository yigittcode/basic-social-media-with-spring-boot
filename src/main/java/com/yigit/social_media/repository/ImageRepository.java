package com.yigit.social_media.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yigit.social_media.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
