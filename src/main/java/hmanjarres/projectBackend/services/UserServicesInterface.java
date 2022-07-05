package hmanjarres.projectBackend.services;

import hmanjarres.projectBackend.shared.dto.PostDto;
import hmanjarres.projectBackend.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserServicesInterface extends UserDetailsService {
    public UserDto createUser(UserDto userDto);
    public UserDto getUser(String email);
    public List<PostDto> getUserPost(String email);
}
