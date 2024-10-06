package com.example.assignment.common.model.entity;

import com.example.assignment.common.model.converter.ClothTypeConverter;
import com.example.assignment.common.model.enums.ClothType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "cloth_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothCategory {

    @Id
    @Column(name = "category_code", nullable = false)
    private Integer code;

    @Column(name = "category_group_code", nullable = false)
    private Integer groupCode;

    @Convert(converter = ClothTypeConverter.class)
    @Column(name = "category", unique = true, length = 30, columnDefinition = "varchar(30)", nullable = false)
    private ClothType category;

    @Column(name = "desc", length = 50, columnDefinition = "varchar(50)")
    private String desc;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    public ClothCategory(Integer code, Integer groupCode, ClothType category, String desc) {
        this.code = code;
        this.groupCode = groupCode;
        this.category = category;
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClothCategory category = (ClothCategory) o;
        return code != null && Objects.equals(code, category.code);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
