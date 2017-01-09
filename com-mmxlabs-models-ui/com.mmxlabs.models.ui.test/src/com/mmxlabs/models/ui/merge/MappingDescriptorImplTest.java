/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.merge;

import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.junit.Assert;
import org.junit.Test;

public class MappingDescriptorImplTest {

	@Test
	public void testMappingDescriptorImpl() {
		final EObject sourceContainer = mock(EObject.class);
		final EObject destinationContainer = mock(EObject.class);
		final EReference ref = mock(EReference.class);
		@SuppressWarnings("unchecked")
		final Map<EObject, EObject> destinationToSourceMap = mock(Map.class);
		@SuppressWarnings("unchecked")
		final List<EObject> objectsAdded = mock(List.class);
		@SuppressWarnings("unchecked")
		final List<EObject> objectsRemoved = mock(List.class);

		final MappingDescriptorImpl descriptor = new MappingDescriptorImpl(sourceContainer, destinationContainer, ref, destinationToSourceMap, objectsAdded, objectsRemoved);

		Assert.assertSame(sourceContainer, descriptor.getSourceContainer());
		Assert.assertSame(destinationContainer, descriptor.getDestinationContainer());
		Assert.assertSame(ref, descriptor.getReference());
		Assert.assertSame(destinationToSourceMap, descriptor.getDestinationToSourceMap());
		Assert.assertSame(objectsAdded, descriptor.getAddedObjects());
		Assert.assertSame(objectsRemoved, descriptor.getRemovedObjects());
	}

}
