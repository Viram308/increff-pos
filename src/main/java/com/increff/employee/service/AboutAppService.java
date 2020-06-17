package com.increff.employee.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.increff.employee.model.AboutAppData;

@Service
public class AboutAppService {

	@Value("${app.name}")
	private String name;
	@Value("${app.version}")
	private String version;

	public AboutAppData getNameandVersion() {
		AboutAppData d = new AboutAppData();
		d.setName(name);
		d.setVersion(version);
		return d;
	}
}
