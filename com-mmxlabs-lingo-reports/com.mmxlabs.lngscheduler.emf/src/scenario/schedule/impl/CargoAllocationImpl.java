/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.impl;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;

import scenario.schedule.CargoAllocation;
import scenario.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getFuelVolume <em>Fuel Volume</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getDischargeVolume <em>Discharge Volume</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getLoadDate <em>Load Date</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getDischargeDate <em>Discharge Date</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getLoadPriceM3 <em>Load Price M3</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getDischargePriceM3 <em>Discharge Price M3</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoAllocationImpl extends EObjectImpl implements CargoAllocation {
	/**
	 * The cached value of the '{@link #getLoadSlot() <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot loadSlot;

	/**
	 * The cached value of the '{@link #getDischargeSlot() <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot dischargeSlot;

	/**
	 * The default value of the '{@link #getFuelVolume() <em>Fuel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelVolume()
	 * @generated
	 * @ordered
	 */
	protected static final long FUEL_VOLUME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getFuelVolume() <em>Fuel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelVolume()
	 * @generated
	 * @ordered
	 */
	protected long fuelVolume = FUEL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected static final long DISCHARGE_VOLUME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected long dischargeVolume = DISCHARGE_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getLoadDate() <em>Load Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date LOAD_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLoadDate() <em>Load Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadDate()
	 * @generated
	 * @ordered
	 */
	protected Date loadDate = LOAD_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDischargeDate() <em>Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DISCHARGE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDischargeDate() <em>Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeDate()
	 * @generated
	 * @ordered
	 */
	protected Date dischargeDate = DISCHARGE_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLoadPriceM3() <em>Load Price M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadPriceM3()
	 * @generated
	 * @ordered
	 */
	protected static final int LOAD_PRICE_M3_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLoadPriceM3() <em>Load Price M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadPriceM3()
	 * @generated
	 * @ordered
	 */
	protected int loadPriceM3 = LOAD_PRICE_M3_EDEFAULT;

	/**
	 * The default value of the '{@link #getDischargePriceM3() <em>Discharge Price M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargePriceM3()
	 * @generated
	 * @ordered
	 */
	protected static final int DISCHARGE_PRICE_M3_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDischargePriceM3() <em>Discharge Price M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargePriceM3()
	 * @generated
	 * @ordered
	 */
	protected int dischargePriceM3 = DISCHARGE_PRICE_M3_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CARGO_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot getLoadSlot() {
		if (loadSlot != null && loadSlot.eIsProxy()) {
			InternalEObject oldLoadSlot = (InternalEObject)loadSlot;
			loadSlot = (LoadSlot)eResolveProxy(oldLoadSlot);
			if (loadSlot != oldLoadSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__LOAD_SLOT, oldLoadSlot, loadSlot));
			}
		}
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetLoadSlot() {
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadSlot(LoadSlot newLoadSlot) {
		LoadSlot oldLoadSlot = loadSlot;
		loadSlot = newLoadSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LOAD_SLOT, oldLoadSlot, loadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot getDischargeSlot() {
		if (dischargeSlot != null && dischargeSlot.eIsProxy()) {
			InternalEObject oldDischargeSlot = (InternalEObject)dischargeSlot;
			dischargeSlot = (Slot)eResolveProxy(oldDischargeSlot);
			if (dischargeSlot != oldDischargeSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
			}
		}
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetDischargeSlot() {
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeSlot(Slot newDischargeSlot) {
		Slot oldDischargeSlot = dischargeSlot;
		dischargeSlot = newDischargeSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getFuelVolume() {
		return fuelVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFuelVolume(long newFuelVolume) {
		long oldFuelVolume = fuelVolume;
		fuelVolume = newFuelVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__FUEL_VOLUME, oldFuelVolume, fuelVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getDischargeVolume() {
		return dischargeVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeVolume(long newDischargeVolume) {
		long oldDischargeVolume = dischargeVolume;
		dischargeVolume = newDischargeVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME, oldDischargeVolume, dischargeVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLoadDate() {
		return loadDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadDate(Date newLoadDate) {
		Date oldLoadDate = loadDate;
		loadDate = newLoadDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LOAD_DATE, oldLoadDate, loadDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getDischargeDate() {
		return dischargeDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeDate(Date newDischargeDate) {
		Date oldDischargeDate = dischargeDate;
		dischargeDate = newDischargeDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_DATE, oldDischargeDate, dischargeDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLoadPriceM3() {
		return loadPriceM3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadPriceM3(int newLoadPriceM3) {
		int oldLoadPriceM3 = loadPriceM3;
		loadPriceM3 = newLoadPriceM3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LOAD_PRICE_M3, oldLoadPriceM3, loadPriceM3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDischargePriceM3() {
		return dischargePriceM3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargePriceM3(int newDischargePriceM3) {
		int oldDischargePriceM3 = dischargePriceM3;
		dischargePriceM3 = newDischargePriceM3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_PRICE_M3, oldDischargePriceM3, dischargePriceM3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.CARGO_ALLOCATION__LOAD_SLOT:
				if (resolve) return getLoadSlot();
				return basicGetLoadSlot();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_SLOT:
				if (resolve) return getDischargeSlot();
				return basicGetDischargeSlot();
			case SchedulePackage.CARGO_ALLOCATION__FUEL_VOLUME:
				return getFuelVolume();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				return getDischargeVolume();
			case SchedulePackage.CARGO_ALLOCATION__LOAD_DATE:
				return getLoadDate();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_DATE:
				return getDischargeDate();
			case SchedulePackage.CARGO_ALLOCATION__LOAD_PRICE_M3:
				return getLoadPriceM3();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_PRICE_M3:
				return getDischargePriceM3();
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
			case SchedulePackage.CARGO_ALLOCATION__LOAD_SLOT:
				setLoadSlot((LoadSlot)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_SLOT:
				setDischargeSlot((Slot)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__FUEL_VOLUME:
				setFuelVolume((Long)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				setDischargeVolume((Long)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_DATE:
				setLoadDate((Date)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_DATE:
				setDischargeDate((Date)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_PRICE_M3:
				setLoadPriceM3((Integer)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_PRICE_M3:
				setDischargePriceM3((Integer)newValue);
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
			case SchedulePackage.CARGO_ALLOCATION__LOAD_SLOT:
				setLoadSlot((LoadSlot)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_SLOT:
				setDischargeSlot((Slot)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__FUEL_VOLUME:
				setFuelVolume(FUEL_VOLUME_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				setDischargeVolume(DISCHARGE_VOLUME_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_DATE:
				setLoadDate(LOAD_DATE_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_DATE:
				setDischargeDate(DISCHARGE_DATE_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_PRICE_M3:
				setLoadPriceM3(LOAD_PRICE_M3_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_PRICE_M3:
				setDischargePriceM3(DISCHARGE_PRICE_M3_EDEFAULT);
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
			case SchedulePackage.CARGO_ALLOCATION__LOAD_SLOT:
				return loadSlot != null;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_SLOT:
				return dischargeSlot != null;
			case SchedulePackage.CARGO_ALLOCATION__FUEL_VOLUME:
				return fuelVolume != FUEL_VOLUME_EDEFAULT;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				return dischargeVolume != DISCHARGE_VOLUME_EDEFAULT;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_DATE:
				return LOAD_DATE_EDEFAULT == null ? loadDate != null : !LOAD_DATE_EDEFAULT.equals(loadDate);
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_DATE:
				return DISCHARGE_DATE_EDEFAULT == null ? dischargeDate != null : !DISCHARGE_DATE_EDEFAULT.equals(dischargeDate);
			case SchedulePackage.CARGO_ALLOCATION__LOAD_PRICE_M3:
				return loadPriceM3 != LOAD_PRICE_M3_EDEFAULT;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_PRICE_M3:
				return dischargePriceM3 != DISCHARGE_PRICE_M3_EDEFAULT;
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
		result.append(" (fuelVolume: ");
		result.append(fuelVolume);
		result.append(", dischargeVolume: ");
		result.append(dischargeVolume);
		result.append(", loadDate: ");
		result.append(loadDate);
		result.append(", dischargeDate: ");
		result.append(dischargeDate);
		result.append(", loadPriceM3: ");
		result.append(loadPriceM3);
		result.append(", dischargePriceM3: ");
		result.append(dischargePriceM3);
		result.append(')');
		return result.toString();
	}

} //CargoAllocationImpl
