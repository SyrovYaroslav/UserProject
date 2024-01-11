package org.example.userproject1.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.entity.User;
import org.example.userproject1.service.UserServise;
import org.example.userproject1.validator.UserValidator;
import org.example.userproject1.validator.ValidationResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServise userServise;
    private final UserValidator userValidator;

    @GetMapping("")
    public ModelAndView list() {
        ModelAndView result = new ModelAndView("allUserPage");
        result.addObject("UserList", userServise.listAll());
        return result;
    }


    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView result = new ModelAndView("createUserPage");
        return result;
    }

    @PostMapping("/create")
    public ModelAndView create(@RequestParam String email,
                            @RequestParam String password) {
        final User user = new User();
        user.setMail(email);
        user.setPassword(password);
        ValidationResult validationResult = userValidator.isValid(user);
        if(validationResult.isValid()){
            return new ModelAndView("createUserPage")
                    .addObject("errors", validationResult.getErrors());
        }
        userServise.create(user);
        return new ModelAndView(new RedirectView("/user"));
    }

    @PostMapping("/delete")
    public RedirectView delete(@RequestParam long id) {
        userServise.deleteById(id);
        return new RedirectView("/user");
    }

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam long id) {
        ModelAndView result = new ModelAndView("editUserPage");
        User user = userServise.getUser(id);
        result.addObject("User", user);
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@RequestParam long id,
                             @RequestParam String email,
                             @RequestParam String password) {
        final User user = new User();
        user.setUser_id(id);
        user.setMail(email);
        user.setPassword(password);
        ValidationResult validationResult = userValidator.isValid(user);
        if(validationResult.isValid()){
            return new ModelAndView("editUserPage")
                    .addObject("errors", validationResult.getErrors())
                    .addObject("User", user);
        }
        userServise.update(user);
        return new ModelAndView(new RedirectView("/user"));
    }
}
