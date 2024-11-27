package restfulapi.three.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import restfulapi.three.entity.User;
import restfulapi.three.repository.UserRepo;

@Service
public class UserDEtailServiceImplementation implements UserDetailsService  {

    @Autowired
    UserRepo userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "NotFound" ));
        return UserDetailImplementation.build(user);
    }
}
    