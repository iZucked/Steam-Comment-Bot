package com.mmxlabs.models.migration;

import org.eclipse.emf.common.util.URI;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.migration.utils.EcoreHelper;

public class EcoreHelperTest {
	@Test
	public void test() throws Exception {

		final URI uri = URI.createURI(getClass().getResource("/models/model-v1.ecore").toString());

		Assert.assertEquals("http://com.mmxlabs.models.migration.tests/model", EcoreHelper.getPackageNS(uri));
	}
}
