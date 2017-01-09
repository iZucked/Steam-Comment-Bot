/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.ui.test.model.ModelFactory;
import com.mmxlabs.models.ui.test.model.ModelRoot;
import com.mmxlabs.models.ui.test.model.MultipleReference;
import com.mmxlabs.models.ui.test.model.SimpleObject;
import com.mmxlabs.models.ui.test.model.SingleContainmentReference;
import com.mmxlabs.models.ui.test.model.SingleReference;

public class DialogEcoreCopierTest {

	/**
	 * Test attribute change is tracked and undo/redo works
	 */
	@Test
	public void testSimpleObject() {

		final String originalValue = "OriginalValue";
		final String newValue = "NewValue";

		final DialogEcoreCopier copier = new DialogEcoreCopier();

		final SimpleObject simpleObject = ModelFactory.eINSTANCE.createSimpleObject();
		simpleObject.setAttribute(originalValue);

		final SimpleObject copy = (SimpleObject) copier.copy(simpleObject);
		copier.record();

		Assert.assertEquals(simpleObject.getAttribute(), copy.getAttribute());
		copy.setAttribute(newValue);

		Assert.assertEquals(originalValue, simpleObject.getAttribute());
		Assert.assertEquals(newValue, copy.getAttribute());

		
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Command cmd = copier.createEditCommand(ed);

		cmd.execute();

		Assert.assertEquals(newValue, simpleObject.getAttribute());

		cmd.undo();

		Assert.assertEquals(originalValue, simpleObject.getAttribute());

		cmd.redo();

		Assert.assertEquals(newValue, simpleObject.getAttribute());

	}

