package restfulapi.three.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restfulapi.three.entity.User;

public interface UserRepo extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    
}

