/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.InputModelImpl#getElementAssignments <em>Element Assignments</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InputModelImpl extends UUIDObjectImpl implements InputModel {
	/**
	 * The cached value of the '{@link #getElementAssignments() <em>Element Assignments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementAssignments()
	 * @generated
	 * @ordered
	 */
	protected EList<ElementAssignment> elementAssignments;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InputPackage.Literals.INPUT_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ElementAssignment> getElementAssignments() {
		if (elementAssignments == null) {
			elementAssignments = new EObjectContainmentEList<ElementAssignment>(ElementAssignment.class, this, InputPackage.INPUT_MODEL__ELEMENT_ASSIGNMENTS);
		}
		return elementAssignments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case InputPackage.INPUT_MODEL__ELEMENT_ASSIGNMENTS:
				return ((InternalEList<?>)getElementAssignments()).basicRemove(otherEnd, msgs);
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
			case InputPackage.INPUT_MODEL__ELEMENT_ASSIGNMENTS:
				return getElementAssignments();
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
			case InputPackage.INPUT_MODEL__ELEMENT_ASSIGNMENTS:
				getElementAssignments().clear();
				getElementAssignments().addAll((Collection<? extends ElementAssignment>)newValue);
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
			case InputPackage.INPUT_MODEL__ELEMENT_ASSIGNMENTS:
				getElementAssignments().clear();
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
			case InputPackage.INPUT_MODEL__ELEMENT_ASSIGNMENTS:
				return elementAssignments != null && !elementAssignments.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of InputModelImpl

// finish type fixing
