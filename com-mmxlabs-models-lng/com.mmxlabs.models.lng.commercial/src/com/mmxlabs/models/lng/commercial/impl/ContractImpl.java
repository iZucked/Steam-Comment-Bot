/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LegalEntity;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.impl.AContractImpl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getAllowedPorts <em>Allowed Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPreferredPort <em>Preferred Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContractImpl extends AContractImpl implements Contract {
	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected LegalEntity entity;

	/**
	 * The cached value of the '{@link #getAllowedPorts() <em>Allowed Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> allowedPorts;
	/**
	 * The cached value of the '{@link #getPreferredPort() <em>Preferred Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreferredPort()
	 * @generated
	 * @ordered
	 */
	protected Port preferredPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (LegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.CONTRACT__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegalEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(LegalEntity newEntity) {
		LegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getAllowedPorts() {
		if (allowedPorts == null) {
			allowedPorts = new EObjectResolvingEList<APortSet>(APortSet.class, this, CommercialPackage.CONTRACT__ALLOWED_PORTS);
		}
		return allowedPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getPreferredPort() {
		if (preferredPort != null && preferredPort.eIsProxy()) {
			InternalEObject oldPreferredPort = (InternalEObject)preferredPort;
			preferredPort = (Port)eResolveProxy(oldPreferredPort);
			if (preferredPort != oldPreferredPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.CONTRACT__PREFERRED_PORT, oldPreferredPort, preferredPort));
			}
		}
		return preferredPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPreferredPort() {
		return preferredPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPreferredPort(Port newPreferredPort) {
		Port oldPreferredPort = preferredPort;
		preferredPort = newPreferredPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PREFERRED_PORT, oldPreferredPort, preferredPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.CONTRACT__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				return getAllowedPorts();
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				if (resolve) return getPreferredPort();
				return basicGetPreferredPort();
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
			case CommercialPackage.CONTRACT__ENTITY:
				setEntity((LegalEntity)newValue);
				return;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				getAllowedPorts().clear();
				getAllowedPorts().addAll((Collection<? extends APortSet>)newValue);
				return;
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				setPreferredPort((Port)newValue);
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
			case CommercialPackage.CONTRACT__ENTITY:
				setEntity((LegalEntity)null);
				return;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				getAllowedPorts().clear();
				return;
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				setPreferredPort((Port)null);
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
			case CommercialPackage.CONTRACT__ENTITY:
				return entity != null;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				return allowedPorts != null && !allowedPorts.isEmpty();
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				return preferredPort != null;
		}
		return super.eIsSet(featureID);
	}

} // end of ContractImpl

// finish type fixing
