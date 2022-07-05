package hmanjarres.projectBackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hmanjarres.projectBackend.SpringApplicationContext;
import hmanjarres.projectBackend.models.requests.UserLoginRequestModel;
import hmanjarres.projectBackend.services.UserServicesInterface;
import hmanjarres.projectBackend.shared.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequestModel userModel = new ObjectMapper().readValue(request.getInputStream(),UserLoginRequestModel.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userModel.getEmail(),
                            userModel.getPassword(),
                            new ArrayList<>()));
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE))
                .signWith(SignatureAlgorithm.ES256.HS512, SecurityConstants.getTokenSecret())
                .compact();

        UserServicesInterface userServices = (UserServicesInterface) SpringApplicationContext.getBean("userService");
        UserDto userDto = userServices.getUser(username);

        response.addHeader("Access-Control-Expose-Headers","Authorization, UserId");
        response.addHeader("UserId",userDto.getUserId());
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

    }
}
