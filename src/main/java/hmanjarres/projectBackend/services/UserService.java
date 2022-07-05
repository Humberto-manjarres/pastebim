package hmanjarres.projectBackend.services;

import hmanjarres.projectBackend.entities.PostEntity;
import hmanjarres.projectBackend.exceptions.EmailExistsException;
import hmanjarres.projectBackend.repositories.PostRepository;
import hmanjarres.projectBackend.repositories.UserRepository;
import hmanjarres.projectBackend.entities.UserEntity;
import hmanjarres.projectBackend.shared.dto.PostDto;
import hmanjarres.projectBackend.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServicesInterface{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    @Qualifier("encryptPassword")
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail())!= null) throw new EmailExistsException("mail in use!");
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user,userEntity);

        /**encriptamos la contraseña*/
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        /**generando un id publico aleatorio con la clase de java UUID*/
        UUID userId = UUID.randomUUID();
        userEntity.setUserId(userId.toString());

        BeanUtils.copyProperties(userRepository.save(userEntity), user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null){
            throw new UsernameNotFoundException(email);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null){
            throw new UsernameNotFoundException(email);
        }
        UserDto userToReturn = new UserDto();
        BeanUtils.copyProperties(userEntity,userToReturn);
        return userToReturn;
    }

    @Override
        public List<PostDto> getUserPost(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        List<PostEntity> postEntities = postRepository.getByUserIdOrderByCreatedAtDesc(userEntity.getId());

        List<PostDto> postDtos = postEntities.stream().map(dto -> {
            PostDto postDto = mapper.map(dto, PostDto.class);
            return postDto;
        }).collect(Collectors.toList());


        return postDtos;
    }
}
