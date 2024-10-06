package com.example.assignment.common.model.entity;

import com.example.assignment.common.model.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(
        name = "sale_cloth",
        indexes = {
                @Index(name = "idx_category_brand_price", columnList = "cloth_category_code, brand_id, price"),
                @Index(name = "idx_category_price", columnList = "cloth_category_code, price")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleCloth {

    @Id
//    @Column(name = "sale_cloth_id", columnDefinition = "bigint unsigned", nullable = false) // h2는 unsigned를 지원하지 않는다..
    @Column(name = "sale_cloth_id", columnDefinition = "bigint", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "cloth_category_code", referencedColumnName = "category_code", nullable = false)
    private ClothCategory category;

    @Setter
    @Column(name = "price", nullable = false)
    private BigDecimal price;

//    @Column(name = "currency", nullable = false)
//    private String currency;

    @Setter
    @Column(name = "cloth_name", nullable = false, columnDefinition = "varchar(30)")
    private String clothName;

    /*
     * 할인율이 들어가면 프로모션 기간, 중복 할인등이 고려되어야 해서
     * 구현이 너무 광범위해지므로 우선 제외
     */
//    @Column(name = "discount")
//    private BigDecimal discount;

    // -1인 경우 무제한
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    /*
     * 옷 치수 정보등도 제외
     */
//    @Column(name = "cloth_size")
//    private Integer clothSize;

//    @Column(name = "is_enable", nullable = false)
//    private Boolean isEnable;

//    // 0 이면 기한없음
//    @Column(name = "start_date", nullable = false)
//    private ZonedDateTime startDate;
//
//    // 0 이면 기한없음
//    @Column(name = "end_date", nullable = false)
//    private ZonedDateTime endDate;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    public static SaleCloth ofDefault(Brand brand, ClothCategory category, BigDecimal price, String clothName) {
        SaleCloth result = new SaleCloth();

        result.brand = brand;
        result.category = category;
//        result.currency = "KRW";
        result.price = price;
        result.clothName = clothName;
        result.quantity = -1;
        result.gender = Gender.ALL;
//        result.isEnable = true;
//        result.startDate = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
//        result.endDate = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaleCloth saleCloth = (SaleCloth) o;
        return id != null && Objects.equals(id, saleCloth.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
