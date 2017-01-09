/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.ui.test.model.ModelPackage;
import com.mmxlabs.models.ui.test.model.MultipleContainmentReference;
import com.mmxlabs.models.ui.test.model.SimpleObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiple Containment Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.MultipleContainmentReferenceImpl#getSimpleObjects <em>Simple Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultipleContainmentReferenceImpl extends EObjectImpl implements MultipleContainmentReference {
	/**
	 * The cached value of the '{@link #getSimpleObjects() <em>Simple Objects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleObjects()
	 * @generated
	 * @ordered
	 */
	protected EList<SimpleObject> simpleObjects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MultipleContainmentReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.MULTIPLE_CONTAINMENT_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SimpleObject> getSimpleObjects() {
		if (simpleObjects == null) {
			simpleObjects = new EObjectContainmentEList<SimpleObject>(SimpleObject.class, this, ModelPackage.MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS);
		}
		return simpleObjects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS:
				return ((InternalEList<?>)getSimpleObjects()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS:
				return getSimpleObjects();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS:
				getSimpleObjects().clear();
				getSimpleObjects().addAll((Collection<? extends SimpleObject>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS:
				getSimpleObjects().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS:
				return simpleObjects != null && !simpleObjects.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MultipleContainmentReferenceImpl
