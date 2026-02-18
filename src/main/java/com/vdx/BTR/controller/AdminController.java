package com.vdx.BTR.controller;

import com.vdx.BTR.model.Role;
import com.vdx.BTR.model.User;
import com.vdx.BTR.service.SettingsService;
import com.vdx.BTR.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final SettingsService settingsService;

    public AdminController(UserService userService, SettingsService settingsService) {
        this.userService = userService;
        this.settingsService = settingsService;
    }


    // Lista użytkowników
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin-users";
    }

    // Formularz edycji ról
    @GetMapping("/users/{id}/edit")
    public String editUserRoles(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        model.addAttribute("user", user);
        model.addAttribute("allRoles", Arrays.asList(Role.values()));
        return "admin-user-edit";
    }

    // Zapis ról
    @PostMapping("/users/{id}/edit")
    public String updateUserRoles(@PathVariable Long id,
                                  @RequestParam(required = false, name = "roles") List<Role> roles) {

        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        Set<Role> newRoles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        user.setRoles(newRoles);

        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/settings")
    public String showSettings(Model model) {
        model.addAttribute("settings", settingsService.getSettings());
        return "admin-settings";
    }

    @PostMapping("/settings")
    public String updateSettings(@RequestParam("approvalThreshold") BigDecimal approvalThreshold) {
        settingsService.updateThreshold(approvalThreshold);
        return "redirect:/admin/settings";
    }
}
