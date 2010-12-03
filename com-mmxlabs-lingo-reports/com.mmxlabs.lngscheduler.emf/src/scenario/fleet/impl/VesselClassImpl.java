/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.fleet.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import scenario.fleet.FleetPackage;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getBaseFuelUnitPrice <em>Base Fuel Unit Price</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getMinHeelVolume <em>Min Heel Volume</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getDailyCharterPrice <em>Daily Charter Price</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getBaseFuelEquivalenceFactor <em>Base Fuel Equivalence Factor</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselClassImpl extends EObjectImpl implements VesselClass {
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
	 * The default value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final long CAPACITY_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected long capacity = CAPACITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinSpeed() <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final float MIN_SPEED_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getMinSpeed() <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpeed()
	 * @generated
	 * @ordered
	 */
	protected float minSpeed = MIN_SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final float MAX_SPEED_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected float maxSpeed = MAX_SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseFuelUnitPrice() <em>Base Fuel Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_FUEL_UNIT_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseFuelUnitPrice() <em>Base Fuel Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected int baseFuelUnitPrice = BASE_FUEL_UNIT_PRICE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLadenAttributes() <em>Laden Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenAttributes()
	 * @generated
	 * @ordered
	 */
	protected VesselStateAttributes ladenAttributes;

	/**
	 * The cached value of the '{@link #getBallastAttributes() <em>Ballast Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastAttributes()
	 * @generated
	 * @ordered
	 */
	protected VesselStateAttributes ballastAttributes;

	/**
	 * The default value of the '{@link #getMinHeelVolume() <em>Min Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeelVolume()
	 * @generated
	 * @ordered
	 */
	protected static final long MIN_HEEL_VOLUME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMinHeelVolume() <em>Min Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeelVolume()
	 * @generated
	 * @ordered
	 */
	protected long minHeelVolume = MIN_HEEL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final float FILL_CAPACITY_EDEFAULT = 0.958F;

	/**
	 * The cached value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected float fillCapacity = FILL_CAPACITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getDailyCharterPrice() <em>Daily Charter Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyCharterPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int DAILY_CHARTER_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDailyCharterPrice() <em>Daily Charter Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyCharterPrice()
	 * @generated
	 * @ordered
	 */
	protected int dailyCharterPrice = DAILY_CHARTER_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_CHARTER_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected int spotCharterCount = SPOT_CHARTER_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseFuelEquivalenceFactor() <em>Base Fuel Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelEquivalenceFactor()
	 * @generated
	 * @ordered
	 */
	protected static final double BASE_FUEL_EQUIVALENCE_FACTOR_EDEFAULT = 0.5;

	/**
	 * The cached value of the '{@link #getBaseFuelEquivalenceFactor() <em>Base Fuel Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelEquivalenceFactor()
	 * @generated
	 * @ordered
	 */
	protected double baseFuelEquivalenceFactor = BASE_FUEL_EQUIVALENCE_FACTOR_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInaccessiblePorts() <em>Inaccessible Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInaccessiblePorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> inaccessiblePorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselClassImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_CLASS;
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getCapacity() {
		return capacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCapacity(long newCapacity) {
		long oldCapacity = capacity;
		capacity = newCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__CAPACITY, oldCapacity, capacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMinSpeed() {
		return minSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinSpeed(float newMinSpeed) {
		float oldMinSpeed = minSpeed;
		minSpeed = newMinSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_SPEED, oldMinSpeed, minSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxSpeed(float newMaxSpeed) {
		float oldMaxSpeed = maxSpeed;
		maxSpeed = newMaxSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MAX_SPEED, oldMaxSpeed, maxSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBaseFuelUnitPrice() {
		return baseFuelUnitPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseFuelUnitPrice(int newBaseFuelUnitPrice) {
		int oldBaseFuelUnitPrice = baseFuelUnitPrice;
		baseFuelUnitPrice = newBaseFuelUnitPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BASE_FUEL_UNIT_PRICE, oldBaseFuelUnitPrice, baseFuelUnitPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes getLadenAttributes() {
		return ladenAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLadenAttributes(VesselStateAttributes newLadenAttributes, NotificationChain msgs) {
		VesselStateAttributes oldLadenAttributes = ladenAttributes;
		ladenAttributes = newLadenAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, oldLadenAttributes, newLadenAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenAttributes(VesselStateAttributes newLadenAttributes) {
		if (newLadenAttributes != ladenAttributes) {
			NotificationChain msgs = null;
			if (ladenAttributes != null)
				msgs = ((InternalEObject)ladenAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, msgs);
			if (newLadenAttributes != null)
				msgs = ((InternalEObject)newLadenAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, msgs);
			msgs = basicSetLadenAttributes(newLadenAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, newLadenAttributes, newLadenAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes getBallastAttributes() {
		return ballastAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBallastAttributes(VesselStateAttributes newBallastAttributes, NotificationChain msgs) {
		VesselStateAttributes oldBallastAttributes = ballastAttributes;
		ballastAttributes = newBallastAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, oldBallastAttributes, newBallastAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastAttributes(VesselStateAttributes newBallastAttributes) {
		if (newBallastAttributes != ballastAttributes) {
			NotificationChain msgs = null;
			if (ballastAttributes != null)
				msgs = ((InternalEObject)ballastAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, msgs);
			if (newBallastAttributes != null)
				msgs = ((InternalEObject)newBallastAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, msgs);
			msgs = basicSetBallastAttributes(newBallastAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, newBallastAttributes, newBallastAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMinHeelVolume() {
		return minHeelVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinHeelVolume(long newMinHeelVolume) {
		long oldMinHeelVolume = minHeelVolume;
		minHeelVolume = newMinHeelVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME, oldMinHeelVolume, minHeelVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getFillCapacity() {
		return fillCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFillCapacity(float newFillCapacity) {
		float oldFillCapacity = fillCapacity;
		fillCapacity = newFillCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__FILL_CAPACITY, oldFillCapacity, fillCapacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDailyCharterPrice() {
		return dailyCharterPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDailyCharterPrice(int newDailyCharterPrice) {
		int oldDailyCharterPrice = dailyCharterPrice;
		dailyCharterPrice = newDailyCharterPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__DAILY_CHARTER_PRICE, oldDailyCharterPrice, dailyCharterPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotCharterCount() {
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotCharterCount(int newSpotCharterCount) {
		int oldSpotCharterCount = spotCharterCount;
		spotCharterCount = newSpotCharterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getBaseFuelEquivalenceFactor() {
		return baseFuelEquivalenceFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseFuelEquivalenceFactor(double newBaseFuelEquivalenceFactor) {
		double oldBaseFuelEquivalenceFactor = baseFuelEquivalenceFactor;
		baseFuelEquivalenceFactor = newBaseFuelEquivalenceFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR, oldBaseFuelEquivalenceFactor, baseFuelEquivalenceFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getInaccessiblePorts() {
		if (inaccessiblePorts == null) {
			inaccessiblePorts = new EObjectResolvingEList<Port>(Port.class, this, FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS);
		}
		return inaccessiblePorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return basicSetLadenAttributes(null, msgs);
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return basicSetBallastAttributes(null, msgs);
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
			case FleetPackage.VESSEL_CLASS__NAME:
				return getName();
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				return getCapacity();
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				return getMinSpeed();
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				return getMaxSpeed();
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_UNIT_PRICE:
				return getBaseFuelUnitPrice();
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return getLadenAttributes();
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return getBallastAttributes();
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				return getMinHeelVolume();
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				return getFillCapacity();
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_PRICE:
				return getDailyCharterPrice();
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				return getSpotCharterCount();
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR:
				return getBaseFuelEquivalenceFactor();
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				return getInaccessiblePorts();
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
			case FleetPackage.VESSEL_CLASS__NAME:
				setName((String)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				setCapacity((Long)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				setMinSpeed((Float)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				setMaxSpeed((Float)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_UNIT_PRICE:
				setBaseFuelUnitPrice((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				setMinHeelVolume((Long)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				setFillCapacity((Float)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_PRICE:
				setDailyCharterPrice((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR:
				setBaseFuelEquivalenceFactor((Double)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				getInaccessiblePorts().addAll((Collection<? extends Port>)newValue);
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
			case FleetPackage.VESSEL_CLASS__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				setCapacity(CAPACITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				setMinSpeed(MIN_SPEED_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				setMaxSpeed(MAX_SPEED_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_UNIT_PRICE:
				setBaseFuelUnitPrice(BASE_FUEL_UNIT_PRICE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				setMinHeelVolume(MIN_HEEL_VOLUME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				setFillCapacity(FILL_CAPACITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_PRICE:
				setDailyCharterPrice(DAILY_CHARTER_PRICE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				setSpotCharterCount(SPOT_CHARTER_COUNT_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR:
				setBaseFuelEquivalenceFactor(BASE_FUEL_EQUIVALENCE_FACTOR_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
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
			case FleetPackage.VESSEL_CLASS__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				return capacity != CAPACITY_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				return minSpeed != MIN_SPEED_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				return maxSpeed != MAX_SPEED_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_UNIT_PRICE:
				return baseFuelUnitPrice != BASE_FUEL_UNIT_PRICE_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return ladenAttributes != null;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return ballastAttributes != null;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				return minHeelVolume != MIN_HEEL_VOLUME_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				return fillCapacity != FILL_CAPACITY_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_PRICE:
				return dailyCharterPrice != DAILY_CHARTER_PRICE_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				return spotCharterCount != SPOT_CHARTER_COUNT_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR:
				return baseFuelEquivalenceFactor != BASE_FUEL_EQUIVALENCE_FACTOR_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				return inaccessiblePorts != null && !inaccessiblePorts.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", capacity: ");
		result.append(capacity);
		result.append(", minSpeed: ");
		result.append(minSpeed);
		result.append(", maxSpeed: ");
		result.append(maxSpeed);
		result.append(", baseFuelUnitPrice: ");
		result.append(baseFuelUnitPrice);
		result.append(", minHeelVolume: ");
		result.append(minHeelVolume);
		result.append(", fillCapacity: ");
		result.append(fillCapacity);
		result.append(", dailyCharterPrice: ");
		result.append(dailyCharterPrice);
		result.append(", spotCharterCount: ");
		result.append(spotCharterCount);
		result.append(", baseFuelEquivalenceFactor: ");
		result.append(baseFuelEquivalenceFactor);
		result.append(')');
		return result.toString();
	}

} //VesselClassImpl
