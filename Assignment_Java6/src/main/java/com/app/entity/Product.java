package com.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity 
@Getter
@Setter
@Table(name = "products")
public class Product implements Serializable{
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String image;
	String image1;
	String image2;
	String image3;
	String description;
	Double price;
	@Temporal(TemporalType.DATE)
	@Column(name = "createdate")
	Date createDate = new Date();
	Boolean available;
	@ManyToOne
	@JoinColumn(name = "categoryid")
	Category category;
	@ManyToOne
	@JoinColumn(name = "brand_id")
	Brand brand;
	@ManyToOne
	@JoinColumn(name = "color_id")
	Color color ;
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<OrderDetail> orderDetails;
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<ProductSize> productSizes;

}
