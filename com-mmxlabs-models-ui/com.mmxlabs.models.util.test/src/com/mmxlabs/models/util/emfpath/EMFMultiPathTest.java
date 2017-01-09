/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.emfpath;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Assert;
import org.junit.Test;

public class EMFMultiPathTest {

	@Test
	public void testNoPaths_Silent() {

		EObject object = EcoreFactory.eINSTANCE.createEObject();

		EMFMultiPath path = new EMFMultiPath(true);

		Assert.assertNull(path.get(object));
		Assert.assertNull(path.get(object, 0));
	}

	@Test
	public void testNoPaths() {

		EObject object = EcoreFactory.eINSTANCE.createEObject();

		EMFMultiPath path = new EMFMultiPath(false);

		Assert.assertNull(path.get(object));
		Assert.assertNull(path.get(object, 0));
	}

	@Test
	public void testSinglePath_Silent_1() {

		EClass testClass = EcoreFactory.eINSTANCE.createEClass();
		testClass.setName("Name");

		EMFMultiPath path = new EMFMultiPath(true, new EMFPath(true, EcorePackage.Literals.ENAMED_ELEMENT__NAME));

		Assert.assertEquals("Name", path.get(testClass));
	}

	@Test
	public void testSinglePath_Silent_2() {

		EObject object = EcoreFactory.eINSTANCE.createEObject();

		EMFMultiPath path = new EMFMultiPath(true, new EMFPath(true, EcorePackage.Literals.ENAMED_ELEMENT__NAME));

		Assert.assertNull(path.get(object));
	}

	@Test
	public void testSinglePath_2A() {

		EObject object = EcoreFactory.eINSTANCE.createEObject();

		EMFMultiPath path = new EMFMultiPath(false, new EMFPath(true, EcorePackage.Literals.ENAMED_ELEMENT__NAME));

		Assert.assertNull(path.get(object));
	}

	@Test(expected = RuntimeException.class)
	public void testSinglePath_2B() {

		EObject object = EcoreFactory.eINSTANCE.createEObject();

		EMFMultiPath path = new EMFMultiPath(false, new EMFPath(false, EcorePackage.Literals.ENAMED_ELEMENT__NAME));

		Assert.assertNull(path.get(object));
	}

	@Test
	public void testMultiplePath_Silent_1() {

		EClass testClass = EcoreFactory.eINSTANCE.createEClass();
		testClass.setInstanceTypeName("instance_name");

		EPackage testPackage = EcoreFactory.eINSTANCE.createEPackage();
		testPackage.setNsPrefix("prefix");

		EMFMultiPath path = new EMFMultiPath(true, new EMFPath(true, EcorePackage.Literals.ECLASSIFIER__INSTANCE_TYPE_NAME), new EMFPath(true, EcorePackage.Literals.EPACKAGE__NS_PREFIX));

		Assert.assertEquals("instance_name", path.get(testClass));
		Assert.assertEquals("prefix", path.get(testPackage));
	}
}
