package org.example.userproject1.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.service.PhoneServise;
import org.example.userproject1.service.UserSevise;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/contacts")
public class PhoneController {

    private final PhoneServise phoneServise;
    private final UserSevise userSevise;

    @GetMapping("/{id}")
    public ModelAndView listContact(@PathVariable long id) {
        ModelAndView result = new ModelAndView("contactPage");
        result.addObject("PhoneNumbers",phoneServise.userContacts(id));
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
        phoneTemp.setPhone(phone);
        phoneTemp.setUser(userSevise.getUser(id));
        phoneServise.createContact(phoneTemp);
        return new ModelAndView(new RedirectView("/user/contacts/" + id));
    }

    @PostMapping("/{id}/delete")
    public RedirectView delete(@PathVariable long id,
            @RequestParam long contact_id) {
        phoneServise.deleteById(contact_id);
        return new RedirectView("/user/contacts/" + id);
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable long id,
                             @RequestParam long contact_id) {
        ModelAndView result = new ModelAndView("editContactPage");
        Phone phone = phoneServise.getPhone(contact_id);
        result.addObject("Contact", phone.getPhone());
        result.addObject("ContactId", phone.getId());
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
        phone.setPhone(phone_number);
        phone.setUser(userSevise.getUser(id));
        phoneServise.update(phone);
        return new ModelAndView(new RedirectView("/user/contacts/" + id));
    }

}
