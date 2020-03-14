package com.increff.employee.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;

import com.increff.employee.model.BillData;

public class GenerateXMLTest {
	// test XML Generation
	@Test
	public void testGenerateXML() throws ParserConfigurationException, TransformerException {
		List<BillData> billItemList = new ArrayList<BillData>();
		BillData b = new BillData();
		int id = 1;
		int orderId = 2;
		String name = "increff";
		int quantity = 40;
		double mrp = 10.50;
		b.setId(id);
		b.setName(name);
		b.setQuantity(quantity);
		b.setMrp(mrp);
		billItemList.add(b);
		// test is successful if it does not throw exception
		GenerateXML.createXml(billItemList, orderId);
	}
}
