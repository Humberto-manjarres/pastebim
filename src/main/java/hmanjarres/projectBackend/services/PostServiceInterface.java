package hmanjarres.projectBackend.services;

import hmanjarres.projectBackend.shared.dto.PostCreationDto;
import hmanjarres.projectBackend.shared.dto.PostDto;

import java.util.List;

public interface PostServiceInterface {
    public PostDto createPost(PostCreationDto post);
    public List<PostDto> getLastPosts();
    public PostDto getPost(String postId);
    public void deletePost(String postId, Long userDto);
    public PostDto updatePost(String postId, Long userId, PostCreationDto postUpdateDto);
}
