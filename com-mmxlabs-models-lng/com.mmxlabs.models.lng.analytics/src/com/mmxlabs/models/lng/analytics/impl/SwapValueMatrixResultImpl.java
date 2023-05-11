/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixNonShippedCargoResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.Schedule;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapPnlMinusBasePnl <em>Swap Pnl Minus Base Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBasePrecedingPnl <em>Base Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseSucceedingPnl <em>Base Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapPrecedingPnl <em>Swap Preceding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapSucceedingPnl <em>Swap Succeeding Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getBaseCargo <em>Base Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapDiversionCargo <em>Swap Diversion Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixResultImpl#getSwapBackfillCargo <em>Swap Backfill Cargo</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SwapValueMatrixResultImpl extends UUIDObjectImpl implements SwapValueMatrixResult {
	/**
	 * The default value of the '{@link #getSwapPnlMinusBasePnl() <em>Swap Pnl Minus Base Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPnlMinusBasePnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_PNL_MINUS_BASE_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapPnlMinusBasePnl() <em>Swap Pnl Minus Base Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPnlMinusBasePnl()
	 * @generated
	 * @ordered
	 */
	protected long swapPnlMinusBasePnl = SWAP_PNL_MINUS_BASE_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBasePrecedingPnl() <em>Base Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_PRECEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBasePrecedingPnl() <em>Base Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long basePrecedingPnl = BASE_PRECEDING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseSucceedingPnl() <em>Base Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long BASE_SUCCEEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getBaseSucceedingPnl() <em>Base Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long baseSucceedingPnl = BASE_SUCCEEDING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapPrecedingPnl() <em>Swap Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_PRECEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapPrecedingPnl() <em>Swap Preceding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPrecedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapPrecedingPnl = SWAP_PRECEDING_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSwapSucceedingPnl() <em>Swap Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long SWAP_SUCCEEDING_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSwapSucceedingPnl() <em>Swap Succeeding Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapSucceedingPnl()
	 * @generated
	 * @ordered
	 */
	protected long swapSucceedingPnl = SWAP_SUCCEEDING_PNL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBaseCargo() <em>Base Cargo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCargo()
	 * @generated
	 * @ordered
	 */
	protected SwapValueMatrixShippedCargoResult baseCargo;

	/**
	 * The cached value of the '{@link #getSwapDiversionCargo() <em>Swap Diversion Cargo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapDiversionCargo()
	 * @generated
	 * @ordered
	 */
	protected SwapValueMatrixShippedCargoResult swapDiversionCargo;

	/**
	 * The cached value of the '{@link #getSwapBackfillCargo() <em>Swap Backfill Cargo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapBackfillCargo()
	 * @generated
	 * @ordered
	 */
	protected SwapValueMatrixNonShippedCargoResult swapBackfillCargo;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapPnlMinusBasePnl() {
		return swapPnlMinusBasePnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapPnlMinusBasePnl(long newSwapPnlMinusBasePnl) {
		long oldSwapPnlMinusBasePnl = swapPnlMinusBasePnl;
		swapPnlMinusBasePnl = newSwapPnlMinusBasePnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL, oldSwapPnlMinusBasePnl, swapPnlMinusBasePnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBasePrecedingPnl() {
		return basePrecedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBasePrecedingPnl(long newBasePrecedingPnl) {
		long oldBasePrecedingPnl = basePrecedingPnl;
		basePrecedingPnl = newBasePrecedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL, oldBasePrecedingPnl, basePrecedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getBaseSucceedingPnl() {
		return baseSucceedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseSucceedingPnl(long newBaseSucceedingPnl) {
		long oldBaseSucceedingPnl = baseSucceedingPnl;
		baseSucceedingPnl = newBaseSucceedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL, oldBaseSucceedingPnl, baseSucceedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapPrecedingPnl() {
		return swapPrecedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapPrecedingPnl(long newSwapPrecedingPnl) {
		long oldSwapPrecedingPnl = swapPrecedingPnl;
		swapPrecedingPnl = newSwapPrecedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL, oldSwapPrecedingPnl, swapPrecedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSwapSucceedingPnl() {
		return swapSucceedingPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapSucceedingPnl(long newSwapSucceedingPnl) {
		long oldSwapSucceedingPnl = swapSucceedingPnl;
		swapSucceedingPnl = newSwapSucceedingPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL, oldSwapSucceedingPnl, swapSucceedingPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SwapValueMatrixShippedCargoResult getBaseCargo() {
		return baseCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseCargo(SwapValueMatrixShippedCargoResult newBaseCargo, NotificationChain msgs) {
		SwapValueMatrixShippedCargoResult oldBaseCargo = baseCargo;
		baseCargo = newBaseCargo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO, oldBaseCargo, newBaseCargo);
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
	public void setBaseCargo(SwapValueMatrixShippedCargoResult newBaseCargo) {
		if (newBaseCargo != baseCargo) {
			NotificationChain msgs = null;
			if (baseCargo != null)
				msgs = ((InternalEObject)baseCargo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO, null, msgs);
			if (newBaseCargo != null)
				msgs = ((InternalEObject)newBaseCargo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO, null, msgs);
			msgs = basicSetBaseCargo(newBaseCargo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO, newBaseCargo, newBaseCargo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SwapValueMatrixShippedCargoResult getSwapDiversionCargo() {
		return swapDiversionCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSwapDiversionCargo(SwapValueMatrixShippedCargoResult newSwapDiversionCargo, NotificationChain msgs) {
		SwapValueMatrixShippedCargoResult oldSwapDiversionCargo = swapDiversionCargo;
		swapDiversionCargo = newSwapDiversionCargo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO, oldSwapDiversionCargo, newSwapDiversionCargo);
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
	public void setSwapDiversionCargo(SwapValueMatrixShippedCargoResult newSwapDiversionCargo) {
		if (newSwapDiversionCargo != swapDiversionCargo) {
			NotificationChain msgs = null;
			if (swapDiversionCargo != null)
				msgs = ((InternalEObject)swapDiversionCargo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO, null, msgs);
			if (newSwapDiversionCargo != null)
				msgs = ((InternalEObject)newSwapDiversionCargo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO, null, msgs);
			msgs = basicSetSwapDiversionCargo(newSwapDiversionCargo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO, newSwapDiversionCargo, newSwapDiversionCargo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SwapValueMatrixNonShippedCargoResult getSwapBackfillCargo() {
		return swapBackfillCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSwapBackfillCargo(SwapValueMatrixNonShippedCargoResult newSwapBackfillCargo, NotificationChain msgs) {
		SwapValueMatrixNonShippedCargoResult oldSwapBackfillCargo = swapBackfillCargo;
		swapBackfillCargo = newSwapBackfillCargo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO, oldSwapBackfillCargo, newSwapBackfillCargo);
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
	public void setSwapBackfillCargo(SwapValueMatrixNonShippedCargoResult newSwapBackfillCargo) {
		if (newSwapBackfillCargo != swapBackfillCargo) {
			NotificationChain msgs = null;
			if (swapBackfillCargo != null)
				msgs = ((InternalEObject)swapBackfillCargo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO, null, msgs);
			if (newSwapBackfillCargo != null)
				msgs = ((InternalEObject)newSwapBackfillCargo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO, null, msgs);
			msgs = basicSetSwapBackfillCargo(newSwapBackfillCargo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO, newSwapBackfillCargo, newSwapBackfillCargo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO:
				return basicSetBaseCargo(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO:
				return basicSetSwapDiversionCargo(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO:
				return basicSetSwapBackfillCargo(null, msgs);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				return getSwapPnlMinusBasePnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				return getBasePrecedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				return getBaseSucceedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				return getSwapPrecedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				return getSwapSucceedingPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO:
				return getBaseCargo();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO:
				return getSwapDiversionCargo();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO:
				return getSwapBackfillCargo();
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				setSwapPnlMinusBasePnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				setBasePrecedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				setBaseSucceedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				setSwapPrecedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				setSwapSucceedingPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO:
				setBaseCargo((SwapValueMatrixShippedCargoResult)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO:
				setSwapDiversionCargo((SwapValueMatrixShippedCargoResult)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO:
				setSwapBackfillCargo((SwapValueMatrixNonShippedCargoResult)newValue);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				setSwapPnlMinusBasePnl(SWAP_PNL_MINUS_BASE_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				setBasePrecedingPnl(BASE_PRECEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				setBaseSucceedingPnl(BASE_SUCCEEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				setSwapPrecedingPnl(SWAP_PRECEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				setSwapSucceedingPnl(SWAP_SUCCEEDING_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO:
				setBaseCargo((SwapValueMatrixShippedCargoResult)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO:
				setSwapDiversionCargo((SwapValueMatrixShippedCargoResult)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO:
				setSwapBackfillCargo((SwapValueMatrixNonShippedCargoResult)null);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PNL_MINUS_BASE_PNL:
				return swapPnlMinusBasePnl != SWAP_PNL_MINUS_BASE_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_PRECEDING_PNL:
				return basePrecedingPnl != BASE_PRECEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_SUCCEEDING_PNL:
				return baseSucceedingPnl != BASE_SUCCEEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_PRECEDING_PNL:
				return swapPrecedingPnl != SWAP_PRECEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_SUCCEEDING_PNL:
				return swapSucceedingPnl != SWAP_SUCCEEDING_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__BASE_CARGO:
				return baseCargo != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_DIVERSION_CARGO:
				return swapDiversionCargo != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_RESULT__SWAP_BACKFILL_CARGO:
				return swapBackfillCargo != null;
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
		result.append(" (swapPnlMinusBasePnl: ");
		result.append(swapPnlMinusBasePnl);
		result.append(", basePrecedingPnl: ");
		result.append(basePrecedingPnl);
		result.append(", baseSucceedingPnl: ");
		result.append(baseSucceedingPnl);
		result.append(", swapPrecedingPnl: ");
		result.append(swapPrecedingPnl);
		result.append(", swapSucceedingPnl: ");
		result.append(swapSucceedingPnl);
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixResultImpl
