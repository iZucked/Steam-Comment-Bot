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
import scenario.cargo.LoadSlot;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getUnitPrice <em>Unit Price</em>}</li>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getPort <em>Port</em>}</li>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getWindowDuration <em>Window Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LoadSlotImpl extends EObjectImpl implements LoadSlot {
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
	protected static final long MIN_QUANTITY_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected long minQuantity = MIN_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final long MAX_QUANTITY_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected long maxQuantity = MAX_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int UNIT_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected int unitPrice = UNIT_PRICE_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LoadSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.LOAD_SLOT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMinQuantity() {
		return minQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinQuantity(long newMinQuantity) {
		long oldMinQuantity = minQuantity;
		minQuantity = newMinQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__MIN_QUANTITY, oldMinQuantity, minQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMaxQuantity() {
		return maxQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxQuantity(long newMaxQuantity) {
		long oldMaxQuantity = maxQuantity;
		maxQuantity = newMaxQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__MAX_QUANTITY, oldMaxQuantity, maxQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getUnitPrice() {
		return unitPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnitPrice(int newUnitPrice) {
		int oldUnitPrice = unitPrice;
		unitPrice = newUnitPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__UNIT_PRICE, oldUnitPrice, unitPrice));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.LOAD_SLOT__PORT, oldPort, port));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__PORT, oldPort, port));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__WINDOW_START, oldWindowStart, windowStart));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__WINDOW_DURATION, oldWindowDuration, windowDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.LOAD_SLOT__ID:
				return getId();
			case CargoPackage.LOAD_SLOT__MIN_QUANTITY:
				return getMinQuantity();
			case CargoPackage.LOAD_SLOT__MAX_QUANTITY:
				return getMaxQuantity();
			case CargoPackage.LOAD_SLOT__UNIT_PRICE:
				return getUnitPrice();
			case CargoPackage.LOAD_SLOT__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case CargoPackage.LOAD_SLOT__WINDOW_START:
				return getWindowStart();
			case CargoPackage.LOAD_SLOT__WINDOW_DURATION:
				return getWindowDuration();
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
			case CargoPackage.LOAD_SLOT__ID:
				setId((String)newValue);
				return;
			case CargoPackage.LOAD_SLOT__MIN_QUANTITY:
				setMinQuantity((Long)newValue);
				return;
			case CargoPackage.LOAD_SLOT__MAX_QUANTITY:
				setMaxQuantity((Long)newValue);
				return;
			case CargoPackage.LOAD_SLOT__UNIT_PRICE:
				setUnitPrice((Integer)newValue);
				return;
			case CargoPackage.LOAD_SLOT__PORT:
				setPort((Port)newValue);
				return;
			case CargoPackage.LOAD_SLOT__WINDOW_START:
				setWindowStart((Date)newValue);
				return;
			case CargoPackage.LOAD_SLOT__WINDOW_DURATION:
				setWindowDuration((Integer)newValue);
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
			case CargoPackage.LOAD_SLOT__ID:
				setId(ID_EDEFAULT);
				return;
			case CargoPackage.LOAD_SLOT__MIN_QUANTITY:
				setMinQuantity(MIN_QUANTITY_EDEFAULT);
				return;
			case CargoPackage.LOAD_SLOT__MAX_QUANTITY:
				setMaxQuantity(MAX_QUANTITY_EDEFAULT);
				return;
			case CargoPackage.LOAD_SLOT__UNIT_PRICE:
				setUnitPrice(UNIT_PRICE_EDEFAULT);
				return;
			case CargoPackage.LOAD_SLOT__PORT:
				setPort((Port)null);
				return;
			case CargoPackage.LOAD_SLOT__WINDOW_START:
				setWindowStart(WINDOW_START_EDEFAULT);
				return;
			case CargoPackage.LOAD_SLOT__WINDOW_DURATION:
				setWindowDuration(WINDOW_DURATION_EDEFAULT);
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
			case CargoPackage.LOAD_SLOT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case CargoPackage.LOAD_SLOT__MIN_QUANTITY:
				return minQuantity != MIN_QUANTITY_EDEFAULT;
			case CargoPackage.LOAD_SLOT__MAX_QUANTITY:
				return maxQuantity != MAX_QUANTITY_EDEFAULT;
			case CargoPackage.LOAD_SLOT__UNIT_PRICE:
				return unitPrice != UNIT_PRICE_EDEFAULT;
			case CargoPackage.LOAD_SLOT__PORT:
				return port != null;
			case CargoPackage.LOAD_SLOT__WINDOW_START:
				return WINDOW_START_EDEFAULT == null ? windowStart != null : !WINDOW_START_EDEFAULT.equals(windowStart);
			case CargoPackage.LOAD_SLOT__WINDOW_DURATION:
				return windowDuration != WINDOW_DURATION_EDEFAULT;
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
		result.append(", unitPrice: ");
		result.append(unitPrice);
		result.append(", windowStart: ");
		result.append(windowStart);
		result.append(", windowDuration: ");
		result.append(windowDuration);
		result.append(')');
		return result.toString();
	}

} //LoadSlotImpl
