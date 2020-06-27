package com.increff.pos.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.increff.pos.model.BillData;

public class GenerateXML {

	public static void createXml(List<BillData> billDataItems, int orderId)
			throws ParserConfigurationException, TransformerException {
		String xmlFilePath = "billDataXML.xml";

		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();

		int i = 0;
		// root element
		Element root = document.createElement("bill");
		document.appendChild(root);
		double finalBill = 0;

		Element date = document.createElement("date");
		date.appendChild(document.createTextNode(getDate()));
		root.appendChild(date);

		Element time = document.createElement("time");
		time.appendChild(document.createTextNode(getTime()));
		root.appendChild(time);
		// Create elements from BillData list
		for (i = 0; i < billDataItems.size(); i++) {
			Element item = document.createElement("item");
			root.appendChild(item);
			Element id = document.createElement("id");
			id.appendChild(document.createTextNode(String.valueOf(billDataItems.get(i).getId())));
			item.appendChild(id);

			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(billDataItems.get(i).getName()));
			item.appendChild(name);
			// Calculate total bill amount
			finalBill = finalBill + billDataItems.get(i).getQuantity() * billDataItems.get(i).getMrp();
			Element quantity = document.createElement("quantity");
			quantity.appendChild(document.createTextNode(String.valueOf(billDataItems.get(i).getQuantity())));
			item.appendChild(quantity);

			Element mrp = document.createElement("mrp");
			mrp.appendChild(document.createTextNode(String.valueOf(billDataItems.get(i).getMrp())));
			item.appendChild(mrp);

		}

		Element total = document.createElement("total");
		total.appendChild(document.createTextNode(String.valueOf(finalBill) + " Rs."));
		root.appendChild(total);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);

		StreamResult streamResult = new StreamResult(new File(xmlFilePath));

		transformer.transform(domSource, streamResult);
	}

	// Get date in required format
	private static String getDate() {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date dateobj = new Date();
		String date = df.format(dateobj);
		return date;
	}

	// Get time in required format
	private static String getTime() {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date dateobj = new Date();
		String time = df.format(dateobj);
		return time;
	}

}
