package com.example.drycleaning.controllers;

import com.example.drycleaning.services.ServiceService;
import contracts.controllers.ServiceController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.service.ServiceListViewModel;
import contracts.viewmodel.service.ServiceViewModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class ServiceControllerImpl implements ServiceController {

    private ServiceService serviceService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }


    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(pageTitle);
    }

    @Override
    @GetMapping()
    public String listServices(Principal principal, Model model) {
        if (principal != null) {
            LOG.log(Level.INFO, "User " + principal.getName() + " show all services");
        } else {
            LOG.log(Level.INFO, "Unauthenticated user show all services");
        }
        var baseViewModel = createBaseViewModel("Услуги");
        if (principal != null) {
            String email = principal.getName();
            var viewModel = new ServiceListViewModel(
                    baseViewModel,
                    serviceService.findAllForUser(email).stream().map(s -> new ServiceViewModel(s.getName(),s.getDescription(), s.getCost())).toList());
            model.addAttribute("model", viewModel);
        }else {
            var viewModel = new ServiceListViewModel(
                   baseViewModel,
                    serviceService.findAll().stream().map(s -> new ServiceViewModel(s.getName(), s.getDescription(), s.getCost())).toList());
            model.addAttribute("model", viewModel);
        }
        return "services-list";
    }

}
