package hmanjarres.projectBackend.services;

import hmanjarres.projectBackend.entities.ExposureEntity;
import hmanjarres.projectBackend.entities.PostEntity;
import hmanjarres.projectBackend.entities.UserEntity;
import hmanjarres.projectBackend.repositories.ExposureRepository;
import hmanjarres.projectBackend.repositories.PostRepository;
import hmanjarres.projectBackend.repositories.UserRepository;
import hmanjarres.projectBackend.shared.dto.ExposureDto;
import hmanjarres.projectBackend.shared.dto.PostCreationDto;
import hmanjarres.projectBackend.shared.dto.PostDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService implements PostServiceInterface{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExposureRepository exposureRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public PostDto createPost(PostCreationDto post) {
        UserEntity userEntity = userRepository.findByEmail(post.getUserEmail());
        ExposureEntity exposureEntity = exposureRepository.findById(post.getExposureId()).get();

        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setExposure(exposureEntity);
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
        postEntity.setPostId(UUID.randomUUID().toString());
        /**a la fecha actual se le suma los minutos a expirar */
        postEntity.setExpiresAt(new Date(System.currentTimeMillis()+ (post.getExpitarionTime()*60000)));


        PostDto postToReturn = mapper.map(postRepository.save(postEntity),PostDto.class);
        return postToReturn;
    }

    @Override
    public List<PostDto> getLastPosts() {
        Long exposure = 2L;
        String fecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        List<PostEntity> postEntities = postRepository.getLastPublicPosts(exposure,fecha);
        List<PostDto> postDtos = postEntities.stream().map( post -> {
            PostDto postDto = mapper.map(post, PostDto.class);
            return postDto;
        }).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPost(String postId) {
        PostEntity p = postRepository.findByPostId(postId);
        PostDto postDto = mapper.map(p,PostDto.class);
        return postDto;
    }

    @Override
    public void deletePost(String postId, Long userDto) {
        PostDto postDto = mapper.map(postRepository.findByPostId(postId),PostDto.class);
        //si es post pertenece al usuario
        if (postDto.getUser().getId() != userDto){
            throw new RuntimeException("No se puede realizar esta acción");
        }
        postRepository.deleteById(postDto.getId());
    }

    @Override
    public PostDto updatePost(String postId, Long userId, PostCreationDto postUpdateDto) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        PostDto postDto = mapper.map(postEntity,PostDto.class);
        if (postDto.getUser().getId() != userId){
            throw new RuntimeException("No se puede realizar esta acción");
        }

        ExposureEntity exposureEntity = exposureRepository.findById(postUpdateDto.getExposureId()).get();
        postEntity.setExposure(exposureEntity);
        postEntity.setTitle(postUpdateDto.getTitle());
        postEntity.setContent(postUpdateDto.getContent());
        /**a la fecha actual se le suma los minutos a expirar */
        postEntity.setExpiresAt(new Date(System.currentTimeMillis()+ (postUpdateDto.getExpitarionTime()*60000)));

        PostEntity updatedPost = postRepository.save(postEntity);
        PostDto dto = mapper.map(updatedPost, PostDto.class);
        return dto;
    }
}