	/**
	 * Test that EOpposite/containment is correctly maintained between copies and originals
	 */
	@Test
	public void testSimpleReference() {

		final DialogEcoreCopier copier = new DialogEcoreCopier();

		final ModelRoot modelRoot = ModelFactory.eINSTANCE.createModelRoot();

		final SingleReference singleReference1 = ModelFactory.eINSTANCE.createSingleReference();
		final SingleReference singleReference2 = ModelFactory.eINSTANCE.createSingleReference();

		final MultipleReference multipleReference = ModelFactory.eINSTANCE.createMultipleReference();
		singleReference1.setMultipleReference(multipleReference);
		singleReference2.setMultipleReference(multipleReference);

		Assert.assertEquals(2, multipleReference.getSingleReferences().size());
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));

		modelRoot.getMultipleReferences().add(multipleReference);
		modelRoot.getSingleReferences().add(singleReference1);
		modelRoot.getSingleReferences().add(singleReference2);

		Assert.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assert.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assert.assertEquals(modelRoot, multipleReference.getModelRoot());

		final MultipleReference copyMultipleReference = (MultipleReference) copier.copy(multipleReference);
		final SingleReference copySingleReference1 = (SingleReference) copier.copy(singleReference1);
		final SingleReference copySingleReference2 = (SingleReference) copier.copy(singleReference2);

		copier.record();

		// Check original has not changed
		Assert.assertEquals(2, multipleReference.getSingleReferences().size());
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assert.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assert.assertEquals(multipleReference, singleReference2.getMultipleReference());

		Assert.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assert.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assert.assertEquals(modelRoot, multipleReference.getModelRoot());

		// Check copy is correctly hooked up
		Assert.assertEquals(2, copyMultipleReference.getSingleReferences().size());
		Assert.assertTrue(copyMultipleReference.getSingleReferences().contains(copySingleReference1));
		Assert.assertTrue(copyMultipleReference.getSingleReferences().contains(copySingleReference2));
		Assert.assertEquals(copyMultipleReference, copySingleReference1.getMultipleReference());
		Assert.assertEquals(copyMultipleReference, copySingleReference2.getMultipleReference());

		// Do something?

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Command cmd = copier.createEditCommand(ed);

		cmd.execute();

		// Check original has not changed
		Assert.assertEquals(2, multipleReference.getSingleReferences().size());
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assert.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assert.assertEquals(multipleReference, singleReference2.getMultipleReference());
		Assert.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assert.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assert.assertEquals(modelRoot, multipleReference.getModelRoot());

		Assert.assertEquals(2, modelRoot.getSingleReferences().size());
		Assert.assertEquals(1, modelRoot.getMultipleReferences().size());

		Assert.assertNull(copySingleReference1.getModelRoot());
		Assert.assertNull(copySingleReference2.getModelRoot());
		Assert.assertNull(copyMultipleReference.getModelRoot());

		cmd.undo();

		// Check original has not changed
		Assert.assertEquals(2, multipleReference.getSingleReferences().size());
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assert.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assert.assertEquals(multipleReference, singleReference2.getMultipleReference());
		Assert.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assert.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assert.assertEquals(modelRoot, multipleReference.getModelRoot());

		Assert.assertEquals(2, modelRoot.getSingleReferences().size());
		Assert.assertEquals(1, modelRoot.getMultipleReferences().size());

		Assert.assertNull(copySingleReference1.getModelRoot());
		Assert.assertNull(copySingleReference2.getModelRoot());
		Assert.assertNull(copyMultipleReference.getModelRoot());

		cmd.redo();

		// Check original has not changed
		Assert.assertEquals(2, multipleReference.getSingleReferences().size());
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assert.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assert.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assert.assertEquals(multipleReference, singleReference2.getMultipleReference());
		Assert.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assert.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assert.assertEquals(modelRoot, multipleReference.getModelRoot());

		Assert.assertNull(copySingleReference1.getModelRoot());
		Assert.assertNull(copySingleReference2.getModelRoot());
		Assert.assertNull(copyMultipleReference.getModelRoot());

		Assert.assertEquals(2, modelRoot.getSingleReferences().size());
		Assert.assertEquals(1, modelRoot.getMultipleReferences().size());

	}

	/**
	 * Test that EOpposite/containment is correctly maintained between copies and originals
	 */
	@Test
	public void testSingleContainmentReference() {

		final String originalValue = "OriginalValue";
		final String newValue = "NewValue";

		final DialogEcoreCopier copier = new DialogEcoreCopier();

		final SimpleObject simpleObject = ModelFactory.eINSTANCE.createSimpleObject();
		simpleObject.setAttribute(originalValue);

		final SingleContainmentReference singleContainmentReference = ModelFactory.eINSTANCE.createSingleContainmentReference();
		singleContainmentReference.setSimpleObject(simpleObject);

		Assert.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assert.assertEquals(singleContainmentReference, simpleObject.eContainer());

		final SingleContainmentReference copySingleContainmentReference = (SingleContainmentReference) copier.copy(singleContainmentReference);
		// Contained objects automatically copied.
		final SimpleObject copySimpleObject = copySingleContainmentReference.getSimpleObject();//(SimpleObject) copier.copy(simpleObject);

		Assert.assertNotSame(simpleObject, copySimpleObject);
		
		copier.record();

		// Check original has not changed
		Assert.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assert.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assert.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());
		Assert.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());

		// Do something?
		copySimpleObject.setAttribute(newValue);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Command cmd = copier.createEditCommand(ed);

		cmd.execute();

		// Check original has not changed
		Assert.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assert.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assert.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());
		Assert.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());

		// Check attrrib change
		Assert.assertEquals(newValue, simpleObject.getAttribute());

		cmd.undo();

		// Check original has not changed
		Assert.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assert.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assert.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());
		Assert.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());

		// Check attrrib change
		Assert.assertEquals(originalValue, simpleObject.getAttribute());

		cmd.redo();

		// Check original has not changed
		Assert.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assert.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assert.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());
		Assert.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());

		// Check attrrib change
		Assert.assertEquals(newValue, simpleObject.getAttribute());

	}
}
