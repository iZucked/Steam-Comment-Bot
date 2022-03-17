/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

		Assertions.assertEquals(simpleObject.getAttribute(), copy.getAttribute());
		copy.setAttribute(newValue);

		Assertions.assertEquals(originalValue, simpleObject.getAttribute());
		Assertions.assertEquals(newValue, copy.getAttribute());

		
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Command cmd = copier.createEditCommand(ed);

		cmd.execute();

		Assertions.assertEquals(newValue, simpleObject.getAttribute());

		cmd.undo();

		Assertions.assertEquals(originalValue, simpleObject.getAttribute());

		cmd.redo();

		Assertions.assertEquals(newValue, simpleObject.getAttribute());

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

		Assertions.assertEquals(2, multipleReference.getSingleReferences().size());
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));

		modelRoot.getMultipleReferences().add(multipleReference);
		modelRoot.getSingleReferences().add(singleReference1);
		modelRoot.getSingleReferences().add(singleReference2);

		Assertions.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assertions.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assertions.assertEquals(modelRoot, multipleReference.getModelRoot());

		final MultipleReference copyMultipleReference = (MultipleReference) copier.copy(multipleReference);
		final SingleReference copySingleReference1 = (SingleReference) copier.copy(singleReference1);
		final SingleReference copySingleReference2 = (SingleReference) copier.copy(singleReference2);

		copier.record();

		// Check original has not changed
		Assertions.assertEquals(2, multipleReference.getSingleReferences().size());
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assertions.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assertions.assertEquals(multipleReference, singleReference2.getMultipleReference());

		Assertions.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assertions.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assertions.assertEquals(modelRoot, multipleReference.getModelRoot());

		// Check copy is correctly hooked up
		Assertions.assertEquals(2, copyMultipleReference.getSingleReferences().size());
		Assertions.assertTrue(copyMultipleReference.getSingleReferences().contains(copySingleReference1));
		Assertions.assertTrue(copyMultipleReference.getSingleReferences().contains(copySingleReference2));
		Assertions.assertEquals(copyMultipleReference, copySingleReference1.getMultipleReference());
		Assertions.assertEquals(copyMultipleReference, copySingleReference2.getMultipleReference());

		// Do something?

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Command cmd = copier.createEditCommand(ed);

		cmd.execute();

		// Check original has not changed
		Assertions.assertEquals(2, multipleReference.getSingleReferences().size());
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assertions.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assertions.assertEquals(multipleReference, singleReference2.getMultipleReference());
		Assertions.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assertions.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assertions.assertEquals(modelRoot, multipleReference.getModelRoot());

		Assertions.assertEquals(2, modelRoot.getSingleReferences().size());
		Assertions.assertEquals(1, modelRoot.getMultipleReferences().size());

		Assertions.assertNull(copySingleReference1.getModelRoot());
		Assertions.assertNull(copySingleReference2.getModelRoot());
		Assertions.assertNull(copyMultipleReference.getModelRoot());

		cmd.undo();

		// Check original has not changed
		Assertions.assertEquals(2, multipleReference.getSingleReferences().size());
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assertions.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assertions.assertEquals(multipleReference, singleReference2.getMultipleReference());
		Assertions.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assertions.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assertions.assertEquals(modelRoot, multipleReference.getModelRoot());

		Assertions.assertEquals(2, modelRoot.getSingleReferences().size());
		Assertions.assertEquals(1, modelRoot.getMultipleReferences().size());

		Assertions.assertNull(copySingleReference1.getModelRoot());
		Assertions.assertNull(copySingleReference2.getModelRoot());
		Assertions.assertNull(copyMultipleReference.getModelRoot());

		cmd.redo();

		// Check original has not changed
		Assertions.assertEquals(2, multipleReference.getSingleReferences().size());
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference1));
		Assertions.assertTrue(multipleReference.getSingleReferences().contains(singleReference2));
		Assertions.assertEquals(multipleReference, singleReference1.getMultipleReference());
		Assertions.assertEquals(multipleReference, singleReference2.getMultipleReference());
		Assertions.assertEquals(modelRoot, singleReference1.getModelRoot());
		Assertions.assertEquals(modelRoot, singleReference2.getModelRoot());
		Assertions.assertEquals(modelRoot, multipleReference.getModelRoot());

		Assertions.assertNull(copySingleReference1.getModelRoot());
		Assertions.assertNull(copySingleReference2.getModelRoot());
		Assertions.assertNull(copyMultipleReference.getModelRoot());

		Assertions.assertEquals(2, modelRoot.getSingleReferences().size());
		Assertions.assertEquals(1, modelRoot.getMultipleReferences().size());

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

		Assertions.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assertions.assertEquals(singleContainmentReference, simpleObject.eContainer());

		final SingleContainmentReference copySingleContainmentReference = (SingleContainmentReference) copier.copy(singleContainmentReference);
		// Contained objects automatically copied.
		final SimpleObject copySimpleObject = copySingleContainmentReference.getSimpleObject();//(SimpleObject) copier.copy(simpleObject);

		Assertions.assertNotSame(simpleObject, copySimpleObject);
		
		copier.record();

		// Check original has not changed
		Assertions.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assertions.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assertions.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());
		Assertions.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());

		// Do something?
		copySimpleObject.setAttribute(newValue);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		final Command cmd = copier.createEditCommand(ed);

		cmd.execute();

		// Check original has not changed
		Assertions.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assertions.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assertions.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());
		Assertions.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());

		// Check attrrib change
		Assertions.assertEquals(newValue, simpleObject.getAttribute());

		cmd.undo();

		// Check original has not changed
		Assertions.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assertions.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assertions.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());
		Assertions.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());

		// Check attrrib change
		Assertions.assertEquals(originalValue, simpleObject.getAttribute());

		cmd.redo();

		// Check original has not changed
		Assertions.assertEquals(simpleObject, singleContainmentReference.getSimpleObject());
		Assertions.assertEquals(singleContainmentReference, simpleObject.eContainer());

		// Check copy is correctly hooked up
		Assertions.assertEquals(copySimpleObject, copySingleContainmentReference.getSimpleObject());
		Assertions.assertEquals(copySingleContainmentReference, copySimpleObject.eContainer());

		// Check attrrib change
		Assertions.assertEquals(newValue, simpleObject.getAttribute());

	}
}
