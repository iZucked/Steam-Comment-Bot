/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
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

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit Cost Matrix</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getFromPorts <em>From Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getToPorts <em>To Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getNotionalDayRate <em>Notional Day Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#isRoundTrip <em>Round Trip</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMinimumLoad <em>Minimum Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMaximumLoad <em>Maximum Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMinimumDischarge <em>Minimum Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getMaximumDischarge <em>Maximum Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getRetainHeel <em>Retain Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getCargoPrice <em>Cargo Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getCostLines <em>Cost Lines</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getAllowedRoutes <em>Allowed Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getRevenueShare <em>Revenue Share</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getLadenTimeAllowance <em>Laden Time Allowance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostMatrixImpl#getBallastTimeAllowance <em>Ballast Time Allowance</em>}</li>
 * </ul>
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
	 * The cached value of the '{@link #getFromPorts() <em>From Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> fromPorts;

	/**
	 * The cached value of the '{@link #getToPorts() <em>To Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> toPorts;

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
	 * The default value of the '{@link #getRetainHeel() <em>Retain Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRetainHeel()
	 * @generated
	 * @ordered
	 */
	protected static final int RETAIN_HEEL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRetainHeel() <em>Retain Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRetainHeel()
	 * @generated
	 * @ordered
	 */
	protected int retainHeel = RETAIN_HEEL_EDEFAULT;

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
	 * The default value of the '{@link #getRevenueShare() <em>Revenue Share</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevenueShare()
	 * @generated
	 * @ordered
	 */
	protected static final double REVENUE_SHARE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRevenueShare() <em>Revenue Share</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevenueShare()
	 * @generated
	 * @ordered
	 */
	protected double revenueShare = REVENUE_SHARE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLadenTimeAllowance() <em>Laden Time Allowance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenTimeAllowance()
	 * @generated
	 * @ordered
	 */
	protected static final double LADEN_TIME_ALLOWANCE_EDEFAULT = 0.06;

	/**
	 * The cached value of the '{@link #getLadenTimeAllowance() <em>Laden Time Allowance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenTimeAllowance()
	 * @generated
	 * @ordered
	 */
	protected double ladenTimeAllowance = LADEN_TIME_ALLOWANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastTimeAllowance() <em>Ballast Time Allowance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastTimeAllowance()
	 * @generated
	 * @ordered
	 */
	protected static final double BALLAST_TIME_ALLOWANCE_EDEFAULT = 0.06;

	/**
	 * The cached value of the '{@link #getBallastTimeAllowance() <em>Ballast Time Allowance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastTimeAllowance()
	 * @generated
	 * @ordered
	 */
	protected double ballastTimeAllowance = BALLAST_TIME_ALLOWANCE_EDEFAULT;

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
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public EList<APortSet<Port>> getFromPorts() {
		if (fromPorts == null) {
			fromPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, AnalyticsPackage.UNIT_COST_MATRIX__FROM_PORTS);
		}
		return fromPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getToPorts() {
		if (toPorts == null) {
			toPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, AnalyticsPackage.UNIT_COST_MATRIX__TO_PORTS);
		}
		return toPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
	public int getNotionalDayRate() {
		return notionalDayRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
	public boolean isSetSpeed() {
		return speedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRoundTrip() {
		return roundTrip;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMinimumLoad() {
		return minimumLoad;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMaximumLoad() {
		return maximumLoad;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMinimumDischarge() {
		return minimumDischarge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMaximumDischarge() {
		return maximumDischarge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getRetainHeel() {
		return retainHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRetainHeel(int newRetainHeel) {
		int oldRetainHeel = retainHeel;
		retainHeel = newRetainHeel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__RETAIN_HEEL, oldRetainHeel, retainHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCargoPrice() {
		return cargoPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public double getBaseFuelPrice() {
		return baseFuelPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
	public boolean isSetCvValue() {
		return cvValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NO
	 */
	@Override
	public EList<UnitCostLine> getCostLines() {
		if (costLines == null) {
			costLines = new EObjectContainmentEList<UnitCostLine>(UnitCostLine.class, this, AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES) {
				@Override
				public boolean isUnique() {
					return false;
				}
			};
		}
		return costLines;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public double getRevenueShare() {
		return revenueShare;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRevenueShare(double newRevenueShare) {
		double oldRevenueShare = revenueShare;
		revenueShare = newRevenueShare;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__REVENUE_SHARE, oldRevenueShare, revenueShare));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getLadenTimeAllowance() {
		return ladenTimeAllowance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenTimeAllowance(double newLadenTimeAllowance) {
		double oldLadenTimeAllowance = ladenTimeAllowance;
		ladenTimeAllowance = newLadenTimeAllowance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE, oldLadenTimeAllowance, ladenTimeAllowance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getBallastTimeAllowance() {
		return ballastTimeAllowance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastTimeAllowance(double newBallastTimeAllowance) {
		double oldBallastTimeAllowance = ballastTimeAllowance;
		ballastTimeAllowance = newBallastTimeAllowance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE, oldBallastTimeAllowance, ballastTimeAllowance));
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
			case AnalyticsPackage.UNIT_COST_MATRIX__FROM_PORTS:
				return getFromPorts();
			case AnalyticsPackage.UNIT_COST_MATRIX__TO_PORTS:
				return getToPorts();
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
			case AnalyticsPackage.UNIT_COST_MATRIX__RETAIN_HEEL:
				return getRetainHeel();
			case AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE:
				return getCargoPrice();
			case AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE:
				return getBaseFuelPrice();
			case AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE:
				return getCvValue();
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				return getCostLines();
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				return getAllowedRoutes();
			case AnalyticsPackage.UNIT_COST_MATRIX__REVENUE_SHARE:
				return getRevenueShare();
			case AnalyticsPackage.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE:
				return getLadenTimeAllowance();
			case AnalyticsPackage.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE:
				return getBallastTimeAllowance();
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
			case AnalyticsPackage.UNIT_COST_MATRIX__FROM_PORTS:
				getFromPorts().clear();
				getFromPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__TO_PORTS:
				getToPorts().clear();
				getToPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
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
			case AnalyticsPackage.UNIT_COST_MATRIX__RETAIN_HEEL:
				setRetainHeel((Integer)newValue);
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
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				getCostLines().clear();
				getCostLines().addAll((Collection<? extends UnitCostLine>)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				getAllowedRoutes().clear();
				getAllowedRoutes().addAll((Collection<? extends Route>)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__REVENUE_SHARE:
				setRevenueShare((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE:
				setLadenTimeAllowance((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE:
				setBallastTimeAllowance((Double)newValue);
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
			case AnalyticsPackage.UNIT_COST_MATRIX__FROM_PORTS:
				getFromPorts().clear();
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__TO_PORTS:
				getToPorts().clear();
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
			case AnalyticsPackage.UNIT_COST_MATRIX__RETAIN_HEEL:
				setRetainHeel(RETAIN_HEEL_EDEFAULT);
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
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				getCostLines().clear();
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				getAllowedRoutes().clear();
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__REVENUE_SHARE:
				setRevenueShare(REVENUE_SHARE_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE:
				setLadenTimeAllowance(LADEN_TIME_ALLOWANCE_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE:
				setBallastTimeAllowance(BALLAST_TIME_ALLOWANCE_EDEFAULT);
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
			case AnalyticsPackage.UNIT_COST_MATRIX__FROM_PORTS:
				return fromPorts != null && !fromPorts.isEmpty();
			case AnalyticsPackage.UNIT_COST_MATRIX__TO_PORTS:
				return toPorts != null && !toPorts.isEmpty();
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
			case AnalyticsPackage.UNIT_COST_MATRIX__RETAIN_HEEL:
				return retainHeel != RETAIN_HEEL_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__CARGO_PRICE:
				return cargoPrice != CARGO_PRICE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__BASE_FUEL_PRICE:
				return baseFuelPrice != BASE_FUEL_PRICE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__CV_VALUE:
				return isSetCvValue();
			case AnalyticsPackage.UNIT_COST_MATRIX__COST_LINES:
				return costLines != null && !costLines.isEmpty();
			case AnalyticsPackage.UNIT_COST_MATRIX__ALLOWED_ROUTES:
				return allowedRoutes != null && !allowedRoutes.isEmpty();
			case AnalyticsPackage.UNIT_COST_MATRIX__REVENUE_SHARE:
				return revenueShare != REVENUE_SHARE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE:
				return ladenTimeAllowance != LADEN_TIME_ALLOWANCE_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE:
				return ballastTimeAllowance != BALLAST_TIME_ALLOWANCE_EDEFAULT;
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
		result.append(", retainHeel: ");
		result.append(retainHeel);
		result.append(", cargoPrice: ");
		result.append(cargoPrice);
		result.append(", baseFuelPrice: ");
		result.append(baseFuelPrice);
		result.append(", cvValue: ");
		if (cvValueESet) result.append(cvValue); else result.append("<unset>");
		result.append(", revenueShare: ");
		result.append(revenueShare);
		result.append(", ladenTimeAllowance: ");
		result.append(ladenTimeAllowance);
		result.append(", ballastTimeAllowance: ");
		result.append(ballastTimeAllowance);
		result.append(')');
		return result.toString();
	}

} // end of UnitCostMatrixImpl

// finish type fixing
