/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.merge;

import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

		Assertions.assertSame(sourceContainer, descriptor.getSourceContainer());
		Assertions.assertSame(destinationContainer, descriptor.getDestinationContainer());
		Assertions.assertSame(ref, descriptor.getReference());
		Assertions.assertSame(destinationToSourceMap, descriptor.getDestinationToSourceMap());
		Assertions.assertSame(objectsAdded, descriptor.getAddedObjects());
		Assertions.assertSame(objectsRemoved, descriptor.getRemovedObjects());
	}

}
