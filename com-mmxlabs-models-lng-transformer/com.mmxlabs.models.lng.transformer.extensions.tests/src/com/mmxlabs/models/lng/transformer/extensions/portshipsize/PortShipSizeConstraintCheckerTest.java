/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.IPortShipSizeProvider;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeConstraintChecker;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.SequencesAttributesProviderImpl;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class PortShipSizeConstraintCheckerTest {

	@Test
	public void testCompatibleVesselSequenceElementPassesConstraintCheck() {
		final String name = "name";
		final IPortShipSizeProvider portShipSizeProvider = Mockito.mock(IPortShipSizeProvider.class);
		final INominatedVesselProvider nominatedVesselProvider = Mockito.mock(INominatedVesselProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final PortShipSizeConstraintChecker checker = createChecker(name, portShipSizeProvider, nominatedVesselProvider, vesselProvider);

		//Check compatible cases.
		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);

		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
	
		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);
		
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element1)).thenReturn(true);
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element2)).thenReturn(true);
		
		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));

		Mockito.verify(portShipSizeProvider).isCompatible(vessel1, element1);
		Mockito.verify(portShipSizeProvider).isCompatible(vessel1, element2);

		Mockito.verifyNoMoreInteractions(portShipSizeProvider);
	}

	@Test
	public void testIncompatibleVesselSequenceElementFailsConstraintCheck() {
		final String name = "name";
		final IPortShipSizeProvider portShipSizeProvider = Mockito.mock(IPortShipSizeProvider.class);
		final INominatedVesselProvider nominatedVesselProvider = Mockito.mock(INominatedVesselProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final PortShipSizeConstraintChecker checker = createChecker(name, portShipSizeProvider, nominatedVesselProvider, vesselProvider);

		//Check compatible cases.
		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);

		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);
		
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element1)).thenReturn(false);
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element2)).thenReturn(false);
		
		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
	}
		
	@Test
	public void testCompatibleVesselSequenceElementListPassesConstraintCheck() {
		final String name = "name";
		final IPortShipSizeProvider portShipSizeProvider = Mockito.mock(IPortShipSizeProvider.class);
		final INominatedVesselProvider nominatedVesselProvider = Mockito.mock(INominatedVesselProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final PortShipSizeConstraintChecker checker = createChecker(name, portShipSizeProvider, nominatedVesselProvider, vesselProvider);

		//Check compatible cases.
		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);

		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element3 = Mockito.mock(ISequenceElement.class);
		
		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);
		
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element1)).thenReturn(true);
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element2)).thenReturn(true);
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element3)).thenReturn(true);
		ISequence sequence = new ListSequence(List.of(element1, element2, element3));
		final ISequences sequences = new Sequences(Collections.singletonList(resource1), CollectionsUtil.makeHashMap(resource1, sequence), Collections.emptyList(), new SequencesAttributesProviderImpl());
		Collection<@NonNull IResource> changedResources = Set.of(resource1);
		
		Assertions.assertTrue(checker.checkConstraints(sequences, changedResources, new ArrayList<>()));
	}
	
	@Test
	public void testIncompatibleVesselSequenceElementsListFailsConstraintCheck() {
		final String name = "name";
		final IPortShipSizeProvider portShipSizeProvider = Mockito.mock(IPortShipSizeProvider.class);
		final INominatedVesselProvider nominatedVesselProvider = Mockito.mock(INominatedVesselProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final PortShipSizeConstraintChecker checker = createChecker(name, portShipSizeProvider, nominatedVesselProvider, vesselProvider);

		//Check compatible cases.
		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselCharter vesselCharter1 = Mockito.mock(IVesselCharter.class);

		Mockito.when(vesselCharter1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element3 = Mockito.mock(ISequenceElement.class);
		
		Mockito.when(vesselProvider.getVesselCharter(resource1)).thenReturn(vesselCharter1);
		Mockito.when(vesselCharter1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);
		
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element1)).thenReturn(true);
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element2)).thenReturn(true);
		Mockito.when(portShipSizeProvider.isCompatible(vessel1, element3)).thenReturn(false);
		ISequence sequence = new ListSequence(List.of(element1, element2, element3));
		final ISequences sequences = new Sequences(Collections.singletonList(resource1), CollectionsUtil.makeHashMap(resource1, sequence), Collections.emptyList(), new SequencesAttributesProviderImpl());
		Collection<@NonNull IResource> changedResources = Set.of(resource1);
		
		Assertions.assertFalse(checker.checkConstraints(sequences, changedResources, new ArrayList<>()));
	}
	
	private PortShipSizeConstraintChecker createChecker(final String name, final IPortShipSizeProvider portShipSizeProvider, 
			final INominatedVesselProvider nominatedVesselProvider, final IVesselProvider vesselProvider) {

		final AbstractModule module = new AbstractModule() {

			@Override
			protected void configure() {
				bind(IPortShipSizeProvider.class).toInstance(portShipSizeProvider);
				bind(INominatedVesselProvider.class).toInstance(nominatedVesselProvider);
				bind(IVesselProvider.class).toInstance(vesselProvider);
			}

			@Provides
			PortShipSizeConstraintChecker create(Injector injector) {
				PortShipSizeConstraintChecker checker = new PortShipSizeConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}
		};
		final Injector injector = Guice.createInjector(module);
		return injector.getInstance(PortShipSizeConstraintChecker.class);
	}
}
