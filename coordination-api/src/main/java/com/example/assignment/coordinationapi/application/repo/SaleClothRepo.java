package com.example.assignment.coordinationapi.application.repo;

import com.example.assignment.common.model.entity.SaleCloth;
import com.example.assignment.coordinationapi.application.model.coordination.BrandIdSum;
import com.example.assignment.coordinationapi.application.repo.q.QSaleClothRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SaleClothRepo extends JpaRepository<SaleCloth, BigInteger>, QSaleClothRepo {
    // querydsl은 from절에 subquery가 불가능하여 native query로 사용한다.
    // mybatis 쓸까 했으나 native query 사용하는곳이 한군데밖에 그냥 사용한다.
    @Query(value = "select brand_id, sum(price) as sum\n" +
            "from (\n" +
            "  select brand_id, cloth_category_code, min(price) as price from sale_cloth where cloth_category_code in (10000, 10001, 20001, 30001, 40000, 50000, 60000, 70000) group by cloth_category_code, brand_id\n" +
            ") a\n" +
            "group by brand_id\n" +
            "order by sum\n" +
            "limit 1", nativeQuery = true)
    BrandIdSum getCheapestBrandSum();
}
