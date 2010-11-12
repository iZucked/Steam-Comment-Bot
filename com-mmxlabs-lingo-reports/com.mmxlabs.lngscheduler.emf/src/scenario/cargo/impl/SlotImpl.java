/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo.impl;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;

import scenario.market.Market;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot</b></em>'.
 * <!-- end-user-doc -->
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
 *   <li>{@link scenario.cargo.impl.SlotImpl#getMarket <em>Market</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SlotImpl extends EObjectImpl implements Slot {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected int minQuantity = MIN_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected int maxQuantity = MAX_QUANTITY_EDEFAULT;

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
	 * The default value of the '{@link #getWindowStart() <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowStart()
	 * @generated
	 * @ordered
	 */
	protected static final Date WINDOW_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWindowStart() <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowStart()
	 * @generated
	 * @ordered
	 */
	protected Date windowStart = WINDOW_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowDuration() <em>Window Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowDuration() <em>Window Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowDuration()
	 * @generated
	 * @ordered
	 */
	protected int windowDuration = WINDOW_DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getSlotDuration() <em>Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int SLOT_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSlotDuration() <em>Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotDuration()
	 * @generated
	 * @ordered
	 */
	protected int slotDuration = SLOT_DURATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected Market market;

	/**
	 * This is true if the Market reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean marketESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinQuantity() {
		return minQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinQuantity(int newMinQuantity) {
		int oldMinQuantity = minQuantity;
		minQuantity = newMinQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MIN_QUANTITY, oldMinQuantity, minQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxQuantity() {
		return maxQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxQuantity(int newMaxQuantity) {
		int oldMaxQuantity = maxQuantity;
		maxQuantity = newMaxQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MAX_QUANTITY, oldMaxQuantity, maxQuantity));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__PORT, oldPort, port));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getWindowStart() {
		return windowStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindowStart(Date newWindowStart) {
		Date oldWindowStart = windowStart;
		windowStart = newWindowStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_START, oldWindowStart, windowStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWindowDuration() {
		return windowDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindowDuration(int newWindowDuration) {
		int oldWindowDuration = windowDuration;
		windowDuration = newWindowDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__WINDOW_DURATION, oldWindowDuration, windowDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSlotDuration() {
		return slotDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlotDuration(int newSlotDuration) {
		int oldSlotDuration = slotDuration;
		slotDuration = newSlotDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__SLOT_DURATION, oldSlotDuration, slotDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Market getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (Market)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SLOT__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Market basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarket(Market newMarket) {
		Market oldMarket = market;
		market = newMarket;
		boolean oldMarketESet = marketESet;
		marketESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SLOT__MARKET, oldMarket, market, !oldMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMarket() {
		Market oldMarket = market;
		boolean oldMarketESet = marketESet;
		market = null;
		marketESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.SLOT__MARKET, oldMarket, null, oldMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMarket() {
		return marketESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
			case CargoPackage.SLOT__MARKET:
				if (resolve) return getMarket();
				return basicGetMarket();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
				setWindowStart((Date)newValue);
				return;
			case CargoPackage.SLOT__WINDOW_DURATION:
				setWindowDuration((Integer)newValue);
				return;
			case CargoPackage.SLOT__SLOT_DURATION:
				setSlotDuration((Integer)newValue);
				return;
			case CargoPackage.SLOT__MARKET:
				setMarket((Market)newValue);
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
			case CargoPackage.SLOT__ID:
				setId(ID_EDEFAULT);
				return;
			case CargoPackage.SLOT__MIN_QUANTITY:
				setMinQuantity(MIN_QUANTITY_EDEFAULT);
				return;
			case CargoPackage.SLOT__MAX_QUANTITY:
				setMaxQuantity(MAX_QUANTITY_EDEFAULT);
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
				setSlotDuration(SLOT_DURATION_EDEFAULT);
				return;
			case CargoPackage.SLOT__MARKET:
				unsetMarket();
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
			case CargoPackage.SLOT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case CargoPackage.SLOT__MIN_QUANTITY:
				return minQuantity != MIN_QUANTITY_EDEFAULT;
			case CargoPackage.SLOT__MAX_QUANTITY:
				return maxQuantity != MAX_QUANTITY_EDEFAULT;
			case CargoPackage.SLOT__PORT:
				return port != null;
			case CargoPackage.SLOT__WINDOW_START:
				return WINDOW_START_EDEFAULT == null ? windowStart != null : !WINDOW_START_EDEFAULT.equals(windowStart);
			case CargoPackage.SLOT__WINDOW_DURATION:
				return windowDuration != WINDOW_DURATION_EDEFAULT;
			case CargoPackage.SLOT__SLOT_DURATION:
				return slotDuration != SLOT_DURATION_EDEFAULT;
			case CargoPackage.SLOT__MARKET:
				return isSetMarket();
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
		result.append(" (id: ");
		result.append(id);
		result.append(", minQuantity: ");
		result.append(minQuantity);
		result.append(", maxQuantity: ");
		result.append(maxQuantity);
		result.append(", windowStart: ");
		result.append(windowStart);
		result.append(", windowDuration: ");
		result.append(windowDuration);
		result.append(", slotDuration: ");
		result.append(slotDuration);
		result.append(')');
		return result.toString();
	}

} //SlotImpl
