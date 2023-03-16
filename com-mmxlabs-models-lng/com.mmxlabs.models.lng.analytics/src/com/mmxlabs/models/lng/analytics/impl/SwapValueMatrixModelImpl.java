/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getSwapValueMatrixResult <em>Swap Value Matrix Result</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SwapValueMatrixModelImpl extends AbstractAnalysisModelImpl implements SwapValueMatrixModel {
	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected SwapValueMatrixParameters parameters;

	/**
	 * The cached value of the '{@link #getSwapValueMatrixResult() <em>Swap Value Matrix Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 * @ordered
	 */
	protected SwapValueMatrixResultSet swapValueMatrixResult;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SwapValueMatrixParameters getParameters() {
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParameters(SwapValueMatrixParameters newParameters, NotificationChain msgs) {
		SwapValueMatrixParameters oldParameters = parameters;
		parameters = newParameters;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS, oldParameters, newParameters);
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
	public void setParameters(SwapValueMatrixParameters newParameters) {
		if (newParameters != parameters) {
			NotificationChain msgs = null;
			if (parameters != null)
				msgs = ((InternalEObject)parameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS, null, msgs);
			if (newParameters != null)
				msgs = ((InternalEObject)newParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS, null, msgs);
			msgs = basicSetParameters(newParameters, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS, newParameters, newParameters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SwapValueMatrixResultSet getSwapValueMatrixResult() {
		return swapValueMatrixResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSwapValueMatrixResult(SwapValueMatrixResultSet newSwapValueMatrixResult, NotificationChain msgs) {
		SwapValueMatrixResultSet oldSwapValueMatrixResult = swapValueMatrixResult;
		swapValueMatrixResult = newSwapValueMatrixResult;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, oldSwapValueMatrixResult, newSwapValueMatrixResult);
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
	public void setSwapValueMatrixResult(SwapValueMatrixResultSet newSwapValueMatrixResult) {
		if (newSwapValueMatrixResult != swapValueMatrixResult) {
			NotificationChain msgs = null;
			if (swapValueMatrixResult != null)
				msgs = ((InternalEObject)swapValueMatrixResult).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, null, msgs);
			if (newSwapValueMatrixResult != null)
				msgs = ((InternalEObject)newSwapValueMatrixResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, null, msgs);
			msgs = basicSetSwapValueMatrixResult(newSwapValueMatrixResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, newSwapValueMatrixResult, newSwapValueMatrixResult));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS:
				return basicSetParameters(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				return basicSetSwapValueMatrixResult(null, msgs);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS:
				return getParameters();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				return getSwapValueMatrixResult();
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS:
				setParameters((SwapValueMatrixParameters)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				setSwapValueMatrixResult((SwapValueMatrixResultSet)newValue);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS:
				setParameters((SwapValueMatrixParameters)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				setSwapValueMatrixResult((SwapValueMatrixResultSet)null);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__PARAMETERS:
				return parameters != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				return swapValueMatrixResult != null;
		}
		return super.eIsSet(featureID);
	}

} //SwapValueMatrixModelImpl
