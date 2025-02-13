package com.yigit.social_media.service.post;

import com.yigit.social_media.dto.post.request.PostRequestDTO;
import com.yigit.social_media.dto.post.response.PostResponseDTO;
import java.util.List;
public interface IPostService {
    PostResponseDTO createPost(PostRequestDTO postRequestDTO);
    PostResponseDTO updatePost(Long postId, PostRequestDTO postRequestDTO);
    void deletePost(Long postId);
    List<PostResponseDTO> getAllPosts();
    List<PostResponseDTO> getAllPostsByUser(Long userId);
    List<PostResponseDTO> myPosts();
    PostResponseDTO getPostById(Long postId);
}
