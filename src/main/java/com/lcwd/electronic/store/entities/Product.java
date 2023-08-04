package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Products")
@Builder
public class Product {
@Id
private String productId;
private String title;
@Column(length = 10000)
private String description;

private int price;
private int discountPrice;
private int quantity;
private Date addedDate;
private boolean live;
private boolean stock;
private String productImage;
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "category_id")
private Category category;
}
