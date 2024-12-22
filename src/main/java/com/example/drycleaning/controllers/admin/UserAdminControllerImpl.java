package com.example.drycleaning.controllers.admin;

import com.example.drycleaning.dtos.edit.UserRoleDtoEdit;
import com.example.drycleaning.dtos.output.CityDtoOutput;
import com.example.drycleaning.models.Role;
import com.example.drycleaning.models.UserRoles;
import com.example.drycleaning.services.CityService;
import com.example.drycleaning.services.LoyaltyService;
import com.example.drycleaning.services.UserService;
import contracts.controllers.admin.UserAdminController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.admin.user.UserAdminEditViewModel;
import contracts.viewmodel.admin.user.UserAdminViewModel;
import contracts.viewmodel.admin.user.UserListAdminViewModel;
import contracts.viewmodel.admin.user.form.UserAdminEditForm;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class UserAdminControllerImpl implements UserAdminController {

    private UserService userService;

    private LoyaltyService loyaltyService;
    private CityService cityService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setLoyaltyService(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(
                pageTitle
        );
    }

    @Override
    @GetMapping
    public String list(Model model) {
        var userViewModels = userService.findAll().stream()
                .map(u -> new UserAdminViewModel(u.getId(),u.getLastName(),u.getName(),u.getPhone(),u.getEmail(),u.getPassword(),cityService.findById(u.getCity()).getName(),
                        loyaltyService.findByUser(u.getId()).getDiscount(), loyaltyService.findByUser(u.getId()).getNumOrders(),u.getRoles().stream()                // Преобразуем роли
                        .map(Role::getName)
                        .map(UserRoles::getDescription)
                        .toList()
                ))
                .toList();
        var viewModel = new UserListAdminViewModel(
                createBaseViewModel("Пользователи"),
                userViewModels
        );

        model.addAttribute("model", viewModel);
        return "admin-user-list";
    }


    @Override
    @GetMapping("/edit")
    public String editForm(@RequestParam String id, Model model) {
        var user = userService.findById(id);
        var viewModel = new UserAdminEditViewModel(
                createBaseViewModel("Редактирование пользователя")
        );
        List<CityDtoOutput> cities = cityService.findAll();
        model.addAttribute("id", id);
        model.addAttribute("model", viewModel);
        model.addAttribute("userRoles", Arrays.stream(UserRoles.values())
                        .map(UserRoles::getDescription)
                .toList());
        model.addAttribute("form", new UserAdminEditForm(user.getRoles().stream().map(r -> r.getName().getDescription()).toList()));
        return "admin-user-edit";
    }

    @Override
    @PostMapping("/edit")
    public String edit(@RequestParam String id,@Valid @ModelAttribute("form")  UserAdminEditForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Admin update role for user  " + id);
        if (bindingResult.hasErrors()) {
            var viewModel = new UserAdminEditViewModel(
                    createBaseViewModel("Редактирование пользователя")
            );
            List<CityDtoOutput> cities = cityService.findAll();
            model.addAttribute("id", id);
            model.addAttribute("model", viewModel);
            model.addAttribute("userRoles", Arrays.stream(UserRoles.values()));
            model.addAttribute("form", form);
            return "admin-user-edit";
        }
        System.out.println(form.roles().toString());
        List<String> roleDescriptions = form.roles();
        List<Role> newRoles = roleDescriptions.stream()
                .map(description -> {
                    for (UserRoles role : UserRoles.values()) {
                        if (role.getDescription().equals(description)) {
                            System.out.println(role);
                            return new Role(role);
                        }
                    }
                    throw new IllegalArgumentException("Роль не найдена: " + description);
                })
                .toList();

       userService.updateRole(new UserRoleDtoEdit(newRoles),id);
        return "redirect:/admin/users";
    }

    @Override
    @GetMapping("/delete")
    public String delete(@RequestParam String id) {
        LOG.log(Level.INFO, "Admin delete user " + id);
        userService.remove(id);
        return "redirect:/admin/users";
    }
}