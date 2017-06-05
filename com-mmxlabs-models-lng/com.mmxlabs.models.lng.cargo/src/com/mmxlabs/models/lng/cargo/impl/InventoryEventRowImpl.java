/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory Event Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl#getPeriod <em>Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl#getCounterParty <em>Counter Party</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl#getVolume <em>Volume</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryEventRowImpl extends EObjectImpl implements InventoryEventRow {
	/**
	 * The default value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate startDate = START_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate END_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate endDate = END_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPeriod() <em>Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriod()
	 * @generated
	 * @ordered
	 */
	protected static final InventoryFrequency PERIOD_EDEFAULT = InventoryFrequency.CARGO;

	/**
	 * The cached value of the '{@link #getPeriod() <em>Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriod()
	 * @generated
	 * @ordered
	 */
	protected InventoryFrequency period = PERIOD_EDEFAULT;

	/**
	 * The default value of the '{@link #getCounterParty() <em>Counter Party</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCounterParty()
	 * @generated
	 * @ordered
	 */
	protected static final String COUNTER_PARTY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCounterParty() <em>Counter Party</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCounterParty()
	 * @generated
	 * @ordered
	 */
	protected String counterParty = COUNTER_PARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected int volume = VOLUME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryEventRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.INVENTORY_EVENT_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartDate(LocalDate newStartDate) {
		LocalDate oldStartDate = startDate;
		startDate = newStartDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_EVENT_ROW__START_DATE, oldStartDate, startDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndDate(LocalDate newEndDate) {
		LocalDate oldEndDate = endDate;
		endDate = newEndDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_EVENT_ROW__END_DATE, oldEndDate, endDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InventoryFrequency getPeriod() {
		return period;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPeriod(InventoryFrequency newPeriod) {
		InventoryFrequency oldPeriod = period;
		period = newPeriod == null ? PERIOD_EDEFAULT : newPeriod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_EVENT_ROW__PERIOD, oldPeriod, period));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCounterParty() {
		return counterParty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCounterParty(String newCounterParty) {
		String oldCounterParty = counterParty;
		counterParty = newCounterParty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_EVENT_ROW__COUNTER_PARTY, oldCounterParty, counterParty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolume(int newVolume) {
		int oldVolume = volume;
		volume = newVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY_EVENT_ROW__VOLUME, oldVolume, volume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.INVENTORY_EVENT_ROW__START_DATE:
				return getStartDate();
			case CargoPackage.INVENTORY_EVENT_ROW__END_DATE:
				return getEndDate();
			case CargoPackage.INVENTORY_EVENT_ROW__PERIOD:
				return getPeriod();
			case CargoPackage.INVENTORY_EVENT_ROW__COUNTER_PARTY:
				return getCounterParty();
			case CargoPackage.INVENTORY_EVENT_ROW__VOLUME:
				return getVolume();
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
			case CargoPackage.INVENTORY_EVENT_ROW__START_DATE:
				setStartDate((LocalDate)newValue);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__END_DATE:
				setEndDate((LocalDate)newValue);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__PERIOD:
				setPeriod((InventoryFrequency)newValue);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__COUNTER_PARTY:
				setCounterParty((String)newValue);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__VOLUME:
				setVolume((Integer)newValue);
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
			case CargoPackage.INVENTORY_EVENT_ROW__START_DATE:
				setStartDate(START_DATE_EDEFAULT);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__END_DATE:
				setEndDate(END_DATE_EDEFAULT);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__PERIOD:
				setPeriod(PERIOD_EDEFAULT);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__COUNTER_PARTY:
				setCounterParty(COUNTER_PARTY_EDEFAULT);
				return;
			case CargoPackage.INVENTORY_EVENT_ROW__VOLUME:
				setVolume(VOLUME_EDEFAULT);
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
			case CargoPackage.INVENTORY_EVENT_ROW__START_DATE:
				return START_DATE_EDEFAULT == null ? startDate != null : !START_DATE_EDEFAULT.equals(startDate);
			case CargoPackage.INVENTORY_EVENT_ROW__END_DATE:
				return END_DATE_EDEFAULT == null ? endDate != null : !END_DATE_EDEFAULT.equals(endDate);
			case CargoPackage.INVENTORY_EVENT_ROW__PERIOD:
				return period != PERIOD_EDEFAULT;
			case CargoPackage.INVENTORY_EVENT_ROW__COUNTER_PARTY:
				return COUNTER_PARTY_EDEFAULT == null ? counterParty != null : !COUNTER_PARTY_EDEFAULT.equals(counterParty);
			case CargoPackage.INVENTORY_EVENT_ROW__VOLUME:
				return volume != VOLUME_EDEFAULT;
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
		result.append(" (startDate: ");
		result.append(startDate);
		result.append(", endDate: ");
		result.append(endDate);
		result.append(", period: ");
		result.append(period);
		result.append(", counterParty: ");
		result.append(counterParty);
		result.append(", volume: ");
		result.append(volume);
		result.append(')');
		return result.toString();
	}

} //InventoryEventRowImpl
