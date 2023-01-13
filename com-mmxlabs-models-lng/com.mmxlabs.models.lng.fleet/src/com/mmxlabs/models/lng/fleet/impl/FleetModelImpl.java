/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getBaseFuels <em>Base Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVesselGroups <em>Vessel Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getFleetVersionRecord <em>Fleet Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVesselGroupVersionRecord <em>Vessel Group Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getBunkerFuelsVersionRecord <em>Bunker Fuels Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getMMXVesselDBVersion <em>MMX Vessel DB Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FleetModelImpl extends UUIDObjectImpl implements FleetModel {
	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<Vessel> vessels;

	/**
	 * The cached value of the '{@link #getBaseFuels() <em>Base Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseFuel> baseFuels;

	/**
	 * The cached value of the '{@link #getVesselGroups() <em>Vessel Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselGroup> vesselGroups;

	/**
	 * The cached value of the '{@link #getFleetVersionRecord() <em>Fleet Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleetVersionRecord()
	 * @generated
	 * @ordered
	 */
	protected VersionRecord fleetVersionRecord;

	/**
	 * The cached value of the '{@link #getVesselGroupVersionRecord() <em>Vessel Group Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselGroupVersionRecord()
	 * @generated
	 * @ordered
	 */
	protected VersionRecord vesselGroupVersionRecord;

	/**
	 * The cached value of the '{@link #getBunkerFuelsVersionRecord() <em>Bunker Fuels Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBunkerFuelsVersionRecord()
	 * @generated
	 * @ordered
	 */
	protected VersionRecord bunkerFuelsVersionRecord;

	/**
	 * The default value of the '{@link #getMMXVesselDBVersion() <em>MMX Vessel DB Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMMXVesselDBVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String MMX_VESSEL_DB_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMMXVesselDBVersion() <em>MMX Vessel DB Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMMXVesselDBVersion()
	 * @generated
	 * @ordered
	 */
	protected String mmxVesselDBVersion = MMX_VESSEL_DB_VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.FLEET_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Vessel> getVessels() {
		if (vessels == null) {
			vessels = new EObjectContainmentEList<Vessel>(Vessel.class, this, FleetPackage.FLEET_MODEL__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BaseFuel> getBaseFuels() {
		if (baseFuels == null) {
			baseFuels = new EObjectContainmentEList<BaseFuel>(BaseFuel.class, this, FleetPackage.FLEET_MODEL__BASE_FUELS);
		}
		return baseFuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselGroup> getVesselGroups() {
		if (vesselGroups == null) {
			vesselGroups = new EObjectContainmentEList<VesselGroup>(VesselGroup.class, this, FleetPackage.FLEET_MODEL__VESSEL_GROUPS);
		}
		return vesselGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VersionRecord getFleetVersionRecord() {
		return fleetVersionRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFleetVersionRecord(VersionRecord newFleetVersionRecord, NotificationChain msgs) {
		VersionRecord oldFleetVersionRecord = fleetVersionRecord;
		fleetVersionRecord = newFleetVersionRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD, oldFleetVersionRecord, newFleetVersionRecord);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFleetVersionRecord(VersionRecord newFleetVersionRecord) {
		if (newFleetVersionRecord != fleetVersionRecord) {
			NotificationChain msgs = null;
			if (fleetVersionRecord != null)
				msgs = ((InternalEObject)fleetVersionRecord).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD, null, msgs);
			if (newFleetVersionRecord != null)
				msgs = ((InternalEObject)newFleetVersionRecord).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD, null, msgs);
			msgs = basicSetFleetVersionRecord(newFleetVersionRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD, newFleetVersionRecord, newFleetVersionRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VersionRecord getVesselGroupVersionRecord() {
		return vesselGroupVersionRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetVesselGroupVersionRecord(VersionRecord newVesselGroupVersionRecord, NotificationChain msgs) {
		VersionRecord oldVesselGroupVersionRecord = vesselGroupVersionRecord;
		vesselGroupVersionRecord = newVesselGroupVersionRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD, oldVesselGroupVersionRecord, newVesselGroupVersionRecord);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselGroupVersionRecord(VersionRecord newVesselGroupVersionRecord) {
		if (newVesselGroupVersionRecord != vesselGroupVersionRecord) {
			NotificationChain msgs = null;
			if (vesselGroupVersionRecord != null)
				msgs = ((InternalEObject)vesselGroupVersionRecord).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD, null, msgs);
			if (newVesselGroupVersionRecord != null)
				msgs = ((InternalEObject)newVesselGroupVersionRecord).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD, null, msgs);
			msgs = basicSetVesselGroupVersionRecord(newVesselGroupVersionRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD, newVesselGroupVersionRecord, newVesselGroupVersionRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VersionRecord getBunkerFuelsVersionRecord() {
		return bunkerFuelsVersionRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBunkerFuelsVersionRecord(VersionRecord newBunkerFuelsVersionRecord, NotificationChain msgs) {
		VersionRecord oldBunkerFuelsVersionRecord = bunkerFuelsVersionRecord;
		bunkerFuelsVersionRecord = newBunkerFuelsVersionRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD, oldBunkerFuelsVersionRecord, newBunkerFuelsVersionRecord);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBunkerFuelsVersionRecord(VersionRecord newBunkerFuelsVersionRecord) {
		if (newBunkerFuelsVersionRecord != bunkerFuelsVersionRecord) {
			NotificationChain msgs = null;
			if (bunkerFuelsVersionRecord != null)
				msgs = ((InternalEObject)bunkerFuelsVersionRecord).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD, null, msgs);
			if (newBunkerFuelsVersionRecord != null)
				msgs = ((InternalEObject)newBunkerFuelsVersionRecord).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD, null, msgs);
			msgs = basicSetBunkerFuelsVersionRecord(newBunkerFuelsVersionRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD, newBunkerFuelsVersionRecord, newBunkerFuelsVersionRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMMXVesselDBVersion() {
		return mmxVesselDBVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMMXVesselDBVersion(String newMMXVesselDBVersion) {
		String oldMMXVesselDBVersion = mmxVesselDBVersion;
		mmxVesselDBVersion = newMMXVesselDBVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__MMX_VESSEL_DB_VERSION, oldMMXVesselDBVersion, mmxVesselDBVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__VESSELS:
				return ((InternalEList<?>)getVessels()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return ((InternalEList<?>)getBaseFuels()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				return ((InternalEList<?>)getVesselGroups()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD:
				return basicSetFleetVersionRecord(null, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD:
				return basicSetVesselGroupVersionRecord(null, msgs);
			case FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD:
				return basicSetBunkerFuelsVersionRecord(null, msgs);
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				return getVessels();
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return getBaseFuels();
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				return getVesselGroups();
			case FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD:
				return getFleetVersionRecord();
			case FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD:
				return getVesselGroupVersionRecord();
			case FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD:
				return getBunkerFuelsVersionRecord();
			case FleetPackage.FLEET_MODEL__MMX_VESSEL_DB_VERSION:
				return getMMXVesselDBVersion();
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends Vessel>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				getBaseFuels().clear();
				getBaseFuels().addAll((Collection<? extends BaseFuel>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				getVesselGroups().clear();
				getVesselGroups().addAll((Collection<? extends VesselGroup>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD:
				setFleetVersionRecord((VersionRecord)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD:
				setVesselGroupVersionRecord((VersionRecord)newValue);
				return;
			case FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD:
				setBunkerFuelsVersionRecord((VersionRecord)newValue);
				return;
			case FleetPackage.FLEET_MODEL__MMX_VESSEL_DB_VERSION:
				setMMXVesselDBVersion((String)newValue);
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				getVessels().clear();
				return;
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				getBaseFuels().clear();
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				getVesselGroups().clear();
				return;
			case FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD:
				setFleetVersionRecord((VersionRecord)null);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD:
				setVesselGroupVersionRecord((VersionRecord)null);
				return;
			case FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD:
				setBunkerFuelsVersionRecord((VersionRecord)null);
				return;
			case FleetPackage.FLEET_MODEL__MMX_VESSEL_DB_VERSION:
				setMMXVesselDBVersion(MMX_VESSEL_DB_VERSION_EDEFAULT);
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return baseFuels != null && !baseFuels.isEmpty();
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				return vesselGroups != null && !vesselGroups.isEmpty();
			case FleetPackage.FLEET_MODEL__FLEET_VERSION_RECORD:
				return fleetVersionRecord != null;
			case FleetPackage.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD:
				return vesselGroupVersionRecord != null;
			case FleetPackage.FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD:
				return bunkerFuelsVersionRecord != null;
			case FleetPackage.FLEET_MODEL__MMX_VESSEL_DB_VERSION:
				return MMX_VESSEL_DB_VERSION_EDEFAULT == null ? mmxVesselDBVersion != null : !MMX_VESSEL_DB_VERSION_EDEFAULT.equals(mmxVesselDBVersion);
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (MMXVesselDBVersion: ");
		result.append(mmxVesselDBVersion);
		result.append(')');
		return result.toString();
	}

} // end of FleetModelImpl

// finish type fixing
