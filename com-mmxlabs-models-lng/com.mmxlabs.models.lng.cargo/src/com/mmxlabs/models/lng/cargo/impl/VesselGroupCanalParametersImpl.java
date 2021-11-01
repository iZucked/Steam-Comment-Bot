/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Group Canal Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselGroupCanalParametersImpl#getVesselGroup <em>Vessel Group</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselGroupCanalParametersImpl extends NamedObjectImpl implements VesselGroupCanalParameters {
	/**
	 * The cached value of the '{@link #getVesselGroup() <em>Vessel Group</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselGroup()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet<Vessel>> vesselGroup;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselGroupCanalParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AVesselSet<Vessel>> getVesselGroup() {
		if (vesselGroup == null) {
			vesselGroup = new EObjectResolvingEList<AVesselSet<Vessel>>(AVesselSet.class, this, CargoPackage.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP);
		}
		return vesselGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP:
				return getVesselGroup();
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
			case CargoPackage.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP:
				getVesselGroup().clear();
				getVesselGroup().addAll((Collection<? extends AVesselSet<Vessel>>)newValue);
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
			case CargoPackage.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP:
				getVesselGroup().clear();
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
			case CargoPackage.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP:
				return vesselGroup != null && !vesselGroup.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //VesselGroupCanalParametersImpl
