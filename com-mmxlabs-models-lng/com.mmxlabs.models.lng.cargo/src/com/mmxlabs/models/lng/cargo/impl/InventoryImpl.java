/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;

import java.time.LocalDate;

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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getStartVolume <em>Start Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getFeeds <em>Feeds</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getOfftakes <em>Offtakes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getCapacities <em>Capacities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryImpl extends EObjectImpl implements Inventory {
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
	 * The default value of the '{@link #getStartVolume() <em>Start Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int START_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStartVolume() <em>Start Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartVolume()
	 * @generated
	 * @ordered
	 */
	protected int startVolume = START_VOLUME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFeeds() <em>Feeds</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeeds()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryEventRow> feeds;

	/**
	 * The cached value of the '{@link #getOfftakes() <em>Offtakes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOfftakes()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryEventRow> offtakes;

	/**
	 * The cached value of the '{@link #getCapacities() <em>Capacities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacities()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryCapacityRow> capacities;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.INVENTORY;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY__START_DATE, oldStartDate, startDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStartVolume() {
		return startVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartVolume(int newStartVolume) {
		int oldStartVolume = startVolume;
		startVolume = newStartVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY__START_VOLUME, oldStartVolume, startVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InventoryEventRow> getFeeds() {
		if (feeds == null) {
			feeds = new EObjectContainmentEList.Resolving<InventoryEventRow>(InventoryEventRow.class, this, CargoPackage.INVENTORY__FEEDS);
		}
		return feeds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InventoryEventRow> getOfftakes() {
		if (offtakes == null) {
			offtakes = new EObjectContainmentEList.Resolving<InventoryEventRow>(InventoryEventRow.class, this, CargoPackage.INVENTORY__OFFTAKES);
		}
		return offtakes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InventoryCapacityRow> getCapacities() {
		if (capacities == null) {
			capacities = new EObjectContainmentEList.Resolving<InventoryCapacityRow>(InventoryCapacityRow.class, this, CargoPackage.INVENTORY__CAPACITIES);
		}
		return capacities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.INVENTORY__FEEDS:
				return ((InternalEList<?>)getFeeds()).basicRemove(otherEnd, msgs);
			case CargoPackage.INVENTORY__OFFTAKES:
				return ((InternalEList<?>)getOfftakes()).basicRemove(otherEnd, msgs);
			case CargoPackage.INVENTORY__CAPACITIES:
				return ((InternalEList<?>)getCapacities()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.INVENTORY__START_DATE:
				return getStartDate();
			case CargoPackage.INVENTORY__START_VOLUME:
				return getStartVolume();
			case CargoPackage.INVENTORY__FEEDS:
				return getFeeds();
			case CargoPackage.INVENTORY__OFFTAKES:
				return getOfftakes();
			case CargoPackage.INVENTORY__CAPACITIES:
				return getCapacities();
			case CargoPackage.INVENTORY__NAME:
				return getName();
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
			case CargoPackage.INVENTORY__START_DATE:
				setStartDate((LocalDate)newValue);
				return;
			case CargoPackage.INVENTORY__START_VOLUME:
				setStartVolume((Integer)newValue);
				return;
			case CargoPackage.INVENTORY__FEEDS:
				getFeeds().clear();
				getFeeds().addAll((Collection<? extends InventoryEventRow>)newValue);
				return;
			case CargoPackage.INVENTORY__OFFTAKES:
				getOfftakes().clear();
				getOfftakes().addAll((Collection<? extends InventoryEventRow>)newValue);
				return;
			case CargoPackage.INVENTORY__CAPACITIES:
				getCapacities().clear();
				getCapacities().addAll((Collection<? extends InventoryCapacityRow>)newValue);
				return;
			case CargoPackage.INVENTORY__NAME:
				setName((String)newValue);
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
			case CargoPackage.INVENTORY__START_DATE:
				setStartDate(START_DATE_EDEFAULT);
				return;
			case CargoPackage.INVENTORY__START_VOLUME:
				setStartVolume(START_VOLUME_EDEFAULT);
				return;
			case CargoPackage.INVENTORY__FEEDS:
				getFeeds().clear();
				return;
			case CargoPackage.INVENTORY__OFFTAKES:
				getOfftakes().clear();
				return;
			case CargoPackage.INVENTORY__CAPACITIES:
				getCapacities().clear();
				return;
			case CargoPackage.INVENTORY__NAME:
				setName(NAME_EDEFAULT);
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
			case CargoPackage.INVENTORY__START_DATE:
				return START_DATE_EDEFAULT == null ? startDate != null : !START_DATE_EDEFAULT.equals(startDate);
			case CargoPackage.INVENTORY__START_VOLUME:
				return startVolume != START_VOLUME_EDEFAULT;
			case CargoPackage.INVENTORY__FEEDS:
				return feeds != null && !feeds.isEmpty();
			case CargoPackage.INVENTORY__OFFTAKES:
				return offtakes != null && !offtakes.isEmpty();
			case CargoPackage.INVENTORY__CAPACITIES:
				return capacities != null && !capacities.isEmpty();
			case CargoPackage.INVENTORY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
		result.append(", startVolume: ");
		result.append(startVolume);
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //InventoryImpl
