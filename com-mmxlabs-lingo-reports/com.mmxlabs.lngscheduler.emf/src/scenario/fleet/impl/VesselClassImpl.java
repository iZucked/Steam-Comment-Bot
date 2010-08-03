/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;

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
	protected static final int MIN_SPEED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinSpeed() <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpeed()
	 * @generated
	 * @ordered
	 */
	protected int minSpeed = MIN_SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_SPEED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected int maxSpeed = MAX_SPEED_EDEFAULT;

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
	public int getMinSpeed() {
		return minSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinSpeed(int newMinSpeed) {
		int oldMinSpeed = minSpeed;
		minSpeed = newMinSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_SPEED, oldMinSpeed, minSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxSpeed(int newMaxSpeed) {
		int oldMaxSpeed = maxSpeed;
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
				setMinSpeed((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				setMaxSpeed((Integer)newValue);
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
		result.append(')');
		return result.toString();
	}

} //VesselClassImpl
