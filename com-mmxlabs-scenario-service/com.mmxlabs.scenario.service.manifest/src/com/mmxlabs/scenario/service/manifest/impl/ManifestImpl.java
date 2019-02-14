/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestPackage;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Manifest</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getModelURIs <em>Model UR Is</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getUUID <em>UUID</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getScenarioType <em>Scenario Type</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getScenarioVersion <em>Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getVersionContext <em>Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getClientScenarioVersion <em>Client Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getClientVersionContext <em>Client Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getModelDependencies <em>Model Dependencies</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl#getModelFragments <em>Model Fragments</em>}</li>
 * </ul>
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
	 * @generated NOT - replace default value with generated value; when loading the parser will replace this if needs be.b8smotel
	 * 
	 * @ordered
	 */
	protected String uuid = EcoreUtil.generateUUID();;

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
	 * The default value of the '{@link #getScenarioVersion() <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int SCENARIO_VERSION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getScenarioVersion() <em>Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected int scenarioVersion = SCENARIO_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersionContext() <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersionContext()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_CONTEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersionContext() <em>Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersionContext()
	 * @generated
	 * @ordered
	 */
	protected String versionContext = VERSION_CONTEXT_EDEFAULT;

	/**
	 * The default value of the '{@link #getClientScenarioVersion() <em>Client Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int CLIENT_SCENARIO_VERSION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getClientScenarioVersion() <em>Client Scenario Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientScenarioVersion()
	 * @generated
	 * @ordered
	 */
	protected int clientScenarioVersion = CLIENT_SCENARIO_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getClientVersionContext() <em>Client Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientVersionContext()
	 * @generated
	 * @ordered
	 */
	protected static final String CLIENT_VERSION_CONTEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getClientVersionContext() <em>Client Version Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientVersionContext()
	 * @generated
	 * @ordered
	 */
	protected String clientVersionContext = CLIENT_VERSION_CONTEXT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getModelDependencies() <em>Model Dependencies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelArtifact> modelDependencies;

	/**
	 * The cached value of the '{@link #getModelFragments() <em>Model Fragments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelFragments()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelArtifact> modelFragments;

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
	@Override
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
	@Override
	public String getUUID() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public String getScenarioType() {
		return scenarioType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public int getScenarioVersion() {
		return scenarioVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setScenarioVersion(int newScenarioVersion) {
		int oldScenarioVersion = scenarioVersion;
		scenarioVersion = newScenarioVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MANIFEST__SCENARIO_VERSION, oldScenarioVersion, scenarioVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVersionContext() {
		return versionContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVersionContext(String newVersionContext) {
		String oldVersionContext = versionContext;
		versionContext = newVersionContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MANIFEST__VERSION_CONTEXT, oldVersionContext, versionContext));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getClientScenarioVersion() {
		return clientScenarioVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setClientScenarioVersion(int newClientScenarioVersion) {
		int oldClientScenarioVersion = clientScenarioVersion;
		clientScenarioVersion = newClientScenarioVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MANIFEST__CLIENT_SCENARIO_VERSION, oldClientScenarioVersion, clientScenarioVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getClientVersionContext() {
		return clientVersionContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setClientVersionContext(String newClientVersionContext) {
		String oldClientVersionContext = clientVersionContext;
		clientVersionContext = newClientVersionContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MANIFEST__CLIENT_VERSION_CONTEXT, oldClientVersionContext, clientVersionContext));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ModelArtifact> getModelDependencies() {
		if (modelDependencies == null) {
			modelDependencies = new EObjectContainmentEList<ModelArtifact>(ModelArtifact.class, this, ManifestPackage.MANIFEST__MODEL_DEPENDENCIES);
		}
		return modelDependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ModelArtifact> getModelFragments() {
		if (modelFragments == null) {
			modelFragments = new EObjectContainmentEList<ModelArtifact>(ModelArtifact.class, this, ManifestPackage.MANIFEST__MODEL_FRAGMENTS);
		}
		return modelFragments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ManifestPackage.MANIFEST__MODEL_DEPENDENCIES:
				return ((InternalEList<?>)getModelDependencies()).basicRemove(otherEnd, msgs);
			case ManifestPackage.MANIFEST__MODEL_FRAGMENTS:
				return ((InternalEList<?>)getModelFragments()).basicRemove(otherEnd, msgs);
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
			case ManifestPackage.MANIFEST__MODEL_UR_IS:
				return getModelURIs();
			case ManifestPackage.MANIFEST__UUID:
				return getUUID();
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				return getScenarioType();
			case ManifestPackage.MANIFEST__SCENARIO_VERSION:
				return getScenarioVersion();
			case ManifestPackage.MANIFEST__VERSION_CONTEXT:
				return getVersionContext();
			case ManifestPackage.MANIFEST__CLIENT_SCENARIO_VERSION:
				return getClientScenarioVersion();
			case ManifestPackage.MANIFEST__CLIENT_VERSION_CONTEXT:
				return getClientVersionContext();
			case ManifestPackage.MANIFEST__MODEL_DEPENDENCIES:
				return getModelDependencies();
			case ManifestPackage.MANIFEST__MODEL_FRAGMENTS:
				return getModelFragments();
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
			case ManifestPackage.MANIFEST__UUID:
				setUUID((String)newValue);
				return;
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				setScenarioType((String)newValue);
				return;
			case ManifestPackage.MANIFEST__SCENARIO_VERSION:
				setScenarioVersion((Integer)newValue);
				return;
			case ManifestPackage.MANIFEST__VERSION_CONTEXT:
				setVersionContext((String)newValue);
				return;
			case ManifestPackage.MANIFEST__CLIENT_SCENARIO_VERSION:
				setClientScenarioVersion((Integer)newValue);
				return;
			case ManifestPackage.MANIFEST__CLIENT_VERSION_CONTEXT:
				setClientVersionContext((String)newValue);
				return;
			case ManifestPackage.MANIFEST__MODEL_DEPENDENCIES:
				getModelDependencies().clear();
				getModelDependencies().addAll((Collection<? extends ModelArtifact>)newValue);
				return;
			case ManifestPackage.MANIFEST__MODEL_FRAGMENTS:
				getModelFragments().clear();
				getModelFragments().addAll((Collection<? extends ModelArtifact>)newValue);
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
			case ManifestPackage.MANIFEST__UUID:
				setUUID(UUID_EDEFAULT);
				return;
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				setScenarioType(SCENARIO_TYPE_EDEFAULT);
				return;
			case ManifestPackage.MANIFEST__SCENARIO_VERSION:
				setScenarioVersion(SCENARIO_VERSION_EDEFAULT);
				return;
			case ManifestPackage.MANIFEST__VERSION_CONTEXT:
				setVersionContext(VERSION_CONTEXT_EDEFAULT);
				return;
			case ManifestPackage.MANIFEST__CLIENT_SCENARIO_VERSION:
				setClientScenarioVersion(CLIENT_SCENARIO_VERSION_EDEFAULT);
				return;
			case ManifestPackage.MANIFEST__CLIENT_VERSION_CONTEXT:
				setClientVersionContext(CLIENT_VERSION_CONTEXT_EDEFAULT);
				return;
			case ManifestPackage.MANIFEST__MODEL_DEPENDENCIES:
				getModelDependencies().clear();
				return;
			case ManifestPackage.MANIFEST__MODEL_FRAGMENTS:
				getModelFragments().clear();
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
			case ManifestPackage.MANIFEST__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case ManifestPackage.MANIFEST__SCENARIO_TYPE:
				return SCENARIO_TYPE_EDEFAULT == null ? scenarioType != null : !SCENARIO_TYPE_EDEFAULT.equals(scenarioType);
			case ManifestPackage.MANIFEST__SCENARIO_VERSION:
				return scenarioVersion != SCENARIO_VERSION_EDEFAULT;
			case ManifestPackage.MANIFEST__VERSION_CONTEXT:
				return VERSION_CONTEXT_EDEFAULT == null ? versionContext != null : !VERSION_CONTEXT_EDEFAULT.equals(versionContext);
			case ManifestPackage.MANIFEST__CLIENT_SCENARIO_VERSION:
				return clientScenarioVersion != CLIENT_SCENARIO_VERSION_EDEFAULT;
			case ManifestPackage.MANIFEST__CLIENT_VERSION_CONTEXT:
				return CLIENT_VERSION_CONTEXT_EDEFAULT == null ? clientVersionContext != null : !CLIENT_VERSION_CONTEXT_EDEFAULT.equals(clientVersionContext);
			case ManifestPackage.MANIFEST__MODEL_DEPENDENCIES:
				return modelDependencies != null && !modelDependencies.isEmpty();
			case ManifestPackage.MANIFEST__MODEL_FRAGMENTS:
				return modelFragments != null && !modelFragments.isEmpty();
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
		result.append(", UUID: ");
		result.append(uuid);
		result.append(", scenarioType: ");
		result.append(scenarioType);
		result.append(", scenarioVersion: ");
		result.append(scenarioVersion);
		result.append(", versionContext: ");
		result.append(versionContext);
		result.append(", clientScenarioVersion: ");
		result.append(clientScenarioVersion);
		result.append(", clientVersionContext: ");
		result.append(clientVersionContext);
		result.append(')');
		return result.toString();
	}

} //ManifestImpl
