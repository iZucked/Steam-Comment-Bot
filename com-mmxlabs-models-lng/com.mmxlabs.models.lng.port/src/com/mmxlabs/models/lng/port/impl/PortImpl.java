/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.impl.APortSetImpl;
import java.lang.reflect.InvocationTargetException;
import java.time.ZoneId;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getShortName <em>Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getLoadDuration <em>Load Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getDischargeDuration <em>Discharge Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getBerths <em>Berths</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getDefaultStartTime <em>Default Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#isAllowCooldown <em>Allow Cooldown</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getDefaultWindowSize <em>Default Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getDefaultWindowSizeUnits <em>Default Window Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getMinCvValue <em>Min Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getMaxCvValue <em>Max Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getMinVesselSize <em>Min Vessel Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortImpl#getMaxVesselSize <em>Max Vessel Size</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortImpl extends APortSetImpl<Port> implements Port {
	/**
	 * The default value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected String shortName = SHORT_NAME_EDEFAULT;

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
	 * The cached value of the '{@link #getCapabilities() <em>Capabilities</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCapability> capabilities;

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
	 * The default value of the '{@link #getBerths() <em>Berths</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBerths()
	 * @generated
	 * @ordered
	 */
	protected static final int BERTHS_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getBerths() <em>Berths</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBerths()
	 * @generated
	 * @ordered
	 */
	protected int berths = BERTHS_EDEFAULT;

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
	 * The default value of the '{@link #getDefaultWindowSizeUnits() <em>Default Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final TimePeriod DEFAULT_WINDOW_SIZE_UNITS_EDEFAULT = TimePeriod.HOURS;

	/**
	 * The cached value of the '{@link #getDefaultWindowSizeUnits() <em>Default Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod defaultWindowSizeUnits = DEFAULT_WINDOW_SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinCvValue() <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double MIN_CV_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMinCvValue() <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCvValue()
	 * @generated
	 * @ordered
	 */
	protected double minCvValue = MIN_CV_VALUE_EDEFAULT;

	/**
	 * This is true if the Min Cv Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minCvValueESet;

	/**
	 * The default value of the '{@link #getMaxCvValue() <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double MAX_CV_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMaxCvValue() <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCvValue()
	 * @generated
	 * @ordered
	 */
	protected double maxCvValue = MAX_CV_VALUE_EDEFAULT;

	/**
	 * This is true if the Max Cv Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxCvValueESet;

	/**
	 * The default value of the '{@link #getMinVesselSize() <em>Min Vessel Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVesselSize()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_VESSEL_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinVesselSize() <em>Min Vessel Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVesselSize()
	 * @generated
	 * @ordered
	 */
	protected int minVesselSize = MIN_VESSEL_SIZE_EDEFAULT;

	/**
	 * This is true if the Min Vessel Size attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minVesselSizeESet;

	/**
	 * The default value of the '{@link #getMaxVesselSize() <em>Max Vessel Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVesselSize()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_VESSEL_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxVesselSize() <em>Max Vessel Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVesselSize()
	 * @generated
	 * @ordered
	 */
	protected int maxVesselSize = MAX_VESSEL_SIZE_EDEFAULT;

	/**
	 * This is true if the Max Vessel Size attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxVesselSizeESet;

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
	@Override
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
	@Override
	public int getLoadDuration() {
		return loadDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getDischargeDuration() {
		return dischargeDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getBerths() {
		return berths;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBerths(int newBerths) {
		int oldBerths = berths;
		berths = newBerths;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__BERTHS, oldBerths, berths));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCvValue() {
		return cvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getDefaultStartTime() {
		return defaultStartTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public boolean isAllowCooldown() {
		return allowCooldown;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getDefaultWindowSize() {
		return defaultWindowSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public TimePeriod getDefaultWindowSizeUnits() {
		return defaultWindowSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultWindowSizeUnits(TimePeriod newDefaultWindowSizeUnits) {
		TimePeriod oldDefaultWindowSizeUnits = defaultWindowSizeUnits;
		defaultWindowSizeUnits = newDefaultWindowSizeUnits == null ? DEFAULT_WINDOW_SIZE_UNITS_EDEFAULT : newDefaultWindowSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_WINDOW_SIZE_UNITS, oldDefaultWindowSizeUnits, defaultWindowSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	public double getMinCvValue() {
		return minCvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinCvValue(double newMinCvValue) {
		double oldMinCvValue = minCvValue;
		minCvValue = newMinCvValue;
		boolean oldMinCvValueESet = minCvValueESet;
		minCvValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__MIN_CV_VALUE, oldMinCvValue, minCvValue, !oldMinCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinCvValue() {
		double oldMinCvValue = minCvValue;
		boolean oldMinCvValueESet = minCvValueESet;
		minCvValue = MIN_CV_VALUE_EDEFAULT;
		minCvValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PortPackage.PORT__MIN_CV_VALUE, oldMinCvValue, MIN_CV_VALUE_EDEFAULT, oldMinCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinCvValue() {
		return minCvValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getMaxCvValue() {
		return maxCvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxCvValue(double newMaxCvValue) {
		double oldMaxCvValue = maxCvValue;
		maxCvValue = newMaxCvValue;
		boolean oldMaxCvValueESet = maxCvValueESet;
		maxCvValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__MAX_CV_VALUE, oldMaxCvValue, maxCvValue, !oldMaxCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxCvValue() {
		double oldMaxCvValue = maxCvValue;
		boolean oldMaxCvValueESet = maxCvValueESet;
		maxCvValue = MAX_CV_VALUE_EDEFAULT;
		maxCvValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PortPackage.PORT__MAX_CV_VALUE, oldMaxCvValue, MAX_CV_VALUE_EDEFAULT, oldMaxCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxCvValue() {
		return maxCvValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinVesselSize() {
		return minVesselSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinVesselSize(int newMinVesselSize) {
		int oldMinVesselSize = minVesselSize;
		minVesselSize = newMinVesselSize;
		boolean oldMinVesselSizeESet = minVesselSizeESet;
		minVesselSizeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__MIN_VESSEL_SIZE, oldMinVesselSize, minVesselSize, !oldMinVesselSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinVesselSize() {
		int oldMinVesselSize = minVesselSize;
		boolean oldMinVesselSizeESet = minVesselSizeESet;
		minVesselSize = MIN_VESSEL_SIZE_EDEFAULT;
		minVesselSizeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PortPackage.PORT__MIN_VESSEL_SIZE, oldMinVesselSize, MIN_VESSEL_SIZE_EDEFAULT, oldMinVesselSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinVesselSize() {
		return minVesselSizeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxVesselSize() {
		return maxVesselSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxVesselSize(int newMaxVesselSize) {
		int oldMaxVesselSize = maxVesselSize;
		maxVesselSize = newMaxVesselSize;
		boolean oldMaxVesselSizeESet = maxVesselSizeESet;
		maxVesselSizeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__MAX_VESSEL_SIZE, oldMaxVesselSize, maxVesselSize, !oldMaxVesselSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxVesselSize() {
		int oldMaxVesselSize = maxVesselSize;
		boolean oldMaxVesselSizeESet = maxVesselSizeESet;
		maxVesselSize = MAX_VESSEL_SIZE_EDEFAULT;
		maxVesselSizeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PortPackage.PORT__MAX_VESSEL_SIZE, oldMaxVesselSize, MAX_VESSEL_SIZE_EDEFAULT, oldMaxVesselSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxVesselSize() {
		return maxVesselSizeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZoneId getZoneId() {
		return getLocation().getZoneId();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String mmxID() {
		if (getLocation() != null) {
			return getLocation().getMmxId();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getShortName() {
		return shortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShortName(String newShortName) {
		String oldShortName = shortName;
		shortName = newShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__SHORT_NAME, oldShortName, shortName));
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
			case PortPackage.PORT__SHORT_NAME:
				return getShortName();
			case PortPackage.PORT__LOCATION:
				return getLocation();
			case PortPackage.PORT__CAPABILITIES:
				return getCapabilities();
			case PortPackage.PORT__LOAD_DURATION:
				return getLoadDuration();
			case PortPackage.PORT__DISCHARGE_DURATION:
				return getDischargeDuration();
			case PortPackage.PORT__BERTHS:
				return getBerths();
			case PortPackage.PORT__CV_VALUE:
				return getCvValue();
			case PortPackage.PORT__DEFAULT_START_TIME:
				return getDefaultStartTime();
			case PortPackage.PORT__ALLOW_COOLDOWN:
				return isAllowCooldown();
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE:
				return getDefaultWindowSize();
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE_UNITS:
				return getDefaultWindowSizeUnits();
			case PortPackage.PORT__MIN_CV_VALUE:
				return getMinCvValue();
			case PortPackage.PORT__MAX_CV_VALUE:
				return getMaxCvValue();
			case PortPackage.PORT__MIN_VESSEL_SIZE:
				return getMinVesselSize();
			case PortPackage.PORT__MAX_VESSEL_SIZE:
				return getMaxVesselSize();
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
			case PortPackage.PORT__SHORT_NAME:
				setShortName((String)newValue);
				return;
			case PortPackage.PORT__LOCATION:
				setLocation((Location)newValue);
				return;
			case PortPackage.PORT__CAPABILITIES:
				getCapabilities().clear();
				getCapabilities().addAll((Collection<? extends PortCapability>)newValue);
				return;
			case PortPackage.PORT__LOAD_DURATION:
				setLoadDuration((Integer)newValue);
				return;
			case PortPackage.PORT__DISCHARGE_DURATION:
				setDischargeDuration((Integer)newValue);
				return;
			case PortPackage.PORT__BERTHS:
				setBerths((Integer)newValue);
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
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE_UNITS:
				setDefaultWindowSizeUnits((TimePeriod)newValue);
				return;
			case PortPackage.PORT__MIN_CV_VALUE:
				setMinCvValue((Double)newValue);
				return;
			case PortPackage.PORT__MAX_CV_VALUE:
				setMaxCvValue((Double)newValue);
				return;
			case PortPackage.PORT__MIN_VESSEL_SIZE:
				setMinVesselSize((Integer)newValue);
				return;
			case PortPackage.PORT__MAX_VESSEL_SIZE:
				setMaxVesselSize((Integer)newValue);
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
			case PortPackage.PORT__SHORT_NAME:
				setShortName(SHORT_NAME_EDEFAULT);
				return;
			case PortPackage.PORT__LOCATION:
				setLocation((Location)null);
				return;
			case PortPackage.PORT__CAPABILITIES:
				getCapabilities().clear();
				return;
			case PortPackage.PORT__LOAD_DURATION:
				setLoadDuration(LOAD_DURATION_EDEFAULT);
				return;
			case PortPackage.PORT__DISCHARGE_DURATION:
				setDischargeDuration(DISCHARGE_DURATION_EDEFAULT);
				return;
			case PortPackage.PORT__BERTHS:
				setBerths(BERTHS_EDEFAULT);
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
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE_UNITS:
				setDefaultWindowSizeUnits(DEFAULT_WINDOW_SIZE_UNITS_EDEFAULT);
				return;
			case PortPackage.PORT__MIN_CV_VALUE:
				unsetMinCvValue();
				return;
			case PortPackage.PORT__MAX_CV_VALUE:
				unsetMaxCvValue();
				return;
			case PortPackage.PORT__MIN_VESSEL_SIZE:
				unsetMinVesselSize();
				return;
			case PortPackage.PORT__MAX_VESSEL_SIZE:
				unsetMaxVesselSize();
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
			case PortPackage.PORT__SHORT_NAME:
				return SHORT_NAME_EDEFAULT == null ? shortName != null : !SHORT_NAME_EDEFAULT.equals(shortName);
			case PortPackage.PORT__LOCATION:
				return location != null;
			case PortPackage.PORT__CAPABILITIES:
				return capabilities != null && !capabilities.isEmpty();
			case PortPackage.PORT__LOAD_DURATION:
				return loadDuration != LOAD_DURATION_EDEFAULT;
			case PortPackage.PORT__DISCHARGE_DURATION:
				return dischargeDuration != DISCHARGE_DURATION_EDEFAULT;
			case PortPackage.PORT__BERTHS:
				return berths != BERTHS_EDEFAULT;
			case PortPackage.PORT__CV_VALUE:
				return cvValue != CV_VALUE_EDEFAULT;
			case PortPackage.PORT__DEFAULT_START_TIME:
				return defaultStartTime != DEFAULT_START_TIME_EDEFAULT;
			case PortPackage.PORT__ALLOW_COOLDOWN:
				return allowCooldown != ALLOW_COOLDOWN_EDEFAULT;
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE:
				return defaultWindowSize != DEFAULT_WINDOW_SIZE_EDEFAULT;
			case PortPackage.PORT__DEFAULT_WINDOW_SIZE_UNITS:
				return defaultWindowSizeUnits != DEFAULT_WINDOW_SIZE_UNITS_EDEFAULT;
			case PortPackage.PORT__MIN_CV_VALUE:
				return isSetMinCvValue();
			case PortPackage.PORT__MAX_CV_VALUE:
				return isSetMaxCvValue();
			case PortPackage.PORT__MIN_VESSEL_SIZE:
				return isSetMinVesselSize();
			case PortPackage.PORT__MAX_VESSEL_SIZE:
				return isSetMaxVesselSize();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case PortPackage.PORT___GET_ZONE_ID:
				return getZoneId();
			case PortPackage.PORT___MMX_ID:
				return mmxID();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (shortName: ");
		result.append(shortName);
		result.append(", capabilities: ");
		result.append(capabilities);
		result.append(", loadDuration: ");
		result.append(loadDuration);
		result.append(", dischargeDuration: ");
		result.append(dischargeDuration);
		result.append(", berths: ");
		result.append(berths);
		result.append(", cvValue: ");
		result.append(cvValue);
		result.append(", defaultStartTime: ");
		result.append(defaultStartTime);
		result.append(", allowCooldown: ");
		result.append(allowCooldown);
		result.append(", defaultWindowSize: ");
		result.append(defaultWindowSize);
		result.append(", defaultWindowSizeUnits: ");
		result.append(defaultWindowSizeUnits);
		result.append(", minCvValue: ");
		if (minCvValueESet) result.append(minCvValue); else result.append("<unset>");
		result.append(", maxCvValue: ");
		if (maxCvValueESet) result.append(maxCvValue); else result.append("<unset>");
		result.append(", minVesselSize: ");
		if (minVesselSizeESet) result.append(minVesselSize); else result.append("<unset>");
		result.append(", maxVesselSize: ");
		if (maxVesselSizeESet) result.append(maxVesselSize); else result.append("<unset>");
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
