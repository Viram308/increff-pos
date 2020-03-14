package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InventoryPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	// Foreign key for product id
	@ManyToOne(optional = false)
	@JoinColumn(name = "productId", nullable = false)
	private ProductMasterPojo productMasterPojo;
	private int quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProductMasterPojo getProductMasterPojo() {
		return productMasterPojo;
	}

	public void setProductMasterPojo(ProductMasterPojo productMasterPojo) {
		this.productMasterPojo = productMasterPojo;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
