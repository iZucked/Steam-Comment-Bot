/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsize;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.HashMapPortShipSizeProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public class HashMapPortShipSizeProviderEditorTest {

	@Test
	public void test() {
		final HashMapPortShipSizeProviderEditor provider = new HashMapPortShipSizeProviderEditor();

		final ISequenceElement sequenceElement1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement sequenceElement2 = Mockito.mock(ISequenceElement.class);
		final Collection<ISequenceElement> elements = Set.of(sequenceElement1, sequenceElement2);

		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVessel vessel2 = Mockito.mock(IVessel.class);
		
		provider.addIncompatibleResourceElements(vessel1, elements);
		
		//vessel1 compatible with both sequence elements.
		Assertions.assertFalse(provider.isCompatible(vessel1, sequenceElement1));
		Assertions.assertFalse(provider.isCompatible(vessel1, sequenceElement2));

		//vessel2 not compatible with either sequence element.
		Assertions.assertTrue(provider.isCompatible(vessel2, sequenceElement1));
		Assertions.assertTrue(provider.isCompatible(vessel2, sequenceElement2));
	}
}
