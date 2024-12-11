package projekt7.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekt7.entity.User;
import projekt7.repository.RoleRepository;
import projekt7.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findUserByUserName(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}


