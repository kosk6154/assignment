package com.example.assignment.coordinationapi.application.model.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class ModifyBrand {
    @NotBlank
    @Length(max = 20)
    private String oldBrandName;

    @NotBlank
    @Length(max = 20)
    private String newBrandName;
}
