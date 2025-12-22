package com.gaha.whatsapp.Clone.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {

    private final UserRepository userRepo;
    private final UserMapper userMapper;

    public void synchronizeWithIdp(Jwt token) {
        log.info("synchronize User With Idp");
        getUserEmail(token).ifPresent(u -> {
            log.info("synchronize User  having  {}",u);
           // Optional<User> optionalUser = userRepo.findByEmail(u);
            User user = userMapper.fromTokenAttribute(token.getClaims());
            //optionalUser.ifPresent(value -> user.setId(optionalUser.get().getId()));
            userRepo.save(user);
        });
    }

    private Optional<String> getUserEmail(Jwt token) {
        Map<String,Object> attributes = token.getClaims();
        if(attributes.containsKey("email")){
            return Optional.of( attributes.get("email").toString());
        }
        return Optional.empty();
    }

}
