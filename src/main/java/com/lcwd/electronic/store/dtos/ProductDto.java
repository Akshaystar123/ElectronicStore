package com.lcwd.electronic.store.dtos;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String productId;
    private String title;
    private String description;
    private int price;
    private int discountPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;
    private CategoryDto category;


}
