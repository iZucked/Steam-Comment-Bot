/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.migration.utils.EcoreHelper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class EcoreHelperTest {
	@Test
	public void getPackageNSTest() throws Exception {

		final URI uri = URI.createURI(getClass().getResource("/models/model-v1.ecore").toString());
		assert uri != null;

		Assert.assertEquals("http://com.mmxlabs.models.migration.tests/model", EcoreHelper.getPackageNS(uri));
	}

	@Test
	public void copyEObjectTest() {

		final URI uriModelA = URI.createURI(getClass().getResource("/models/EcoreHelperTest/modelA-v1.ecore").toString());
		final URI uriModelB = URI.createURI(getClass().getResource("/models/EcoreHelperTest/modelB-v1.ecore").toString());

		final MetamodelLoader loader = new MetamodelLoader();

		final EPackage pkgA = loader.loadEPackage(uriModelA);
		Assert.assertNotNull(pkgA);

		final EPackage pkgB = loader.loadEPackage(uriModelB);
		Assert.assertNotNull(pkgB);

		// Create the test model A instance.
		EObject modelA;
		final EFactory modelAFactory = pkgA.getEFactoryInstance();
		{
			final EClass class_ClassA = MetamodelUtils.getEClass(pkgA, "ClassA");
			final EStructuralFeature feature_referenceA = MetamodelUtils.getStructuralFeature(class_ClassA, "referenceA");

			final EClass class_ClassB = MetamodelUtils.getEClass(pkgA, "ClassB");
			final EStructuralFeature feature_attributeA = MetamodelUtils.getStructuralFeature(class_ClassB, "attributeA");
			final EStructuralFeature feature_attributeB = MetamodelUtils.getStructuralFeature(class_ClassB, "attributeB");

			modelA = modelAFactory.create(class_ClassA);
			final EObject refA = modelAFactory.create(class_ClassB);

			modelA.eSet(feature_referenceA, refA);
			refA.eSet(feature_attributeA, 1);
			refA.eSet(feature_attributeB, 2);
		}
		final EFactory modelBFactory = pkgB.getEFactoryInstance();
		final EObject modelB = modelBFactory.create(MetamodelUtils.getEClass(pkgB, "ClassA"));
		assert modelB != null;

		// Copy object data into modelB
		EcoreHelper.copyEObject(modelA, modelB);

		// Check outputs
		{
			final EClass class_ClassA = MetamodelUtils.getEClass(pkgB, "ClassA");
			final EStructuralFeature feature_referenceA = MetamodelUtils.getStructuralFeature(class_ClassA, "referenceA");

			final EClass class_ClassB = MetamodelUtils.getEClass(pkgB, "ClassB");
			final EStructuralFeature feature_attributeA = MetamodelUtils.getStructuralFeature(class_ClassB, "attributeA");
			final EStructuralFeature feature_attributeB = MetamodelUtils.getStructuralFeature(class_ClassB, "attributeB");

			final EObject refA = (EObject) modelB.eGet(feature_referenceA);
			Assert.assertNotNull(refA);
			Assert.assertTrue(class_ClassB.isInstance(refA));

			Assert.assertEquals(1, (int) (Integer) refA.eGet(feature_attributeA));
			Assert.assertEquals(2, (int) (Integer) refA.eGet(feature_attributeB));
		}
	}
}
