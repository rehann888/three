package restfulapi.three.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import restfulapi.three.entity.User;
import restfulapi.three.exception.ResourceNotFoundException;
import restfulapi.three.repository.UserRepo;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    public User findById(String Id){
        return userRepo.findById(Id)
        .orElseThrow(() -> new ResourceNotFoundException("User with id " + Id + " not found"));
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public User create(User user) throws BadRequestException{
        
        
        if (!StringUtils.hasText(user.getId())) {
            throw new BadRequestException("User ID must be filled");
        }

        if (userRepo.existsById(user.getId())) {
            throw new BadRequestException("Username with " + user.getId() + "regristered");
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw new BadRequestException("Email must be filled");
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email with " + user.getEmail() + "is regristered");
        }

        user.setActive(true);
        return userRepo.save(user);
    }

    public User edit(User user) throws BadRequestException{

        if (!StringUtils.hasText(user.getEmail())) {
            throw new BadRequestException("Email must be filled");
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email with " + user.getEmail() + "is regristered");
        }

        return userRepo.save(user);
    }

    public void deleteById(String Id){
        userRepo.deleteById(Id);
    }


}
