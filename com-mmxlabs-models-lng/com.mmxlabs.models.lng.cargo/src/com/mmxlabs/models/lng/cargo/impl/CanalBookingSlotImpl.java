/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;

import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal Booking Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getEntryPoint <em>Entry Point</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getBookingDate <em>Booking Date</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CanalBookingSlotImpl extends MMXObjectImpl implements CanalBookingSlot {
	/**
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected Route route;

	/**
	 * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot slot;

	/**
	 * The cached value of the '{@link #getEntryPoint() <em>Entry Point</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryPoint()
	 * @generated
	 * @ordered
	 */
	protected EntryPoint entryPoint;

	/**
	 * The default value of the '{@link #getBookingDate() <em>Booking Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBookingDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate BOOKING_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBookingDate() <em>Booking Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBookingDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate bookingDate = BOOKING_DATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalBookingSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CANAL_BOOKING_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route getRoute() {
		if (route != null && route.eIsProxy()) {
			InternalEObject oldRoute = (InternalEObject)route;
			route = (Route)eResolveProxy(oldRoute);
			if (route != oldRoute) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CANAL_BOOKING_SLOT__ROUTE, oldRoute, route));
			}
		}
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route basicGetRoute() {
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRoute(Route newRoute) {
		Route oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Slot)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CANAL_BOOKING_SLOT__SLOT, oldSlot, slot));
			}
		}
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetSlot() {
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlot(Slot newSlot) {
		Slot oldSlot = slot;
		slot = newSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__SLOT, oldSlot, slot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryPoint getEntryPoint() {
		if (entryPoint != null && entryPoint.eIsProxy()) {
			InternalEObject oldEntryPoint = (InternalEObject)entryPoint;
			entryPoint = (EntryPoint)eResolveProxy(oldEntryPoint);
			if (entryPoint != oldEntryPoint) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CANAL_BOOKING_SLOT__ENTRY_POINT, oldEntryPoint, entryPoint));
			}
		}
		return entryPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryPoint basicGetEntryPoint() {
		return entryPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntryPoint(EntryPoint newEntryPoint) {
		EntryPoint oldEntryPoint = entryPoint;
		entryPoint = newEntryPoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__ENTRY_POINT, oldEntryPoint, entryPoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getBookingDate() {
		return bookingDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBookingDate(LocalDate newBookingDate) {
		LocalDate oldBookingDate = bookingDate;
		bookingDate = newBookingDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE, oldBookingDate, bookingDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getBookingDateAsDateTime() {
		
		final LocalDate bookingDate = getBookingDate();
		if (bookingDate == null) {
			return null;
		}
		ZonedDateTime dateTime = bookingDate.atStartOfDay(ZoneId.of(getTimeZone(CargoPackage.eINSTANCE.getCanalBookingSlot_BookingDate())));
		return dateTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getTimeZone(EAttribute attribute) {
		final EntryPoint entryPoint = getEntryPoint();
		if (entryPoint != null) {
			final Port p = entryPoint.getPort();
			if (p!= null) {
				if (p.getTimeZone() != null && !p.getTimeZone().isEmpty()) {
					return p.getTimeZone();
				}
			}
		}
		return "UTC";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE:
				if (resolve) return getRoute();
				return basicGetRoute();
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case CargoPackage.CANAL_BOOKING_SLOT__ENTRY_POINT:
				if (resolve) return getEntryPoint();
				return basicGetEntryPoint();
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				return getBookingDate();
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
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE:
				setRoute((Route)newValue);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				setSlot((Slot)newValue);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__ENTRY_POINT:
				setEntryPoint((EntryPoint)newValue);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				setBookingDate((LocalDate)newValue);
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
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE:
				setRoute((Route)null);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				setSlot((Slot)null);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__ENTRY_POINT:
				setEntryPoint((EntryPoint)null);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				setBookingDate(BOOKING_DATE_EDEFAULT);
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
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE:
				return route != null;
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				return slot != null;
			case CargoPackage.CANAL_BOOKING_SLOT__ENTRY_POINT:
				return entryPoint != null;
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				return BOOKING_DATE_EDEFAULT == null ? bookingDate != null : !BOOKING_DATE_EDEFAULT.equals(bookingDate);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ITimezoneProvider.class) {
			switch (baseOperationID) {
				case TypesPackage.ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE: return CargoPackage.CANAL_BOOKING_SLOT___GET_TIME_ZONE__EATTRIBUTE;
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.CANAL_BOOKING_SLOT___GET_BOOKING_DATE_AS_DATE_TIME:
				return getBookingDateAsDateTime();
			case CargoPackage.CANAL_BOOKING_SLOT___GET_TIME_ZONE__EATTRIBUTE:
				return getTimeZone((EAttribute)arguments.get(0));
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (bookingDate: ");
		result.append(bookingDate);
		result.append(')');
		return result.toString();
	}

} //CanalBookingSlotImpl
