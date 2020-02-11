package com.increff.employee.util;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.junit.Test;

public class GeneratePDFTest {
	@Test
	public void testGeneratePDF() throws FOPException, TransformerException, IOException {
		GeneratePDF g = new GeneratePDF();
		g.getClass();
		GeneratePDF.createPDF();
	}
}
