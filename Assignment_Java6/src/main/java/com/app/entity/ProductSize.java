package com.app.entity;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
public class ProductSize implements Serializable{
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String size;
	Integer quantity;

	@ManyToOne
  @JoinColumn(name = "product_id")
  Product product;
}
