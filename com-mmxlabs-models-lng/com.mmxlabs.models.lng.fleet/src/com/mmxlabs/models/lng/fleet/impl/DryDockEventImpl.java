/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dry Dock Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DryDockEventImpl extends VesselEventImpl implements DryDockEvent {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DryDockEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.DRY_DOCK_EVENT;
	}

	/**
	 * @since 3.0
	 */
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		return null;
	}

} // end of DryDockEventImpl

// finish type fixing
