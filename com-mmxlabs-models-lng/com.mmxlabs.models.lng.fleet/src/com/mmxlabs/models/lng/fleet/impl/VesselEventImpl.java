

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet.impl;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselEvent;

import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.impl.AVesselEventImpl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getDurationInDays <em>Duration In Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getEarliestStartDate <em>Earliest Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl#getLatestStartDate <em>Latest Start Date</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class VesselEventImpl extends AVesselEventImpl implements VesselEvent {
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
	protected EList<AVesselSet> allowedVessels;

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
	 * The default value of the '{@link #getEarliestStartDate() <em>Earliest Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date EARLIEST_START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEarliestStartDate() <em>Earliest Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestStartDate()
	 * @generated
	 * @ordered
	 */
	protected Date earliestStartDate = EARLIEST_START_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLatestStartDate() <em>Latest Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date LATEST_START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLatestStartDate() <em>Latest Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestStartDate()
	 * @generated
	 * @ordered
	 */
	protected Date latestStartDate = LATEST_START_DATE_EDEFAULT;

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
	public EList<AVesselSet> getAllowedVessels() {
		if (allowedVessels == null) {
			allowedVessels = new EObjectResolvingEList<AVesselSet>(AVesselSet.class, this, FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS);
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
	public Date getEarliestStartDate() {
		return earliestStartDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEarliestStartDate(Date newEarliestStartDate) {
		Date oldEarliestStartDate = earliestStartDate;
		earliestStartDate = newEarliestStartDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_EVENT__EARLIEST_START_DATE, oldEarliestStartDate, earliestStartDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLatestStartDate() {
		return latestStartDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLatestStartDate(Date newLatestStartDate) {
		Date oldLatestStartDate = latestStartDate;
		latestStartDate = newLatestStartDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_EVENT__LATEST_START_DATE, oldLatestStartDate, latestStartDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				return getDurationInDays();
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				return getAllowedVessels();
			case FleetPackage.VESSEL_EVENT__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case FleetPackage.VESSEL_EVENT__EARLIEST_START_DATE:
				return getEarliestStartDate();
			case FleetPackage.VESSEL_EVENT__LATEST_START_DATE:
				return getLatestStartDate();
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
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				setDurationInDays((Integer)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends AVesselSet>)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__PORT:
				setPort((Port)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__EARLIEST_START_DATE:
				setEarliestStartDate((Date)newValue);
				return;
			case FleetPackage.VESSEL_EVENT__LATEST_START_DATE:
				setLatestStartDate((Date)newValue);
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
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				setDurationInDays(DURATION_IN_DAYS_EDEFAULT);
				return;
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				return;
			case FleetPackage.VESSEL_EVENT__PORT:
				setPort((Port)null);
				return;
			case FleetPackage.VESSEL_EVENT__EARLIEST_START_DATE:
				setEarliestStartDate(EARLIEST_START_DATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_EVENT__LATEST_START_DATE:
				setLatestStartDate(LATEST_START_DATE_EDEFAULT);
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
			case FleetPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				return durationInDays != DURATION_IN_DAYS_EDEFAULT;
			case FleetPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				return allowedVessels != null && !allowedVessels.isEmpty();
			case FleetPackage.VESSEL_EVENT__PORT:
				return port != null;
			case FleetPackage.VESSEL_EVENT__EARLIEST_START_DATE:
				return EARLIEST_START_DATE_EDEFAULT == null ? earliestStartDate != null : !EARLIEST_START_DATE_EDEFAULT.equals(earliestStartDate);
			case FleetPackage.VESSEL_EVENT__LATEST_START_DATE:
				return LATEST_START_DATE_EDEFAULT == null ? latestStartDate != null : !LATEST_START_DATE_EDEFAULT.equals(latestStartDate);
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
		result.append(" (durationInDays: ");
		result.append(durationInDays);
		result.append(", earliestStartDate: ");
		result.append(earliestStartDate);
		result.append(", latestStartDate: ");
		result.append(latestStartDate);
		result.append(')');
		return result.toString();
	}

} // end of VesselEventImpl

// finish type fixing
