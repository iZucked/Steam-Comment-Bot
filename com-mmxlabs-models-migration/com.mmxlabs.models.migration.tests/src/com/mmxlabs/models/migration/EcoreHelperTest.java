/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
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

		final EPackage pkgA = loader.loadEPackage(uriModelA, "");
		Assert.assertNotNull(pkgA);

		final EPackage pkgB = loader.loadEPackage(uriModelB, "");
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

	/**
	 * Run the test with a call to update the proxy
	 * 
	 * @throws IOException
	 */
	@Test
	public void updateMMXProxyTest_UpdateProxy() throws IOException {
		updateMMXProxyTest(true);
	}

	/**
	 * Run the test without a call to update the proxy
	 * 
	 * @throws IOException
	 */
	@Test
	public void updateMMXProxyTest_NoUpdateProxy() throws IOException {
		updateMMXProxyTest(false);
	}

	private void updateMMXProxyTest(boolean updateProxy) throws IOException {
		final URI uriMMXCore = URI.createURI("platform:/plugin/com.mmxlabs.models.mmxcore/model/mmxcore.ecore");
		final URI uriModelAV1 = URI.createURI(getClass().getResource("/models/EcoreHelperTest/proxyModelA-v1.ecore").toString());
		final URI uriModelAV2 = URI.createURI(getClass().getResource("/models/EcoreHelperTest/proxyModelA-v2.ecore").toString());

		File tmpFile = File.createTempFile("EcoreHelperTest", ".xmi");
		tmpFile.deleteOnExit();
		URI tmpFileURI = URI.createFileURI(tmpFile.getAbsolutePath());

		{
			final MetamodelLoader loader1 = new MetamodelLoader();

			final EPackage mmxcorePackage = loader1.loadEPackage(uriMMXCore, "");
			final EPackage pkgA = loader1.loadEPackage(uriModelAV1, "http://com.mmxlabs.models.migration.tests/proxyModelA");
			Assert.assertNotNull(pkgA);

			// Create the test model A instance.
			EObject modelA;
			final EFactory modelAFactory = pkgA.getEFactoryInstance();
			final EFactory mmxcoreFactory = mmxcorePackage.getEFactoryInstance();
			{
				final EClass class_ClassA = MetamodelUtils.getEClass(pkgA, "ClassA");
				final EStructuralFeature feature_referenceA = MetamodelUtils.getStructuralFeature(class_ClassA, "referenceA");

				final EClass class_MMXObject = MetamodelUtils.getEClass(mmxcorePackage, "MMXObject");
				final EStructuralFeature feature_proxies = MetamodelUtils.getStructuralFeature(class_MMXObject, "proxies");
				final EClass class_MMXProxy = MetamodelUtils.getEClass(mmxcorePackage, "MMXProxy");
				final EStructuralFeature feature_reference = MetamodelUtils.getStructuralFeature(class_MMXProxy, "reference");

				// Create a proxy with a reference
				final EObject mmxProxy = mmxcoreFactory.create(class_MMXProxy);
				mmxProxy.eSet(feature_reference, feature_referenceA);

				modelA = modelAFactory.create(class_ClassA);
				modelA.eSet(feature_proxies, Collections.singletonList(mmxProxy));

			}
			if (updateProxy) {
				EcoreHelper.updateMMXProxy(modelA, mmxcorePackage);
			}
			// Persist
			XMIResource r = new XMIResourceImpl(tmpFileURI);
			r.getContents().add(modelA);
			r.save(null);
		}

		{
			final MetamodelLoader loader1 = new MetamodelLoader();

			final EPackage mmxcorePackage = loader1.loadEPackage(uriMMXCore, "");
			final EPackage pkgA = loader1.loadEPackage(uriModelAV2, "http://com.mmxlabs.models.migration.tests/proxyModelA");
			Assert.assertNotNull(pkgA);

			// Restore
			Resource r = loader1.getResourceSet().createResource(tmpFileURI);
			r.load(null);
			EObject modelA = r.getContents().get(0);

			final EClass class_ClassA = MetamodelUtils.getEClass(pkgA, "ClassA");
			final EStructuralFeature feature_referenceA = MetamodelUtils.getStructuralFeature(class_ClassA, "referenceA");

			final EClass class_MMXObject = MetamodelUtils.getEClass(mmxcorePackage, "MMXObject");
			final EStructuralFeature feature_proxies = MetamodelUtils.getStructuralFeature(class_MMXObject, "proxies");
			final EClass class_MMXProxy = MetamodelUtils.getEClass(mmxcorePackage, "MMXProxy");
			final EStructuralFeature feature_reference = MetamodelUtils.getStructuralFeature(class_MMXProxy, "reference");

			List<EObject> proxies = MetamodelUtils.getValueAsTypedList(modelA, feature_proxies);
			Assert.assertNotNull(proxies);

			Assert.assertEquals(1, proxies.size());

			EObject mmxProxy = proxies.get(0);

			EReference ref = (EReference) mmxProxy.eGet(feature_reference);
			Assert.assertNotNull(ref);

			EReference resolvedRef = (EReference) EcoreUtil.resolve(ref, loader1.getResourceSet());
			Assert.assertNotNull(resolvedRef);
			if (updateProxy) {
				Assert.assertEquals(feature_referenceA, resolvedRef);
				Assert.assertTrue(class_ClassA.getEAllReferences().contains(resolvedRef));
			} else {
				Assert.assertNotSame(feature_referenceA, resolvedRef);
				Assert.assertFalse(class_ClassA.getEAllReferences().contains(resolvedRef));
			}
		}
		tmpFile.delete();
	}
}
