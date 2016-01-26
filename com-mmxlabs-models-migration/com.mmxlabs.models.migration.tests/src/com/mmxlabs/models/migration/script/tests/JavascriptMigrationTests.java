/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.script.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class JavascriptMigrationTests {

	@Test
	public void migrationTest() throws IOException, ScriptException {

		final ScriptEngineManager manager = new ScriptEngineManager();
		final ScriptEngine engine = manager.getEngineByName("javascript");

		// Create a V1 loader
		final MetamodelLoader v1Loader = new MetamodelLoader();
		final URI v1ResourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/model-v1.ecore", true);
		PackageData v1PkgData = new PackageData("http://com.mmxlabs.models.migration.tests/model", "platform:/plugin/com.mmxlabs.models.migration.tests/models/model.ecore",
				"../../com.mmxlabs.models.migration.tests/models/model.ecore");
		final EPackage v1Pkg = v1Loader.loadEPackage(v1ResourceURI, v1PkgData);
		Assert.assertNotNull(v1Pkg);

		final URI v1SubResourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/sub-model-v1.ecore", true);
		PackageData v1SubPkgData = new PackageData("http://com.mmxlabs.models.migration.tests/submodel", "platform:/plugin/com.mmxlabs.models.migration.tests/models/sub-model.ecore",
				"../../com.mmxlabs.models.migration.tests/models/sub-model.ecore");
		final EPackage v1SubPkg = v1Loader.loadEPackage(v1SubResourceURI, v1SubPkgData);
		Assert.assertNotNull(v1SubPkg);
		Assert.assertNotNull(v1SubPkg);

		EcoreUtil.resolveAll(v1Loader.getResourceSet());

		// Register V1 objects
		engine.put("v1Loader", v1Loader);
		engine.put("v1Pkg", v1Pkg);
		engine.put("v1SubPkg", v1SubPkg);

		// Create a V2 loader
		final MetamodelLoader v2Loader = new MetamodelLoader();
		final URI v2ResourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/model-v2.ecore", true);
		PackageData v2PkgData = new PackageData("http://com.mmxlabs.models.migration.tests/model", "platform:/plugin/com.mmxlabs.models.migration.tests/models/model.ecore",
				"../../com.mmxlabs.models.migration.tests/models/model.ecore");
		final EPackage v2Pkg = v2Loader.loadEPackage(v2ResourceURI, v2PkgData);
		Assert.assertNotNull(v2Pkg);

		final URI v2SubResourceURI = URI.createPlatformPluginURI("/com.mmxlabs.models.migration.tests/models/sub-model-v2.ecore", true);
		PackageData v2SubPkgData = new PackageData("http://com.mmxlabs.models.migration.tests/submodel", "platform:/plugin/com.mmxlabs.models.migration.tests/models/sub-model.ecore",
				"../../com.mmxlabs.models.migration.tests/models/sub-model.ecore");
		final EPackage v2SubPkg = v2Loader.loadEPackage(v2SubResourceURI, v2SubPkgData);
		Assert.assertNotNull(v2SubPkg);

		EcoreUtil.resolveAll(v2Loader.getResourceSet());

		// Register V2 Objects
		engine.put("v2Loader", v2Loader);
		engine.put("v2Pkg", v2Pkg);
		engine.put("v2SubPkg", v2SubPkg);

		// Initialise a V1 model instance
		final EObject v1Object = createV1Model(v1SubPkg, v1Pkg, 1, 2, 3);

		// Create a tmp file to serialise data to
		final File f = File.createTempFile("migrationTest", ".xmi");
		f.deleteOnExit();

		// Save model to tmp file.
		final URI fileURI = URI.createFileURI(f.getCanonicalPath());
		final Resource r1 = v1Loader.getResourceSet().createResource(fileURI);
		r1.getContents().add(v1Object);
		r1.save(Collections.emptyMap());

		// Reload model under V2 metamodels
		final Resource r2 = v2Loader.getResourceSet().createResource(fileURI);
		final Map<Object, Object> loadOptions = new HashMap<Object, Object>();
		// Record features which have no meta-model equivalent so we can perform migration
		loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
		r2.load(loadOptions);

		// Delete tmp file
		f.delete();

		// Check model was loaded
		Assert.assertNotNull(r2.getContents());
		Assert.assertFalse(r2.getContents().isEmpty());

		EObject v2Object = r2.getContents().iterator().next();
		Assert.assertNotNull(v2Object);

		// This map records features with no model equiv - i.e. V1 features not in V2
		final Map<EObject, AnyType> oldFeatures = ((XMLResource) r2).getEObjectToExtensionMap();
		engine.put("v2Object", v2Object);
		engine.put("oldFeatures", oldFeatures);
		// Migration....
		{

			final InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/scripts/migration.js"));
			engine.eval(reader);
		}

		// v2Object = (EObject) engine.get("v2Object");
		// Check our new attribute has required value.
		// TODO: Should we persist and reload?
		Assert.assertEquals(1, ((Integer) v2Object.eGet(v2Object.eClass().getEStructuralFeature("attributeA"))).intValue());
		Assert.assertEquals(2, ((Integer) v2Object.eGet(v2Object.eClass().getEStructuralFeature("attributeC"))).intValue());
		Assert.assertEquals(3, ((Integer) v2Object.eGet(v2Object.eClass().getEStructuralFeature("subAttributeA"))).intValue());

	}

	/**
	 * Create a simple metamodel in V1 format
	 */
	private EObject createV1Model(final EPackage subPkg, final EPackage pkg, final int vAttribA, final int vAttribB, final int vSubAttribA) {

		final EClass classA = MetamodelUtils.getEClass(pkg, "ClassA");
		final EClass subClassA = MetamodelUtils.getEClass(subPkg, "SubClassA");

		final EStructuralFeature attributeA = classA.getEStructuralFeature("attributeA");
		final EStructuralFeature attributeB = classA.getEStructuralFeature("attributeB");
		final EStructuralFeature subAttributeA = subClassA.getEStructuralFeature("subAttributeA");

		final EFactory subFactory = subPkg.getEFactoryInstance();

		final EObject obj = subFactory.create(subClassA);

		obj.eSet(attributeA, vAttribA);
		obj.eSet(attributeB, vAttribB);
		obj.eSet(subAttributeA, vSubAttribA);

		return obj;

	}
}
