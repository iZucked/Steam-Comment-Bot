/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Basic Slot PNL Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl#getExtraShippingPNL <em>Extra Shipping PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl#getAdditionalPNL <em>Additional PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl#getCancellationFees <em>Cancellation Fees</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl#getMiscCostsValue <em>Misc Costs Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl#getExtraUpsidePNL <em>Extra Upside PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl#getReferenceCurves <em>Reference Curves</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BasicSlotPNLDetailsImpl extends GeneralPNLDetailsImpl implements BasicSlotPNLDetails {
	/**
	 * The default value of the '{@link #getExtraShippingPNL() <em>Extra Shipping PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraShippingPNL()
	 * @generated
	 * @ordered
	 */
	protected static final int EXTRA_SHIPPING_PNL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getExtraShippingPNL() <em>Extra Shipping PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraShippingPNL()
	 * @generated
	 * @ordered
	 */
	protected int extraShippingPNL = EXTRA_SHIPPING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getAdditionalPNL() <em>Additional PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalPNL()
	 * @generated
	 * @ordered
	 */
	protected static final int ADDITIONAL_PNL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAdditionalPNL() <em>Additional PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalPNL()
	 * @generated
	 * @ordered
	 */
	protected int additionalPNL = ADDITIONAL_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getCancellationFees() <em>Cancellation Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCancellationFees()
	 * @generated
	 * @ordered
	 */
	protected static final int CANCELLATION_FEES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCancellationFees() <em>Cancellation Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCancellationFees()
	 * @generated
	 * @ordered
	 */
	protected int cancellationFees = CANCELLATION_FEES_EDEFAULT;

	/**
	 * This is true if the Cancellation Fees attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cancellationFeesESet;

	/**
	 * The default value of the '{@link #getMiscCostsValue() <em>Misc Costs Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMiscCostsValue()
	 * @generated
	 * @ordered
	 */
	protected static final int MISC_COSTS_VALUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMiscCostsValue() <em>Misc Costs Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMiscCostsValue()
	 * @generated
	 * @ordered
	 */
	protected int miscCostsValue = MISC_COSTS_VALUE_EDEFAULT;

	/**
	 * This is true if the Misc Costs Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean miscCostsValueESet;

	/**
	 * The default value of the '{@link #getExtraUpsidePNL() <em>Extra Upside PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraUpsidePNL()
	 * @generated
	 * @ordered
	 */
	protected static final int EXTRA_UPSIDE_PNL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getExtraUpsidePNL() <em>Extra Upside PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraUpsidePNL()
	 * @generated
	 * @ordered
	 */
	protected int extraUpsidePNL = EXTRA_UPSIDE_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getReferenceCurves() <em>Reference Curves</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceCurves()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENCE_CURVES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReferenceCurves() <em>Reference Curves</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceCurves()
	 * @generated
	 * @ordered
	 */
	protected String referenceCurves = REFERENCE_CURVES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BasicSlotPNLDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getExtraShippingPNL() {
		return extraShippingPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExtraShippingPNL(int newExtraShippingPNL) {
		int oldExtraShippingPNL = extraShippingPNL;
		extraShippingPNL = newExtraShippingPNL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL, oldExtraShippingPNL, extraShippingPNL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getAdditionalPNL() {
		return additionalPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAdditionalPNL(int newAdditionalPNL) {
		int oldAdditionalPNL = additionalPNL;
		additionalPNL = newAdditionalPNL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL, oldAdditionalPNL, additionalPNL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCancellationFees() {
		return cancellationFees;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCancellationFees(int newCancellationFees) {
		int oldCancellationFees = cancellationFees;
		cancellationFees = newCancellationFees;
		boolean oldCancellationFeesESet = cancellationFeesESet;
		cancellationFeesESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES, oldCancellationFees, cancellationFees, !oldCancellationFeesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCancellationFees() {
		int oldCancellationFees = cancellationFees;
		boolean oldCancellationFeesESet = cancellationFeesESet;
		cancellationFees = CANCELLATION_FEES_EDEFAULT;
		cancellationFeesESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES, oldCancellationFees, CANCELLATION_FEES_EDEFAULT, oldCancellationFeesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCancellationFees() {
		return cancellationFeesESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMiscCostsValue() {
		return miscCostsValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMiscCostsValue(int newMiscCostsValue) {
		int oldMiscCostsValue = miscCostsValue;
		miscCostsValue = newMiscCostsValue;
		boolean oldMiscCostsValueESet = miscCostsValueESet;
		miscCostsValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE, oldMiscCostsValue, miscCostsValue, !oldMiscCostsValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMiscCostsValue() {
		int oldMiscCostsValue = miscCostsValue;
		boolean oldMiscCostsValueESet = miscCostsValueESet;
		miscCostsValue = MISC_COSTS_VALUE_EDEFAULT;
		miscCostsValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE, oldMiscCostsValue, MISC_COSTS_VALUE_EDEFAULT, oldMiscCostsValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMiscCostsValue() {
		return miscCostsValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getExtraUpsidePNL() {
		return extraUpsidePNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExtraUpsidePNL(int newExtraUpsidePNL) {
		int oldExtraUpsidePNL = extraUpsidePNL;
		extraUpsidePNL = newExtraUpsidePNL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL, oldExtraUpsidePNL, extraUpsidePNL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getReferenceCurves() {
		return referenceCurves;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferenceCurves(String newReferenceCurves) {
		String oldReferenceCurves = referenceCurves;
		referenceCurves = newReferenceCurves;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BASIC_SLOT_PNL_DETAILS__REFERENCE_CURVES, oldReferenceCurves, referenceCurves));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL:
				return getExtraShippingPNL();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL:
				return getAdditionalPNL();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES:
				return getCancellationFees();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE:
				return getMiscCostsValue();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL:
				return getExtraUpsidePNL();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__REFERENCE_CURVES:
				return getReferenceCurves();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL:
				setExtraShippingPNL((Integer)newValue);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL:
				setAdditionalPNL((Integer)newValue);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES:
				setCancellationFees((Integer)newValue);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE:
				setMiscCostsValue((Integer)newValue);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL:
				setExtraUpsidePNL((Integer)newValue);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__REFERENCE_CURVES:
				setReferenceCurves((String)newValue);
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
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL:
				setExtraShippingPNL(EXTRA_SHIPPING_PNL_EDEFAULT);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL:
				setAdditionalPNL(ADDITIONAL_PNL_EDEFAULT);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES:
				unsetCancellationFees();
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE:
				unsetMiscCostsValue();
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL:
				setExtraUpsidePNL(EXTRA_UPSIDE_PNL_EDEFAULT);
				return;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__REFERENCE_CURVES:
				setReferenceCurves(REFERENCE_CURVES_EDEFAULT);
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
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL:
				return extraShippingPNL != EXTRA_SHIPPING_PNL_EDEFAULT;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL:
				return additionalPNL != ADDITIONAL_PNL_EDEFAULT;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES:
				return isSetCancellationFees();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE:
				return isSetMiscCostsValue();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL:
				return extraUpsidePNL != EXTRA_UPSIDE_PNL_EDEFAULT;
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS__REFERENCE_CURVES:
				return REFERENCE_CURVES_EDEFAULT == null ? referenceCurves != null : !REFERENCE_CURVES_EDEFAULT.equals(referenceCurves);
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
		result.append(" (extraShippingPNL: ");
		result.append(extraShippingPNL);
		result.append(", additionalPNL: ");
		result.append(additionalPNL);
		result.append(", cancellationFees: ");
		if (cancellationFeesESet) result.append(cancellationFees); else result.append("<unset>");
		result.append(", miscCostsValue: ");
		if (miscCostsValueESet) result.append(miscCostsValue); else result.append("<unset>");
		result.append(", extraUpsidePNL: ");
		result.append(extraUpsidePNL);
		result.append(", referenceCurves: ");
		result.append(referenceCurves);
		result.append(')');
		return result.toString();
	}

} //BasicSlotPNLDetailsImpl
