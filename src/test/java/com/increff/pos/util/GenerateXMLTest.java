package com.increff.pos.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;

import com.increff.pos.model.BillData;
import com.increff.pos.spring.AbstractUnitTest;

public class GenerateXMLTest extends AbstractUnitTest {
	// test XML Generation
	@Test
	public void testGenerateXML() throws ParserConfigurationException, TransformerException {
		List<BillData> billItemList = new ArrayList<BillData>();
		BillData b = new BillData();
		int id = 1;
		String name = "increff";
		int quantity = 40;
		double mrp = 10.50;
		b.id = id;
		b.name = name;
		b.quantity = quantity;
		b.mrp = mrp;
		billItemList.add(b);
		// test is successful if it does not throw exception
		GenerateXML.createXml(billItemList);
	}
}
