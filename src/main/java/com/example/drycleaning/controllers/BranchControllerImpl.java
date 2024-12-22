package com.example.drycleaning.controllers;

import com.example.drycleaning.services.BranchService;
import com.example.drycleaning.services.CityService;
import contracts.controllers.BranchController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.branch.BranchListViewModel;
import contracts.viewmodel.branch.BranchViewModel;
import contracts.viewmodel.review.ReviewViewModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class BranchControllerImpl implements BranchController {

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    private BranchService branchService;


    private CityService cityService;

    @Autowired
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }


    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(pageTitle);
    }

    @Override
    @GetMapping
    public String listBranchesByRating(Principal principal, Model model) {
        if (principal != null) {
            LOG.log(Level.INFO, "User " + principal.getName() + " show all branches");
        } else {
            LOG.log(Level.INFO, "Unauthenticated user show all branches");
        }
        var baseViewModel = createBaseViewModel("Филиалы");
        if (principal != null){
            String email = principal.getName();
            var viewModel = new BranchListViewModel(
                    baseViewModel,
                    branchService.findByCityAndRating(email).stream().map(b -> new BranchViewModel(b.getName(),cityService.findByBranchId(b.getId()).getName(),b.getAddress(),b.getPhone(),b.getRating(),
                                    b.getReviews().stream().map(r->new ReviewViewModel(r.getUserName(), r.getComment(), r.getScore())).toList()))
                            .toList());
            model.addAttribute("model", viewModel);
        }
        else {
            var viewModel = new BranchListViewModel(
                    baseViewModel,
                    branchService.findAllBranchesSortedByRating().stream().map(b -> new BranchViewModel(b.getName(), cityService.findByBranchId(b.getId()).getName(), b.getAddress(), b.getPhone(), b.getRating(),
                                    b.getReviews().stream().map(r -> new ReviewViewModel(r.getUserName(), r.getComment(), r.getScore())).toList()))
                            .toList());
            model.addAttribute("model", viewModel);
        }
        return "branches-list";
    }

}
