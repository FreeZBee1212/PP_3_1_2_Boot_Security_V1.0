package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(RegistrationService registrationService, RoleRepository roleRepository) {
        this.registrationService = registrationService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user, @ModelAttribute("role") Role role) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") User user, @ModelAttribute("role") Role role2,
                                      @RequestParam(value = "roles", required = false) List<String> roles) {
        Set<Role> userRoles = new HashSet<>();
        if (roles != null) {
            for (String role : roles) {
                Role roleObj = roleRepository.findByName(role);
                if (roleObj != null) {
                    userRoles.add(roleObj);
                }
            }
        }
        user.setRoles(userRoles);
        registrationService.registration(user);
        return "redirect:/auth/login";
    }

}
