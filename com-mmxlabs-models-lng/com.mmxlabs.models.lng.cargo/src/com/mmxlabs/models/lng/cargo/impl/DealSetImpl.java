/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deal Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.DealSetImpl#getSlots <em>Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.DealSetImpl#getPaperDeals <em>Paper Deals</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DealSetImpl extends NamedObjectImpl implements DealSet {
	/**
	 * The cached value of the '{@link #getSlots() <em>Slots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot<?>> slots;

	/**
	 * The cached value of the '{@link #getPaperDeals() <em>Paper Deals</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaperDeals()
	 * @generated
	 * @ordered
	 */
	protected EList<PaperDeal> paperDeals;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DealSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.DEAL_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Slot<?>> getSlots() {
		if (slots == null) {
			slots = new EObjectResolvingEList<Slot<?>>(Slot.class, this, CargoPackage.DEAL_SET__SLOTS);
		}
		return slots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PaperDeal> getPaperDeals() {
		if (paperDeals == null) {
			paperDeals = new EObjectResolvingEList<PaperDeal>(PaperDeal.class, this, CargoPackage.DEAL_SET__PAPER_DEALS);
		}
		return paperDeals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.DEAL_SET__SLOTS:
				return getSlots();
			case CargoPackage.DEAL_SET__PAPER_DEALS:
				return getPaperDeals();
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
			case CargoPackage.DEAL_SET__SLOTS:
				getSlots().clear();
				getSlots().addAll((Collection<? extends Slot<?>>)newValue);
				return;
			case CargoPackage.DEAL_SET__PAPER_DEALS:
				getPaperDeals().clear();
				getPaperDeals().addAll((Collection<? extends PaperDeal>)newValue);
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
			case CargoPackage.DEAL_SET__SLOTS:
				getSlots().clear();
				return;
			case CargoPackage.DEAL_SET__PAPER_DEALS:
				getPaperDeals().clear();
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
			case CargoPackage.DEAL_SET__SLOTS:
				return slots != null && !slots.isEmpty();
			case CargoPackage.DEAL_SET__PAPER_DEALS:
				return paperDeals != null && !paperDeals.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DealSetImpl
