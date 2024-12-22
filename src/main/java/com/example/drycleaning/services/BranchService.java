package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.BranchDtoInput;
import com.example.drycleaning.dtos.output.BranchDtoOutput;

import java.util.List;

public interface BranchService {

    BranchDtoOutput add(BranchDtoInput branchDto);

    List<BranchDtoOutput> findAll();

    BranchDtoOutput findById(String branchId);

    void remove(String branchId);

    void modify(BranchDtoInput newBranchDto, String branchId);

    List<BranchDtoOutput> findByCityAndRating(String email);

    List<BranchDtoOutput> findAllBranchesSortedByRating();

}
