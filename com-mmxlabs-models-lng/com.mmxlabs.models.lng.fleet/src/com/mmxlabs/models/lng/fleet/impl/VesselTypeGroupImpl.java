

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselType;
import com.mmxlabs.models.lng.fleet.VesselTypeGroup;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.AVesselSetImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Type Group</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselTypeGroupImpl#getVesselType <em>Vessel Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselTypeGroupImpl extends AVesselSetImpl implements VesselTypeGroup {
	/**
	 * The default value of the '{@link #getVesselType() <em>Vessel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselType()
	 * @generated
	 * @ordered
	 */
	protected static final VesselType VESSEL_TYPE_EDEFAULT = VesselType.OWNED;

	/**
	 * The cached value of the '{@link #getVesselType() <em>Vessel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselType()
	 * @generated
	 * @ordered
	 */
	protected VesselType vesselType = VESSEL_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselTypeGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_TYPE_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselType getVesselType() {
		return vesselType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselType(VesselType newVesselType) {
		VesselType oldVesselType = vesselType;
		vesselType = newVesselType == null ? VESSEL_TYPE_EDEFAULT : newVesselType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE, oldVesselType, vesselType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
				return getVesselType();
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
			case FleetPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
				setVesselType((VesselType)newValue);
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
			case FleetPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
				setVesselType(VESSEL_TYPE_EDEFAULT);
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
			case FleetPackage.VESSEL_TYPE_GROUP__VESSEL_TYPE:
				return vesselType != VESSEL_TYPE_EDEFAULT;
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
		result.append(" (vesselType: ");
		result.append(vesselType);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated NO
	 */
	@Override
	public EList<AVessel> collect(EList<AVesselSet> marked) {
		if (marked.contains(this)) return org.eclipse.emf.common.util.ECollections.emptyEList();
		final org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel> result = new org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel>();
		marked.add(this);
		final EObject parent = eContainer();
		if (parent instanceof FleetModel) {
			for (final Vessel v : ((FleetModel) parent).getVessels()) {
				// NOTE: This works while there are only two VesselTypes
				if (v.isSetTimeCharterRate() ^ getVesselType() == VesselType.OWNED) {
					result.addAll(v.collect(marked));
				}
			}
		}
		return result;
	}
	
} // end of VesselTypeGroupImpl

// finish type fixing
