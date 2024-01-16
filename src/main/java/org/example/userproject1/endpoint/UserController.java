package org.example.userproject1.endpoint;


import lombok.RequiredArgsConstructor;
import org.example.userproject1.dto.UserDto;
import org.example.userproject1.entity.User;
import org.example.userproject1.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ModelAndView list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String like) {
        ModelAndView result = new ModelAndView("allUserPage");
        Page<User> userPage = userService.listAll(page, 9, like);
        result.addObject("UserList", userPage.getContent());
        result.addObject("Like", like);
        result.addObject("currentPage", userPage.getNumber());
        result.addObject("totalPages", userPage.getTotalPages());
        result.addObject("pageNumbers", userService.pageSlicer(userPage.getNumber(), userPage.getTotalPages()));
        return result;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("createUserPage");
    }

    @PostMapping("/create")
    public ModelAndView create(@RequestParam String email,
                            @RequestParam String password) {
        UserDto userDto = userService.saveUser(email, password);
        return userDto.getError().isEmpty()
                ?new ModelAndView(new RedirectView("/user"))
                :new ModelAndView("createUserPage")
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
        UserDto userDto = userService.saveUser(email, password, id);
        return userDto.getError().isEmpty()
                ?new ModelAndView(new RedirectView("/user"))
                :new ModelAndView("editUserPage")
                .addObject("errors", userDto.getError())
                .addObject("User", userDto);
    }
}
