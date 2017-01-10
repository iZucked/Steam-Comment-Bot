/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectReferencerCondition;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselType;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.AVesselSetImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Vessel Type Group</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselTypeGroupImpl#getVesselType <em>Vessel Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselTypeGroupImpl extends AVesselSetImpl<Vessel> implements VesselTypeGroup {
	/**
	 * The default value of the '{@link #getVesselType() <em>Vessel Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVesselType()
	 * @generated
	 * @ordered
	 */
	protected static final VesselType VESSEL_TYPE_EDEFAULT = VesselType.OWNED;

	/**
	 * The cached value of the '{@link #getVesselType() <em>Vessel Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVesselType()
	 * @generated
	 * @ordered
	 */
	protected VesselType vesselType = VESSEL_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VesselTypeGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.VESSEL_TYPE_GROUP;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VesselType getVesselType() {
		return vesselType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVesselType(VesselType newVesselType) {
		VesselType oldVesselType = vesselType;
		vesselType = newVesselType == null ? VESSEL_TYPE_EDEFAULT : newVesselType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE, oldVesselType, vesselType));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case CargoPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
			return getVesselType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case CargoPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
			setVesselType((VesselType) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case CargoPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
			setVesselType(VESSEL_TYPE_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case CargoPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
			return vesselType != VESSEL_TYPE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (vesselType: ");
		result.append(vesselType);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public EList<Vessel> collect(EList<AVesselSet<Vessel>> marked) {
		if (marked.contains(this)) {
			return ECollections.emptyEList();
		}
		final UniqueEList<Vessel> result = new UniqueEList<Vessel>();
		marked.add(this);

		final EObject parent = eContainer();
		if (parent instanceof FleetModel) {
			for (final Vessel v : ((FleetModel) parent).getVessels()) {
				// We query the resource as we cannot reference LNGScenarioModel and LNGPortfolioModel here directly without creating circular references.
				// TODO: This could get slow as the size of the resource set increases. Look into EMF Query API to be able to make this faster. Currently we find *all* references to a single vessel at
				// a time

				final SELECT select = new SELECT(new FROM(this.eResource().getContents()), new WHERE(new EObjectReferencerCondition(v)));
				final IQueryResult execute = select.execute();
				for (final EObject eObj : execute) {
					if (eObj instanceof VesselAvailability) {
						final VesselAvailability va = (VesselAvailability) eObj;
						if ((va.isFleet() && (getVesselType() == VesselType.OWNED)) //
								|| (!va.isFleet() && (getVesselType() == VesselType.TIME_CHARTERED))) {
							result.addAll(va.getVessel().collect(marked));
						}
					}
				}

			}
		}
		return result;
	}
} // end of VesselTypeGroupImpl

// finish type fixing
