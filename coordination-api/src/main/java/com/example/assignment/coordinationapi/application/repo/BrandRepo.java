package com.example.assignment.coordinationapi.application.repo;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.coordinationapi.application.repo.q.QBrandRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Integer>, QBrandRepo {

}
