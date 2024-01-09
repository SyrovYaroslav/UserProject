package org.example.userproject1.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.service.PhoneServise;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/contacts")
public class PhoneController {

    private final PhoneServise phoneServise;

    @PostMapping("")
    public ModelAndView getContact(@RequestParam long id) {
        ModelAndView result = new ModelAndView("contactUserPage");
        result.addObject("PhoneNumbers",phoneServise.userContacts(id));
        return result;
    }
}
