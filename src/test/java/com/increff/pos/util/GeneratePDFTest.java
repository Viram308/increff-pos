package com.increff.pos.util;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class GeneratePDFTest extends AbstractUnitTest{
	// test PDF Generation
	@Test
	public void testGeneratePDF() throws FOPException, TransformerException, IOException {
		GeneratePDF g = new GeneratePDF();
		g.getClass();
		// test is successful if it does not throw exception
		GeneratePDF.createPDF();
	}
}
