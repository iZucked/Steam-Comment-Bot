/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.AllocationElement;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.InventoryProfile;
import com.mmxlabs.models.lng.adp.RelativeEntitlementElement;

import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getInventory <em>Inventory</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getGeneratedSlots <em>Generated Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getEntityTable <em>Entity Table</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getWindowSize <em>Window Size</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryProfileImpl extends EObjectImpl implements InventoryProfile {
	/**
	 * The cached value of the '{@link #getInventory() <em>Inventory</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInventory()
	 * @generated
	 * @ordered
	 */
	protected Inventory inventory;

	/**
	 * The cached value of the '{@link #getGeneratedSlots() <em>Generated Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratedSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<LoadSlot> generatedSlots;

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
	 * The cached value of the '{@link #getEntityTable() <em>Entity Table</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntityTable()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryADPEntityRow> entityTable;

	/**
	 * The default value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected int windowSize = WINDOW_SIZE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.INVENTORY_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Inventory getInventory() {
		if (inventory != null && inventory.eIsProxy()) {
			InternalEObject oldInventory = (InternalEObject)inventory;
			inventory = (Inventory)eResolveProxy(oldInventory);
			if (inventory != oldInventory) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.INVENTORY_PROFILE__INVENTORY, oldInventory, inventory));
			}
		}
		return inventory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Inventory basicGetInventory() {
		return inventory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInventory(Inventory newInventory) {
		Inventory oldInventory = inventory;
		inventory = newInventory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.INVENTORY_PROFILE__INVENTORY, oldInventory, inventory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<LoadSlot> getGeneratedSlots() {
		if (generatedSlots == null) {
			generatedSlots = new EObjectContainmentEList.Resolving<LoadSlot>(LoadSlot.class, this, ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS);
		}
		return generatedSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVolume() {
		return volume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolume(int newVolume) {
		int oldVolume = volume;
		volume = newVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.INVENTORY_PROFILE__VOLUME, oldVolume, volume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InventoryADPEntityRow> getEntityTable() {
		if (entityTable == null) {
			entityTable = new EObjectContainmentEList.Resolving<InventoryADPEntityRow>(InventoryADPEntityRow.class, this, ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE);
		}
		return entityTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowSize(int newWindowSize) {
		int oldWindowSize = windowSize;
		windowSize = newWindowSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.INVENTORY_PROFILE__WINDOW_SIZE, oldWindowSize, windowSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				return ((InternalEList<?>)getGeneratedSlots()).basicRemove(otherEnd, msgs);
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				return ((InternalEList<?>)getEntityTable()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.INVENTORY_PROFILE__INVENTORY:
				if (resolve) return getInventory();
				return basicGetInventory();
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				return getGeneratedSlots();
			case ADPPackage.INVENTORY_PROFILE__VOLUME:
				return getVolume();
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				return getEntityTable();
			case ADPPackage.INVENTORY_PROFILE__WINDOW_SIZE:
				return getWindowSize();
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
			case ADPPackage.INVENTORY_PROFILE__INVENTORY:
				setInventory((Inventory)newValue);
				return;
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				getGeneratedSlots().clear();
				getGeneratedSlots().addAll((Collection<? extends LoadSlot>)newValue);
				return;
			case ADPPackage.INVENTORY_PROFILE__VOLUME:
				setVolume((Integer)newValue);
				return;
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				getEntityTable().clear();
				getEntityTable().addAll((Collection<? extends InventoryADPEntityRow>)newValue);
				return;
			case ADPPackage.INVENTORY_PROFILE__WINDOW_SIZE:
				setWindowSize((Integer)newValue);
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
			case ADPPackage.INVENTORY_PROFILE__INVENTORY:
				setInventory((Inventory)null);
				return;
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				getGeneratedSlots().clear();
				return;
			case ADPPackage.INVENTORY_PROFILE__VOLUME:
				setVolume(VOLUME_EDEFAULT);
				return;
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				getEntityTable().clear();
				return;
			case ADPPackage.INVENTORY_PROFILE__WINDOW_SIZE:
				setWindowSize(WINDOW_SIZE_EDEFAULT);
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
			case ADPPackage.INVENTORY_PROFILE__INVENTORY:
				return inventory != null;
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				return generatedSlots != null && !generatedSlots.isEmpty();
			case ADPPackage.INVENTORY_PROFILE__VOLUME:
				return volume != VOLUME_EDEFAULT;
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				return entityTable != null && !entityTable.isEmpty();
			case ADPPackage.INVENTORY_PROFILE__WINDOW_SIZE:
				return windowSize != WINDOW_SIZE_EDEFAULT;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (volume: ");
		result.append(volume);
		result.append(", windowSize: ");
		result.append(windowSize);
		result.append(')');
		return result.toString();
	}

} //InventoryProfileImpl
