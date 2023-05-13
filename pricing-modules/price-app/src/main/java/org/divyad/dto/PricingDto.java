package org.divyad.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PricingDto {

    @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "Field: STOREID, Error: Store ID should be a positive number only")
    @NotNull(message = "Field: STOREID, Error: Store ID is required")
    private String STOREID;

    @NotNull(message = "Field: SKU, Error: SKU cannot be blank")
    @Size(min = 1, max = 50, message = "Field: SKU, Error: Length of SKU cannot exceed 50")
    private String SKU;

    @NotNull(message = "Field: NAME, Error: Product Name cannot be blank")
    @Size(min = 1, max = 50, message = "Field: SKU, Error: Length of NAME cannot exceed 1000")
    private String NAME;

    @NotNull(message = "Field: PRICE, Error: Price cannot be blank")
    @Pattern(regexp = "^(\\s*|^[0-9]{1,9}$)$", message = "Field: PRICE, Error: Price should be a number only")
    private String PRICE;

    @NotNull(message = "Field: DATE, Error: Date cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Field: DATE, Error: Date should be only in yyyy-MM-dd format")
    private String DATE;
}
