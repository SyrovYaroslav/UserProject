package org.example.userproject1.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.dto.UserDto;
import org.example.userproject1.entity.User;
import org.example.userproject1.service.UserService;
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

    private final UserService userService;

    @GetMapping("")
    public ModelAndView list() {
        ModelAndView result = new ModelAndView("allUserPage");
        result.addObject("UserList", userService.listAll());
        return result;
    }


    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("createUserPage");
    }

    @PostMapping("/create")
    public ModelAndView create(@RequestParam String email,
                            @RequestParam String password) {
        final User user = new User();
        user.setMail(email);
        user.setPassword(password);
        UserDto userDto = userService.saveUser(user);
        if (userDto.getError().isEmpty()){
            return new ModelAndView(new RedirectView("/user"));
        }
        return new ModelAndView("createUserPage")
                .addObject("errors", userDto.getError());
    }

    @PostMapping("/delete")
    public RedirectView delete(@RequestParam long id) {
        userService.deleteById(id);
        return new RedirectView("/user");
    }

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam long id) {
        ModelAndView result = new ModelAndView("editUserPage");
        User user = userService.getUser(id);
        result.addObject("User", user);
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@RequestParam long id,
                             @RequestParam String email,
                             @RequestParam String password) {
        final User user = new User();
        user.setId(id);
        user.setMail(email);
        user.setPassword(password);

        UserDto userDto = userService.saveUser(user);

        if (userDto.getError().isEmpty()){
            return new ModelAndView(new RedirectView("/user"));
        }
        return new ModelAndView("editUserPage")
                .addObject("errors", userDto.getError())
                .addObject("User", user);
    }
}
