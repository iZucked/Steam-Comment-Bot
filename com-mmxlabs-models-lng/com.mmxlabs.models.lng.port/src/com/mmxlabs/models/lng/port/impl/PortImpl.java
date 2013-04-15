/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.impl.APortSetImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getLoadDuration <em>Load Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getDischargeDuration <em>Discharge Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getDefaultStartTime <em>Default Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#isAllowCooldown <em>Allow Cooldown</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getDefaultWindowSize <em>Default Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getPortCode <em>Port Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getLocation <em>Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PortImpl extends APortSetImpl<Port> implements Port {
	/**
	 * The cached value of the '{@link #getCapabilities() <em>Capabilities</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCapability> capabilities;

	/**
	 * The default value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected static final String TIME_ZONE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected String timeZone = TIME_ZONE_EDEFAULT;
	/**
	 * The default value of the '{@link #getLoadDuration() <em>Load Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int LOAD_DURATION_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getLoadDuration() <em>Load Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadDuration()
	 * @generated
	 * @ordered
	 */
	protected int loadDuration = LOAD_DURATION_EDEFAULT;
	/**
	 * The default value of the '{@link #getDischargeDuration() <em>Discharge Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DISCHARGE_DURATION_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getDischargeDuration() <em>Discharge Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeDuration()
	 * @generated
	 * @ordered
	 */
	protected int dischargeDuration = DISCHARGE_DURATION_EDEFAULT;
	/**
	 * The default value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_VALUE_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected double cvValue = CV_VALUE_EDEFAULT;
	/**
	 * The default value of the '{@link #getDefaultStartTime() <em>Default Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultStartTime()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_START_TIME_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getDefaultStartTime() <em>Default Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultStartTime()
	 * @generated
	 * @ordered
	 */
	protected int defaultStartTime = DEFAULT_START_TIME_EDEFAULT;
	/**
	 * The default value of the '{@link #isAllowCooldown() <em>Allow Cooldown</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowCooldown()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ALLOW_COOLDOWN_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isAllowCooldown() <em>Allow Cooldown</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowCooldown()
	 * @generated
	 * @ordered
	 */
	protected boolean allowCooldown = ALLOW_COOLDOWN_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultWindowSize() <em>Default Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultWindowSize()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_WINDOW_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultWindowSize() <em>Default Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultWindowSize()
	 * @generated
	 * @ordered
	 */
	protected int defaultWindowSize = DEFAULT_WINDOW_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortCode() <em>Port Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCode()
	 * @generated
	 * @ordered
	 */
	protected static final String PORT_CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPortCode() <em>Port Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCode()
	 * @generated
	 * @ordered
	 */
	protected String portCode = PORT_CODE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected Location location;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PortCapability> getCapabilities() {
		if (capabilities == null) {
			capabilities = new EDataTypeUniqueEList<PortCapability>(PortCapability.class, this, PortPackage.PORT__CAPABILITIES);
		}
		return capabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeZone(String newTimeZone) {
		String oldTimeZone = timeZone;
		timeZone = newTimeZone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__TIME_ZONE, oldTimeZone, timeZone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLoadDuration() {
		return loadDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadDuration(int newLoadDuration) {
		int oldLoadDuration = loadDuration;
		loadDuration = newLoadDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__LOAD_DURATION, oldLoadDuration, loadDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDischargeDuration() {
		return dischargeDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeDuration(int newDischargeDuration) {
		int oldDischargeDuration = dischargeDuration;
		dischargeDuration = newDischargeDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DISCHARGE_DURATION, oldDischargeDuration, dischargeDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCvValue() {
		return cvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCvValue(double newCvValue) {
		double oldCvValue = cvValue;
		cvValue = newCvValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__CV_VALUE, oldCvValue, cvValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDefaultStartTime() {
		return defaultStartTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultStartTime(int newDefaultStartTime) {
		int oldDefaultStartTime = defaultStartTime;
		defaultStartTime = newDefaultStartTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_START_TIME, oldDefaultStartTime, defaultStartTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAllowCooldown() {
		return allowCooldown;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAllowCooldown(boolean newAllowCooldown) {
		boolean oldAllowCooldown = allowCooldown;
		allowCooldown = newAllowCooldown;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__ALLOW_COOLDOWN, oldAllowCooldown, allowCooldown));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDefaultWindowSize() {
		return defaultWindowSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultWindowSize(int newDefaultWindowSize) {
		int oldDefaultWindowSize = defaultWindowSize;
		defaultWindowSize = newDefaultWindowSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_WINDOW_SIZE, oldDefaultWindowSize, defaultWindowSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPortCode() {
		return portCode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPortCode(String newPortCode) {
		String oldPortCode = portCode;
		portCode = newPortCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__PORT_CODE, oldPortCode, portCode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLocation(Location newLocation, NotificationChain msgs) {
		Location oldLocation = location;
		location = newLocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.PORT__LOCATION, oldLocation, newLocation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(Location newLocation) {
		if (newLocation != location) {
			NotificationChain msgs = null;
			if (location != null)
				msgs = ((InternalEObject)location).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT__LOCATION, null, msgs);
			if (newLocation != null)
				msgs = ((InternalEObject)newLocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.PORT__LOCATION, null, msgs);
			msgs = basicSetLocation(newLocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__LOCATION, newLocation, newLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.PORT__LOCATION:
				return basicSetLocation(null, msgs);
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
			case PortPackage.PORT__CAPABILITIES:
				return getCapabilities();
			case PortPackage.PORT__TIME_ZONE:
				return getTimeZone();
			case PortPackage.PORT__LOAD_DURATION:
				return getLoadDuration();
			case PortPackage.PORT__DISCHARGE_DURATION:
				return getDischargeDuration();
			case PortPackage.PORT__CV_VALUE:
				return getCvValue();
			case PortPackage.PORT__DEFAULT_START_TIME:
				return getDefaultStartTime();
			case PortPackage.PORT__ALLOW_COOLDOWN:
				return isAllowCooldown();
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE:
				return getDefaultWindowSize();
			case PortPackage.PORT__PORT_CODE:
				return getPortCode();
			case PortPackage.PORT__LOCATION:
				return getLocation();
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
			case PortPackage.PORT__CAPABILITIES:
				getCapabilities().clear();
				getCapabilities().addAll((Collection<? extends PortCapability>)newValue);
				return;
			case PortPackage.PORT__TIME_ZONE:
				setTimeZone((String)newValue);
				return;
			case PortPackage.PORT__LOAD_DURATION:
				setLoadDuration((Integer)newValue);
				return;
			case PortPackage.PORT__DISCHARGE_DURATION:
				setDischargeDuration((Integer)newValue);
				return;
			case PortPackage.PORT__CV_VALUE:
				setCvValue((Double)newValue);
				return;
			case PortPackage.PORT__DEFAULT_START_TIME:
				setDefaultStartTime((Integer)newValue);
				return;
			case PortPackage.PORT__ALLOW_COOLDOWN:
				setAllowCooldown((Boolean)newValue);
				return;
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE:
				setDefaultWindowSize((Integer)newValue);
				return;
			case PortPackage.PORT__PORT_CODE:
				setPortCode((String)newValue);
				return;
			case PortPackage.PORT__LOCATION:
				setLocation((Location)newValue);
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
			case PortPackage.PORT__CAPABILITIES:
				getCapabilities().clear();
				return;
			case PortPackage.PORT__TIME_ZONE:
				setTimeZone(TIME_ZONE_EDEFAULT);
				return;
			case PortPackage.PORT__LOAD_DURATION:
				setLoadDuration(LOAD_DURATION_EDEFAULT);
				return;
			case PortPackage.PORT__DISCHARGE_DURATION:
				setDischargeDuration(DISCHARGE_DURATION_EDEFAULT);
				return;
			case PortPackage.PORT__CV_VALUE:
				setCvValue(CV_VALUE_EDEFAULT);
				return;
			case PortPackage.PORT__DEFAULT_START_TIME:
				setDefaultStartTime(DEFAULT_START_TIME_EDEFAULT);
				return;
			case PortPackage.PORT__ALLOW_COOLDOWN:
				setAllowCooldown(ALLOW_COOLDOWN_EDEFAULT);
				return;
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE:
				setDefaultWindowSize(DEFAULT_WINDOW_SIZE_EDEFAULT);
				return;
			case PortPackage.PORT__PORT_CODE:
				setPortCode(PORT_CODE_EDEFAULT);
				return;
			case PortPackage.PORT__LOCATION:
				setLocation((Location)null);
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
			case PortPackage.PORT__CAPABILITIES:
				return capabilities != null && !capabilities.isEmpty();
			case PortPackage.PORT__TIME_ZONE:
				return TIME_ZONE_EDEFAULT == null ? timeZone != null : !TIME_ZONE_EDEFAULT.equals(timeZone);
			case PortPackage.PORT__LOAD_DURATION:
				return loadDuration != LOAD_DURATION_EDEFAULT;
			case PortPackage.PORT__DISCHARGE_DURATION:
				return dischargeDuration != DISCHARGE_DURATION_EDEFAULT;
			case PortPackage.PORT__CV_VALUE:
				return cvValue != CV_VALUE_EDEFAULT;
			case PortPackage.PORT__DEFAULT_START_TIME:
				return defaultStartTime != DEFAULT_START_TIME_EDEFAULT;
			case PortPackage.PORT__ALLOW_COOLDOWN:
				return allowCooldown != ALLOW_COOLDOWN_EDEFAULT;
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE:
				return defaultWindowSize != DEFAULT_WINDOW_SIZE_EDEFAULT;
			case PortPackage.PORT__PORT_CODE:
				return PORT_CODE_EDEFAULT == null ? portCode != null : !PORT_CODE_EDEFAULT.equals(portCode);
			case PortPackage.PORT__LOCATION:
				return location != null;
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
		result.append(" (capabilities: ");
		result.append(capabilities);
		result.append(", timeZone: ");
		result.append(timeZone);
		result.append(", loadDuration: ");
		result.append(loadDuration);
		result.append(", dischargeDuration: ");
		result.append(dischargeDuration);
		result.append(", cvValue: ");
		result.append(cvValue);
		result.append(", defaultStartTime: ");
		result.append(defaultStartTime);
		result.append(", allowCooldown: ");
		result.append(allowCooldown);
		result.append(", defaultWindowSize: ");
		result.append(defaultWindowSize);
		result.append(", portCode: ");
		result.append(portCode);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc --> 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Port> collect(EList<APortSet<Port>> marked) {
		if (marked.contains(this)) {
			return ECollections.emptyEList();
		}
		final UniqueEList<Port> result = new UniqueEList<Port>();
		marked.add(this);
		result.add(this);
		return result;
	}
	
} //PortImpl
