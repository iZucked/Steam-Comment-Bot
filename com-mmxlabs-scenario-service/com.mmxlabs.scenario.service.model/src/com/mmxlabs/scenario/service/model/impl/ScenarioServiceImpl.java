/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import org.eclipse.emf.ecore.EClass;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#getServiceRef <em>Service Ref</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#isSupportsForking <em>Supports Forking</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#isSupportsImport <em>Supports Import</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#getScenarioModel <em>Scenario Model</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#isLocal <em>Local</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#getServiceID <em>Service ID</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioServiceImpl extends ContainerImpl implements ScenarioService {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getScenarioService();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioService_Description(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioService_Description(), newDescription);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IScenarioService getServiceRef() {
		return (IScenarioService) eGet(ScenarioServicePackage.eINSTANCE.getScenarioService_ServiceRef(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceRef(IScenarioService newServiceRef) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioService_ServiceRef(), newServiceRef);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSupportsForking() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getScenarioService_SupportsForking(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSupportsForking(boolean newSupportsForking) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioService_SupportsForking(), newSupportsForking);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSupportsImport() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getScenarioService_SupportsImport(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSupportsImport(boolean newSupportsImport) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioService_SupportsImport(), newSupportsImport);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioModel getScenarioModel() {
		return (ScenarioModel) eGet(ScenarioServicePackage.eINSTANCE.getScenarioService_ScenarioModel(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenarioModel(ScenarioModel newScenarioModel) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioService_ScenarioModel(), newScenarioModel);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLocal() {
		return (Boolean) eGet(ScenarioServicePackage.eINSTANCE.getScenarioService_Local(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocal(boolean newLocal) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioService_Local(), newLocal);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getServiceID() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getScenarioService_ServiceID(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceID(String newServiceID) {
		eSet(ScenarioServicePackage.eINSTANCE.getScenarioService_ServiceID(), newServiceID);
	}

} //ScenarioServiceImpl
