package com.example.drycleaning;

import com.example.drycleaning.dtos.input.*;
import com.example.drycleaning.dtos.output.UserDtoOutput;
import com.example.drycleaning.models.*;
import com.example.drycleaning.repositories.*;
import com.example.drycleaning.services.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
public class DryCleaningApplication{
	public static void main(String[] args) {
		SpringApplication.run(DryCleaningApplication.class, args);
	}

}