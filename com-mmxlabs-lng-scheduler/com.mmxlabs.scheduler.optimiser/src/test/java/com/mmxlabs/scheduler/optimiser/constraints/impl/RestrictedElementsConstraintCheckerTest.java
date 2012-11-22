package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IRestrictedElementsProvider;

public class RestrictedElementsConstraintCheckerTest {

	@Test
	public void test1() {

		final String name = "name";
		final RestrictedElementsConstraintChecker checker = new RestrictedElementsConstraintChecker(name);

		Assert.assertEquals(name, checker.getName());

		final Injector injector = createInjector();
		injector.injectMembers(checker);

		final IRestrictedElementsProvider provider = injector.getInstance(IRestrictedElementsProvider.class);

		final IResource resource1 = Mockito.mock(IResource.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(provider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> emptySet());
		Mockito.when(provider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> emptySet());

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));

		Mockito.verify(provider).getRestrictedFollowerElements(element1);
		Mockito.verify(provider).getRestrictedPrecedingElements(element2);

		Mockito.verifyNoMoreInteractions(provider);
	}

	@Test
	public void test2() {

		final String name = "name";
		final RestrictedElementsConstraintChecker checker = new RestrictedElementsConstraintChecker(name);

		Assert.assertEquals(name, checker.getName());

		final Injector injector = createInjector();
		injector.injectMembers(checker);

		final IRestrictedElementsProvider provider = injector.getInstance(IRestrictedElementsProvider.class);

		final IResource resource1 = Mockito.mock(IResource.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(provider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> singleton(element2));
		Mockito.when(provider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> emptySet());

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1));

		Mockito.verify(provider).getRestrictedFollowerElements(element1);
		Mockito.verify(provider).getRestrictedFollowerElements(element2);
		Mockito.verify(provider).getRestrictedPrecedingElements(element1);
		// Early fail in && means we should not get this invocation
		// Mockito.verify(provider).getRestrictedPrecedingElements(element2);

		Mockito.verifyNoMoreInteractions(provider);
	}

	@Test
	public void test3() {

		final String name = "name";
		final RestrictedElementsConstraintChecker checker = new RestrictedElementsConstraintChecker(name);

		Assert.assertEquals(name, checker.getName());

		final Injector injector = createInjector();
		injector.injectMembers(checker);

		final IRestrictedElementsProvider provider = injector.getInstance(IRestrictedElementsProvider.class);

		final IResource resource1 = Mockito.mock(IResource.class);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(provider.getRestrictedFollowerElements(element1)).thenReturn(Collections.<ISequenceElement> emptySet());
		Mockito.when(provider.getRestrictedPrecedingElements(element2)).thenReturn(Collections.<ISequenceElement> singleton(element1));

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertTrue(checker.checkPairwiseConstraint(element2, element1, resource1));

		Mockito.verify(provider).getRestrictedFollowerElements(element1);
		Mockito.verify(provider).getRestrictedFollowerElements(element2);
		Mockito.verify(provider).getRestrictedPrecedingElements(element1);
		Mockito.verify(provider).getRestrictedPrecedingElements(element2);

		Mockito.verifyNoMoreInteractions(provider);
	}

	private Injector createInjector() {

		final AbstractModule module = new AbstractModule() {

			@Override
			protected void configure() {
				final IRestrictedElementsProvider provider = Mockito.mock(IRestrictedElementsProvider.class);
				bind(IRestrictedElementsProvider.class).toInstance(provider);
			}
		};
		return Guice.createInjector(module);
	}
}
