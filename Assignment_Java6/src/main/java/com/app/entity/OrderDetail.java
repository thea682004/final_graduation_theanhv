package com.app.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity 
@Table(name = "Orderdetails")
public class OrderDetail implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	Integer quantity;
	Boolean reviewstatus;
	String size;
	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;
	@ManyToOne
	@JoinColumn(name = "order_id")
	Order order;
}
