package org.example.userproject1.endpoint;

import lombok.RequiredArgsConstructor;

import org.example.userproject1.entity.SecurityUser;
import org.example.userproject1.service.SecurityService;
import org.example.userproject1.service.UserService;
import org.example.userproject1.validator.Error;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;
    private final UserService userService;

    @GetMapping("/notAuthorized")
    public ModelAndView notAuthorized() {
        return new ModelAndView("notAuthorized");
    }

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("/login");
    }

    @GetMapping("/registration")
    public ModelAndView registration(){
        return new ModelAndView("registration");
    }

    @PostMapping("/registration")
    public ModelAndView registrationNewAccount(@RequestParam String username,
                                               @RequestParam String email,
                                               @RequestParam String password,
                                               @RequestParam String confirmPassword){
        List<Error> errors = securityService.addNewSecurityUser(username, email, password, confirmPassword);
        return errors.isEmpty()?new ModelAndView(new RedirectView("/login"))
                :new ModelAndView("registration").addObject("errors", errors);
    }

    @GetMapping("/admin")
    public ModelAndView adminPage(@RequestParam(defaultValue = "0") int page){
        Page<SecurityUser> userPage = securityService.listAllSecurityUsers(page, 1);
        return new ModelAndView("adminPage")
                .addObject("Accounts", userPage.getContent())
                .addObject("currentPage", userPage.getNumber())
                .addObject("totalPages", userPage.getTotalPages())
                .addObject("pageNumbers", userService.pageSlicer(userPage.getNumber(), userPage.getTotalPages()));
    }
}
