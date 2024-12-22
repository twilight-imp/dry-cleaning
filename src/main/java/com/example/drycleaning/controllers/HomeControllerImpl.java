package com.example.drycleaning.controllers;

import com.example.drycleaning.services.ServiceService;
import contracts.controllers.HomeController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.home.HomeViewModel;
import contracts.viewmodel.service.ServiceViewModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeControllerImpl implements HomeController {

    private ServiceService serviceService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }


    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(pageTitle);
    }

    @Override
    @GetMapping
    public String index(Model model) {
        LOG.log(Level.INFO, "User show home page");
        var viewModel = new HomeViewModel(
                createBaseViewModel("Популярные услуги"),
                serviceService.findPopular().stream().map(s -> new ServiceViewModel(s.getName(), s.getDescription(), s.getCost()))
                        .toList());
        model.addAttribute("model", viewModel);

        return "index";
    }
}
