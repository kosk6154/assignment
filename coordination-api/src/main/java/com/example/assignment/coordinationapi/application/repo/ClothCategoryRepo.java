package com.example.assignment.coordinationapi.application.repo;

import com.example.assignment.common.model.entity.ClothCategory;
import com.example.assignment.coordinationapi.application.repo.q.QClothCategoryRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothCategoryRepo extends JpaRepository<ClothCategory, Integer>, QClothCategoryRepo {
    
}
