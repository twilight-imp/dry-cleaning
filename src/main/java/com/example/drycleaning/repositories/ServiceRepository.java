package com.example.drycleaning.repositories;


import com.example.drycleaning.models.Review;
import com.example.drycleaning.models.Service;

import java.time.LocalDate;
import java.util.List;

public interface ServiceRepository extends BaseRepository<Service> {
    List<Service> findPopularFromDate(LocalDate localDate);

}
