package hmanjarres.projectBackend.controllers;

import hmanjarres.projectBackend.models.requests.PostCreateRequestModel;
import hmanjarres.projectBackend.models.responses.OperationStatusModel;
import hmanjarres.projectBackend.models.responses.PostRest;
import hmanjarres.projectBackend.services.PostServiceInterface;
import hmanjarres.projectBackend.services.UserServicesInterface;
import hmanjarres.projectBackend.shared.dto.PostCreationDto;
import hmanjarres.projectBackend.shared.dto.PostDto;
import hmanjarres.projectBackend.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostServiceInterface postService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserServicesInterface userService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestModel createRequestModel){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

        PostCreationDto postCreationDto = mapper.map(createRequestModel,PostCreationDto.class);
        postCreationDto.setUserEmail(email);

        PostRest postToReturn = mapper.map(postService.createPost(postCreationDto),PostRest.class);
        if (postToReturn.getExpiresAt().compareTo(new Date(System.currentTimeMillis())) < 0 ){
            postToReturn.setExpired(true);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(postToReturn);
    }

    @GetMapping("/last")
    public ResponseEntity<?> lastPosts(){
        List<PostDto> posts = postService.getLastPosts();
        System.out.println("lastPosts");

        List<PostRest> postRests = posts.stream().map(rest -> {
            PostRest postRest = mapper.map(rest,PostRest.class);
            return postRest;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(postRests);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable String postId){
        PostDto postDto = postService.getPost(postId);
        PostRest postRest = mapper.map(postDto, PostRest.class);

        //si el post expiró -> si es menor a la fecha actual
        if (postRest.getExpiresAt().compareTo(new Date(System.currentTimeMillis())) < 0 ){
            postRest.setExpired(true);
        }

        //validar si es post es privado o si ya expiró
        if (postRest.getExposure().getId() == 1 || postRest.isExpired()){
            //si el post es privado o ta expiró, debe autenticarse
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getPrincipal().toString();
            UserDto userDto = userService.getUser(email);

            //si el post no pertenece al usuario que está enviando el token
            if (userDto.getId() != postDto.getUser().getId()){
                throw new RuntimeException("No tienes permiso para realizar esta acción");
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(postRest);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        UserDto userDto = userService.getUser(email);

        postService.deletePost(postId,userDto.getId());
        OperationStatusModel operationStatusModel = new OperationStatusModel( "DELETE","success");

        return ResponseEntity.status(HttpStatus.OK).body(operationStatusModel);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@RequestBody PostCreateRequestModel  postCreateRequestModel ,@PathVariable String postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        UserDto userDto = userService.getUser(email);

        PostCreationDto postUpdateDto = mapper.map(postCreateRequestModel, PostCreationDto.class);

        PostRest postRest = mapper.map(postService.updatePost(postId, userDto.getId(), postUpdateDto),PostRest.class);

        return ResponseEntity.status(HttpStatus.OK).body(postRest);
    }

}
