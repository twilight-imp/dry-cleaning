package com.example.drycleaning.controllers.admin;

import com.example.drycleaning.dtos.input.ServiceDtoInput;
import com.example.drycleaning.services.CityService;
import com.example.drycleaning.services.ServiceService;
import contracts.controllers.admin.ServiceAdminController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.admin.service.ServiceAdminCreateViewModel;
import contracts.viewmodel.admin.service.ServiceAdminEditViewModel;
import contracts.viewmodel.admin.service.ServiceAdminViewModel;
import contracts.viewmodel.admin.service.ServiceListAdminViewModel;
import contracts.viewmodel.admin.service.form.ServiceAdminCreateForm;
import contracts.viewmodel.admin.service.form.ServiceAdminEditForm;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ServiceAdminControllerImpl implements ServiceAdminController {

    private ServiceService serviceService;

    private CityService cityService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setServiceService(ServiceService serviceSerivice){
        this.serviceService = serviceSerivice;
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

        var serviceViewModels = serviceService.findAll().stream()
                .map(s -> new ServiceAdminViewModel(s.getServiceId(),s.getName(),s.getDescription(), s.getCost(), s.getDeleted()))
                .toList();

        var viewModel = new ServiceListAdminViewModel(
                createBaseViewModel("Услуги"),
                serviceViewModels
        );

        model.addAttribute("model", viewModel);
        return "admin-service-list";
    }

    @Override
    @GetMapping("/create")
    public String createForm(Model model) {

        var viewModel = new ServiceAdminCreateViewModel(
                createBaseViewModel("Добавление услуги")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new ServiceAdminCreateForm("","",0));
        return "admin-service-create";
    }

    @Override
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form")ServiceAdminCreateForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Admin create new service");
        if (bindingResult.hasErrors()) {
            var viewModel = new ServiceAdminCreateViewModel(
                    createBaseViewModel("Добавление услуги")
            );
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "admin-service-create";
        }
        var id = serviceService.add(new ServiceDtoInput(form.name(),form.description(),form.cost()));
        return "redirect:/admin/services";
    }

    @Override
    @GetMapping("/edit")
    public String editForm(@RequestParam String id, Model model) {
        var service = serviceService.findById(id);
        var viewModel = new ServiceAdminEditViewModel(
                createBaseViewModel("Редактирование услуги")
        );
        model.addAttribute("id", id);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new ServiceAdminEditForm(service.getServiceId(),service.getName(),service.getDescription(),service.getCost()));
        return "admin-service-edit";
    }

    @Override
    @PostMapping("/edit")
    public String edit(@RequestParam String id,@Valid @ModelAttribute("form")  ServiceAdminEditForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Admin edit service " + id);
        if (bindingResult.hasErrors()) {
            var viewModel = new ServiceAdminEditViewModel(
                    createBaseViewModel("Редактирование изделия")
            );
            model.addAttribute("id", id);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "admin-service-edit";
        }

        serviceService.modify(new ServiceDtoInput(form.name(),form.description(),form.cost()),id);
        return "redirect:/admin/services";
    }

    @Override
    @GetMapping("/delete")
    public String delete(@RequestParam String id) {
        LOG.log(Level.INFO, "Admin delete service " + id);
        serviceService.remove(id);
        return "redirect:/admin/services";
    }
}




