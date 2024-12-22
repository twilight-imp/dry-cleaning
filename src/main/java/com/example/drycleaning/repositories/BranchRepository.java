package com.example.drycleaning.repositories;

import com.example.drycleaning.models.Branch;

import java.util.List;

public interface BranchRepository extends BaseRepository<Branch>{

    List<Branch> findByUserCity(String userCityName);

    double findRatingByBranchId(String branchId);

    Branch findDefaultBranch(String userId);

}
