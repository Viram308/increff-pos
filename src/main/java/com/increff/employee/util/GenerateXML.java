package com.increff.employee.util;

import java.io.File;
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

import com.increff.employee.model.BillData;

public class GenerateXML {
	static String xmlFilePath = "billDataXML.xml";

	public static void createXml(List<BillData> billDataItems, int orderId)
			throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();

		int i = 0;
		// root element
		Element root = document.createElement("bill");
		document.appendChild(root);

		for (i = 0; i < billDataItems.size(); i++) {
			Element item = document.createElement("item");
			root.appendChild(item);
			Element id = document.createElement("id");
			id.appendChild(document.createTextNode(String.valueOf(billDataItems.get(i).getId())));
			item.appendChild(id);

			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(billDataItems.get(i).getName()));
			item.appendChild(name);

			Element quantity = document.createElement("quantity");
			quantity.appendChild(document.createTextNode(String.valueOf(billDataItems.get(i).getQuantity())));
			item.appendChild(quantity);

			Element mrp = document.createElement("mrp");
			mrp.appendChild(document.createTextNode(String.valueOf(billDataItems.get(i).getMrp())));
			item.appendChild(mrp);

		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);

		StreamResult streamResult = new StreamResult(new File(xmlFilePath));

		transformer.transform(domSource, streamResult);
	}
}
