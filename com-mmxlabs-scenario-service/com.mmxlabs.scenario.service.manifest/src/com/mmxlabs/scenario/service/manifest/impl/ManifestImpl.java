/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest.impl;

import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Manifest</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getModelURIs <em>Model UR Is</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getDependencyUUIDs <em>Dependency UUI Ds</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getUUID <em>UUID</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getScenarioType <em>Scenario Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ManifestImpl extends EObjectImpl implements Manifest {
	/**
	 * The cached value of the '{@link #getModelURIs() <em>Model UR Is</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelURIs()
	 * @generated
	 * @ordered
	 */
	protected EList<String> modelURIs;

	/**
	 * The cached value of the '{@link #getDependencyUUIDs() <em>Dependency UUI Ds</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencyUUIDs()
	 * @generated
	 * @ordered
	 */
	protected EList<String> dependencyUUIDs;

	/**
	 * The default value of the '{@link #getUUID() <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUUID()
	 * @generated
	 * @ordered
	 */
	protected static final String UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUUID() <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUUID()
	 * @generated
	 * @ordered
	 */
	protected String uuid = UUID_EDEFAULT;

	/**
	 * The default value of the '{@link #getScenarioType() <em>Scenario Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioType()
	 * @generated
	 * @ordered
	 */
	protected static final String SCENARIO_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getScenarioType() <em>Scenario Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioType()
	 * @generated
	 * @ordered
	 */
	protected String scenarioType = SCENARIO_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ManifestImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ManifestPackage.Literals.MANIFEST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getModelURIs() {
		if (modelURIs == null) {
			modelURIs = new EDataTypeUniqueEList<String>(String.class, this, ManifestPackage.MANIFEST__MODEL_UR_IS);
		}
		return modelURIs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getDependencyUUIDs() {
		if (dependencyUUIDs == null) {
			dependencyUUIDs = new EDataTypeUniqueEList<String>(String.class, this, ManifestPackage.MANIFEST__DEPENDENCY_UUI_DS);
		}
		return dependencyUUIDs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUUID() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUUID(String newUUID) {
		String oldUUID = uuid;
		uuid = newUUID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MANIFEST__UUID, oldUUID, uuid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getScenarioType() {
		return scenarioType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenarioType(String newScenarioType) {
		String oldScenarioType = scenarioType;
		scenarioType = newScenarioType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MANIFEST__SCENARIO_TYPE, oldScenarioType, scenarioType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ManifestPackage.MANIFEST__MODEL_UR_IS:
				return getModelURIs();
			case ManifestPackage.MANIFEST__DEPENDENCY_UUI_DS:
				return getDependencyUUIDs();
			case ManifestPackage.MANIFEST__UUID:
				return getUUID();
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				return getScenarioType();
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
			case ManifestPackage.MANIFEST__MODEL_UR_IS:
				getModelURIs().clear();
				getModelURIs().addAll((Collection<? extends String>)newValue);
				return;
			case ManifestPackage.MANIFEST__DEPENDENCY_UUI_DS:
				getDependencyUUIDs().clear();
				getDependencyUUIDs().addAll((Collection<? extends String>)newValue);
				return;
			case ManifestPackage.MANIFEST__UUID:
				setUUID((String)newValue);
				return;
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				setScenarioType((String)newValue);
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
			case ManifestPackage.MANIFEST__MODEL_UR_IS:
				getModelURIs().clear();
				return;
			case ManifestPackage.MANIFEST__DEPENDENCY_UUI_DS:
				getDependencyUUIDs().clear();
				return;
			case ManifestPackage.MANIFEST__UUID:
				setUUID(UUID_EDEFAULT);
				return;
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				setScenarioType(SCENARIO_TYPE_EDEFAULT);
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
			case ManifestPackage.MANIFEST__MODEL_UR_IS:
				return modelURIs != null && !modelURIs.isEmpty();
			case ManifestPackage.MANIFEST__DEPENDENCY_UUI_DS:
				return dependencyUUIDs != null && !dependencyUUIDs.isEmpty();
			case ManifestPackage.MANIFEST__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				return SCENARIO_TYPE_EDEFAULT == null ? scenarioType != null : !SCENARIO_TYPE_EDEFAULT.equals(scenarioType);
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
		result.append(" (modelURIs: ");
		result.append(modelURIs);
		result.append(", dependencyUUIDs: ");
		result.append(dependencyUUIDs);
		result.append(", UUID: ");
		result.append(uuid);
		result.append(", scenarioType: ");
		result.append(scenarioType);
		result.append(')');
		return result.toString();
	}

} //ManifestImpl
