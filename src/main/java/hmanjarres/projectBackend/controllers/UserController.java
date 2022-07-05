package hmanjarres.projectBackend.controllers;

import hmanjarres.projectBackend.models.requests.UserDetailRequestModel;
import hmanjarres.projectBackend.models.responses.PostRest;
import hmanjarres.projectBackend.models.responses.UserRest;
import hmanjarres.projectBackend.services.UserServicesInterface;
import hmanjarres.projectBackend.shared.dto.PostDto;
import hmanjarres.projectBackend.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServicesInterface userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getUser(){
        /**obtenemos el email del contexto, del usuario al que le pertenece el token*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

//        UserRest userToReturn = new UserRest();
//        BeanUtils.copyProperties(userService.getUser(email),userToReturn);

        UserRest userToReturn = mapper.map(userService.getUser(email), UserRest.class);

        return ResponseEntity.status(HttpStatus.OK).body(userToReturn);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDetailRequestModel userDetails){
        /**con la anotación @Valid, se harán las validaciones que están en la clase modelo UserDetailRequestModel*/

        UserRest userToReturn = new UserRest();
        UserDto userDto = new UserDto();

        /**copia las propiedades de un objeto en otro objeto (quien pasa, quien recibe)*/
        BeanUtils.copyProperties(userDetails,userDto);

        BeanUtils.copyProperties(userService.createUser(userDto),userToReturn);

        return ResponseEntity.status(HttpStatus.CREATED).body(userToReturn);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(){
        /**obtenemos email del usuario que está enviando el token al sistema*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

        List<PostDto> postDtos = userService.getUserPost(email);

        List<PostRest> postRests = postDtos.stream().map(rest -> {
            PostRest postRest = mapper.map(rest,PostRest.class);
            if (postRest.getExpiresAt().compareTo(new Date(System.currentTimeMillis())) < 0 ){
                postRest.setExpired(true);
            }
            return postRest;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(postRests);
    }


}
