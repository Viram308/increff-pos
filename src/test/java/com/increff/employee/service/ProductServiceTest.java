package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.ProductMasterPojo;
import com.increff.employee.spring.AbstractUnitTest;
import com.increff.employee.util.StringUtil;

public class ProductServiceTest extends AbstractUnitTest {
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService bService;

	// test product service
	@Test
	public void testAdd() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		// Add one time
		service.add(p);
		// create new data with same barcode
		ProductMasterPojo p1 = new ProductMasterPojo();
		p1.setBrand_category(p.getBrand_category());
		p1.setMrp(p.getMrp());
		p1.setName(p.getName());
		p1.setBarcode(p.getBarcode());
		service.add(p1);
		// get barcode for id of first object
		String b1 = service.get(p.getId()).getBarcode();
		// get barcode for very next object
		// here other tests are affecting database so id is not 1 and 2 but p.getId() and p.getId()+1
		String b2 = service.get(p.getId()+1).getBarcode();
		// test for different barcode
		assertNotEquals(b1, b2);
	}

	@Test
	public void testDelete() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		// Delete should be successful and should not throw exception as data exists
		service.delete(p.getId());
	}

	@Test(expected = ApiException.class)
	public void testGetByBarcode() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		ProductMasterPojo pr = service.getByBarcode(p.getBarcode());
		service.delete(pr.getId());
		// After delete throw exception while getting data
		service.getByBarcode(pr.getBarcode());
	}

	@Test
	public void testGet() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		ProductMasterPojo pr = service.get(p.getId());
		// test added data
		assertEquals("product", pr.getName());
	}

	@Test
	public void testGetAll() throws ApiException {
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);
		ProductMasterPojo pr = service.get(p.getId());
		// update data
		pr.setName(" Check ");
		service.update(pr.getId(), pr);
		ProductMasterPojo pm = service.get(pr.getId());
		// test updated data with normalization
		assertEquals("check", pm.getName());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.add(p);

		ProductMasterPojo pr = service.getCheck(p.getId());
		service.delete(pr.getId());
		// After delete throw exception while getting data
		service.getCheck(pr.getId());

	}

	@Test
	public void testNormalize() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		ProductService.normalize(p);
		// test normalized data
		assertEquals("product", p.getName());
	}
	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		ProductMasterPojo p = getProductMasterPojoTest();
		service.checkData(p);
		// throw exception
		p.setName("");
		service.checkData(p);
	}
	private ProductMasterPojo getProductMasterPojoTest() throws ApiException {
		ProductMasterPojo p = new ProductMasterPojo();
		BrandMasterPojo b = new BrandMasterPojo();
		// create data
		String barcode = StringUtil.getAlphaNumericString();
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		bService.add(b);
		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrand_category(b);
		p.setName(" ProDuct ");
		p.setMrp(mrp);
		return p;
	}
}
