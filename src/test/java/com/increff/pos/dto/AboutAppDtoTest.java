package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.increff.pos.model.AboutAppData;
import com.increff.pos.spring.AbstractUnitTest;

public class AboutAppDtoTest extends AbstractUnitTest {
	@Autowired
	private AboutAppDto aboutAppDto;

	@Value("${app.name}")
	private String name;
	@Value("${app.version}")
	private String version;

	@Test
	public void testGetNameandVersion() {
		AboutAppData aboutAppData = aboutAppDto.getNameandVersion();
		// test data
		assertEquals(name, aboutAppData.name);
		assertEquals(version, aboutAppData.version);
	}
}
