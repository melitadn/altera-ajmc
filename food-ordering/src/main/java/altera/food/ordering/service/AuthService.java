package altera.food.ordering.service;

import altera.food.ordering.config.JwtTokenProvider;
import altera.food.ordering.config.SecurityConfig;
import altera.food.ordering.constant.AppConstant;
import altera.food.ordering.domain.dao.User;
import altera.food.ordering.domain.dto.LoginForm;
import altera.food.ordering.domain.dto.TokenResponse;
import altera.food.ordering.domain.dto.UserDto;
import altera.food.ordering.repository.UserRepository;
import altera.food.ordering.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ResponseEntity<Object> register(LoginForm form) {
        try {
            User user = mapper.map(form, User.class);
            user.setUsername(form.getUsername());
            user.setPassword(this.bCryptPasswordEncoder.encode(form.getPassword()));
            user.setActive(form.isActive());
            user.setRole(form.getRole());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            userRepository.save(user);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, mapper.map(user, UserDto.class), HttpStatus.OK);
        }catch (Exception e){
            log.error("Got an error when saving new user. Error: {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenResponse generateToken(LoginForm form) {
        try {
            Authentication authentication = securityConfig.authenticationManagerBean().authenticate(new UsernamePasswordAuthenticationToken(
                    form.getUsername(), form.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);
            return tokenResponse;
        }catch (BadCredentialsException ex){
            log.error("Bad Credentials", ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
