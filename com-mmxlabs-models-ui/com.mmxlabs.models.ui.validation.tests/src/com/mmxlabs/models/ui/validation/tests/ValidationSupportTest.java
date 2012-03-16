package com.mmxlabs.models.ui.validation.tests;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.ui.validation.ValidationSupport;

public class ValidationSupportTest {

	@Test
	public void testGetInstance() {
		Assert.assertNotNull(ValidationSupport.getInstance());
	}

	@Test
	public void testStartEditingObjects() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSame() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndEditingObjects() {
		fail("Not yet implemented");
	}

	@Test
	public void testIgnoreObjects() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnignoreObjects() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearContainers() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetContainers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParentObjectType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContainer() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContents() {
		fail("Not yet implemented");
	}

	/**
	 * Extend class to make constructor public for test purposes. 
	 *
	 */
	class MockValidationSupport extends ValidationSupport {
		public MockValidationSupport() {
			super();
		}
	}
}
