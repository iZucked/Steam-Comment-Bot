

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet.impl;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;
import com.mmxlabs.models.lng.fleet.VesselClass;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.impl.AVesselImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getDailyHireRate <em>Daily Hire Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getInitialPort <em>Initial Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getFinalPort <em>Final Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#isTimeChartered <em>Time Chartered</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselImpl extends AVesselImpl implements Vessel {
	/**
	 * The cached value of the '{@link #getVesselClass() <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClass()
	 * @generated
	 * @ordered
	 */
	protected VesselClass vesselClass;

	/**
	 * The cached value of the '{@link #getInaccessiblePorts() <em>Inaccessible Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInaccessiblePorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> inaccessiblePorts;

	/**
	 * The default value of the '{@link #getDailyHireRate() <em>Daily Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyHireRate()
	 * @generated
	 * @ordered
	 */
	protected static final int DAILY_HIRE_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDailyHireRate() <em>Daily Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyHireRate()
	 * @generated
	 * @ordered
	 */
	protected int dailyHireRate = DAILY_HIRE_RATE_EDEFAULT;

	/**
	 * This is true if the Daily Hire Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean dailyHireRateESet;

	/**
	 * The cached value of the '{@link #getInitialPort() <em>Initial Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialPort()
	 * @generated
	 * @ordered
	 */
	protected Port initialPort;

	/**
	 * This is true if the Initial Port reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean initialPortESet;

	/**
	 * The cached value of the '{@link #getFinalPort() <em>Final Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFinalPort()
	 * @generated
	 * @ordered
	 */
	protected Port finalPort;

	/**
	 * This is true if the Final Port reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean finalPortESet;

	/**
	 * The cached value of the '{@link #getStartHeel() <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartHeel()
	 * @generated
	 * @ordered
	 */
	protected HeelOptions startHeel;

	/**
	 * The cached value of the '{@link #getAvailability() <em>Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailability()
	 * @generated
	 * @ordered
	 */
	protected VesselAvailablility availability;

	/**
	 * The default value of the '{@link #isTimeChartered() <em>Time Chartered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTimeChartered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TIME_CHARTERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTimeChartered() <em>Time Chartered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTimeChartered()
	 * @generated
	 * @ordered
	 */
	protected boolean timeChartered = TIME_CHARTERED_EDEFAULT;

	/**
	 * The default value of the '{@link #getTimeCharterRate() <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeCharterRate()
	 * @generated
	 * @ordered
	 */
	protected static final int TIME_CHARTER_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTimeCharterRate() <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeCharterRate()
	 * @generated
	 * @ordered
	 */
	protected int timeCharterRate = TIME_CHARTER_RATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass getVesselClass() {
		if (vesselClass != null && vesselClass.eIsProxy()) {
			InternalEObject oldVesselClass = (InternalEObject)vesselClass;
			vesselClass = (VesselClass)eResolveProxy(oldVesselClass);
			if (vesselClass != oldVesselClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__VESSEL_CLASS, oldVesselClass, vesselClass));
			}
		}
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetVesselClass() {
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselClass(VesselClass newVesselClass) {
		VesselClass oldVesselClass = vesselClass;
		vesselClass = newVesselClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__VESSEL_CLASS, oldVesselClass, vesselClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getInaccessiblePorts() {
		if (inaccessiblePorts == null) {
			inaccessiblePorts = new EObjectResolvingEList<APortSet>(APortSet.class, this, FleetPackage.VESSEL__INACCESSIBLE_PORTS);
		}
		return inaccessiblePorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDailyHireRate() {
		return dailyHireRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDailyHireRate(int newDailyHireRate) {
		int oldDailyHireRate = dailyHireRate;
		dailyHireRate = newDailyHireRate;
		boolean oldDailyHireRateESet = dailyHireRateESet;
		dailyHireRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__DAILY_HIRE_RATE, oldDailyHireRate, dailyHireRate, !oldDailyHireRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDailyHireRate() {
		int oldDailyHireRate = dailyHireRate;
		boolean oldDailyHireRateESet = dailyHireRateESet;
		dailyHireRate = DAILY_HIRE_RATE_EDEFAULT;
		dailyHireRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__DAILY_HIRE_RATE, oldDailyHireRate, DAILY_HIRE_RATE_EDEFAULT, oldDailyHireRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDailyHireRate() {
		return dailyHireRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getInitialPort() {
		if (initialPort != null && initialPort.eIsProxy()) {
			InternalEObject oldInitialPort = (InternalEObject)initialPort;
			initialPort = (Port)eResolveProxy(oldInitialPort);
			if (initialPort != oldInitialPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__INITIAL_PORT, oldInitialPort, initialPort));
			}
		}
		return initialPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetInitialPort() {
		return initialPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitialPort(Port newInitialPort) {
		Port oldInitialPort = initialPort;
		initialPort = newInitialPort;
		boolean oldInitialPortESet = initialPortESet;
		initialPortESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__INITIAL_PORT, oldInitialPort, initialPort, !oldInitialPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetInitialPort() {
		Port oldInitialPort = initialPort;
		boolean oldInitialPortESet = initialPortESet;
		initialPort = null;
		initialPortESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__INITIAL_PORT, oldInitialPort, null, oldInitialPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetInitialPort() {
		return initialPortESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getFinalPort() {
		if (finalPort != null && finalPort.eIsProxy()) {
			InternalEObject oldFinalPort = (InternalEObject)finalPort;
			finalPort = (Port)eResolveProxy(oldFinalPort);
			if (finalPort != oldFinalPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__FINAL_PORT, oldFinalPort, finalPort));
			}
		}
		return finalPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetFinalPort() {
		return finalPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFinalPort(Port newFinalPort) {
		Port oldFinalPort = finalPort;
		finalPort = newFinalPort;
		boolean oldFinalPortESet = finalPortESet;
		finalPortESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__FINAL_PORT, oldFinalPort, finalPort, !oldFinalPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFinalPort() {
		Port oldFinalPort = finalPort;
		boolean oldFinalPortESet = finalPortESet;
		finalPort = null;
		finalPortESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__FINAL_PORT, oldFinalPort, null, oldFinalPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFinalPort() {
		return finalPortESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HeelOptions getStartHeel() {
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartHeel(HeelOptions newStartHeel, NotificationChain msgs) {
		HeelOptions oldStartHeel = startHeel;
		startHeel = newStartHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__START_HEEL, oldStartHeel, newStartHeel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartHeel(HeelOptions newStartHeel) {
		if (newStartHeel != startHeel) {
			NotificationChain msgs = null;
			if (startHeel != null)
				msgs = ((InternalEObject)startHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_HEEL, null, msgs);
			if (newStartHeel != null)
				msgs = ((InternalEObject)newStartHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_HEEL, null, msgs);
			msgs = basicSetStartHeel(newStartHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__START_HEEL, newStartHeel, newStartHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailablility getAvailability() {
		return availability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAvailability(VesselAvailablility newAvailability, NotificationChain msgs) {
		VesselAvailablility oldAvailability = availability;
		availability = newAvailability;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__AVAILABILITY, oldAvailability, newAvailability);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvailability(VesselAvailablility newAvailability) {
		if (newAvailability != availability) {
			NotificationChain msgs = null;
			if (availability != null)
				msgs = ((InternalEObject)availability).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__AVAILABILITY, null, msgs);
			if (newAvailability != null)
				msgs = ((InternalEObject)newAvailability).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__AVAILABILITY, null, msgs);
			msgs = basicSetAvailability(newAvailability, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__AVAILABILITY, newAvailability, newAvailability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTimeChartered() {
		return timeChartered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeChartered(boolean newTimeChartered) {
		boolean oldTimeChartered = timeChartered;
		timeChartered = newTimeChartered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__TIME_CHARTERED, oldTimeChartered, timeChartered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTimeCharterRate() {
		return timeCharterRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeCharterRate(int newTimeCharterRate) {
		int oldTimeCharterRate = timeCharterRate;
		timeCharterRate = newTimeCharterRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__TIME_CHARTER_RATE, oldTimeCharterRate, timeCharterRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL__START_HEEL:
				return basicSetStartHeel(null, msgs);
			case FleetPackage.VESSEL__AVAILABILITY:
				return basicSetAvailability(null, msgs);
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
			case FleetPackage.VESSEL__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				return getInaccessiblePorts();
			case FleetPackage.VESSEL__DAILY_HIRE_RATE:
				return getDailyHireRate();
			case FleetPackage.VESSEL__INITIAL_PORT:
				if (resolve) return getInitialPort();
				return basicGetInitialPort();
			case FleetPackage.VESSEL__FINAL_PORT:
				if (resolve) return getFinalPort();
				return basicGetFinalPort();
			case FleetPackage.VESSEL__START_HEEL:
				return getStartHeel();
			case FleetPackage.VESSEL__AVAILABILITY:
				return getAvailability();
			case FleetPackage.VESSEL__TIME_CHARTERED:
				return isTimeChartered();
			case FleetPackage.VESSEL__TIME_CHARTER_RATE:
				return getTimeCharterRate();
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
			case FleetPackage.VESSEL__VESSEL_CLASS:
				setVesselClass((VesselClass)newValue);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				getInaccessiblePorts().addAll((Collection<? extends APortSet>)newValue);
				return;
			case FleetPackage.VESSEL__DAILY_HIRE_RATE:
				setDailyHireRate((Integer)newValue);
				return;
			case FleetPackage.VESSEL__INITIAL_PORT:
				setInitialPort((Port)newValue);
				return;
			case FleetPackage.VESSEL__FINAL_PORT:
				setFinalPort((Port)newValue);
				return;
			case FleetPackage.VESSEL__START_HEEL:
				setStartHeel((HeelOptions)newValue);
				return;
			case FleetPackage.VESSEL__AVAILABILITY:
				setAvailability((VesselAvailablility)newValue);
				return;
			case FleetPackage.VESSEL__TIME_CHARTERED:
				setTimeChartered((Boolean)newValue);
				return;
			case FleetPackage.VESSEL__TIME_CHARTER_RATE:
				setTimeCharterRate((Integer)newValue);
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
			case FleetPackage.VESSEL__VESSEL_CLASS:
				setVesselClass((VesselClass)null);
				return;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				return;
			case FleetPackage.VESSEL__DAILY_HIRE_RATE:
				unsetDailyHireRate();
				return;
			case FleetPackage.VESSEL__INITIAL_PORT:
				unsetInitialPort();
				return;
			case FleetPackage.VESSEL__FINAL_PORT:
				unsetFinalPort();
				return;
			case FleetPackage.VESSEL__START_HEEL:
				setStartHeel((HeelOptions)null);
				return;
			case FleetPackage.VESSEL__AVAILABILITY:
				setAvailability((VesselAvailablility)null);
				return;
			case FleetPackage.VESSEL__TIME_CHARTERED:
				setTimeChartered(TIME_CHARTERED_EDEFAULT);
				return;
			case FleetPackage.VESSEL__TIME_CHARTER_RATE:
				setTimeCharterRate(TIME_CHARTER_RATE_EDEFAULT);
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
			case FleetPackage.VESSEL__VESSEL_CLASS:
				return vesselClass != null;
			case FleetPackage.VESSEL__INACCESSIBLE_PORTS:
				return inaccessiblePorts != null && !inaccessiblePorts.isEmpty();
			case FleetPackage.VESSEL__DAILY_HIRE_RATE:
				return isSetDailyHireRate();
			case FleetPackage.VESSEL__INITIAL_PORT:
				return isSetInitialPort();
			case FleetPackage.VESSEL__FINAL_PORT:
				return isSetFinalPort();
			case FleetPackage.VESSEL__START_HEEL:
				return startHeel != null;
			case FleetPackage.VESSEL__AVAILABILITY:
				return availability != null;
			case FleetPackage.VESSEL__TIME_CHARTERED:
				return timeChartered != TIME_CHARTERED_EDEFAULT;
			case FleetPackage.VESSEL__TIME_CHARTER_RATE:
				return timeCharterRate != TIME_CHARTER_RATE_EDEFAULT;
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
		result.append(" (dailyHireRate: ");
		if (dailyHireRateESet) result.append(dailyHireRate); else result.append("<unset>");
		result.append(", timeChartered: ");
		result.append(timeChartered);
		result.append(", timeCharterRate: ");
		result.append(timeCharterRate);
		result.append(')');
		return result.toString();
	}

} // end of VesselImpl

// finish type fixing
