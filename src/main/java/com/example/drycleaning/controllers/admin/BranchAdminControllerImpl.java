package com.example.drycleaning.controllers.admin;

import com.example.drycleaning.dtos.input.BranchDtoInput;
import com.example.drycleaning.dtos.output.CityDtoOutput;
import com.example.drycleaning.services.BranchService;
import com.example.drycleaning.services.CityService;
import contracts.controllers.admin.BranchAdminController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.admin.branch.BranchAdminCreateViewModel;
import contracts.viewmodel.admin.branch.BranchAdminEditViewModel;
import contracts.viewmodel.admin.branch.BranchAdminViewModel;
import contracts.viewmodel.admin.branch.BranchListAdminViewModel;
import contracts.viewmodel.admin.branch.form.BranchAdminCreateForm;
import contracts.viewmodel.admin.branch.form.BranchAdminEditForm;
import contracts.viewmodel.branch.BranchViewModel;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BranchAdminControllerImpl implements BranchAdminController {

    private BranchService branchService;

    private CityService cityService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setBranchService(BranchService branchService){
        this.branchService = branchService;
    }

    @Autowired
    public void setCityService(CityService cityService){
        this.cityService = cityService;
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
        var branchViewModels = branchService.findAll().stream()
                .map(b -> new BranchAdminViewModel(b.getId(),b.getName(), cityService.findById(b.getCity()).getName(), b.getAddress(),b.getPhone()))
                .toList();

        var viewModel = new BranchListAdminViewModel(
                createBaseViewModel("Филиалы"),
                branchViewModels
        );

        model.addAttribute("model", viewModel);
        return "admin-branch-list";
    }

    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        var viewModel = new BranchAdminCreateViewModel(
                createBaseViewModel("Добавление филиала")
        );
        List<CityDtoOutput> cities = cityService.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new BranchAdminCreateForm("", "", "",""));
        return "admin-branch-create";
    }

    @Override
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form")BranchAdminCreateForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Admin create new branch");
        if (bindingResult.hasErrors()) {
            var viewModel = new BranchAdminCreateViewModel(
                    createBaseViewModel("Добавление филиала")
            );
            List<CityDtoOutput> cities = cityService.findAll();
            model.addAttribute("cities", cities);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "admin-branch-create";
        }
        var id = branchService.add(new BranchDtoInput(form.name(), form.city(), form.address(), form.phone()));
        return "redirect:/admin/branches";
    }

    @Override
    @GetMapping("/edit")
    public String editForm(@RequestParam String id, Model model) {
        var branch = branchService.findById(id);
            var viewModel = new BranchAdminEditViewModel(
                    createBaseViewModel("Редактирование филиала")
            );
        List<CityDtoOutput> cities = cityService.findAll();
        model.addAttribute("id", id);
        model.addAttribute("cities", cities);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new BranchAdminEditForm(branch.getId(), branch.getName(), branch.getCity(), branch.getAddress(), branch.getPhone()));
        return "admin-branch-edit";
    }

    @Override
    @PostMapping("/edit")
    public String edit(@RequestParam String id,@Valid @ModelAttribute("form")  BranchAdminEditForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Admin edit branch " + id);
        if (bindingResult.hasErrors()) {
            var viewModel = new BranchAdminEditViewModel(
                    createBaseViewModel("Редактирование филиала")
            );
            List<CityDtoOutput> cities = cityService.findAll();
            model.addAttribute("id", id);
            model.addAttribute("cities", cities);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "admin-branch-edit";
        }

        branchService.modify(new BranchDtoInput(form.name(),form.city(),form.address(), form.phone()),id);
        return "redirect:/admin/branches";
    }

    @Override
    @GetMapping("/delete")
    public String delete(@RequestParam String id) {
        LOG.log(Level.INFO, "Admin delete branch " + id);
       branchService.remove(id);
       return "redirect:/admin/branches";
    }
}
