/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.merge;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.OtherNamesObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class MMXObjectEquivalanceTest {

	@Test
	public void testEquivalentUUIDObject() {

		final UUIDObject u1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject u2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject u3 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject u4 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject u5 = MMXCoreFactory.eINSTANCE.createUUIDObject();

		// null string
		u1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), null);

		// Empty string
		u2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "");

		// UUID Field
		u3.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");
		// Same UUID, different instance
		u4.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");
		// Different instance, different UUID
		u5.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid2");

		// Check identity
		Assert.assertTrue(MMXObjectEquivalance.equivalentUUIDObject(u1, u1));

		// Null to empty string is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentUUIDObject(u1, u2));

		// Null to non-empty string is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentUUIDObject(u1, u3));

		// Empty string to null is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentUUIDObject(u2, u1));

		// Non-empty string to null is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentUUIDObject(u3, u1));

		// Different instance, same UUID is equivalent
		Assert.assertTrue(MMXObjectEquivalance.equivalentUUIDObject(u3, u4));

		// Different instance, different UUID is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentUUIDObject(u3, u5));
	}

	@Test
	public void testEquivalentNamedObject() {

		final NamedObject u1 = MMXCoreFactory.eINSTANCE.createNamedObject();
		final NamedObject u2 = MMXCoreFactory.eINSTANCE.createNamedObject();
		final NamedObject u3 = MMXCoreFactory.eINSTANCE.createNamedObject();
		final NamedObject u4 = MMXCoreFactory.eINSTANCE.createNamedObject();
		final NamedObject u5 = MMXCoreFactory.eINSTANCE.createNamedObject();

		// null string
		u1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), null);

		// Empty string
		u2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "");

		// Name Field
		u3.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		// Same Name, different instance
		u4.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		// Different instance, different Name
		u5.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");

		// Check identity
		Assert.assertTrue(MMXObjectEquivalance.equivalentNamedObject(u1, u1));

		// Null to empty string is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentNamedObject(u1, u2));

		// Null to non-empty string is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentNamedObject(u1, u3));

		// Empty string to null is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentNamedObject(u2, u1));

		// Non-empty string to null is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentNamedObject(u3, u1));

		// Different instance, same Name is equivalent
		Assert.assertTrue(MMXObjectEquivalance.equivalentNamedObject(u3, u4));

		// Different instance, different Name is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentNamedObject(u3, u5));
	}

	@Test
	public void testEquivalentOtherNamesObject() {

		final OtherNamesObject u1 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		final OtherNamesObject u2 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		final OtherNamesObject u3 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		final OtherNamesObject u4 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		final OtherNamesObject u5 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		final OtherNamesObject u6 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		final OtherNamesObject u7 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();

		// null string
		u1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), null);

		// Empty string
		u2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "");

		// Called name1, but has an other name of name2
		u3.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		u3.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name2"));

		// Same Name, different instance
		u4.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		u4.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name2"));

		// Same Name, different instance
		u5.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "");
		u5.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name1"));

		// Same Name, different instance
		u6.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");

		// Different instance, different Name
		u7.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name3");

		// Check identity
		Assert.assertTrue(MMXObjectEquivalance.equivalentOtherNamesObject(u1, u1));

		// Null to empty string is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentOtherNamesObject(u1, u2));

		// Null to non-empty string is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentOtherNamesObject(u1, u3));

		// Empty string to null is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentOtherNamesObject(u2, u1));

		// Non-empty string to null is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentOtherNamesObject(u3, u1));

		// Different instance, same Name is equivalent
		Assert.assertTrue(MMXObjectEquivalance.equivalentOtherNamesObject(u3, u4));
		Assert.assertTrue(MMXObjectEquivalance.equivalentOtherNamesObject(u3, u5));
		Assert.assertTrue(MMXObjectEquivalance.equivalentOtherNamesObject(u3, u6));
		Assert.assertTrue(MMXObjectEquivalance.equivalentOtherNamesObject(u4, u3));
		Assert.assertTrue(MMXObjectEquivalance.equivalentOtherNamesObject(u5, u3));
		Assert.assertTrue(MMXObjectEquivalance.equivalentOtherNamesObject(u6, u3));

		// Different instance, different Name is not equivalent
		Assert.assertFalse(MMXObjectEquivalance.equivalentOtherNamesObject(u3, u7));
	}

	@Test
	public void testEquivalent() {

		EObject e1 = EcoreFactory.eINSTANCE.createEObject();
		EObject e2 = EcoreFactory.eINSTANCE.createEObject();

		NamedObject n1 = MMXCoreFactory.eINSTANCE.createNamedObject();
		NamedObject n2 = MMXCoreFactory.eINSTANCE.createNamedObject();
		NamedObject n3 = MMXCoreFactory.eINSTANCE.createNamedObject();
		n1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		n2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		n3.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");

		OtherNamesObject on1 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		OtherNamesObject on2 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		OtherNamesObject on3 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		OtherNamesObject on4 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();
		OtherNamesObject on5 = MMXCoreFactory.eINSTANCE.createOtherNamesObject();

		on1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		on2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		on3.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");
		on1.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name3"));
		on2.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name3"));
		on3.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name4"));

		on4.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name6");
		on4.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name5"));
		on5.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name7");
		on5.eSet(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), Lists.newArrayList("name5"));

		UUIDObject u1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		UUIDObject u2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		UUIDObject u3 = MMXCoreFactory.eINSTANCE.createUUIDObject();

		u1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "name1");
		u2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "name1");
		u3.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "name2");

		// Eobjects different
		Assert.assertFalse(MMXObjectEquivalance.equivalent(e1, e2));

		// Identity checks
		Assert.assertTrue(MMXObjectEquivalance.equivalent(n1, n1));
		Assert.assertTrue(MMXObjectEquivalance.equivalent(on1, on1));
		Assert.assertTrue(MMXObjectEquivalance.equivalent(u1, u1));

		// Same Checks
		Assert.assertTrue(MMXObjectEquivalance.equivalent(n1, n2));
		Assert.assertTrue(MMXObjectEquivalance.equivalent(on1, on2));
		Assert.assertTrue(MMXObjectEquivalance.equivalent(u1, u2));

		// Different checks
		Assert.assertFalse(MMXObjectEquivalance.equivalent(n1, n3));
		Assert.assertFalse(MMXObjectEquivalance.equivalent(on1, on3));
		Assert.assertFalse(MMXObjectEquivalance.equivalent(u1, u3));

		// Mixed instance checks
		Assert.assertFalse(MMXObjectEquivalance.equivalent(n1, u1));
		// FIXME: This case does not apply as OtherNames extends NamedObject. Not strictly correct as objects are of differnt types, but does not cause problems.
		// Assert.assertFalse(MMXObjectEquivalance.equivalent(n1, on1));
		Assert.assertFalse(MMXObjectEquivalance.equivalent(u1, on1));

		// OtherNames over Names - Named route will be false, but OtherNames will be true
		Assert.assertTrue(MMXObjectEquivalance.equivalent(on4, on5));

	}

	@Ignore("Test currently fail due to dynamic eobject usage. Waiting for response on EMF mailing list. - SG 2013-06-21")
	@Test
	public void testEquivalent_CombinedObject() {

		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		EClass cls = EcoreFactory.eINSTANCE.createEClass();
		cls.setName("TestClass");
		// Inherit both named and uuid objects
		cls.getESuperTypes().add(MMXCorePackage.eINSTANCE.getNamedObject());
		cls.getESuperTypes().add(MMXCorePackage.eINSTANCE.getUUIDObject());

		ePackage.getEClassifiers().add(cls);

		EObject e1 = ePackage.getEFactoryInstance().create(cls);
		EObject e2 = ePackage.getEFactoryInstance().create(cls);
		EObject e3 = ePackage.getEFactoryInstance().create(cls);
		EObject e4 = ePackage.getEFactoryInstance().create(cls);
		EObject e5 = ePackage.getEFactoryInstance().create(cls);
		EObject e6 = ePackage.getEFactoryInstance().create(cls);

		e1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		e1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		Assert.assertEquals("name1", e1.eGet(MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		Assert.assertEquals("uuid1", e1.eGet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid()));
		
		// Same as e1
		e2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		e2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		// Same uuid as e1, but different name
		e3.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");
		e3.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		// Same names, no uuid
		e4.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");
		e5.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");

		// Different name & uuid
		e6.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name3");
		e6.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid3");

		Assert.assertTrue(MMXObjectEquivalance.equivalent(e1, e2));
		Assert.assertTrue(MMXObjectEquivalance.equivalent(e1, e3));

		Assert.assertFalse(MMXObjectEquivalance.equivalent(e1, e4));
		Assert.assertFalse(MMXObjectEquivalance.equivalent(e1, e5));
		Assert.assertTrue(MMXObjectEquivalance.equivalent(e3, e4));
		Assert.assertTrue(MMXObjectEquivalance.equivalent(e4, e5));

		Assert.assertFalse(MMXObjectEquivalance.equivalent(e1, e6));

	}
}
