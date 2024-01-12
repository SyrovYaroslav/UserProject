package org.example.userproject1.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.dto.PhoneDto;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.service.PhoneService;
import org.example.userproject1.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/contacts")
public class PhoneController {

    private final PhoneService phoneService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ModelAndView listContact(@PathVariable long id) {
        ModelAndView result = new ModelAndView("contactPage");
        result.addObject("PhoneNumbers", phoneService.userContacts(id));
        result.addObject("UserId", id);
        return result;
    }

    @GetMapping("/{id}/create")
    public ModelAndView create(@PathVariable String id) {
        return new ModelAndView("createContactPage").addObject("UserId", id);
    }

    @PostMapping("/{id}/create")
    public ModelAndView create(@PathVariable long id,
                                @RequestParam String phone) {
        final Phone phoneTemp = new Phone();
        phoneTemp.setPhoneNumber(phone);
        phoneTemp.setUser(userService.getUser(id));

        PhoneDto phoneDto = phoneService.saveContact(phoneTemp);

        if (phoneDto.getError().isEmpty()){
            return new ModelAndView(new RedirectView("/user/contacts/" + id));
        }
        return new ModelAndView("createContactPage")
                .addObject("errors", phoneDto.getError())
                .addObject("UserId", id);
    }

    @PostMapping("/{id}/delete")
    public RedirectView delete(@PathVariable long id,
            @RequestParam long contact_id) {
        phoneService.deleteById(contact_id);
        return new RedirectView("/user/contacts/" + id);
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable long id,
                             @RequestParam long contact_id) {
        ModelAndView result = new ModelAndView("editContactPage");
        Phone phone = phoneService.getPhone(contact_id);
        result.addObject("Phone", phone);
        result.addObject("UserId", id);
        return result;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable long id,
                             @RequestParam String phone_number,
                             @RequestParam long contact_id
                             ){
        final Phone phone = new Phone();
        phone.setId(contact_id);
        phone.setPhoneNumber(phone_number);
        phone.setUser(userService.getUser(id));

        PhoneDto phoneDto = phoneService.saveContact(phone);

        if (phoneDto.getError().isEmpty()){
            return new ModelAndView(new RedirectView("/user/contacts/" + id));
        }
        return new ModelAndView("editContactPage")
                .addObject("errors", phoneDto.getError())
                .addObject("Phone", phone)
                .addObject("UserId", id);
    }
}
