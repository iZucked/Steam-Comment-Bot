/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedslots;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class RestrictedSlotsConstraintCheckerTest {

	@Test
	public void test1() {

		final String name = "name";
		final IRestrictedSlotsProvider restrictedElementsProvider = Mockito.mock(IRestrictedSlotsProvider.class);
		final RestrictedSlotsConstraintChecker checker = createChecker(name, restrictedElementsProvider);

		final IResource resource1 = Mockito.mock(IResource.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> emptySet());

		Assertions.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
	}

	@Test
	public void test2() {

		final String name = "name";
		final IRestrictedSlotsProvider restrictedElementsProvider = Mockito.mock(IRestrictedSlotsProvider.class);
		final RestrictedSlotsConstraintChecker checker = createChecker(name, restrictedElementsProvider);

		final IResource resource1 = Mockito.mock(IResource.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> singleton(element2));
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> emptySet());

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
		Assertions.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1, new ArrayList<>()));
	}

	@Test
	public void test3() {

		final String name = "name";
		final IRestrictedSlotsProvider restrictedElementsProvider = Mockito.mock(IRestrictedSlotsProvider.class);
		final RestrictedSlotsConstraintChecker checker = createChecker(name, restrictedElementsProvider);

		Assertions.assertEquals(name, checker.getName());

		final IResource resource1 = Mockito.mock(IResource.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(restrictedElementsProvider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> emptySet());
		Mockito.when(restrictedElementsProvider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> singleton(element1));

		Assertions.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1, new ArrayList<>()));
		Assertions.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1, new ArrayList<>()));
	}


	private RestrictedSlotsConstraintChecker createChecker(final String name, final IRestrictedSlotsProvider restrictedElementsProvider) {

		final AbstractModule module = new AbstractModule() {

			@Override
			protected void configure() {
				bind(IRestrictedSlotsProvider.class).toInstance(restrictedElementsProvider);
			}

			@Provides
			RestrictedSlotsConstraintChecker create(Injector injector) {
				RestrictedSlotsConstraintChecker checker = new RestrictedSlotsConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}
		};
		final Injector injector = Guice.createInjector(module);
		return injector.getInstance(RestrictedSlotsConstraintChecker.class);
	}
}
