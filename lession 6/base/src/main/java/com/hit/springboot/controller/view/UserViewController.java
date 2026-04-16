package com.hit.springboot.controller.view;

import com.hit.springboot.dto.request.CreateUserRequest;
import com.hit.springboot.dto.response.UserDetailResponse;
import com.hit.springboot.dto.response.UserListResponse;
import com.hit.springboot.exception.extended.DuplicateResourceException;
import com.hit.springboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    // Hiển thị danh sách users
    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {

        Page<UserListResponse> users = userService.findAll(
                PageRequest.of(page, size, Sort.by("createdAt").descending()), search);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        return "users/list";  // → templates/users/list.html
    }

    // Hiển thị chi tiết user
    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        UserDetailResponse user = userService.findById(id);
        model.addAttribute("user", user);
        return "users/detail";  // → templates/users/detail.html
    }

    // Hiển thị form tạo user mới
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("userForm", new CreateUserRequest());
        return "users/form";
    }

    // Xử lý form submit — tạo user
    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userForm") CreateUserRequest req,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "users/form";  // Trở lại form nếu validation fail
        }

        try {
            userService.create(req);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo user thành công!");
            return "redirect:/users";  // Redirect sau POST (PRG pattern)
        } catch (DuplicateResourceException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "users/form";
        }
    }
}
