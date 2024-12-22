package com.example.drycleaning.controllers;


import com.example.drycleaning.dtos.input.UserRegistrationDto;
import com.example.drycleaning.dtos.output.CityDtoOutput;
import com.example.drycleaning.errors.AddUserException;
import com.example.drycleaning.services.CityService;
import com.example.drycleaning.services.impl.AuthServiceImpl;
import contracts.controllers.AuthController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.user.auth.LogInForm;
import contracts.viewmodel.user.auth.LogInViewModel;
import contracts.viewmodel.user.auth.RegisterForm;
import contracts.viewmodel.user.auth.RegisterViewModel;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private AuthServiceImpl authService;

    private CityService cityService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setAuthService(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    @GetMapping("/register")
    public String registerForm(Model model) {
        var viewModel = new RegisterViewModel(
                createBaseViewModel("Регистрация")
        );
        List<CityDtoOutput> cities = cityService.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new RegisterForm("", "", "", "", "", "", ""));
        return "register";
    }

    @Override
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Register user");
        if (bindingResult.hasErrors()) {
            var viewModel = new RegisterViewModel(
                    createBaseViewModel("Регистрация")
            );
            List<CityDtoOutput> cities = cityService.findAll();
            model.addAttribute("cities", cities);

            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "register";
        }
        try {
            authService.register(new UserRegistrationDto(form.lastName(), form.name(), form.phone(), form.email(), form.password(), form.confirmPassword(), form.city()));
            LOG.log(Level.INFO, "User successfully registered");
        }catch (AddUserException e){
            var viewModel = new RegisterViewModel(
                    createBaseViewModel("Регистрация")
            );
            List<CityDtoOutput> cities = cityService.findAll();
            model.addAttribute("cities", cities);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        return "redirect:/auth/login";


    }
    @Override
    @GetMapping("/login")
    public String loginForm(Model model) {
        LOG.log(Level.INFO, "Login user");
        var viewModel = new LogInViewModel(
                createBaseViewModel("Вход")
        );
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new LogInForm("", ""));
        return "log-in";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {
        LOG.log(Level.INFO, "Failed login user");
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);
        return "redirect:/auth/login";
    }


    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(pageTitle);
    }
}
