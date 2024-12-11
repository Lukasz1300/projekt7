package projekt7.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projekt7.entity.Role;
import projekt7.entity.User;
import projekt7.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

 // metoda pobiera z bazy użytkownika z rola Admin
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Wyszukaj użytkownika w bazie danych
//        User user = userRepository.findByUsername(username);
//
//        // Sprawdź, czy użytkownik istnieje
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        // Tworzenie obiektu UserDetails z informacjami o użytkowniku oraz jego rolami
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                user.getRoles().stream()
//                        .map(role -> new SimpleGrantedAuthority(role.getName()))
//                        .collect(Collectors.toList())
//        );
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("testuser".equals(username)) { // Sprawdzanie, czy użytkownik to admin
            // Tworzenie użytkownika admina na sztywno
            User user = new User();
            user.setUsername("testuser");
            user.setPassword("$2a$10$.mZNNJmnAdVM5KS9qhsYDeI8ZDWgZMbTsSrCr2cyErJ2y6T1SMftq"); // Pamiętaj, aby hasło było odpowiednio zaszyfrowane
            user.setRoles(Set.of(new Role(1L, "ROLE_ADMIN", new HashSet<>()))); // Przypisz rolę admina

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
        }
        // Jeśli użytkownik nie jest adminem, wyszukaj go w bazie danych
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            e.printStackTrace(); // stack trace
            throw e;
        }
    }
}




