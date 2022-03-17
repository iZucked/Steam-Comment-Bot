/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import com.mmxlabs.scenario.service.manifest.Manifest;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getRootObjectURI <em>Root Object URI</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getScenarioVersion <em>Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getVersionContext <em>Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getClientScenarioVersion <em>Client Scenario Version</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getClientVersionContext <em>Client Version Context</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getFragments <em>Fragments</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isReadonly <em>Readonly</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#isCloudLocked <em>Cloud Locked</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getValidationStatusCode <em>Validation Status Code</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getManifest <em>Manifest</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl#getExternalID <em>External ID</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioInstanceImpl extends ContainerImpl implements ScenarioInstance {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getScenarioInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getUuid() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUuid(String newUuid) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid(), newUuid);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Metadata getMetadata() {
		return (Metadata) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Metadata(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMetadata(Metadata newMetadata) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Metadata(), newMetadata);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRootObjectURI() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_RootObjectURI(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRootObjectURI(String newRootObjectURI) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_RootObjectURI(), newRootObjectURI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getScenarioVersion() {
		return (Integer) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ScenarioVersion(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setScenarioVersion(int newScenarioVersion) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ScenarioVersion(), newScenarioVersion);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVersionContext() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_VersionContext(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVersionContext(String newVersionContext) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_VersionContext(), newVersionContext);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EList<ScenarioFragment> getFragments() {
		return (EList<ScenarioFragment>) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Fragments(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isReadonly() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Readonly(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReadonly(boolean newReadonly) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Readonly(), newReadonly);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isCloudLocked() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_CloudLocked(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCloudLocked(boolean newCloudLocked) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_CloudLocked(), newCloudLocked);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValidationStatusCode() {
		return (Integer) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValidationStatusCode(int newValidationStatusCode) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode(), newValidationStatusCode);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Manifest getManifest() {
		return (Manifest) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Manifest(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setManifest(Manifest newManifest) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Manifest(), newManifest);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getExternalID() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ExternalID(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExternalID(String newExternalID) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ExternalID(), newExternalID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getClientScenarioVersion() {
		return (Integer) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ClientScenarioVersion(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setClientScenarioVersion(int newClientScenarioVersion) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ClientScenarioVersion(), newClientScenarioVersion);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getClientVersionContext() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ClientVersionContext(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setClientVersionContext(String newClientVersionContext) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioInstance_ClientVersionContext(), newClientVersionContext);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getContainedInstanceCount() {
		return super.getContainedInstanceCount() + 1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<Adapter> eAdapters() {
		// This is the same as the super class except we have the additional #grow call to try and reduce concurrency issues
		if (eAdapters == null) {
			eAdapters = new EAdapterList<Adapter>(this);
			eAdapters.grow(20);
		}
		return eAdapters;
	}
} //ScenarioInstanceImpl
