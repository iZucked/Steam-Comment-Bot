/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CommandUtilTest {

	@Test
	public void testMultiples() {

		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		BasicCommandStack commandStack = new BasicCommandStack();

		EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

		EClass sample = EcoreFactory.eINSTANCE.createEClass();

		EClass refClass1 = EcoreFactory.eINSTANCE.createEClass();
		EClass refClass2 = EcoreFactory.eINSTANCE.createEClass();
		EClass refClass3 = EcoreFactory.eINSTANCE.createEClass();

		sample.getESuperTypes().add(refClass1);

		Command cmd = CommandUtil.createMultipleAttributeSetter(editingDomain, sample, EcorePackage.Literals.ECLASS__ESUPER_TYPES, Lists.newArrayList(refClass1, refClass1, refClass2));
		Assert.assertNotNull(cmd);

		cmd.execute();
		assert(sample.getESuperTypes().size() == sample.getESuperTypes().stream().distinct().count());
		Assert.assertEquals(2, sample.getESuperTypes().size());

	}

}
