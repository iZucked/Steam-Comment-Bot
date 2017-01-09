/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.merge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class EMFModelMergeToolsTest {

	/**
	 * Make sure the {@link IMappingDescriptor} is correct computed
	 */
	@Test
	public void testGenerateMappingDescriptorManyContainment() {

		// Create dynamic metamodel with a class that contains UUIDObjects
		final SimpleModelBuilder builder = new SimpleModelBuilder(MMXCorePackage.eINSTANCE.getUUIDObject());

		// Create the containers
		final EObject sourceContainer = builder.createContainer();
		final EObject destinationContainer = builder.createContainer();

		// Create somedata
		final UUIDObject source1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject source2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject source3 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject source4 = MMXCoreFactory.eINSTANCE.createUUIDObject();

		final UUIDObject dest1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject dest2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject dest3 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		final UUIDObject dest4 = MMXCoreFactory.eINSTANCE.createUUIDObject();

		// Matching uuids
		source1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");
		dest1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		source2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid2");
		dest2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid2");

		// Mis matched ids. Expect sources added and dests removed
		source3.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid3");
		source4.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid4");
		dest3.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid5");
		dest4.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid6");

		// Add objects to container
		final List<UUIDObject> sourceObjects = Lists.newArrayList(source1, source2, source3, source4);
		final List<UUIDObject> destObjects = Lists.newArrayList(dest1, dest2, dest3, dest4);

		final EReference containerReference = builder.getContainerReference();
		sourceContainer.eSet(containerReference, sourceObjects);
		destinationContainer.eSet(containerReference, destObjects);

		final IMappingDescriptor descriptor = EMFModelMergeTools.generateMappingDescriptorManyContainment(sourceContainer, destinationContainer, containerReference);

		Assert.assertNotNull(descriptor);

		// Check basic inputs
		Assert.assertSame(containerReference, descriptor.getReference());
		Assert.assertSame(sourceContainer, descriptor.getSourceContainer());
		Assert.assertSame(destinationContainer, descriptor.getDestinationContainer());

		// // Check derived data.

		// Check objects added
		final List<EObject> objectsAdded = descriptor.getAddedObjects();
		Assert.assertEquals(2, objectsAdded.size());
		Assert.assertTrue(objectsAdded.contains(source3));
		Assert.assertTrue(objectsAdded.contains(source4));

		// Check objects removed
		final Collection<EObject> objectsRemoved = descriptor.getRemovedObjects();
		Assert.assertEquals(2, objectsRemoved.size());
		Assert.assertTrue(objectsRemoved.contains(dest3));
		Assert.assertTrue(objectsRemoved.contains(dest4));

		// Check Mapping
		final Map<EObject, EObject> destinationToSourceMap = descriptor.getDestinationToSourceMap();
		Assert.assertEquals(2, destinationToSourceMap.size());
		Assert.assertSame(source1, destinationToSourceMap.get(dest1));
		Assert.assertSame(source2, destinationToSourceMap.get(dest2));
	}

	/**
	 * Make sure the {@link IMappingDescriptor} is correct computed
	 */
	@Test
	public void testGenerateMappingDescriptorSingleNonContainment() {

		// Create dynamic metamodel with a class that contains UUIDObjects
		final SimpleModelBuilder builder = new SimpleModelBuilder(MMXCorePackage.eINSTANCE.getUUIDObject());

		// Create the containers
		final EObject sourceContainer = builder.createContainer();
		final EObject destinationContainer = builder.createContainer();

		// Create somedata
		final UUIDObject source1 = MMXCoreFactory.eINSTANCE.createUUIDObject();

		final UUIDObject dest1 = MMXCoreFactory.eINSTANCE.createUUIDObject();

		// Matching uuids
		source1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");
		dest1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		// Add objects to container
		final List<UUIDObject> sourceObjects = Lists.newArrayList(source1);
		final List<UUIDObject> destObjects = Lists.newArrayList(dest1);

		final EReference containerReference = builder.getContainerReference();
		sourceContainer.eSet(containerReference, sourceObjects);
		destinationContainer.eSet(containerReference, destObjects);

		final EReference nonContainerReference = builder.getNonContainerReference();
		sourceContainer.eSet(nonContainerReference, source1);
		destinationContainer.eSet(nonContainerReference, dest1);

		final IMappingDescriptor descriptor = EMFModelMergeTools.generateMappingDescriptorSingleNonContainment(sourceContainer, destinationContainer, nonContainerReference);

		Assert.assertNotNull(descriptor);

		// Check basic inputs
		Assert.assertSame(nonContainerReference, descriptor.getReference());
		Assert.assertSame(sourceContainer, descriptor.getSourceContainer());
		Assert.assertSame(destinationContainer, descriptor.getDestinationContainer());

		// // Check derived data.

		// Check objects added
		final List<EObject> objectsAdded = descriptor.getAddedObjects();
		Assert.assertEquals(1, objectsAdded.size());
		Assert.assertTrue(objectsAdded.contains(source1));

		// Check objects removed
		final Collection<EObject> objectsRemoved = descriptor.getRemovedObjects();
		Assert.assertTrue(objectsRemoved.isEmpty());

		// Check Mapping
		final Map<EObject, EObject> destinationToSourceMap = descriptor.getDestinationToSourceMap();
		Assert.assertTrue(destinationToSourceMap.isEmpty());
	}

	private class SimpleModelBuilder {

		private EPackage ePkg;
		private EClass containerClass;
		private EReference containerReference;
		private EReference nonContainerReference;
		private final EClass referenceType;

		public SimpleModelBuilder(final EClass referenceType) {
			this.referenceType = referenceType;
			init();
		}

		private void init() {
			ePkg = EcoreFactory.eINSTANCE.createEPackage();
			containerClass = EcoreFactory.eINSTANCE.createEClass();
			containerClass.setName("container");

			containerReference = EcoreFactory.eINSTANCE.createEReference();
			containerReference.setName("list");

			// Changeable
			containerReference.setChangeable(true);
			// Turn into many
			containerReference.setUpperBound(-1);
			// Contain contents
			containerReference.setContainment(true);

			// Set the type of objects in this reference
			containerReference.setEType(referenceType);

			// Add the reference to the container class
			containerClass.getEStructuralFeatures().add(containerReference);

			nonContainerReference = EcoreFactory.eINSTANCE.createEReference();
			nonContainerReference.setName("ref");

			// Changeable
			nonContainerReference.setChangeable(true);
			// Turn into many
			nonContainerReference.setUpperBound(1);
			// Contain contents
			nonContainerReference.setContainment(false);

			// Set the type of objects in this reference
			nonContainerReference.setEType(referenceType);

			// Add the reference to the container class
			containerClass.getEStructuralFeatures().add(nonContainerReference);

			// Register class with the package.
			ePkg.getEClassifiers().add(containerClass);
		}

		public EObject createContainer() {
			return ePkg.getEFactoryInstance().create(containerClass);
		}

		public EReference getContainerReference() {
			return containerReference;
		}

		public EReference getNonContainerReference() {
			return nonContainerReference;
		}

	}

	/**
	 * This test check to see that we can handle multiple mapping descriptors and update references to object not in a mapping update. We create three object sets. The first is a simple
	 * {@link UUIDObject} which will be replaced. The second are {@link NamedObject}s with a reference to the {@link UUIDObject}s. These objects will also be replaced. A third set of objects
	 * referencing the UUIDObjects are also created. These are not replaced, but expect to have references updated (including set to null as appropriate).
	 * 
	 * In this test case, there is no existing data
	 */
	@Test
	public void testModelUpdate_NoDestData() {

		final ComplexModelBuilder builder = new ComplexModelBuilder();

		// Create three containment structures;
		// one a list of "UUIDObject"
		// second a list of NamedObject sub classes (called data1) with a reference to the UUIDObjects
		// third a list of EObject sub classes (called data2) with a reference to the UUIDObjects
		// the UUIDObjects and dataA list will be subject to the "merge" will dataB should only have references updated
		final Pair<EClass, EReference> dataPairA = builder.createReferencingType(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.eINSTANCE.getUUIDObject(), "dataA");
		final Pair<EClass, EReference> dataPairB = builder.createReferencingType(EcorePackage.eINSTANCE.getEObject(), MMXCorePackage.eINSTANCE.getUUIDObject(), "dataB");

		final EClass cls_container1 = builder.createContainerClass("container1", MMXCorePackage.eINSTANCE.getUUIDObject());
		final EClass cls_container2 = builder.createContainerClass("container2", dataPairA.getFirst());
		final EClass cls_container3 = builder.createContainerClass("container3", dataPairB.getFirst());

		final EReference nonContainmentRef = builder.createSingleNonContainmentReferencingType(MMXCorePackage.eINSTANCE.getUUIDObject(), "uuidRef");

		// Create data model instances - these will be pre-created with the above container class instances.
		final EObject sourceRoot = builder.createRootModel();
		final EObject destinationRoot = builder.createRootModel();

		// //// Create source data model
		final UUIDObject sourceUUID1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		sourceUUID1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		final UUIDObject sourceUUID2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		sourceUUID2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid2");

		final EObject sourceDataA1 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		sourceDataA1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		sourceDataA1.eSet(dataPairA.getSecond(), sourceUUID1);

		final EObject sourceDataA2 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		sourceDataA2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");
		sourceDataA2.eSet(dataPairA.getSecond(), sourceUUID2);

		// Add objects to relevant containers
		final List<Object> sourceContainer1Objects = Lists.<Object> newArrayList(sourceUUID1, sourceUUID2);
		final List<Object> sourceContainer2Objects = Lists.<Object> newArrayList(sourceDataA1, sourceDataA2);
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), sourceContainer1Objects);
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), sourceContainer2Objects);

		// Add objects to relevant containers
		final List<Object> destContainer1Objects = Collections.emptyList();
		final List<Object> destContainer2Objects = Collections.emptyList();
		final List<Object> destContainer3Objects = Collections.emptyList();
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), destContainer1Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), destContainer2Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container3))).eSet(builder.getContainerReference(cls_container3), destContainer3Objects);

		sourceRoot.eSet(nonContainmentRef, sourceUUID1);
		destinationRoot.eSet(nonContainmentRef, null);

		// Generate the mapping descriptors
		final IMappingDescriptor descriptor1 = EMFModelMergeTools.generateMappingDescriptorManyContainment(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))), builder.getContainerReference(cls_container1));

		final IMappingDescriptor descriptor2 = EMFModelMergeTools.generateMappingDescriptorManyContainment(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container2))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container2))), builder.getContainerReference(cls_container2));

		final IMappingDescriptor descriptor3 = EMFModelMergeTools.generateMappingDescriptorSingleNonContainment(sourceRoot, destinationRoot, nonContainmentRef);

		final List<IMappingDescriptor> descriptors = Lists.newArrayList(descriptor1, descriptor2, descriptor3);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		// Add a reflective item provider for our dynamic metamodel - delete command will not work otherwise
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		final EditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		// Create a resource in the editing domain and attach model so the DeleteCommand can find cross-referencing objects
		final Resource r = new ResourceImpl();
		r.getContents().add(destinationRoot);
		domain.getResourceSet().getResources().add(r);

		// Generate the command to patch in the model
		final Command cmd = EMFModelMergeTools.applyMappingDescriptors(domain, destinationRoot, descriptors);
		Assert.assertNotNull(cmd);

		Assert.assertTrue(cmd.canExecute());

		// Execute command to modify the model state
		domain.getCommandStack().execute(cmd);

		// Check our destination has been correctly updated.

		final EObject destContainer1 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container1));
		final EObject destContainer2 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container2));
		final EObject destContainer3 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container3));

		Assert.assertNotNull(destContainer1);
		Assert.assertNotNull(destContainer2);
		Assert.assertNotNull(destContainer3);

		final List<Object> newDestContainer1Objects = (List<Object>) destContainer1.eGet(builder.getContainerReference(cls_container1));
		final List<Object> newDestContainer2Objects = (List<Object>) destContainer2.eGet(builder.getContainerReference(cls_container2));
		final List<Object> newDestContainer3Objects = (List<Object>) destContainer3.eGet(builder.getContainerReference(cls_container3));

		// New data switched owner
		Assert.assertSame(destContainer1, sourceUUID1.eContainer());
		Assert.assertSame(destContainer1, sourceUUID2.eContainer());
		Assert.assertSame(destContainer2, sourceDataA1.eContainer());
		Assert.assertSame(destContainer2, sourceDataA2.eContainer());
		// This data no change

		// Have UUID objects been replaced?
		Assert.assertEquals(2, newDestContainer1Objects.size());
		Assert.assertTrue(newDestContainer1Objects.contains(sourceUUID1));
		Assert.assertTrue(newDestContainer1Objects.contains(sourceUUID2));

		// Have dataA objects been replaced?
		Assert.assertEquals(2, newDestContainer2Objects.size());
		Assert.assertTrue(newDestContainer2Objects.contains(sourceDataA1));
		Assert.assertTrue(newDestContainer2Objects.contains(sourceDataA2));

		// Are dataA objects still pointing to correct refs?
		Assert.assertSame(sourceUUID1, sourceDataA1.eGet(dataPairA.getSecond()));
		Assert.assertSame(sourceUUID2, sourceDataA2.eGet(dataPairA.getSecond()));

		// Make sure container 3 objects are still the same
		Assert.assertEquals(0, newDestContainer3Objects.size());
		
		// Check single non-containment ref update
		Assert.assertSame(sourceUUID1, destinationRoot.eGet(nonContainmentRef));
	}

	/**
	 * This test check to see that we can handle multiple mapping descriptors and update references to object not in a mapping update. We create three object sets. The first is a simple
	 * {@link UUIDObject} which will be replaced. The second are {@link NamedObject}s with a reference to the {@link UUIDObject}s. These objects will also be replaced. A third set of objects
	 * referencing the UUIDObjects are also created. These are not replaced, but expect to have references updated (including set to null as appropriate).
	 */
	@Test
	public void testModelUpdate_Replace() {

		final ComplexModelBuilder builder = new ComplexModelBuilder();

		// Create three containment structures;
		// one a list of "UUIDObject"
		// second a list of NamedObject sub classes (called data1) with a reference to the UUIDObjects
		// third a list of EObject sub classes (called data2) with a reference to the UUIDObjects
		// the UUIDObjects and dataA list will be subject to the "merge" will dataB should only have references updated
		final Pair<EClass, EReference> dataPairA = builder.createReferencingType(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.eINSTANCE.getUUIDObject(), "dataA");
		final Pair<EClass, EReference> dataPairB = builder.createReferencingType(EcorePackage.eINSTANCE.getEObject(), MMXCorePackage.eINSTANCE.getUUIDObject(), "dataB");

		final EClass cls_container1 = builder.createContainerClass("container1", MMXCorePackage.eINSTANCE.getUUIDObject());
		final EClass cls_container2 = builder.createContainerClass("container2", dataPairA.getFirst());
		final EClass cls_container3 = builder.createContainerClass("container3", dataPairB.getFirst());

		final EReference nonContainmentRef = builder.createSingleNonContainmentReferencingType(MMXCorePackage.eINSTANCE.getUUIDObject(), "uuidRef");

		// Create data model instances - these will be pre-created with the above container class instances.
		final EObject sourceRoot = builder.createRootModel();
		final EObject destinationRoot = builder.createRootModel();

		// //// Create source data model
		final UUIDObject sourceUUID1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		sourceUUID1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		final UUIDObject sourceUUID2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		sourceUUID2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid2");

		final EObject sourceDataA1 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		sourceDataA1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		sourceDataA1.eSet(dataPairA.getSecond(), sourceUUID1);

		final EObject sourceDataA2 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		sourceDataA2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");
		sourceDataA2.eSet(dataPairA.getSecond(), sourceUUID2);

		// Add objects to relevant containers
		final List<Object> sourceContainer1Objects = Lists.<Object> newArrayList(sourceUUID1, sourceUUID2);
		final List<Object> sourceContainer2Objects = Lists.<Object> newArrayList(sourceDataA1, sourceDataA2);
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), sourceContainer1Objects);
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), sourceContainer2Objects);

		// //// Create dest data model
		final UUIDObject destUUID1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		destUUID1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		final UUIDObject destUUID2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		destUUID2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid3");

		final EObject destDataA1 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		destDataA1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		destDataA1.eSet(dataPairA.getSecond(), destUUID1);

		final EObject destDataA2 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		destDataA2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name3");
		destDataA2.eSet(dataPairA.getSecond(), destUUID2);

		// This data should stay the same, but references updated
		final EObject destDataB1 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		destDataB1.eSet(dataPairB.getSecond(), destUUID1);

		final EObject destDataB2 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		destDataB2.eSet(dataPairB.getSecond(), destUUID2);

		sourceRoot.eSet(nonContainmentRef, sourceUUID1);
		destinationRoot.eSet(nonContainmentRef, destUUID1);

		// Add objects to relevant containers
		final List<Object> destContainer1Objects = Lists.<Object> newArrayList(destUUID1, destUUID2);
		final List<Object> destContainer2Objects = Lists.<Object> newArrayList(destDataA1, destDataA2);
		final List<Object> destContainer3Objects = Lists.<Object> newArrayList(destDataB1, destDataB2);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), destContainer1Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), destContainer2Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container3))).eSet(builder.getContainerReference(cls_container3), destContainer3Objects);

		// Generate the mapping descriptors
		final IMappingDescriptor descriptor1 = EMFModelMergeTools.generateMappingDescriptorManyContainment(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))), builder.getContainerReference(cls_container1));

		final IMappingDescriptor descriptor2 = EMFModelMergeTools.generateMappingDescriptorManyContainment(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container2))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container2))), builder.getContainerReference(cls_container2));

		final IMappingDescriptor descriptor3 = EMFModelMergeTools.generateMappingDescriptorSingleNonContainment(sourceRoot, destinationRoot, nonContainmentRef);

		final List<IMappingDescriptor> descriptors = Lists.newArrayList(descriptor1, descriptor2, descriptor3);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		// Add a reflective item provider for our dynamic metamodel - delete command will not work otherwise
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		final EditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		// Create a resource in the editing domain and attach model so the DeleteCommand can find cross-referencing objects
		final Resource r = new ResourceImpl();
		r.getContents().add(destinationRoot);
		domain.getResourceSet().getResources().add(r);

		// Generate the command to patch in the model
		final Command cmd = EMFModelMergeTools.applyMappingDescriptors(domain, destinationRoot, descriptors);
		Assert.assertNotNull(cmd);

		Assert.assertTrue(cmd.canExecute());

		// Execute command to modify the model state
		domain.getCommandStack().execute(cmd);

		// Check our destination has been correctly updated.

		final EObject destContainer1 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container1));
		final EObject destContainer2 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container2));
		final EObject destContainer3 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container3));

		Assert.assertNotNull(destContainer1);
		Assert.assertNotNull(destContainer2);
		Assert.assertNotNull(destContainer3);

		final List<Object> newDestContainer1Objects = (List<Object>) destContainer1.eGet(builder.getContainerReference(cls_container1));
		final List<Object> newDestContainer2Objects = (List<Object>) destContainer2.eGet(builder.getContainerReference(cls_container2));
		final List<Object> newDestContainer3Objects = (List<Object>) destContainer3.eGet(builder.getContainerReference(cls_container3));

		// Check containership -- old data no longer contained
		Assert.assertNull(destUUID1.eContainer());
		Assert.assertNull(destUUID2.eContainer());
		Assert.assertNull(destDataA1.eContainer());
		Assert.assertNull(destDataA2.eContainer());
		// New data switched owner
		Assert.assertSame(destContainer1, sourceUUID1.eContainer());
		Assert.assertSame(destContainer1, sourceUUID2.eContainer());
		Assert.assertSame(destContainer2, sourceDataA1.eContainer());
		Assert.assertSame(destContainer2, sourceDataA2.eContainer());
		// This data no change
		Assert.assertSame(destContainer3, destDataB1.eContainer());
		Assert.assertSame(destContainer3, destDataB2.eContainer());

		// Have UUID objects been replaced?
		Assert.assertEquals(2, newDestContainer1Objects.size());
		Assert.assertTrue(newDestContainer1Objects.contains(sourceUUID1));
		Assert.assertTrue(newDestContainer1Objects.contains(sourceUUID2));
		Assert.assertFalse(newDestContainer1Objects.contains(destUUID1));
		Assert.assertFalse(newDestContainer1Objects.contains(destUUID2));

		// Have dataA objects been replaced?
		Assert.assertEquals(2, newDestContainer2Objects.size());
		Assert.assertTrue(newDestContainer2Objects.contains(sourceDataA1));
		Assert.assertTrue(newDestContainer2Objects.contains(sourceDataA2));
		Assert.assertFalse(newDestContainer2Objects.contains(destDataA1));
		Assert.assertFalse(newDestContainer2Objects.contains(destDataA2));

		// Are dataA objects still pointing to correct refs?
		Assert.assertSame(sourceUUID1, sourceDataA1.eGet(dataPairA.getSecond()));
		Assert.assertSame(sourceUUID2, sourceDataA2.eGet(dataPairA.getSecond()));

		// Make sure container 3 objects are still the same
		Assert.assertEquals(2, newDestContainer3Objects.size());
		Assert.assertTrue(newDestContainer3Objects.contains(destDataB1));
		Assert.assertTrue(newDestContainer3Objects.contains(destDataB2));

		// Make sure container 3 references are correctly updated
		Assert.assertSame(sourceUUID1, destDataB1.eGet(dataPairB.getSecond()));
		Assert.assertNull(destDataB2.eGet(dataPairB.getSecond()));
		
		// Check single non-containment ref update
		Assert.assertSame(sourceUUID1, destinationRoot.eGet(nonContainmentRef));
	}

	/**
	 * This test check to see that we can handle multiple mapping descriptors and update references to object not in a mapping update. We create three object sets. The first is a simple
	 * {@link UUIDObject} which will be replaced. The second are {@link NamedObject}s with a reference to the {@link UUIDObject}s. These objects will also be replaced. A third set of objects
	 * referencing the UUIDObjects are also created. These are not replaced, but expect to have references updated (including set to null as appropriate).
	 * 
	 * In this case there is no source data, so destination data will be removed
	 */
	@Test
	public void testModelUpdate_NoSourceData() {

		final ComplexModelBuilder builder = new ComplexModelBuilder();

		// Create three containment structures;
		// one a list of "UUIDObject"
		// second a list of NamedObject sub classes (called data1) with a reference to the UUIDObjects
		// third a list of EObject sub classes (called data2) with a reference to the UUIDObjects
		// the UUIDObjects and dataA list will be subject to the "merge" will dataB should only have references updated
		final Pair<EClass, EReference> dataPairA = builder.createReferencingType(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.eINSTANCE.getUUIDObject(), "dataA");
		final Pair<EClass, EReference> dataPairB = builder.createReferencingType(EcorePackage.eINSTANCE.getEObject(), MMXCorePackage.eINSTANCE.getUUIDObject(), "dataB");

		final EClass cls_container1 = builder.createContainerClass("container1", MMXCorePackage.eINSTANCE.getUUIDObject());
		final EClass cls_container2 = builder.createContainerClass("container2", dataPairA.getFirst());
		final EClass cls_container3 = builder.createContainerClass("container3", dataPairB.getFirst());

		final EReference nonContainmentRef = builder.createSingleNonContainmentReferencingType(MMXCorePackage.eINSTANCE.getUUIDObject(), "uuidRef");

		// Create data model instances - these will be pre-created with the above container class instances.
		final EObject sourceRoot = builder.createRootModel();
		final EObject destinationRoot = builder.createRootModel();

		// Add objects to relevant containers
		final List<Object> sourceContainer1Objects = Collections.emptyList();
		final List<Object> sourceContainer2Objects = Collections.emptyList();
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), sourceContainer1Objects);
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), sourceContainer2Objects);

		// //// Create dest data model
		final UUIDObject destUUID1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		destUUID1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		final UUIDObject destUUID2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		destUUID2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid3");

		final EObject destDataA1 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		destDataA1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		destDataA1.eSet(dataPairA.getSecond(), destUUID1);

		final EObject destDataA2 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		destDataA2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name3");
		destDataA2.eSet(dataPairA.getSecond(), destUUID2);

		// This data should stay the same, but references updated
		final EObject destDataB1 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		destDataB1.eSet(dataPairB.getSecond(), destUUID1);

		final EObject destDataB2 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		destDataB2.eSet(dataPairB.getSecond(), destUUID2);

		// Add objects to relevant containers
		final List<Object> destContainer1Objects = Lists.<Object> newArrayList(destUUID1, destUUID2);
		final List<Object> destContainer2Objects = Lists.<Object> newArrayList(destDataA1, destDataA2);
		final List<Object> destContainer3Objects = Lists.<Object> newArrayList(destDataB1, destDataB2);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), destContainer1Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), destContainer2Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container3))).eSet(builder.getContainerReference(cls_container3), destContainer3Objects);

		sourceRoot.eSet(nonContainmentRef, null);
		destinationRoot.eSet(nonContainmentRef, destUUID1);

		// Generate the mapping descriptors
		final IMappingDescriptor descriptor1 = EMFModelMergeTools.generateMappingDescriptorManyContainment(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))), builder.getContainerReference(cls_container1));

		final IMappingDescriptor descriptor2 = EMFModelMergeTools.generateMappingDescriptorManyContainment(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container2))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container2))), builder.getContainerReference(cls_container2));

		final IMappingDescriptor descriptor3 = EMFModelMergeTools.generateMappingDescriptorSingleNonContainment(sourceRoot, destinationRoot, nonContainmentRef);

		final List<IMappingDescriptor> descriptors = Lists.newArrayList(descriptor1, descriptor2, descriptor3);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		// Add a reflective item provider for our dynamic metamodel - delete command will not work otherwise
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		final EditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		// Create a resource in the editing domain and attach model so the DeleteCommand can find cross-referencing objects
		final Resource r = new ResourceImpl();
		r.getContents().add(destinationRoot);
		domain.getResourceSet().getResources().add(r);

		// Generate the command to patch in the model
		final Command cmd = EMFModelMergeTools.applyMappingDescriptors(domain, destinationRoot, descriptors);
		Assert.assertNotNull(cmd);

		Assert.assertTrue(cmd.canExecute());

		// Execute command to modify the model state
		domain.getCommandStack().execute(cmd);

		// Check our destination has been correctly updated.

		final EObject destContainer1 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container1));
		final EObject destContainer2 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container2));
		final EObject destContainer3 = (EObject) destinationRoot.eGet(builder.getRootReference(cls_container3));

		Assert.assertNotNull(destContainer1);
		Assert.assertNotNull(destContainer2);
		Assert.assertNotNull(destContainer3);

		final List<Object> newDestContainer1Objects = (List<Object>) destContainer1.eGet(builder.getContainerReference(cls_container1));
		final List<Object> newDestContainer2Objects = (List<Object>) destContainer2.eGet(builder.getContainerReference(cls_container2));
		final List<Object> newDestContainer3Objects = (List<Object>) destContainer3.eGet(builder.getContainerReference(cls_container3));

		// Check containership -- old data no longer contained
		Assert.assertNull(destUUID1.eContainer());
		Assert.assertNull(destUUID2.eContainer());
		Assert.assertNull(destDataA1.eContainer());
		Assert.assertNull(destDataA2.eContainer());
		// This data no change
		Assert.assertSame(destContainer3, destDataB1.eContainer());
		Assert.assertSame(destContainer3, destDataB2.eContainer());

		// Have UUID objects been replaced?
		Assert.assertEquals(0, newDestContainer1Objects.size());
		Assert.assertFalse(newDestContainer1Objects.contains(destUUID1));
		Assert.assertFalse(newDestContainer1Objects.contains(destUUID2));

		// Have dataA objects been replaced?
		Assert.assertEquals(0, newDestContainer2Objects.size());
		Assert.assertFalse(newDestContainer2Objects.contains(destDataA1));
		Assert.assertFalse(newDestContainer2Objects.contains(destDataA2));

		// Make sure container 3 objects are still the same
		Assert.assertEquals(2, newDestContainer3Objects.size());
		Assert.assertTrue(newDestContainer3Objects.contains(destDataB1));
		Assert.assertTrue(newDestContainer3Objects.contains(destDataB2));

		// Make sure container 3 references are correctly updated
		Assert.assertNull(destDataB1.eGet(dataPairB.getSecond()));
		Assert.assertNull(destDataB2.eGet(dataPairB.getSecond()));
		
		// Check single non-containment ref update
		Assert.assertNull(destinationRoot.eGet(nonContainmentRef));
	}

	/**
	 * Dynamic EMF model builder used by {@link #testModelUpdate()}
	 * 
	 * 
	 */
	private class ComplexModelBuilder {

		private EPackage ePkg;
		private EClass rootClass;
		Map<String, EClass> clsMap = new HashMap<String, EClass>();
		// Root -> Container
		Map<EClass, EReference> rootContainerReferenceMap = new HashMap<EClass, EReference>();
		// Container -> Input
		Map<EClass, EReference> containerToListReferenceMap = new HashMap<EClass, EReference>();

		public ComplexModelBuilder() {
			init();
		}

		/**
		 * Create the basic package with a "root" element
		 */
		private void init() {
			ePkg = EcoreFactory.eINSTANCE.createEPackage();
			rootClass = EcoreFactory.eINSTANCE.createEClass();
			rootClass.setName("root");
			// Register class with the package.
			ePkg.getEClassifiers().add(rootClass);
		}

		/**
		 * Create a new single non-containment reference on the rootObject
		 * 
		 * @param superClass
		 * @param referencingClass
		 * @param name
		 * @return
		 */
		EReference createSingleNonContainmentReferencingType(final EClass referencingClass, final String name) {

			final EReference reference = EcoreFactory.eINSTANCE.createEReference();
			reference.setName(name);

			// Changeable
			reference.setChangeable(true);
			// Turn into many
			reference.setUpperBound(1);
			// Contain contents
			reference.setContainment(false);

			// Set the type of objects in this reference
			reference.setEType(referencingClass);

			// Add the reference to the container class
			rootClass.getEStructuralFeatures().add(reference);

			return reference;

		}

		/**
		 * Create a new type with a given super class and an class to hold a non-containment reference.
		 * 
		 * @param superClass
		 * @param referencingClass
		 * @param name
		 * @return
		 */
		Pair<EClass, EReference> createReferencingType(final EClass superClass, final EClass referencingClass, final String name) {
			final EClass cls = EcoreFactory.eINSTANCE.createEClass();
			cls.setName(name);

			// Register class with the package.
			ePkg.getEClassifiers().add(cls);

			cls.getESuperTypes().add(superClass);

			final EReference containerReference = EcoreFactory.eINSTANCE.createEReference();
			containerReference.setName("ref");

			// Changeable
			containerReference.setChangeable(true);
			// Turn into many
			containerReference.setUpperBound(1);
			// Contain contents
			containerReference.setContainment(false);

			// Set the type of objects in this reference
			containerReference.setEType(referencingClass);

			// Add the reference to the container class
			cls.getEStructuralFeatures().add(containerReference);

			return new Pair<EClass, EReference>(cls, containerReference);

		}

		/**
		 * Create a new EClass with the given name under the Root class. This will have a feature called "list" which contains objects of the 'containedType'
		 * 
		 * @param name
		 * @param containedType
		 * @return
		 */
		EClass createContainerClass(final String name, final EClass containedType) {
			final EClass cls = EcoreFactory.eINSTANCE.createEClass();
			cls.setName(name);

			// Register class with the package.
			ePkg.getEClassifiers().add(cls);

			clsMap.put(name, cls);
			{
				// Link container class to root
				final EReference rootReference = EcoreFactory.eINSTANCE.createEReference();
				rootReference.setName("name");
				// Changeable
				rootReference.setChangeable(true);
				// Turn into many
				rootReference.setUpperBound(1);
				// Contain contents
				rootReference.setContainment(true);

				// Set the type of objects in this reference
				rootReference.setEType(cls);

				rootContainerReferenceMap.put(cls, rootReference);

				// Add the reference to the container class
				rootClass.getEStructuralFeatures().add(rootReference);
			}

			{
				final EReference containerReference = EcoreFactory.eINSTANCE.createEReference();
				containerReference.setName("list");

				// Changeable
				containerReference.setChangeable(true);
				// Turn into many
				containerReference.setUpperBound(-1);
				// Contain contents
				containerReference.setContainment(true);

				// Set the type of objects in this reference
				containerReference.setEType(containedType);

				containerToListReferenceMap.put(cls, containerReference);

				// Add the reference to the container class
				cls.getEStructuralFeatures().add(containerReference);
			}

			return cls;
		}

		/**
		 * Create a new model instance with the container models pre-created
		 * 
		 * @return
		 */
		public EObject createRootModel() {
			final EObject root = ePkg.getEFactoryInstance().create(rootClass);

			for (final Map.Entry<EClass, EReference> e : rootContainerReferenceMap.entrySet()) {
				final EObject o = ePkg.getEFactoryInstance().create(e.getKey());
				root.eSet(e.getValue(), o);
			}

			return root;
		}

		// Return a reference to the container from the root
		public EReference getRootReference(final EClass cls) {
			return rootContainerReferenceMap.get(cls);
		}

		// Return a reference to the containment list for this eclass
		public EReference getContainerReference(final EClass cls) {
			return containerToListReferenceMap.get(cls);
		}

	}

	/**
	 * This test check to see that we can handle multiple mapping descriptors and update references to object not in a mapping update. We create three object sets. The first is a simple
	 * {@link UUIDObject} which will be replaced. The second are {@link NamedObject}s with a reference to the {@link UUIDObject}s. These objects will also be replaced. A third set of objects
	 * referencing the UUIDObjects are also created. These are not replaced, but expect to have references updated (including set to null as appropriate).
	 */
	@Test
	public void rewriteMappingDescriptorsVoid() {

		final ComplexModelBuilder builder = new ComplexModelBuilder();

		// Create three containment structures;
		// one a list of "UUIDObject"
		// second a list of NamedObject sub classes (called data1) with a reference to the UUIDObjects
		// third a list of EObject sub classes (called data2) with a reference to the UUIDObjects
		// the UUIDObjects and dataA list will be subject to the "merge" will dataB should only have references updated
		final Pair<EClass, EReference> dataPairA = builder.createReferencingType(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.eINSTANCE.getUUIDObject(), "dataA");
		final Pair<EClass, EReference> dataPairB = builder.createReferencingType(EcorePackage.eINSTANCE.getEObject(), dataPairA.getFirst(), "dataB");

		final EClass cls_container1 = builder.createContainerClass("container1", MMXCorePackage.eINSTANCE.getUUIDObject());
		final EClass cls_container2 = builder.createContainerClass("container2", dataPairA.getFirst());
		final EClass cls_container3 = builder.createContainerClass("container3", dataPairB.getFirst());

		// Create data model instances - these will be pre-created with the above container class instances.
		final EObject sourceRoot = builder.createRootModel();
		final EObject destinationRoot = builder.createRootModel();

		// //// Create source data model
		final UUIDObject sourceUUID1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		sourceUUID1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		final UUIDObject sourceUUID2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		sourceUUID2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid2");

		final EObject sourceDataA1 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		sourceDataA1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		sourceDataA1.eSet(dataPairA.getSecond(), sourceUUID1);

		final EObject sourceDataA2 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		sourceDataA2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name2");
		sourceDataA2.eSet(dataPairA.getSecond(), sourceUUID2);

		// This data should stay the same, but references updated
		final EObject sourceDataB1 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		sourceDataB1.eSet(dataPairB.getSecond(), sourceDataA1);

		final EObject sourceDataB2 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		sourceDataB2.eSet(dataPairB.getSecond(), sourceDataA2);

		// Add objects to relevant containers
		final List<Object> sourceContainer1Objects = Lists.<Object> newArrayList(sourceUUID1, sourceUUID2);
		final List<Object> sourceContainer2Objects = Lists.<Object> newArrayList(sourceDataA1, sourceDataA2);
		final List<Object> sourceContainer3Objects = Lists.<Object> newArrayList(sourceDataB1, sourceDataB2);

		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), sourceContainer1Objects);
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), sourceContainer2Objects);
		((EObject) sourceRoot.eGet(builder.getRootReference(cls_container3))).eSet(builder.getContainerReference(cls_container3), sourceContainer3Objects);

		// //// Create dest data model
		final UUIDObject destUUID1 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		destUUID1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid1");

		final UUIDObject destUUID2 = MMXCoreFactory.eINSTANCE.createUUIDObject();
		destUUID2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "uuid3");

		final EObject destDataA1 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		destDataA1.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name1");
		destDataA1.eSet(dataPairA.getSecond(), destUUID1);

		final EObject destDataA2 = dataPairA.getFirst().getEPackage().getEFactoryInstance().create(dataPairA.getFirst());
		destDataA2.eSet(MMXCorePackage.eINSTANCE.getNamedObject_Name(), "name3");
		destDataA2.eSet(dataPairA.getSecond(), destUUID2);

		// This data should stay the same, but references updated
		final EObject destDataB1 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		destDataB1.eSet(dataPairB.getSecond(), destDataA1);

		final EObject destDataB2 = dataPairB.getFirst().getEPackage().getEFactoryInstance().create(dataPairB.getFirst());
		destDataB2.eSet(dataPairB.getSecond(), destDataA2);

		// Add objects to relevant containers
		final List<Object> destContainer1Objects = Lists.<Object> newArrayList(destUUID1, destUUID2);
		final List<Object> destContainer2Objects = Lists.<Object> newArrayList(destDataA1, destDataA2);
		final List<Object> destContainer3Objects = Lists.<Object> newArrayList(destDataB1, destDataB2);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))).eSet(builder.getContainerReference(cls_container1), destContainer1Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container2))).eSet(builder.getContainerReference(cls_container2), destContainer2Objects);
		((EObject) destinationRoot.eGet(builder.getRootReference(cls_container3))).eSet(builder.getContainerReference(cls_container3), destContainer3Objects);

		// Generate the mapping descriptors
		final IMappingDescriptor descriptor1 = EMFModelMergeTools.generateMappingDescriptor(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container1))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container1))), builder.getContainerReference(cls_container1));

		final IMappingDescriptor descriptor2 = EMFModelMergeTools.generateMappingDescriptor(((EObject) sourceRoot.eGet(builder.getRootReference(cls_container3))),
				((EObject) destinationRoot.eGet(builder.getRootReference(cls_container3))), builder.getContainerReference(cls_container3));

		final List<IMappingDescriptor> descriptors = Lists.newArrayList(descriptor1, descriptor2);

		EMFModelMergeTools.rewriteMappingDescriptors(descriptors, sourceRoot, destinationRoot);

		// Replaced with dest model item
		Assert.assertSame(destDataA1, sourceDataB1.eGet(dataPairB.getSecond()));
		// Oops, not found so null!
		Assert.assertNull(sourceDataB2.eGet(dataPairB.getSecond()));

		// Check no other data changes
		Assert.assertSame(sourceUUID1, sourceDataA1.eGet(dataPairA.getSecond()));
		Assert.assertSame(sourceUUID2, sourceDataA2.eGet(dataPairA.getSecond()));

	}

}
