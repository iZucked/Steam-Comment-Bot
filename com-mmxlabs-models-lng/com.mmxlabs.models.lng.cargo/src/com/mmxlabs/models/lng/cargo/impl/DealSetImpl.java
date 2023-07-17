/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
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
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.DealSetImpl#isAllowExposure <em>Allow Exposure</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.DealSetImpl#isAllowHedging <em>Allow Hedging</em>}</li>
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
	 * The default value of the '{@link #isAllowExposure() <em>Allow Exposure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowExposure()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ALLOW_EXPOSURE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAllowExposure() <em>Allow Exposure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowExposure()
	 * @generated
	 * @ordered
	 */
	protected boolean allowExposure = ALLOW_EXPOSURE_EDEFAULT;

	/**
	 * The default value of the '{@link #isAllowHedging() <em>Allow Hedging</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowHedging()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ALLOW_HEDGING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAllowHedging() <em>Allow Hedging</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowHedging()
	 * @generated
	 * @ordered
	 */
	protected boolean allowHedging = ALLOW_HEDGING_EDEFAULT;

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
	public boolean isAllowExposure() {
		return allowExposure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAllowExposure(boolean newAllowExposure) {
		boolean oldAllowExposure = allowExposure;
		allowExposure = newAllowExposure;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.DEAL_SET__ALLOW_EXPOSURE, oldAllowExposure, allowExposure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isAllowHedging() {
		return allowHedging;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAllowHedging(boolean newAllowHedging) {
		boolean oldAllowHedging = allowHedging;
		allowHedging = newAllowHedging;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.DEAL_SET__ALLOW_HEDGING, oldAllowHedging, allowHedging));
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
			case CargoPackage.DEAL_SET__ALLOW_EXPOSURE:
				return isAllowExposure();
			case CargoPackage.DEAL_SET__ALLOW_HEDGING:
				return isAllowHedging();
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
			case CargoPackage.DEAL_SET__ALLOW_EXPOSURE:
				setAllowExposure((Boolean)newValue);
				return;
			case CargoPackage.DEAL_SET__ALLOW_HEDGING:
				setAllowHedging((Boolean)newValue);
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
			case CargoPackage.DEAL_SET__ALLOW_EXPOSURE:
				setAllowExposure(ALLOW_EXPOSURE_EDEFAULT);
				return;
			case CargoPackage.DEAL_SET__ALLOW_HEDGING:
				setAllowHedging(ALLOW_HEDGING_EDEFAULT);
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
			case CargoPackage.DEAL_SET__ALLOW_EXPOSURE:
				return allowExposure != ALLOW_EXPOSURE_EDEFAULT;
			case CargoPackage.DEAL_SET__ALLOW_HEDGING:
				return allowHedging != ALLOW_HEDGING_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (allowExposure: ");
		result.append(allowExposure);
		result.append(", allowHedging: ");
		result.append(allowHedging);
		result.append(')');
		return result.toString();
	}

} //DealSetImpl
