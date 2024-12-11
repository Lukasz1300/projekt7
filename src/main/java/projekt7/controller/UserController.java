package projekt7.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.User;
import projekt7.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Pobranie wszystkich użytkowników
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/list";  // Widok "list1.html" w katalogu "user"
    }

    // Pobranie użytkownika po ID
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model, HttpServletResponse response) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/detail";  // Widok "detail.html" w katalogu "user"
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("error", "User Id not found");
            return "error/404";  // Widok strony błędu 404
        }
    }

    // Pobranie użytkownika po nazwie użytkownika
    @GetMapping("/username/{username}")
    public String getUserByUsername(@PathVariable String username, Model model, HttpServletResponse response) {
        Optional<User> user = userService.findUserByUserName(username);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/detail";  // Widok "detail.html" w katalogu "user"
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("error", "User not found");
            return "error/404";  // Widok 404
        }
    }

    // Pobranie użytkownika po adresie e-mail
    @GetMapping("/email/{email}")
    public String getUserByEmail(@PathVariable String email, Model model, HttpServletResponse response) {
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/detail";  // Widok "detail.html" w katalogu "user"
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("error", "User not found");
            return "error/404";  // Widok strony błędu 404
        }
    }

    // Formularz dodawania nowego użytkownika
    @GetMapping("/form")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/form";  // Widok "form1.html" w katalogu "user"
    }

    // Formularz edycji użytkownika
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model, HttpServletResponse response) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/edit";  // Widok "edit.html" w katalogu "user"
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("error", "User not found");
            return "error/404";  // Widok 404 w przypadku nieznalezienia użytkownika
        }
    }

        // Metoda do obsługi aktualizacji użytkownika (z formularza)
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT)
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "user/edit";  // Jeśli wystąpiły błędy, wróć do formularza
        }
        // Pobranie istniejącego użytkownika
        Optional<User> existingUserOptional = userService.findUserById(id);

        // Sprawdzenie, czy użytkownik istnieje
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();  // Pobranie użytkownika z Optional
            user.setId(id);  // Ustawienie ID użytkownika

            // Zapisanie zaktualizowanego użytkownika
            userService.saveUser(user);
            return "redirect:/users";  // Po zapisaniu użytkownika, przekierowanie na listę
        } else {
            model.addAttribute("error", "User not found");
            return "redirect:/users";  // Jeśli użytkownik nie istnieje, przekierowanie na listę
        }
    }



    // Dodanie nowego użytkownika
    @PostMapping
    public String saveUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);  // Przekaż użytkownika z powrotem do formularza
            return "user/form";  // Zwróć widok formularza, jeśli wystąpiły błędy
        }

        userService.saveUser(user);
        return "redirect:/users";  // Po zapisaniu użytkownika, przekierowanie na listę
    }

    // Usuwanie użytkownika po ID
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);  // Usuwanie użytkownika z bazy danych
        return "redirect:/users";  // Przekierowanie po usunięciu
    }
}

