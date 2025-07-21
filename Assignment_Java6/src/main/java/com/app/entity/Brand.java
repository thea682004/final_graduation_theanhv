package com.app.entity;

import java.io.Serializable;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
@SuppressWarnings("serial")
@Data
@Entity 
@Table(name = "brands")
public class Brand implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String name;
	@JsonIgnore
	@OneToMany(mappedBy = "brand")
	List<Product> products;
	
}
