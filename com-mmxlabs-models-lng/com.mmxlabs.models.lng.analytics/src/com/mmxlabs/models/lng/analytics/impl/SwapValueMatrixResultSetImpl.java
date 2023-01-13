/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet;

import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Result Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl#getResults <em>Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl#getSwapFee <em>Swap Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl#getGeneratedSpotLoadSlot <em>Generated Spot Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultSetImpl#getGeneratedSpotDischargeSlot <em>Generated Spot Discharge Slot</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SwapValueMatrixResultSetImpl extends UUIDObjectImpl implements SwapValueMatrixResultSet {
	/**
	 * The cached value of the '{@link #getResults() <em>Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResults()
	 * @generated
	 * @ordered
	 */
	protected EList<SwapValueMatrixResult> results;

	/**
	 * The default value of the '{@link #getSwapFee() <em>Swap Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFee()
	 * @generated
	 * @ordered
	 */
	protected static final double SWAP_FEE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSwapFee() <em>Swap Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFee()
	 * @generated
	 * @ordered
	 */
	protected double swapFee = SWAP_FEE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGeneratedSpotLoadSlot() <em>Generated Spot Load Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratedSpotLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected SpotLoadSlot generatedSpotLoadSlot;

	/**
	 * The cached value of the '{@link #getGeneratedSpotDischargeSlot() <em>Generated Spot Discharge Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratedSpotDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected SpotDischargeSlot generatedSpotDischargeSlot;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixResultSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SwapValueMatrixResult> getResults() {
		if (results == null) {
			results = new EObjectContainmentEList<SwapValueMatrixResult>(SwapValueMatrixResult.class, this, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__RESULTS);
		}
		return results;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getSwapFee() {
		return swapFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapFee(double newSwapFee) {
		double oldSwapFee = swapFee;
		swapFee = newSwapFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE, oldSwapFee, swapFee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotLoadSlot getGeneratedSpotLoadSlot() {
		return generatedSpotLoadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGeneratedSpotLoadSlot(SpotLoadSlot newGeneratedSpotLoadSlot, NotificationChain msgs) {
		SpotLoadSlot oldGeneratedSpotLoadSlot = generatedSpotLoadSlot;
		generatedSpotLoadSlot = newGeneratedSpotLoadSlot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT, oldGeneratedSpotLoadSlot, newGeneratedSpotLoadSlot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGeneratedSpotLoadSlot(SpotLoadSlot newGeneratedSpotLoadSlot) {
		if (newGeneratedSpotLoadSlot != generatedSpotLoadSlot) {
			NotificationChain msgs = null;
			if (generatedSpotLoadSlot != null)
				msgs = ((InternalEObject)generatedSpotLoadSlot).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT, null, msgs);
			if (newGeneratedSpotLoadSlot != null)
				msgs = ((InternalEObject)newGeneratedSpotLoadSlot).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT, null, msgs);
			msgs = basicSetGeneratedSpotLoadSlot(newGeneratedSpotLoadSlot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT, newGeneratedSpotLoadSlot, newGeneratedSpotLoadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotDischargeSlot getGeneratedSpotDischargeSlot() {
		return generatedSpotDischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGeneratedSpotDischargeSlot(SpotDischargeSlot newGeneratedSpotDischargeSlot, NotificationChain msgs) {
		SpotDischargeSlot oldGeneratedSpotDischargeSlot = generatedSpotDischargeSlot;
		generatedSpotDischargeSlot = newGeneratedSpotDischargeSlot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT, oldGeneratedSpotDischargeSlot, newGeneratedSpotDischargeSlot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGeneratedSpotDischargeSlot(SpotDischargeSlot newGeneratedSpotDischargeSlot) {
		if (newGeneratedSpotDischargeSlot != generatedSpotDischargeSlot) {
			NotificationChain msgs = null;
			if (generatedSpotDischargeSlot != null)
				msgs = ((InternalEObject)generatedSpotDischargeSlot).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT, null, msgs);
			if (newGeneratedSpotDischargeSlot != null)
				msgs = ((InternalEObject)newGeneratedSpotDischargeSlot).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT, null, msgs);
			msgs = basicSetGeneratedSpotDischargeSlot(newGeneratedSpotDischargeSlot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT, newGeneratedSpotDischargeSlot, newGeneratedSpotDischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__RESULTS:
				return ((InternalEList<?>)getResults()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT:
				return basicSetGeneratedSpotLoadSlot(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT:
				return basicSetGeneratedSpotDischargeSlot(null, msgs);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__RESULTS:
				return getResults();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE:
				return getSwapFee();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT:
				return getGeneratedSpotLoadSlot();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT:
				return getGeneratedSpotDischargeSlot();
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__RESULTS:
				getResults().clear();
				getResults().addAll((Collection<? extends SwapValueMatrixResult>)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE:
				setSwapFee((Double)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT:
				setGeneratedSpotLoadSlot((SpotLoadSlot)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT:
				setGeneratedSpotDischargeSlot((SpotDischargeSlot)newValue);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__RESULTS:
				getResults().clear();
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE:
				setSwapFee(SWAP_FEE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT:
				setGeneratedSpotLoadSlot((SpotLoadSlot)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT:
				setGeneratedSpotDischargeSlot((SpotDischargeSlot)null);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__RESULTS:
				return results != null && !results.isEmpty();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__SWAP_FEE:
				return swapFee != SWAP_FEE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_LOAD_SLOT:
				return generatedSpotLoadSlot != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT_SET__GENERATED_SPOT_DISCHARGE_SLOT:
				return generatedSpotDischargeSlot != null;
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
		result.append(" (swapFee: ");
		result.append(swapFee);
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixResultSetImpl
