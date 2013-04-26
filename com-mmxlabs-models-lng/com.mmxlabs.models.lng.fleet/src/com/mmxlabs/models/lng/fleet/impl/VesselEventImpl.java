/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getOtherNames <em>Other Names</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getDurationInDays <em>Duration In Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getStartBy <em>Start By</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class VesselEventImpl extends UUIDObjectImpl implements VesselEvent {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOtherNames() <em>Other Names</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getOtherNames()
	 * @generated
	 * @ordered
	 */
	protected EList<String> otherNames;

	/**
	 * The default value of the '{@link #getDurationInDays() <em>Duration In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurationInDays()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_IN_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDurationInDays() <em>Duration In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurationInDays()
	 * @generated
	 * @ordered
	 */
	protected int durationInDays = DURATION_IN_DAYS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAllowedVessels() <em>Allowed Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet<Vessel>> allowedVessels;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The default value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected Date startAfter = START_AFTER_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartBy() <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBy()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartBy() <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBy()
	 * @generated
	 * @ordered
	 */
	protected Date startBy = START_BY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_EVENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getOtherNames() {
		if (otherNames == null) {
			otherNames = new EDataTypeUniqueEList<String>(String.class, this, FleetPackage.VESSEL_EVENT__OTHER_NAMES);
		}
		return otherNames;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDurationInDays() {
		return durationInDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDurationInDays(int newDurationInDays) {
		int oldDurationInDays = durationInDays;
		durationInDays = newDurationInDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS, oldDurationInDays, durationInDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet<Vessel>> getAllowedVessels() {
		if (allowedVessels == null) {
			allowedVessels = new EObjectResolvingEList<AVesselSet<Vessel>>(AVesselSet.class, this, FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS);
		}
		return allowedVessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL_EVENT__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_EVENT__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartAfter() {
		return startAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartAfter(Date newStartAfter) {
		Date oldStartAfter = startAfter;
		startAfter = newStartAfter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_EVENT__START_AFTER, oldStartAfter, startAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartBy() {
		return startBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartBy(Date newStartBy) {
		Date oldStartBy = startBy;
		startBy = newStartBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_EVENT__START_BY, oldStartBy, startBy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getTimeZone(EAttribute attribute) {
		if (getPort() != null) return getPort().getTimeZone();
		return "UTC";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_EVENT__NAME:
				return getName();
			case FleetPackage.VESSEL_EVENT__OTHER_NAMES:
				return getOtherNames();
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				return getDurationInDays();
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				return getAllowedVessels();
			case FleetPackage.VESSEL_EVENT__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case FleetPackage.VESSEL_EVENT__START_AFTER:
				return getStartAfter();
			case FleetPackage.VESSEL_EVENT__START_BY:
				return getStartBy();
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
			case FleetPackage.VESSEL_EVENT__NAME:
				setName((String)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__OTHER_NAMES:
				getOtherNames().clear();
				getOtherNames().addAll((Collection<? extends String>)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				setDurationInDays((Integer)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends AVesselSet<Vessel>>)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__PORT:
				setPort((Port)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__START_AFTER:
				setStartAfter((Date)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__START_BY:
				setStartBy((Date)newValue);
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
			case FleetPackage.VESSEL_EVENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_EVENT__OTHER_NAMES:
				getOtherNames().clear();
				return;
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				setDurationInDays(DURATION_IN_DAYS_EDEFAULT);
				return;
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				return;
			case FleetPackage.VESSEL_EVENT__PORT:
				setPort((Port)null);
				return;
			case FleetPackage.VESSEL_EVENT__START_AFTER:
				setStartAfter(START_AFTER_EDEFAULT);
				return;
			case FleetPackage.VESSEL_EVENT__START_BY:
				setStartBy(START_BY_EDEFAULT);
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
			case FleetPackage.VESSEL_EVENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FleetPackage.VESSEL_EVENT__OTHER_NAMES:
				return otherNames != null && !otherNames.isEmpty();
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				return durationInDays != DURATION_IN_DAYS_EDEFAULT;
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				return allowedVessels != null && !allowedVessels.isEmpty();
			case FleetPackage.VESSEL_EVENT__PORT:
				return port != null;
			case FleetPackage.VESSEL_EVENT__START_AFTER:
				return START_AFTER_EDEFAULT == null ? startAfter != null : !START_AFTER_EDEFAULT.equals(startAfter);
			case FleetPackage.VESSEL_EVENT__START_BY:
				return START_BY_EDEFAULT == null ? startBy != null : !START_BY_EDEFAULT.equals(startBy);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case FleetPackage.VESSEL_EVENT__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				case FleetPackage.VESSEL_EVENT__OTHER_NAMES: return MMXCorePackage.NAMED_OBJECT__OTHER_NAMES;
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return FleetPackage.VESSEL_EVENT__NAME;
				case MMXCorePackage.NAMED_OBJECT__OTHER_NAMES: return FleetPackage.VESSEL_EVENT__OTHER_NAMES;
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", otherNames: ");
		result.append(otherNames);
		result.append(", durationInDays: ");
		result.append(durationInDays);
		result.append(", startAfter: ");
		result.append(startAfter);
		result.append(", startBy: ");
		result.append(startBy);
		result.append(')');
		return result.toString();
	}

} // end of VesselEventImpl

// finish type fixing
