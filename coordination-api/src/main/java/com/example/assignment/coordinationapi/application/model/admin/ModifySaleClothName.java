package com.example.assignment.coordinationapi.application.model.admin;

import com.example.assignment.common.model.enums.ClothType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class ModifySaleClothName {
    @NotBlank
    @Length(max = 20)
    private String brandName;

    @NotNull
    private ClothType clothType;

    @NotBlank
    @Length(max = 30)
    private String oldClothName;

    @NotBlank
    @Length(max = 30)
    private String newClothName;
}
