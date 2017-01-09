/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * Implementation of {@link EObjectWrapper} which extends {@link DynamicEObjectImpl} for use in the migration process. To use this class, override {@link EFactoryImpl#basicCreate} to return objects of
 * this type rather than standard {@link DynamicEObjectImpl} instances.
 * 
 * @author Simon Goodall
 * 
 */
public class DynamicEObjectWrapperImpl extends DynamicEObjectImpl implements EObjectWrapper {

	public DynamicEObjectWrapperImpl() {
		super();
	}

	public DynamicEObjectWrapperImpl(final EClass eClass) {
		super(eClass);
	}

	@Override
	public EObjectWrapper getRef(final String name) {
		final EReference feature = (EReference) eClass().getEStructuralFeature(name);
		assert feature != null;
		assert !feature.isMany();
		return (EObjectWrapper) eGet(feature);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EObjectWrapper> getRefAsList(final String name) {
		final EReference feature = (EReference) eClass().getEStructuralFeature(name);
		assert feature != null;
		assert feature.isMany();
		return (List<EObjectWrapper>) eGet(feature);
	}

	@Override
	public void setRef(final String name, final Object value) {
		final EReference feature = (EReference) eClass().getEStructuralFeature(name);
		assert feature != null;
		eSet(feature, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttrib(final String name) {
		final EAttribute feature = (EAttribute) eClass().getEStructuralFeature(name);
		assert feature != null;
		assert !feature.isMany();
		return (T) eGet(feature);
	}

	@Override
	public Boolean getAttribAsBooleanObject(final String name) {
		final EAttribute feature = (EAttribute) eClass().getEStructuralFeature(name);
		assert feature != null;
		assert !feature.isMany();
		assert feature.getEType() == EcorePackage.Literals.EBOOLEAN || feature.getEType() == EcorePackage.Literals.EBOOLEAN_OBJECT;
		return (Boolean) eGet(feature);
	}

	@Override
	public boolean getAttribAsBoolean(final String name) {
		final EAttribute feature = (EAttribute) eClass().getEStructuralFeature(name);
		assert feature != null;
		assert !feature.isMany();
		assert feature.getEType() == EcorePackage.Literals.EBOOLEAN;
		return (Boolean) eGet(feature);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAttribAsList(final String name) {
		final EAttribute feature = (EAttribute) eClass().getEStructuralFeature(name);
		assert feature != null;
		assert feature.isMany();
		return (List<T>) eGet(feature);
	}

	@Override
	public void setAttrib(final String name, final Object value) {
		final EAttribute feature = (EAttribute) eClass().getEStructuralFeature(name);
		eSet(feature, value);
	}

	@Override
	public void unsetFeature(final String name) {
		final EStructuralFeature feature = eClass().getEStructuralFeature(name);
		eUnset(feature);
	}

	@Override
	public boolean isSetFeature(final String name) {
		final EStructuralFeature feature = eClass().getEStructuralFeature(name);
		return eIsSet(feature);
	}
}
