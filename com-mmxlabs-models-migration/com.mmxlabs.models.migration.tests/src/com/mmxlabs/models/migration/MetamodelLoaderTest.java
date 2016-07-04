/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MetamodelLoaderTest {

	@Test
	public void testSingleModel() {

		final MetamodelLoader loader = new MetamodelLoader();

		// Test load
		final URL resourceURL = getClass().getResource("/models/model-v1.ecore");
		final EPackage pkg1 = loader.loadEPackage(URI.createURI(resourceURL.toString()));
		Assert.assertNotNull(pkg1);

		// Test re-used reference
		final EPackage pkg2 = loader.loadEPackage(URI.createURI(resourceURL.toString()));
		Assert.assertNotNull(pkg2);

		Assert.assertSame(pkg1, pkg2);
	}

	@Test
	public void testMultipleModels() {

		final MetamodelLoader loader = new MetamodelLoader();

		// Test load

		final URL resourceURL_V1 = getClass().getResource("/models/model-v1.ecore");
		final EPackage v1_pkg1 = loader.loadEPackage(URI.createURI(resourceURL_V1.toString()));
		Assert.assertNotNull(v1_pkg1);
		final URL resourceURL_V2 = getClass().getResource("/models/model-v2.ecore");
		final EPackage v2_pkg1 = loader.loadEPackage(URI.createURI(resourceURL_V2.toString()));
		Assert.assertNotNull(v2_pkg1);
		final URL resourceURL_V3 = getClass().getResource("/models/model-v3.ecore");
		final EPackage v3_pkg1 = loader.loadEPackage(URI.createURI(resourceURL_V3.toString()));
		Assert.assertNotNull(v3_pkg1);

		// Test re-used reference
		final EPackage v1_pkg2 = loader.loadEPackage(URI.createURI(resourceURL_V1.toString()));
		Assert.assertNotNull(v1_pkg2);

		final EPackage v2_pkg2 = loader.loadEPackage(URI.createURI(resourceURL_V2.toString()));
		Assert.assertNotNull(v2_pkg2);

		final EPackage v3_pkg2 = loader.loadEPackage(URI.createURI(resourceURL_V3.toString()));
		Assert.assertNotNull(v3_pkg2);

		Assert.assertSame(v1_pkg1, v1_pkg2);
		Assert.assertSame(v2_pkg1, v2_pkg2);
		Assert.assertSame(v3_pkg1, v3_pkg2);
	}

	/**
	 * Load both model references and ensure references in the the super-model can be resolved from sub-model instances.
	 */
	@Test
	public void testSingleSubModel() {

		final MetamodelLoader loader = new MetamodelLoader();

		final URI resourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/model-v1.ecore", true);
		PackageData pkgData = new PackageData("http://com.mmxlabs.models.migration.tests/model", "platform:/plugin/com.mmxlabs.models.migration.tests/models/model.ecore",
				"../../com.mmxlabs.models.migration.tests/models/model.ecore");
		final EPackage pkg = loader.loadEPackage(resourceURI, pkgData);
		Assert.assertNotNull(pkg);

		final URI subResourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/sub-model-v1.ecore", true);
		PackageData subPkgData = new PackageData("http://com.mmxlabs.models.migration.tests/submodel", "platform:/plugin/com.mmxlabs.models.migration.tests/models/sub-model.ecore",
				"../../com.mmxlabs.models.migration.tests/models/sub-model.ecore");
		final EPackage subPackage = loader.loadEPackage(subResourceURI, subPkgData);

		EcoreUtil.resolveAll(loader.getResourceSet());

		final EClass subClassA = (EClass) subPackage.getEClassifier("SubClassA");
		Assert.assertNotNull(subClassA);

		final EClass classA = (EClass) pkg.getEClassifier("ClassA");
		Assert.assertNotNull(classA);

		final EFactory subFactory = subPackage.getEFactoryInstance();

		final EObject object = subFactory.create(subClassA);
		Assert.assertNotNull(object);

		final EStructuralFeature attributeA = classA.getEStructuralFeature("attributeA");

		object.eSet(attributeA, 1);
		Assert.assertEquals(1, ((Integer) object.eGet(attributeA)).intValue());
	}

	/**
	 * Load both model references and ensure references in the the super-model can be resolved from sub-model instances.
	 */
	@Test
	public void testMultiInstanceLoad() {
		{
			final MetamodelLoader loader = new MetamodelLoader();

			final URI resourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/model-v1.ecore", true);
			PackageData pkgData = new PackageData("http://com.mmxlabs.models.migration.tests/model", "platform:/plugin/com.mmxlabs.models.migration.tests/models/model.ecore",
					"../../com.mmxlabs.models.migration.tests/models/model.ecore");
			final EPackage pkg = loader.loadEPackage(resourceURI, pkgData);
			Assert.assertNotNull(pkg);

			final URI subResourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/sub-model-v1.ecore", true);
			PackageData subPkgData = new PackageData("http://com.mmxlabs.models.migration.tests/submodel", "platform:/plugin/com.mmxlabs.models.migration.tests/models/sub-model.ecore",
					"../../com.mmxlabs.models.migration.tests/models/sub-model.ecore");
			final EPackage subPackage = loader.loadEPackage(subResourceURI, subPkgData);
			Assert.assertNotNull(subPackage);

			EcoreUtil.resolveAll(loader.getResourceSet());

			final EClass subClassA = (EClass) subPackage.getEClassifier("SubClassA");
			Assert.assertNotNull(subClassA);

			final EClass classA = (EClass) pkg.getEClassifier("ClassA");
			Assert.assertNotNull(classA);

			final EFactory subFactory = subPackage.getEFactoryInstance();

			final EObject object = subFactory.create(subClassA);
			Assert.assertNotNull(object);

			final EStructuralFeature attributeA = classA.getEStructuralFeature("attributeA");

			object.eSet(attributeA, 1);
			Assert.assertEquals(1, ((Integer) object.eGet(attributeA)).intValue());
		}

		{
			// Create second instance
			final MetamodelLoader loader = new MetamodelLoader();

			// Test load
			final URI subResourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/sub-model-v2.ecore", true);
			PackageData subPkgData = new PackageData("http://com.mmxlabs.models.migration.tests/submodel", "platform:/plugin/com.mmxlabs.models.migration.tests/models/sub-model.ecore",
					"../../com.mmxlabs.models.migration.tests/models/sub-model.ecore");
			final EPackage subPackage = loader.loadEPackage(subResourceURI, subPkgData);
			Assert.assertNotNull(subPackage);

			// Test re-used reference
			final URI resourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/model-v2.ecore", true);
			PackageData pkgData = new PackageData("http://com.mmxlabs.models.migration.tests/model", "platform:/plugin/com.mmxlabs.models.migration.tests/models/model.ecore",
					"../../com.mmxlabs.models.migration.tests/models/model.ecore");

			final EPackage pkg = loader.loadEPackage(resourceURI, pkgData);
			Assert.assertNotNull(pkg);

			final EClass subClassA = (EClass) subPackage.getEClassifier("SubClassA");
			Assert.assertNotNull(subClassA);

			final EClass classA = (EClass) pkg.getEClassifier("ClassA");
			Assert.assertNotNull(classA);

			final EFactory subFactory = subPackage.getEFactoryInstance();

			final EObject object = subFactory.create(subClassA);
			Assert.assertNotNull(object);

			final EStructuralFeature attributeA = classA.getEStructuralFeature("attributeC");

			object.eSet(attributeA, 1);
			Assert.assertEquals(1, ((Integer) object.eGet(attributeA)).intValue());
		}
	}

}
