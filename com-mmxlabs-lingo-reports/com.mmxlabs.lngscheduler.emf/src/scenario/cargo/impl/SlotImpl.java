/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.cargo.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;
import scenario.contract.Contract;
import scenario.port.Port;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Slot</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getPort <em>Port</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getWindowDuration <em>Window Duration</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getSlotDuration <em>Slot Duration</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getFixedPrice <em>Fixed Price</em>}</li>
 *   <li>{@link scenario.cargo.impl.SlotImpl#getContract <em>Contract</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SlotImpl extends EObjectImpl implements Slot {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected int minQuantity = MIN_QUANTITY_EDEFAULT;

	/**
	 * This is true if the Min Quantity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minQuantityESet;

	/**
	 * The default value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_QUANTITY_EDEFAULT = 2147483647;

	/**
	 * The cached value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected int maxQuantity = MAX_QUANTITY_EDEFAULT;

	/**
	 * This is true if the Max Quantity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxQuantityESet;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The default value of the '{@link #getWindowStart() <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWindowStart()
	 * @generated
	 * @ordered
	 */
	protected static final DateAndOptionalTime WINDOW_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWindowStart() <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWindowStart()
	 * @generated
	 * @ordered
	 */
	protected DateAndOptionalTime windowStart = WINDOW_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowDuration() <em>Window Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getWindowDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_DURATION_EDEFAULT = 24;

	/**
	 * The cached value of the '{@link #getWindowDuration() <em>Window Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getWindowDuration()
	 * @generated
	 * @ordered
	 */
	protected int windowDuration = WINDOW_DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getSlotDuration() <em>Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSlotDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int SLOT_DURATION_EDEFAULT = 6;

	/**
	 * The cached value of the '{@link #getSlotDuration() <em>Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSlotDuration()
	 * @generated
	 * @ordered
	 */
	protected int slotDuration = SLOT_DURATION_EDEFAULT;

	/**
	 * This is true if the Slot Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean slotDurationESet;

	/**
	 * The default value of the '{@link #getFixedPrice() <em>Fixed Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFixedPrice()
	 * @generated
	 * @ordered
	 */
	protected static final float FIXED_PRICE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getFixedPrice() <em>Fixed Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFixedPrice()
	 * @generated
	 * @ordered
	 */
	protected float fixedPrice = FIXED_PRICE_EDEFAULT;

	/**
	 * This is true if the Fixed Price attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean fixedPriceESet;

	/**
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected Contract contract;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.SLOT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinQuantity() {
		return minQuantity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinQuantity(int newMinQuantity) {
		int oldMinQuantity = minQuantity;
		minQuantity = newMinQuantity;
		boolean oldMinQuantityESet = minQuantityESet;
		minQuantityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MIN_QUANTITY, oldMinQuantity, minQuantity, !oldMinQuantityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMinQuantity() {
		int oldMinQuantity = minQuantity;
		boolean oldMinQuantityESet = minQuantityESet;
		minQuantity = MIN_QUANTITY_EDEFAULT;
		minQuantityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__MIN_QUANTITY, oldMinQuantity, MIN_QUANTITY_EDEFAULT, oldMinQuantityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMinQuantity() {
		return minQuantityESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxQuantity() {
		return maxQuantity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxQuantity(int newMaxQuantity) {
		int oldMaxQuantity = maxQuantity;
		maxQuantity = newMaxQuantity;
		boolean oldMaxQuantityESet = maxQuantityESet;
		maxQuantityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MAX_QUANTITY, oldMaxQuantity, maxQuantity, !oldMaxQuantityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMaxQuantity() {
		int oldMaxQuantity = maxQuantity;
		boolean oldMaxQuantityESet = maxQuantityESet;
		maxQuantity = MAX_QUANTITY_EDEFAULT;
		maxQuantityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__MAX_QUANTITY, oldMaxQuantity, MAX_QUANTITY_EDEFAULT, oldMaxQuantityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMaxQuantity() {
		return maxQuantityESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DateAndOptionalTime getWindowStart() {
		return windowStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindowStart(DateAndOptionalTime newWindowStart) {
		DateAndOptionalTime oldWindowStart = windowStart;
		windowStart = newWindowStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_START, oldWindowStart, windowStart));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getWindowDuration() {
		return windowDuration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindowDuration(int newWindowDuration) {
		int oldWindowDuration = windowDuration;
		windowDuration = newWindowDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_DURATION, oldWindowDuration, windowDuration));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getSlotDuration() {
		return slotDuration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlotDuration(int newSlotDuration) {
		int oldSlotDuration = slotDuration;
		slotDuration = newSlotDuration;
		boolean oldSlotDurationESet = slotDurationESet;
		slotDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__SLOT_DURATION, oldSlotDuration, slotDuration, !oldSlotDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSlotDuration() {
		int oldSlotDuration = slotDuration;
		boolean oldSlotDurationESet = slotDurationESet;
		slotDuration = SLOT_DURATION_EDEFAULT;
		slotDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__SLOT_DURATION, oldSlotDuration, SLOT_DURATION_EDEFAULT, oldSlotDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSlotDuration() {
		return slotDurationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public float getFixedPrice() {
		return fixedPrice;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setFixedPrice(float newFixedPrice) {
		float oldFixedPrice = fixedPrice;
		fixedPrice = newFixedPrice;
		boolean oldFixedPriceESet = fixedPriceESet;
		fixedPriceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__FIXED_PRICE, oldFixedPrice, fixedPrice, !oldFixedPriceESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFixedPrice() {
		float oldFixedPrice = fixedPrice;
		boolean oldFixedPriceESet = fixedPriceESet;
		fixedPrice = FIXED_PRICE_EDEFAULT;
		fixedPriceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__FIXED_PRICE, oldFixedPrice, FIXED_PRICE_EDEFAULT, oldFixedPriceESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFixedPrice() {
		return fixedPriceESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Contract getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (Contract)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Contract basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setContract(Contract newContract) {
		Contract oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object getLocalWindowStart() {
		final java.util.Calendar calendar = java.util.Calendar.getInstance(
		java.util.TimeZone.getTimeZone(getPort().getTimeZone())
		);
		calendar.setTime(getWindowStart());
		return calendar;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Date getWindowEnd() {
		return new Date(getWindowStart()
								.getTime()
								+ javax.management.timer.Timer.ONE_HOUR
								* getWindowDuration());
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Contract getSlotOrPortContract(Object scenario) {
		if (getContract() != null)
					return getContract();
		else if (getPort() != null) {
					final scenario.port.Port p = getPort();
					if (scenario instanceof scenario.Scenario) {
		final scenario.Scenario scenario2 = (scenario.Scenario) scenario;
					if (scenario2.getContractModel() != null) {
						final scenario.contract.ContractModel cm = scenario2.getContractModel();
						return cm.getDefaultContract(p);
					}
		}
					return null;
		
		} else
					return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSlotOrPortDuration() {
		if (isSetSlotDuration())
			return getSlotDuration();
		else
			return getPort().getDefaultDischargeDuration();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSlotOrContractMinQuantity(Object scenario) {
		if (isSetMinQuantity())
			return getMinQuantity();
		else
			return getSlotOrPortContract(scenario).getMinQuantity();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSlotOrContractMaxQuantity(Object scenario) {
		if (isSetMaxQuantity())
			return getMaxQuantity();
		else
			return getSlotOrPortContract(scenario).getMaxQuantity();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.SLOT__ID:
				return getId();
			case CargoPackage.SLOT__MIN_QUANTITY:
				return getMinQuantity();
			case CargoPackage.SLOT__MAX_QUANTITY:
				return getMaxQuantity();
			case CargoPackage.SLOT__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case CargoPackage.SLOT__WINDOW_START:
				return getWindowStart();
			case CargoPackage.SLOT__WINDOW_DURATION:
				return getWindowDuration();
			case CargoPackage.SLOT__SLOT_DURATION:
				return getSlotDuration();
			case CargoPackage.SLOT__FIXED_PRICE:
				return getFixedPrice();
			case CargoPackage.SLOT__CONTRACT:
				if (resolve) return getContract();
				return basicGetContract();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CargoPackage.SLOT__ID:
				setId((String)newValue);
				return;
			case CargoPackage.SLOT__MIN_QUANTITY:
				setMinQuantity((Integer)newValue);
				return;
			case CargoPackage.SLOT__MAX_QUANTITY:
				setMaxQuantity((Integer)newValue);
				return;
			case CargoPackage.SLOT__PORT:
				setPort((Port)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_START:
				setWindowStart((DateAndOptionalTime)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_DURATION:
				setWindowDuration((Integer)newValue);
				return;
			case CargoPackage.SLOT__SLOT_DURATION:
				setSlotDuration((Integer)newValue);
				return;
			case CargoPackage.SLOT__FIXED_PRICE:
				setFixedPrice((Float)newValue);
				return;
			case CargoPackage.SLOT__CONTRACT:
				setContract((Contract)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CargoPackage.SLOT__ID:
				setId(ID_EDEFAULT);
				return;
			case CargoPackage.SLOT__MIN_QUANTITY:
				unsetMinQuantity();
				return;
			case CargoPackage.SLOT__MAX_QUANTITY:
				unsetMaxQuantity();
				return;
			case CargoPackage.SLOT__PORT:
				setPort((Port)null);
				return;
			case CargoPackage.SLOT__WINDOW_START:
				setWindowStart(WINDOW_START_EDEFAULT);
				return;
			case CargoPackage.SLOT__WINDOW_DURATION:
				setWindowDuration(WINDOW_DURATION_EDEFAULT);
				return;
			case CargoPackage.SLOT__SLOT_DURATION:
				unsetSlotDuration();
				return;
			case CargoPackage.SLOT__FIXED_PRICE:
				unsetFixedPrice();
				return;
			case CargoPackage.SLOT__CONTRACT:
				setContract((Contract)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CargoPackage.SLOT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case CargoPackage.SLOT__MIN_QUANTITY:
				return isSetMinQuantity();
			case CargoPackage.SLOT__MAX_QUANTITY:
				return isSetMaxQuantity();
			case CargoPackage.SLOT__PORT:
				return port != null;
			case CargoPackage.SLOT__WINDOW_START:
				return WINDOW_START_EDEFAULT == null ? windowStart != null : !WINDOW_START_EDEFAULT.equals(windowStart);
			case CargoPackage.SLOT__WINDOW_DURATION:
				return windowDuration != WINDOW_DURATION_EDEFAULT;
			case CargoPackage.SLOT__SLOT_DURATION:
				return isSetSlotDuration();
			case CargoPackage.SLOT__FIXED_PRICE:
				return isSetFixedPrice();
			case CargoPackage.SLOT__CONTRACT:
				return contract != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments)
			throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.SLOT___GET_LOCAL_WINDOW_START:
				return getLocalWindowStart();
			case CargoPackage.SLOT___GET_WINDOW_END:
				return getWindowEnd();
			case CargoPackage.SLOT___GET_SLOT_OR_PORT_CONTRACT__OBJECT:
				return getSlotOrPortContract(arguments.get(0));
			case CargoPackage.SLOT___GET_SLOT_OR_PORT_DURATION:
				return getSlotOrPortDuration();
			case CargoPackage.SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY__OBJECT:
				return getSlotOrContractMinQuantity(arguments.get(0));
			case CargoPackage.SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY__OBJECT:
				return getSlotOrContractMaxQuantity(arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (id: ");
		result.append(id);
		result.append(", minQuantity: ");
		if (minQuantityESet) result.append(minQuantity); else result.append("<unset>");
		result.append(", maxQuantity: ");
		if (maxQuantityESet) result.append(maxQuantity); else result.append("<unset>");
		result.append(", windowStart: ");
		result.append(windowStart);
		result.append(", windowDuration: ");
		result.append(windowDuration);
		result.append(", slotDuration: ");
		if (slotDurationESet) result.append(slotDuration); else result.append("<unset>");
		result.append(", fixedPrice: ");
		if (fixedPriceESet) result.append(fixedPrice); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // SlotImpl