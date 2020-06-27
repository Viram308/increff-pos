package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderItemPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	// Foreign key for order id
	@ManyToOne(optional = false)
	@JoinColumn(name = "orderId", nullable = false)
	private OrderPojo orderPojo;
	// Foreign key for product id
	@ManyToOne(optional = false)
	@JoinColumn(name = "productId", nullable = false)
	private ProductMasterPojo productMasterPojo;
	private int quantity;
	private double sellingPrice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrderPojo getOrderPojo() {
		return orderPojo;
	}

	public void setOrderPojo(OrderPojo orderPojo) {
		this.orderPojo = orderPojo;
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

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

}
