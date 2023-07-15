package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Products")
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

}
