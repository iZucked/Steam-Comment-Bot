/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#getServiceRef <em>Service Ref</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl#isSupportsForking <em>Supports Forking</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioServiceImpl extends ContainerImpl implements ScenarioService {
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getServiceRef() <em>Service Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceRef()
	 * @generated
	 * @ordered
	 */
	protected static final IScenarioService SERVICE_REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getServiceRef() <em>Service Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceRef()
	 * @generated
	 * @ordered
	 */
	protected IScenarioService serviceRef = SERVICE_REF_EDEFAULT;

	/**
	 * The default value of the '{@link #isSupportsForking() <em>Supports Forking</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSupportsForking()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SUPPORTS_FORKING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSupportsForking() <em>Supports Forking</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSupportsForking()
	 * @generated
	 * @ordered
	 */
	protected boolean supportsForking = SUPPORTS_FORKING_EDEFAULT;

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
		return ScenarioServicePackage.Literals.SCENARIO_SERVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_SERVICE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IScenarioService getServiceRef() {
		return serviceRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceRef(IScenarioService newServiceRef) {
		IScenarioService oldServiceRef = serviceRef;
		serviceRef = newServiceRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_SERVICE__SERVICE_REF, oldServiceRef, serviceRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSupportsForking() {
		return supportsForking;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSupportsForking(boolean newSupportsForking) {
		boolean oldSupportsForking = supportsForking;
		supportsForking = newSupportsForking;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_SERVICE__SUPPORTS_FORKING, oldSupportsForking, supportsForking));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ScenarioServicePackage.SCENARIO_SERVICE__DESCRIPTION:
			return getDescription();
		case ScenarioServicePackage.SCENARIO_SERVICE__SERVICE_REF:
			return getServiceRef();
		case ScenarioServicePackage.SCENARIO_SERVICE__SUPPORTS_FORKING:
			return isSupportsForking();
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
		case ScenarioServicePackage.SCENARIO_SERVICE__DESCRIPTION:
			setDescription((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_SERVICE__SERVICE_REF:
			setServiceRef((IScenarioService) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_SERVICE__SUPPORTS_FORKING:
			setSupportsForking((Boolean) newValue);
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
		case ScenarioServicePackage.SCENARIO_SERVICE__DESCRIPTION:
			setDescription(DESCRIPTION_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_SERVICE__SERVICE_REF:
			setServiceRef(SERVICE_REF_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_SERVICE__SUPPORTS_FORKING:
			setSupportsForking(SUPPORTS_FORKING_EDEFAULT);
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
		case ScenarioServicePackage.SCENARIO_SERVICE__DESCRIPTION:
			return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
		case ScenarioServicePackage.SCENARIO_SERVICE__SERVICE_REF:
			return SERVICE_REF_EDEFAULT == null ? serviceRef != null : !SERVICE_REF_EDEFAULT.equals(serviceRef);
		case ScenarioServicePackage.SCENARIO_SERVICE__SUPPORTS_FORKING:
			return supportsForking != SUPPORTS_FORKING_EDEFAULT;
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (description: ");
		result.append(description);
		result.append(", serviceRef: ");
		result.append(serviceRef);
		result.append(", supportsForking: ");
		result.append(supportsForking);
		result.append(')');
		return result.toString();
	}

} //ScenarioServiceImpl
