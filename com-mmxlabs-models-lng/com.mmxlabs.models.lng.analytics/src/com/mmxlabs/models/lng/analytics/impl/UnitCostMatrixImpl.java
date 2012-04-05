

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics.impl;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit Cost Matrix</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getNotionalDayRate <em>Notional Day Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#isRoundTrip <em>Round Trip</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMinimumLoad <em>Minimum Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMaximumLoad <em>Maximum Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMinimumDischarge <em>Minimum Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMaximumDischarge <em>Maximum Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getCargoPrice <em>Cargo Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getDischargeIdleTime <em>Discharge Idle Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getReturnIdleTime <em>Return Idle Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getCostLines <em>Cost Lines</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getAllowedRoutes <em>Allowed Routes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnitCostMatrixImpl extends UUIDObjectImpl implements UnitCostMatrix {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> ports;

	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The default value of the '{@link #getNotionalDayRate() <em>Notional Day Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalDayRate()
	 * @generated
	 * @ordered
	 */
	protected static final int NOTIONAL_DAY_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNotionalDayRate() <em>Notional Day Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalDayRate()
	 * @generated
	 * @ordered
	 */
	protected int notionalDayRate = NOTIONAL_DAY_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected double speed = SPEED_EDEFAULT;

	/**
	 * This is true if the Speed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean speedESet;

	/**
	 * The default value of the '{@link #isRoundTrip() <em>Round Trip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRoundTrip()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ROUND_TRIP_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isRoundTrip() <em>Round Trip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRoundTrip()
	 * @generated
	 * @ordered
	 */
	protected boolean roundTrip = ROUND_TRIP_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinimumLoad() <em>Minimum Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumLoad()
	 * @generated
	 * @ordered
	 */
	protected static final int MINIMUM_LOAD_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinimumLoad() <em>Minimum Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumLoad()
	 * @generated
	 * @ordered
	 */
	protected int minimumLoad = MINIMUM_LOAD_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaximumLoad() <em>Maximum Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumLoad()
	 * @generated
	 * @ordered
	 */
	protected static final int MAXIMUM_LOAD_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaximumLoad() <em>Maximum Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumLoad()
	 * @generated
	 * @ordered
	 */
	protected int maximumLoad = MAXIMUM_LOAD_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinimumDischarge() <em>Minimum Discharge</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumDischarge()
	 * @generated
	 * @ordered
	 */
	protected static final int MINIMUM_DISCHARGE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinimumDischarge() <em>Minimum Discharge</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumDischarge()
	 * @generated
	 * @ordered
	 */
	protected int minimumDischarge = MINIMUM_DISCHARGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaximumDischarge() <em>Maximum Discharge</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumDischarge()
	 * @generated
	 * @ordered
	 */
	protected static final int MAXIMUM_DISCHARGE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaximumDischarge() <em>Maximum Discharge</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumDischarge()
	 * @generated
	 * @ordered
	 */
	protected int maximumDischarge = MAXIMUM_DISCHARGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCargoPrice() <em>Cargo Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double CARGO_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCargoPrice() <em>Cargo Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoPrice()
	 * @generated
	 * @ordered
	 */
	protected double cargoPrice = CARGO_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double BASE_FUEL_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected double baseFuelPrice = BASE_FUEL_PRICE_EDEFAULT;

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
	 * This is true if the Cv Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cvValueESet;

	/**
	 * The default value of the '{@link #getDischargeIdleTime() <em>Discharge Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeIdleTime()
	 * @generated
	 * @ordered
	 */
	protected static final int DISCHARGE_IDLE_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDischargeIdleTime() <em>Discharge Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeIdleTime()
	 * @generated
	 * @ordered
	 */
	protected int dischargeIdleTime = DISCHARGE_IDLE_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getReturnIdleTime() <em>Return Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnIdleTime()
	 * @generated
	 * @ordered
	 */
	protected static final int RETURN_IDLE_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getReturnIdleTime() <em>Return Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnIdleTime()
	 * @generated
	 * @ordered
	 */
	protected int returnIdleTime = RETURN_IDLE_TIME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCostLines() <em>Cost Lines</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCostLines()
	 * @generated
	 * @ordered
	 */
	protected EList<UnitCostLine> costLines;

	/**
	 * The cached value of the '{@link #getAllowedRoutes() <em>Allowed Routes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<Route> allowedRoutes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnitCostMatrixImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.UNIT_COST_MATRIX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<APortSet>(APortSet.class, this, AnalyticsPackage.UNIT_COST_MATRIX__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.UNIT_COST_MATRIX__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNotionalDayRate() {
		return notionalDayRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNotionalDayRate(int newNotionalDayRate) {
		int oldNotionalDayRate = notionalDayRate;
		notionalDayRate = newNotionalDayRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE, oldNotionalDayRate, notionalDayRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpeed(double newSpeed) {
		double oldSpeed = speed;
		speed = newSpeed;
		boolean oldSpeedESet = speedESet;
		speedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__SPEED, oldSpeed, speed, !oldSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSpeed() {
		double oldSpeed = speed;
		boolean oldSpeedESet = speedESet;
		speed = SPEED_EDEFAULT;
		speedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.UNIT_COST_MATRIX__SPEED, oldSpeed, SPEED_EDEFAULT, oldSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSpeed() {
		return speedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRoundTrip() {
		return roundTrip;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRoundTrip(boolean newRoundTrip) {
		boolean oldRoundTrip = roundTrip;
		roundTrip = newRoundTrip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__ROUND_TRIP, oldRoundTrip, roundTrip));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinimumLoad() {
		return minimumLoad;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinimumLoad(int newMinimumLoad) {
		int oldMinimumLoad = minimumLoad;
		minimumLoad = newMinimumLoad;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_LOAD, oldMinimumLoad, minimumLoad));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaximumLoad() {
		return maximumLoad;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximumLoad(int newMaximumLoad) {
		int oldMaximumLoad = maximumLoad;
		maximumLoad = newMaximumLoad;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_LOAD, oldMaximumLoad, maximumLoad));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinimumDischarge() {
		return minimumDischarge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinimumDischarge(int newMinimumDischarge) {
		int oldMinimumDischarge = minimumDischarge;
		minimumDischarge = newMinimumDischarge;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_DISCHARGE, oldMinimumDischarge, minimumDischarge));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaximumDischarge() {
		return maximumDischarge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximumDischarge(int newMaximumDischarge) {
		int oldMaximumDischarge = maximumDischarge;
		maximumDischarge = newMaximumDischarge;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE, oldMaximumDischarge, maximumDischarge));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCargoPrice() {
		return cargoPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoPrice(double newCargoPrice) {
		double oldCargoPrice = cargoPrice;
		cargoPrice = newCargoPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE, oldCargoPrice, cargoPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getBaseFuelPrice() {
		return baseFuelPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseFuelPrice(double newBaseFuelPrice) {
		double oldBaseFuelPrice = baseFuelPrice;
		baseFuelPrice = newBaseFuelPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE, oldBaseFuelPrice, baseFuelPrice));
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
		boolean oldCvValueESet = cvValueESet;
		cvValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE, oldCvValue, cvValue, !oldCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCvValue() {
		double oldCvValue = cvValue;
		boolean oldCvValueESet = cvValueESet;
		cvValue = CV_VALUE_EDEFAULT;
		cvValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE, oldCvValue, CV_VALUE_EDEFAULT, oldCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCvValue() {
		return cvValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDischargeIdleTime() {
		return dischargeIdleTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeIdleTime(int newDischargeIdleTime) {
		int oldDischargeIdleTime = dischargeIdleTime;
		dischargeIdleTime = newDischargeIdleTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME, oldDischargeIdleTime, dischargeIdleTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getReturnIdleTime() {
		return returnIdleTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReturnIdleTime(int newReturnIdleTime) {
		int oldReturnIdleTime = returnIdleTime;
		returnIdleTime = newReturnIdleTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__RETURN_IDLE_TIME, oldReturnIdleTime, returnIdleTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UnitCostLine> getCostLines() {
		if (costLines == null) {
			costLines = new EObjectContainmentEList<UnitCostLine>(UnitCostLine.class, this, AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES);
		}
		return costLines;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Route> getAllowedRoutes() {
		if (allowedRoutes == null) {
			allowedRoutes = new EObjectResolvingEList<Route>(Route.class, this, AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES);
		}
		return allowedRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				return ((InternalEList<?>)getCostLines()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.UNIT_COST_MATRIX__NAME:
				return getName();
			case AnalyticsPackage.UNIT_COST_MATRIX__PORTS:
				return getPorts();
			case AnalyticsPackage.UNIT_COST_MATRIX__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case AnalyticsPackage.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE:
				return getNotionalDayRate();
			case AnalyticsPackage.UNIT_COST_MATRIX__SPEED:
				return getSpeed();
			case AnalyticsPackage.UNIT_COST_MATRIX__ROUND_TRIP:
				return isRoundTrip();
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_LOAD:
				return getMinimumLoad();
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_LOAD:
				return getMaximumLoad();
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_DISCHARGE:
				return getMinimumDischarge();
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE:
				return getMaximumDischarge();
			case AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE:
				return getCargoPrice();
			case AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE:
				return getBaseFuelPrice();
			case AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE:
				return getCvValue();
			case AnalyticsPackage.UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME:
				return getDischargeIdleTime();
			case AnalyticsPackage.UNIT_COST_MATRIX__RETURN_IDLE_TIME:
				return getReturnIdleTime();
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				return getCostLines();
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				return getAllowedRoutes();
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
			case AnalyticsPackage.UNIT_COST_MATRIX__NAME:
				setName((String)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends APortSet>)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE:
				setNotionalDayRate((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__SPEED:
				setSpeed((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__ROUND_TRIP:
				setRoundTrip((Boolean)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_LOAD:
				setMinimumLoad((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_LOAD:
				setMaximumLoad((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_DISCHARGE:
				setMinimumDischarge((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE:
				setMaximumDischarge((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE:
				setCargoPrice((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE:
				setBaseFuelPrice((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE:
				setCvValue((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME:
				setDischargeIdleTime((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__RETURN_IDLE_TIME:
				setReturnIdleTime((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				getCostLines().clear();
				getCostLines().addAll((Collection<? extends UnitCostLine>)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				getAllowedRoutes().clear();
				getAllowedRoutes().addAll((Collection<? extends Route>)newValue);
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
			case AnalyticsPackage.UNIT_COST_MATRIX__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__PORTS:
				getPorts().clear();
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__VESSEL:
				setVessel((Vessel)null);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE:
				setNotionalDayRate(NOTIONAL_DAY_RATE_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__SPEED:
				unsetSpeed();
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__ROUND_TRIP:
				setRoundTrip(ROUND_TRIP_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_LOAD:
				setMinimumLoad(MINIMUM_LOAD_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_LOAD:
				setMaximumLoad(MAXIMUM_LOAD_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_DISCHARGE:
				setMinimumDischarge(MINIMUM_DISCHARGE_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE:
				setMaximumDischarge(MAXIMUM_DISCHARGE_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE:
				setCargoPrice(CARGO_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE:
				setBaseFuelPrice(BASE_FUEL_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE:
				unsetCvValue();
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME:
				setDischargeIdleTime(DISCHARGE_IDLE_TIME_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__RETURN_IDLE_TIME:
				setReturnIdleTime(RETURN_IDLE_TIME_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				getCostLines().clear();
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				getAllowedRoutes().clear();
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
			case AnalyticsPackage.UNIT_COST_MATRIX__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AnalyticsPackage.UNIT_COST_MATRIX__PORTS:
				return ports != null && !ports.isEmpty();
			case AnalyticsPackage.UNIT_COST_MATRIX__VESSEL:
				return vessel != null;
			case AnalyticsPackage.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE:
				return notionalDayRate != NOTIONAL_DAY_RATE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__SPEED:
				return isSetSpeed();
			case AnalyticsPackage.UNIT_COST_MATRIX__ROUND_TRIP:
				return roundTrip != ROUND_TRIP_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_LOAD:
				return minimumLoad != MINIMUM_LOAD_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_LOAD:
				return maximumLoad != MAXIMUM_LOAD_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__MINIMUM_DISCHARGE:
				return minimumDischarge != MINIMUM_DISCHARGE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE:
				return maximumDischarge != MAXIMUM_DISCHARGE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE:
				return cargoPrice != CARGO_PRICE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE:
				return baseFuelPrice != BASE_FUEL_PRICE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE:
				return isSetCvValue();
			case AnalyticsPackage.UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME:
				return dischargeIdleTime != DISCHARGE_IDLE_TIME_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__RETURN_IDLE_TIME:
				return returnIdleTime != RETURN_IDLE_TIME_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				return costLines != null && !costLines.isEmpty();
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				return allowedRoutes != null && !allowedRoutes.isEmpty();
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
				case AnalyticsPackage.UNIT_COST_MATRIX__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
				case MMXCorePackage.NAMED_OBJECT__NAME: return AnalyticsPackage.UNIT_COST_MATRIX__NAME;
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
		result.append(", notionalDayRate: ");
		result.append(notionalDayRate);
		result.append(", speed: ");
		if (speedESet) result.append(speed); else result.append("<unset>");
		result.append(", roundTrip: ");
		result.append(roundTrip);
		result.append(", minimumLoad: ");
		result.append(minimumLoad);
		result.append(", maximumLoad: ");
		result.append(maximumLoad);
		result.append(", minimumDischarge: ");
		result.append(minimumDischarge);
		result.append(", maximumDischarge: ");
		result.append(maximumDischarge);
		result.append(", cargoPrice: ");
		result.append(cargoPrice);
		result.append(", baseFuelPrice: ");
		result.append(baseFuelPrice);
		result.append(", cvValue: ");
		if (cvValueESet) result.append(cvValue); else result.append("<unset>");
		result.append(", dischargeIdleTime: ");
		result.append(dischargeIdleTime);
		result.append(", returnIdleTime: ");
		result.append(returnIdleTime);
		result.append(')');
		return result.toString();
	}

} // end of UnitCostMatrixImpl

// finish type fixing
