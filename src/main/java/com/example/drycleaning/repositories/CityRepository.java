package com.example.drycleaning.repositories;

import com.example.drycleaning.models.City;

public interface CityRepository extends BaseRepository<City>{

    String findIdByName(String cityName);

    String findIdByBranchId(String branchId);
}
