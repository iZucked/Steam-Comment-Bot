package com.mmxlabs.models.ui.validation.tests;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

//TODO: Could also verify returned object is correct.
public class DetailConstraintStatusDecoratorTest {

	@Test
	public void testGetConstraint() {

		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getConstraint();
		Mockito.verify(status).getConstraint();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetTarget() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getTarget();
		Mockito.verify(status).getTarget();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetResultLocus() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getResultLocus();
		Mockito.verify(status).getResultLocus();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetChildren() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getChildren();
		Mockito.verify(status).getChildren();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetCode() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getCode();
		Mockito.verify(status).getCode();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetException() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getException();
		Mockito.verify(status).getException();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetMessage() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getMessage();
		Mockito.verify(status).getMessage();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetPlugin() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getPlugin();
		Mockito.verify(status).getPlugin();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testGetSeverity() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.getSeverity();
		Mockito.verify(status).getSeverity();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testIsMultiStatus() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.isMultiStatus();
		Mockito.verify(status).isMultiStatus();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testIsOK() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.isOK();
		Mockito.verify(status).isOK();

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testMatches() {
		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		deco.matches(1);
		Mockito.verify(status).matches(1);

		Mockito.verifyNoMoreInteractions(status);
	}

	@Test
	public void testAddGetFeaturesForEObject() {

		final IConstraintStatus status = Mockito.mock(IConstraintStatus.class);
		final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(status);

		final EObject obj1 = Mockito.mock(EObject.class);
		final EObject obj2 = Mockito.mock(EObject.class);
		final EObject obj3 = Mockito.mock(EObject.class);
		final EStructuralFeature feature1 = Mockito.mock(EStructuralFeature.class);
		final EStructuralFeature feature2 = Mockito.mock(EStructuralFeature.class);
		final EStructuralFeature feature3 = Mockito.mock(EStructuralFeature.class);

		deco.addEObjectAndFeature(obj1, feature1);
		deco.addEObjectAndFeature(obj1, feature2);
		deco.addEObjectAndFeature(obj2, feature3);

		Assert.assertTrue(deco.getObjects().contains(obj1));
		Assert.assertTrue(deco.getObjects().contains(obj2));
		Assert.assertFalse(deco.getObjects().contains(obj3));

		final Collection<EStructuralFeature> featuresForEObject1 = deco.getFeaturesForEObject(obj1);
		final Collection<EStructuralFeature> featuresForEObject2 = deco.getFeaturesForEObject(obj2);

		Assert.assertEquals(2, featuresForEObject1.size());
		Assert.assertEquals(1, featuresForEObject2.size());

		Assert.assertTrue(featuresForEObject1.contains(feature1));
		Assert.assertTrue(featuresForEObject1.contains(feature2));
		Assert.assertTrue(featuresForEObject2.contains(feature3));

		Mockito.verifyNoMoreInteractions(status);

	}
}
