/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.ActionPlanSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Action Plan Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ActionPlanSettingsImpl#getTotalEvaluations <em>Total Evaluations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ActionPlanSettingsImpl#getInRunEvaluations <em>In Run Evaluations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ActionPlanSettingsImpl#getSearchDepth <em>Search Depth</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ActionPlanSettingsImpl extends EObjectImpl implements ActionPlanSettings {
	/**
	 * The default value of the '{@link #getTotalEvaluations() <em>Total Evaluations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalEvaluations()
	 * @generated
	 * @ordered
	 */
	protected static final int TOTAL_EVALUATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTotalEvaluations() <em>Total Evaluations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalEvaluations()
	 * @generated
	 * @ordered
	 */
	protected int totalEvaluations = TOTAL_EVALUATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getInRunEvaluations() <em>In Run Evaluations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInRunEvaluations()
	 * @generated
	 * @ordered
	 */
	protected static final int IN_RUN_EVALUATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getInRunEvaluations() <em>In Run Evaluations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInRunEvaluations()
	 * @generated
	 * @ordered
	 */
	protected int inRunEvaluations = IN_RUN_EVALUATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSearchDepth() <em>Search Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSearchDepth()
	 * @generated
	 * @ordered
	 */
	protected static final int SEARCH_DEPTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSearchDepth() <em>Search Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSearchDepth()
	 * @generated
	 * @ordered
	 */
	protected int searchDepth = SEARCH_DEPTH_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ActionPlanSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.ACTION_PLAN_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTotalEvaluations() {
		return totalEvaluations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTotalEvaluations(int newTotalEvaluations) {
		int oldTotalEvaluations = totalEvaluations;
		totalEvaluations = newTotalEvaluations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS, oldTotalEvaluations, totalEvaluations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getInRunEvaluations() {
		return inRunEvaluations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInRunEvaluations(int newInRunEvaluations) {
		int oldInRunEvaluations = inRunEvaluations;
		inRunEvaluations = newInRunEvaluations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS, oldInRunEvaluations, inRunEvaluations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSearchDepth() {
		return searchDepth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSearchDepth(int newSearchDepth) {
		int oldSearchDepth = searchDepth;
		searchDepth = newSearchDepth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ACTION_PLAN_SETTINGS__SEARCH_DEPTH, oldSearchDepth, searchDepth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS:
				return getTotalEvaluations();
			case ParametersPackage.ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS:
				return getInRunEvaluations();
			case ParametersPackage.ACTION_PLAN_SETTINGS__SEARCH_DEPTH:
				return getSearchDepth();
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
			case ParametersPackage.ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS:
				setTotalEvaluations((Integer)newValue);
				return;
			case ParametersPackage.ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS:
				setInRunEvaluations((Integer)newValue);
				return;
			case ParametersPackage.ACTION_PLAN_SETTINGS__SEARCH_DEPTH:
				setSearchDepth((Integer)newValue);
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
			case ParametersPackage.ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS:
				setTotalEvaluations(TOTAL_EVALUATIONS_EDEFAULT);
				return;
			case ParametersPackage.ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS:
				setInRunEvaluations(IN_RUN_EVALUATIONS_EDEFAULT);
				return;
			case ParametersPackage.ACTION_PLAN_SETTINGS__SEARCH_DEPTH:
				setSearchDepth(SEARCH_DEPTH_EDEFAULT);
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
			case ParametersPackage.ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS:
				return totalEvaluations != TOTAL_EVALUATIONS_EDEFAULT;
			case ParametersPackage.ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS:
				return inRunEvaluations != IN_RUN_EVALUATIONS_EDEFAULT;
			case ParametersPackage.ACTION_PLAN_SETTINGS__SEARCH_DEPTH:
				return searchDepth != SEARCH_DEPTH_EDEFAULT;
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
		result.append(" (totalEvaluations: ");
		result.append(totalEvaluations);
		result.append(", inRunEvaluations: ");
		result.append(inRunEvaluations);
		result.append(", searchDepth: ");
		result.append(searchDepth);
		result.append(')');
		return result.toString();
	}

} //ActionPlanSettingsImpl
