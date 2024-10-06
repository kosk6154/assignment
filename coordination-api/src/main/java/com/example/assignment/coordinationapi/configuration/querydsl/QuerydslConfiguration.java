package com.example.assignment.coordinationapi.configuration.querydsl;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QuerydslConfiguration {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory queryFactory() {
        // spring boot3 (hibernate 6) 부터 querydsl의 transform 사용 시 오류가 나므로 DEFAULT를 넣어 주어야 한다.
        // https://github.com/querydsl/querydsl/issues/3428#issuecomment-1337472853 참고
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

}
