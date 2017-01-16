/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Availability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotAvailabilityImpl#getConstant <em>Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotAvailabilityImpl#getCurve <em>Curve</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpotAvailabilityImpl extends EObjectImpl implements SpotAvailability {
	/**
	 * The default value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected static final int CONSTANT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected int constant = CONSTANT_EDEFAULT;

	/**
	 * This is true if the Constant attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean constantESet;

	/**
	 * The cached value of the '{@link #getCurve() <em>Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurve()
	 * @generated
	 * @ordered
	 */
	protected DataIndex<Integer> curve;

	/**
	 * This is true if the Curve containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean curveESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotAvailabilityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.SPOT_AVAILABILITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getConstant() {
		return constant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstant(int newConstant) {
		int oldConstant = constant;
		constant = newConstant;
		boolean oldConstantESet = constantESet;
		constantESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_AVAILABILITY__CONSTANT, oldConstant, constant, !oldConstantESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetConstant() {
		int oldConstant = constant;
		boolean oldConstantESet = constantESet;
		constant = CONSTANT_EDEFAULT;
		constantESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.SPOT_AVAILABILITY__CONSTANT, oldConstant, CONSTANT_EDEFAULT, oldConstantESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetConstant() {
		return constantESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataIndex<Integer> getCurve() {
		return curve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCurve(DataIndex<Integer> newCurve, NotificationChain msgs) {
		DataIndex<Integer> oldCurve = curve;
		curve = newCurve;
		boolean oldCurveESet = curveESet;
		curveESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_AVAILABILITY__CURVE, oldCurve, newCurve, !oldCurveESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurve(DataIndex<Integer> newCurve) {
		if (newCurve != curve) {
			NotificationChain msgs = null;
			if (curve != null)
				msgs = ((InternalEObject)curve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_AVAILABILITY__CURVE, null, msgs);
			if (newCurve != null)
				msgs = ((InternalEObject)newCurve).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_AVAILABILITY__CURVE, null, msgs);
			msgs = basicSetCurve(newCurve, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldCurveESet = curveESet;
			curveESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_AVAILABILITY__CURVE, newCurve, newCurve, !oldCurveESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetCurve(NotificationChain msgs) {
		DataIndex<Integer> oldCurve = curve;
		curve = null;
		boolean oldCurveESet = curveESet;
		curveESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.SPOT_AVAILABILITY__CURVE, oldCurve, null, oldCurveESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCurve() {
		if (curve != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject)curve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_AVAILABILITY__CURVE, null, msgs);
			msgs = basicUnsetCurve(msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldCurveESet = curveESet;
			curveESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.SPOT_AVAILABILITY__CURVE, null, null, oldCurveESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCurve() {
		return curveESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SpotMarketsPackage.SPOT_AVAILABILITY__CURVE:
				return basicUnsetCurve(msgs);
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
			case SpotMarketsPackage.SPOT_AVAILABILITY__CONSTANT:
				return getConstant();
			case SpotMarketsPackage.SPOT_AVAILABILITY__CURVE:
				return getCurve();
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
			case SpotMarketsPackage.SPOT_AVAILABILITY__CONSTANT:
				setConstant((Integer)newValue);
				return;
			case SpotMarketsPackage.SPOT_AVAILABILITY__CURVE:
				setCurve((DataIndex<Integer>)newValue);
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
			case SpotMarketsPackage.SPOT_AVAILABILITY__CONSTANT:
				unsetConstant();
				return;
			case SpotMarketsPackage.SPOT_AVAILABILITY__CURVE:
				unsetCurve();
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
			case SpotMarketsPackage.SPOT_AVAILABILITY__CONSTANT:
				return isSetConstant();
			case SpotMarketsPackage.SPOT_AVAILABILITY__CURVE:
				return isSetCurve();
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (constant: ");
		if (constantESet) result.append(constant); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of SpotAvailabilityImpl

// finish type fixing
