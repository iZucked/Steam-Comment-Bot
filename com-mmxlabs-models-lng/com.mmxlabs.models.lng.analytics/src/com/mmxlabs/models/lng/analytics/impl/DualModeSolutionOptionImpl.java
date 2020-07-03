/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dual Mode Solution Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl#getMicroBaseCase <em>Micro Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.DualModeSolutionOptionImpl#getMicroTargetCase <em>Micro Target Case</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DualModeSolutionOptionImpl extends SolutionOptionImpl implements DualModeSolutionOption {
	/**
	 * The cached value of the '{@link #getMicroBaseCase() <em>Micro Base Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMicroBaseCase()
	 * @generated
	 * @ordered
	 */
	protected SolutionOptionMicroCase microBaseCase;

	/**
	 * The cached value of the '{@link #getMicroTargetCase() <em>Micro Target Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMicroTargetCase()
	 * @generated
	 * @ordered
	 */
	protected SolutionOptionMicroCase microTargetCase;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DualModeSolutionOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.DUAL_MODE_SOLUTION_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SolutionOptionMicroCase getMicroBaseCase() {
		return microBaseCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMicroBaseCase(SolutionOptionMicroCase newMicroBaseCase, NotificationChain msgs) {
		SolutionOptionMicroCase oldMicroBaseCase = microBaseCase;
		microBaseCase = newMicroBaseCase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE, oldMicroBaseCase, newMicroBaseCase);
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
	public void setMicroBaseCase(SolutionOptionMicroCase newMicroBaseCase) {
		if (newMicroBaseCase != microBaseCase) {
			NotificationChain msgs = null;
			if (microBaseCase != null)
				msgs = ((InternalEObject)microBaseCase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE, null, msgs);
			if (newMicroBaseCase != null)
				msgs = ((InternalEObject)newMicroBaseCase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE, null, msgs);
			msgs = basicSetMicroBaseCase(newMicroBaseCase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE, newMicroBaseCase, newMicroBaseCase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SolutionOptionMicroCase getMicroTargetCase() {
		return microTargetCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMicroTargetCase(SolutionOptionMicroCase newMicroTargetCase, NotificationChain msgs) {
		SolutionOptionMicroCase oldMicroTargetCase = microTargetCase;
		microTargetCase = newMicroTargetCase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE, oldMicroTargetCase, newMicroTargetCase);
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
	public void setMicroTargetCase(SolutionOptionMicroCase newMicroTargetCase) {
		if (newMicroTargetCase != microTargetCase) {
			NotificationChain msgs = null;
			if (microTargetCase != null)
				msgs = ((InternalEObject)microTargetCase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE, null, msgs);
			if (newMicroTargetCase != null)
				msgs = ((InternalEObject)newMicroTargetCase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE, null, msgs);
			msgs = basicSetMicroTargetCase(newMicroTargetCase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE, newMicroTargetCase, newMicroTargetCase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE:
				return basicSetMicroBaseCase(null, msgs);
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE:
				return basicSetMicroTargetCase(null, msgs);
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
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE:
				return getMicroBaseCase();
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE:
				return getMicroTargetCase();
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
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE:
				setMicroBaseCase((SolutionOptionMicroCase)newValue);
				return;
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE:
				setMicroTargetCase((SolutionOptionMicroCase)newValue);
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
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE:
				setMicroBaseCase((SolutionOptionMicroCase)null);
				return;
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE:
				setMicroTargetCase((SolutionOptionMicroCase)null);
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
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE:
				return microBaseCase != null;
			case AnalyticsPackage.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE:
				return microTargetCase != null;
		}
		return super.eIsSet(featureID);
	}

} //DualModeSolutionOptionImpl
