/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl#getScenarioServices <em>Scenario Services</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioModelImpl extends EObjectImpl implements ScenarioModel {
	/**
	 * The cached value of the '{@link #getScenarioServices() <em>Scenario Services</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioServices()
	 * @generated
	 * @ordered
	 */
	protected EList<ScenarioService> scenarioServices;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.Literals.SCENARIO_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ScenarioService> getScenarioServices() {
		if (scenarioServices == null) {
			scenarioServices = new EObjectResolvingEList<ScenarioService>(ScenarioService.class, this, ScenarioServicePackage.SCENARIO_MODEL__SCENARIO_SERVICES);
		}
		return scenarioServices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_MODEL__SCENARIO_SERVICES:
			return getScenarioServices();
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
		case ScenarioServicePackage.SCENARIO_MODEL__SCENARIO_SERVICES:
			getScenarioServices().clear();
			getScenarioServices().addAll((Collection<? extends ScenarioService>) newValue);
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
		case ScenarioServicePackage.SCENARIO_MODEL__SCENARIO_SERVICES:
			getScenarioServices().clear();
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
		case ScenarioServicePackage.SCENARIO_MODEL__SCENARIO_SERVICES:
			return scenarioServices != null && !scenarioServices.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ScenarioModelImpl
