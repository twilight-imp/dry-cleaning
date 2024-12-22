package com.example.drycleaning.repositories;


import com.example.drycleaning.models.Loyalty;

public interface LoyaltyRepository  extends BaseRepository<Loyalty>{


    Loyalty findLoyaltyByUserId(String userId);


}
