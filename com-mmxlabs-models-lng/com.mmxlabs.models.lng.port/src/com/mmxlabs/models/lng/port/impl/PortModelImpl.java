/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ManualRouteLine;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
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
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPortGroups <em>Port Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getRoutes <em>Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getSpecialPortGroups <em>Special Port Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPortCountryGroups <em>Port Country Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getContingencyMatrix <em>Contingency Matrix</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPortVersionRecord <em>Port Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPortGroupVersionRecord <em>Port Group Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getDistanceVersionRecord <em>Distance Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getMmxDistanceVersion <em>Mmx Distance Version</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getManualDistances <em>Manual Distances</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortModelImpl extends UUIDObjectImpl implements PortModel {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> ports;

	/**
	 * The cached value of the '{@link #getPortGroups() <em>Port Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<PortGroup> portGroups;

	/**
	 * The cached value of the '{@link #getRoutes() <em>Routes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<Route> routes;

	/**
	 * The cached value of the '{@link #getSpecialPortGroups() <em>Special Port Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecialPortGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<CapabilityGroup> specialPortGroups;

	/**
	 * The cached value of the '{@link #getPortCountryGroups() <em>Port Country Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCountryGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCountryGroup> portCountryGroups;

	/**
	 * The cached value of the '{@link #getContingencyMatrix() <em>Contingency Matrix</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContingencyMatrix()
	 * @generated
	 * @ordered
	 */
	protected ContingencyMatrix contingencyMatrix;

	/**
	 * The cached value of the '{@link #getPortVersionRecord() <em>Port Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortVersionRecord()
	 * @generated
	 * @ordered
	 */
	protected VersionRecord portVersionRecord;

	/**
	 * The cached value of the '{@link #getPortGroupVersionRecord() <em>Port Group Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortGroupVersionRecord()
	 * @generated
	 * @ordered
	 */
	protected VersionRecord portGroupVersionRecord;

	/**
	 * The cached value of the '{@link #getDistanceVersionRecord() <em>Distance Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistanceVersionRecord()
	 * @generated
	 * @ordered
	 */
	protected VersionRecord distanceVersionRecord;

	/**
	 * The default value of the '{@link #getMmxDistanceVersion() <em>Mmx Distance Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmxDistanceVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String MMX_DISTANCE_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMmxDistanceVersion() <em>Mmx Distance Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmxDistanceVersion()
	 * @generated
	 * @ordered
	 */
	protected String mmxDistanceVersion = MMX_DISTANCE_VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getManualDistances() <em>Manual Distances</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManualDistances()
	 * @generated
	 * @ordered
	 */
	protected EList<ManualRouteLine> manualDistances;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Port> getPorts() {
		if (ports == null) {
			ports = new EObjectContainmentEList<Port>(Port.class, this, PortPackage.PORT_MODEL__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PortGroup> getPortGroups() {
		if (portGroups == null) {
			portGroups = new EObjectContainmentEList<PortGroup>(PortGroup.class, this, PortPackage.PORT_MODEL__PORT_GROUPS);
		}
		return portGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Route> getRoutes() {
		if (routes == null) {
			routes = new EObjectContainmentEList<Route>(Route.class, this, PortPackage.PORT_MODEL__ROUTES);
		}
		return routes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CapabilityGroup> getSpecialPortGroups() {
		if (specialPortGroups == null) {
			specialPortGroups = new EObjectContainmentEList<CapabilityGroup>(CapabilityGroup.class, this, PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS);
		}
		return specialPortGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PortCountryGroup> getPortCountryGroups() {
		if (portCountryGroups == null) {
			portCountryGroups = new EObjectContainmentEList<PortCountryGroup>(PortCountryGroup.class, this, PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS);
		}
		return portCountryGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContingencyMatrix getContingencyMatrix() {
		return contingencyMatrix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContingencyMatrix(ContingencyMatrix newContingencyMatrix, NotificationChain msgs) {
		ContingencyMatrix oldContingencyMatrix = contingencyMatrix;
		contingencyMatrix = newContingencyMatrix;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__CONTINGENCY_MATRIX, oldContingencyMatrix, newContingencyMatrix);
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
	public void setContingencyMatrix(ContingencyMatrix newContingencyMatrix) {
		if (newContingencyMatrix != contingencyMatrix) {
			NotificationChain msgs = null;
			if (contingencyMatrix != null)
				msgs = ((InternalEObject)contingencyMatrix).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__CONTINGENCY_MATRIX, null, msgs);
			if (newContingencyMatrix != null)
				msgs = ((InternalEObject)newContingencyMatrix).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__CONTINGENCY_MATRIX, null, msgs);
			msgs = basicSetContingencyMatrix(newContingencyMatrix, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__CONTINGENCY_MATRIX, newContingencyMatrix, newContingencyMatrix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VersionRecord getPortVersionRecord() {
		return portVersionRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPortVersionRecord(VersionRecord newPortVersionRecord, NotificationChain msgs) {
		VersionRecord oldPortVersionRecord = portVersionRecord;
		portVersionRecord = newPortVersionRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__PORT_VERSION_RECORD, oldPortVersionRecord, newPortVersionRecord);
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
	public void setPortVersionRecord(VersionRecord newPortVersionRecord) {
		if (newPortVersionRecord != portVersionRecord) {
			NotificationChain msgs = null;
			if (portVersionRecord != null)
				msgs = ((InternalEObject)portVersionRecord).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__PORT_VERSION_RECORD, null, msgs);
			if (newPortVersionRecord != null)
				msgs = ((InternalEObject)newPortVersionRecord).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__PORT_VERSION_RECORD, null, msgs);
			msgs = basicSetPortVersionRecord(newPortVersionRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__PORT_VERSION_RECORD, newPortVersionRecord, newPortVersionRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VersionRecord getPortGroupVersionRecord() {
		return portGroupVersionRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPortGroupVersionRecord(VersionRecord newPortGroupVersionRecord, NotificationChain msgs) {
		VersionRecord oldPortGroupVersionRecord = portGroupVersionRecord;
		portGroupVersionRecord = newPortGroupVersionRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD, oldPortGroupVersionRecord, newPortGroupVersionRecord);
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
	public void setPortGroupVersionRecord(VersionRecord newPortGroupVersionRecord) {
		if (newPortGroupVersionRecord != portGroupVersionRecord) {
			NotificationChain msgs = null;
			if (portGroupVersionRecord != null)
				msgs = ((InternalEObject)portGroupVersionRecord).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD, null, msgs);
			if (newPortGroupVersionRecord != null)
				msgs = ((InternalEObject)newPortGroupVersionRecord).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD, null, msgs);
			msgs = basicSetPortGroupVersionRecord(newPortGroupVersionRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD, newPortGroupVersionRecord, newPortGroupVersionRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VersionRecord getDistanceVersionRecord() {
		return distanceVersionRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDistanceVersionRecord(VersionRecord newDistanceVersionRecord, NotificationChain msgs) {
		VersionRecord oldDistanceVersionRecord = distanceVersionRecord;
		distanceVersionRecord = newDistanceVersionRecord;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD, oldDistanceVersionRecord, newDistanceVersionRecord);
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
	public void setDistanceVersionRecord(VersionRecord newDistanceVersionRecord) {
		if (newDistanceVersionRecord != distanceVersionRecord) {
			NotificationChain msgs = null;
			if (distanceVersionRecord != null)
				msgs = ((InternalEObject)distanceVersionRecord).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD, null, msgs);
			if (newDistanceVersionRecord != null)
				msgs = ((InternalEObject)newDistanceVersionRecord).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD, null, msgs);
			msgs = basicSetDistanceVersionRecord(newDistanceVersionRecord, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD, newDistanceVersionRecord, newDistanceVersionRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMmxDistanceVersion() {
		return mmxDistanceVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMmxDistanceVersion(String newMmxDistanceVersion) {
		String oldMmxDistanceVersion = mmxDistanceVersion;
		mmxDistanceVersion = newMmxDistanceVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT_MODEL__MMX_DISTANCE_VERSION, oldMmxDistanceVersion, mmxDistanceVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ManualRouteLine> getManualDistances() {
		if (manualDistances == null) {
			manualDistances = new EObjectContainmentEList<ManualRouteLine>(ManualRouteLine.class, this, PortPackage.PORT_MODEL__MANUAL_DISTANCES);
		}
		return manualDistances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.PORT_MODEL__PORTS:
				return ((InternalEList<?>)getPorts()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				return ((InternalEList<?>)getPortGroups()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__ROUTES:
				return ((InternalEList<?>)getRoutes()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				return ((InternalEList<?>)getSpecialPortGroups()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				return ((InternalEList<?>)getPortCountryGroups()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__CONTINGENCY_MATRIX:
				return basicSetContingencyMatrix(null, msgs);
			case PortPackage.PORT_MODEL__PORT_VERSION_RECORD:
				return basicSetPortVersionRecord(null, msgs);
			case PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD:
				return basicSetPortGroupVersionRecord(null, msgs);
			case PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD:
				return basicSetDistanceVersionRecord(null, msgs);
			case PortPackage.PORT_MODEL__MANUAL_DISTANCES:
				return ((InternalEList<?>)getManualDistances()).basicRemove(otherEnd, msgs);
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
			case PortPackage.PORT_MODEL__PORTS:
				return getPorts();
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				return getPortGroups();
			case PortPackage.PORT_MODEL__ROUTES:
				return getRoutes();
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				return getSpecialPortGroups();
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				return getPortCountryGroups();
			case PortPackage.PORT_MODEL__CONTINGENCY_MATRIX:
				return getContingencyMatrix();
			case PortPackage.PORT_MODEL__PORT_VERSION_RECORD:
				return getPortVersionRecord();
			case PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD:
				return getPortGroupVersionRecord();
			case PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD:
				return getDistanceVersionRecord();
			case PortPackage.PORT_MODEL__MMX_DISTANCE_VERSION:
				return getMmxDistanceVersion();
			case PortPackage.PORT_MODEL__MANUAL_DISTANCES:
				return getManualDistances();
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
			case PortPackage.PORT_MODEL__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends Port>)newValue);
				return;
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				getPortGroups().clear();
				getPortGroups().addAll((Collection<? extends PortGroup>)newValue);
				return;
			case PortPackage.PORT_MODEL__ROUTES:
				getRoutes().clear();
				getRoutes().addAll((Collection<? extends Route>)newValue);
				return;
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				getSpecialPortGroups().clear();
				getSpecialPortGroups().addAll((Collection<? extends CapabilityGroup>)newValue);
				return;
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				getPortCountryGroups().clear();
				getPortCountryGroups().addAll((Collection<? extends PortCountryGroup>)newValue);
				return;
			case PortPackage.PORT_MODEL__CONTINGENCY_MATRIX:
				setContingencyMatrix((ContingencyMatrix)newValue);
				return;
			case PortPackage.PORT_MODEL__PORT_VERSION_RECORD:
				setPortVersionRecord((VersionRecord)newValue);
				return;
			case PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD:
				setPortGroupVersionRecord((VersionRecord)newValue);
				return;
			case PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD:
				setDistanceVersionRecord((VersionRecord)newValue);
				return;
			case PortPackage.PORT_MODEL__MMX_DISTANCE_VERSION:
				setMmxDistanceVersion((String)newValue);
				return;
			case PortPackage.PORT_MODEL__MANUAL_DISTANCES:
				getManualDistances().clear();
				getManualDistances().addAll((Collection<? extends ManualRouteLine>)newValue);
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
			case PortPackage.PORT_MODEL__PORTS:
				getPorts().clear();
				return;
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				getPortGroups().clear();
				return;
			case PortPackage.PORT_MODEL__ROUTES:
				getRoutes().clear();
				return;
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				getSpecialPortGroups().clear();
				return;
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				getPortCountryGroups().clear();
				return;
			case PortPackage.PORT_MODEL__CONTINGENCY_MATRIX:
				setContingencyMatrix((ContingencyMatrix)null);
				return;
			case PortPackage.PORT_MODEL__PORT_VERSION_RECORD:
				setPortVersionRecord((VersionRecord)null);
				return;
			case PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD:
				setPortGroupVersionRecord((VersionRecord)null);
				return;
			case PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD:
				setDistanceVersionRecord((VersionRecord)null);
				return;
			case PortPackage.PORT_MODEL__MMX_DISTANCE_VERSION:
				setMmxDistanceVersion(MMX_DISTANCE_VERSION_EDEFAULT);
				return;
			case PortPackage.PORT_MODEL__MANUAL_DISTANCES:
				getManualDistances().clear();
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
			case PortPackage.PORT_MODEL__PORTS:
				return ports != null && !ports.isEmpty();
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				return portGroups != null && !portGroups.isEmpty();
			case PortPackage.PORT_MODEL__ROUTES:
				return routes != null && !routes.isEmpty();
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				return specialPortGroups != null && !specialPortGroups.isEmpty();
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				return portCountryGroups != null && !portCountryGroups.isEmpty();
			case PortPackage.PORT_MODEL__CONTINGENCY_MATRIX:
				return contingencyMatrix != null;
			case PortPackage.PORT_MODEL__PORT_VERSION_RECORD:
				return portVersionRecord != null;
			case PortPackage.PORT_MODEL__PORT_GROUP_VERSION_RECORD:
				return portGroupVersionRecord != null;
			case PortPackage.PORT_MODEL__DISTANCE_VERSION_RECORD:
				return distanceVersionRecord != null;
			case PortPackage.PORT_MODEL__MMX_DISTANCE_VERSION:
				return MMX_DISTANCE_VERSION_EDEFAULT == null ? mmxDistanceVersion != null : !MMX_DISTANCE_VERSION_EDEFAULT.equals(mmxDistanceVersion);
			case PortPackage.PORT_MODEL__MANUAL_DISTANCES:
				return manualDistances != null && !manualDistances.isEmpty();
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
		result.append(" (mmxDistanceVersion: ");
		result.append(mmxDistanceVersion);
		result.append(')');
		return result.toString();
	}

} //PortModelImpl
