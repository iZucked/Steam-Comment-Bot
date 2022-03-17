/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.SlotSpecification;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Non Shipped Cargo Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.NonShippedCargoSpecificationImpl#getSlotSpecifications <em>Slot Specifications</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NonShippedCargoSpecificationImpl extends EObjectImpl implements NonShippedCargoSpecification {
	/**
	 * The cached value of the '{@link #getSlotSpecifications() <em>Slot Specifications</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotSpecifications()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotSpecification> slotSpecifications;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NonShippedCargoSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.NON_SHIPPED_CARGO_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SlotSpecification> getSlotSpecifications() {
		if (slotSpecifications == null) {
			slotSpecifications = new EObjectContainmentEList.Resolving<SlotSpecification>(SlotSpecification.class, this, CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS);
		}
		return slotSpecifications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS:
				return ((InternalEList<?>)getSlotSpecifications()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS:
				return getSlotSpecifications();
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
			case CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS:
				getSlotSpecifications().clear();
				getSlotSpecifications().addAll((Collection<? extends SlotSpecification>)newValue);
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
			case CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS:
				getSlotSpecifications().clear();
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
			case CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS:
				return slotSpecifications != null && !slotSpecifications.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //NonShippedCargoSpecificationImpl
