/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;

import scenario.schedule.CargoAllocation;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.fleetallocation.AllocatedVessel;

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
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getLadenLeg <em>Laden Leg</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getBallastLeg <em>Ballast Leg</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getLadenIdle <em>Laden Idle</em>}</li>
 *   <li>{@link scenario.schedule.impl.CargoAllocationImpl#getBallastIdle <em>Ballast Idle</em>}</li>
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
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected AllocatedVessel vessel;

	/**
	 * The cached value of the '{@link #getLadenLeg() <em>Laden Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenLeg()
	 * @generated
	 * @ordered
	 */
	protected Journey ladenLeg;

	/**
	 * The cached value of the '{@link #getBallastLeg() <em>Ballast Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastLeg()
	 * @generated
	 * @ordered
	 */
	protected Journey ballastLeg;

	/**
	 * The cached value of the '{@link #getLadenIdle() <em>Laden Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenIdle()
	 * @generated
	 * @ordered
	 */
	protected Idle ladenIdle;

	/**
	 * The cached value of the '{@link #getBallastIdle() <em>Ballast Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastIdle()
	 * @generated
	 * @ordered
	 */
	protected Idle ballastIdle;

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
	public AllocatedVessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (AllocatedVessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocatedVessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVessel(AllocatedVessel newVessel) {
		AllocatedVessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey getLadenLeg() {
		if (ladenLeg != null && ladenLeg.eIsProxy()) {
			InternalEObject oldLadenLeg = (InternalEObject)ladenLeg;
			ladenLeg = (Journey)eResolveProxy(oldLadenLeg);
			if (ladenLeg != oldLadenLeg) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__LADEN_LEG, oldLadenLeg, ladenLeg));
			}
		}
		return ladenLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey basicGetLadenLeg() {
		return ladenLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenLeg(Journey newLadenLeg) {
		Journey oldLadenLeg = ladenLeg;
		ladenLeg = newLadenLeg;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LADEN_LEG, oldLadenLeg, ladenLeg));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey getBallastLeg() {
		if (ballastLeg != null && ballastLeg.eIsProxy()) {
			InternalEObject oldBallastLeg = (InternalEObject)ballastLeg;
			ballastLeg = (Journey)eResolveProxy(oldBallastLeg);
			if (ballastLeg != oldBallastLeg) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG, oldBallastLeg, ballastLeg));
			}
		}
		return ballastLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey basicGetBallastLeg() {
		return ballastLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastLeg(Journey newBallastLeg) {
		Journey oldBallastLeg = ballastLeg;
		ballastLeg = newBallastLeg;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG, oldBallastLeg, ballastLeg));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle getLadenIdle() {
		if (ladenIdle != null && ladenIdle.eIsProxy()) {
			InternalEObject oldLadenIdle = (InternalEObject)ladenIdle;
			ladenIdle = (Idle)eResolveProxy(oldLadenIdle);
			if (ladenIdle != oldLadenIdle) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE, oldLadenIdle, ladenIdle));
			}
		}
		return ladenIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle basicGetLadenIdle() {
		return ladenIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenIdle(Idle newLadenIdle) {
		Idle oldLadenIdle = ladenIdle;
		ladenIdle = newLadenIdle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE, oldLadenIdle, ladenIdle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle getBallastIdle() {
		if (ballastIdle != null && ballastIdle.eIsProxy()) {
			InternalEObject oldBallastIdle = (InternalEObject)ballastIdle;
			ballastIdle = (Idle)eResolveProxy(oldBallastIdle);
			if (ballastIdle != oldBallastIdle) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE, oldBallastIdle, ballastIdle));
			}
		}
		return ballastIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle basicGetBallastIdle() {
		return ballastIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastIdle(Idle newBallastIdle) {
		Idle oldBallastIdle = ballastIdle;
		ballastIdle = newBallastIdle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE, oldBallastIdle, ballastIdle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getTotalCost() {
		return getLadenLeg().getTotalCost() + getBallastLeg().getTotalCost()
			+ getLadenIdle().getTotalCost() + getBallastIdle().getTotalCost();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getLocalLoadDate() {
		final java.util.Calendar calendar = java.util.Calendar.getInstance(
		java.util.TimeZone.getTimeZone(getLoadSlot().getPort().getTimeZone())
		);
		calendar.setTime(getLoadDate());
		return calendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getLocalDischargeDate() {
		final java.util.Calendar calendar = java.util.Calendar.getInstance(
		java.util.TimeZone.getTimeZone(getDischargeSlot().getPort().getTimeZone())
		);
		calendar.setTime(getDischargeDate());
		return calendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getLoadVolume() {
		return getDischargeVolume() + getFuelVolume();
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
			case SchedulePackage.CARGO_ALLOCATION__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				if (resolve) return getLadenLeg();
				return basicGetLadenLeg();
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				if (resolve) return getBallastLeg();
				return basicGetBallastLeg();
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				if (resolve) return getLadenIdle();
				return basicGetLadenIdle();
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				if (resolve) return getBallastIdle();
				return basicGetBallastIdle();
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
			case SchedulePackage.CARGO_ALLOCATION__VESSEL:
				setVessel((AllocatedVessel)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				setLadenLeg((Journey)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				setBallastLeg((Journey)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				setLadenIdle((Idle)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				setBallastIdle((Idle)newValue);
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
			case SchedulePackage.CARGO_ALLOCATION__VESSEL:
				setVessel((AllocatedVessel)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				setLadenLeg((Journey)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				setBallastLeg((Journey)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				setLadenIdle((Idle)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				setBallastIdle((Idle)null);
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
			case SchedulePackage.CARGO_ALLOCATION__VESSEL:
				return vessel != null;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				return ladenLeg != null;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				return ballastLeg != null;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				return ladenIdle != null;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				return ballastIdle != null;
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
			case SchedulePackage.CARGO_ALLOCATION___GET_TOTAL_COST:
				return getTotalCost();
			case SchedulePackage.CARGO_ALLOCATION___GET_LOCAL_LOAD_DATE:
				return getLocalLoadDate();
			case SchedulePackage.CARGO_ALLOCATION___GET_LOCAL_DISCHARGE_DATE:
				return getLocalDischargeDate();
			case SchedulePackage.CARGO_ALLOCATION___GET_LOAD_VOLUME:
				return getLoadVolume();
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
