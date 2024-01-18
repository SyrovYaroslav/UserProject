package org.example.userproject1.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.userproject1.dto.PhoneDto;
import org.example.userproject1.entity.Phone;
import org.example.userproject1.service.PhoneService;
import org.example.userproject1.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView listContact(@PathVariable long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "") String like) {
        Page<Phone> contactPage = phoneService.userContacts(id, page, 1, like);
        return new ModelAndView("contactPage")
                .addObject("PhoneNumbers", contactPage.getContent())
                .addObject("UserId", id)
                .addObject("Like", like)
                .addObject("currentPage", contactPage.getNumber())
                .addObject("totalPages", contactPage.getTotalPages())
                .addObject("pageNumbers", userService.pageSlicer(contactPage.getNumber(), contactPage.getTotalPages()));
    }

    @GetMapping("/{id}/create")
    public ModelAndView create(@PathVariable String id) {
        return new ModelAndView("createContactPage").addObject("UserId", id);
    }

    @PostMapping("/{id}/create")
    public ModelAndView create(@PathVariable long id,
                               @RequestParam String phone) {


        PhoneDto phoneDto = phoneService.saveContact(id, phone);

        return phoneDto.getError().isEmpty()
                ?new ModelAndView(new RedirectView("/user/contacts/" + id))
                :new ModelAndView("createContactPage")
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
                             @RequestParam long phone_id
    ){
        PhoneDto phoneDto = phoneService.saveContact(id, phone_number, phone_id);

        return phoneDto.getError().isEmpty()
                ?new ModelAndView(new RedirectView("/user/contacts/" + id))
                :new ModelAndView("editContactPage")
                .addObject("errors", phoneDto.getError())
                .addObject("Phone", phoneDto)
                .addObject("UserId", id);
    }
}
