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
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getRelativeEntitlements <em>Relative Entitlements</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getInventory <em>Inventory</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getGeneratedSlots <em>Generated Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getInitialAllocations <em>Initial Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryProfileImpl#getEntityTable <em>Entity Table</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryProfileImpl extends EObjectImpl implements InventoryProfile {
	/**
	 * The cached value of the '{@link #getRelativeEntitlements() <em>Relative Entitlements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelativeEntitlements()
	 * @generated
	 * @ordered
	 */
	protected EList<RelativeEntitlementElement> relativeEntitlements;

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
	 * The cached value of the '{@link #getGeneratedSlots() <em>Generated Slots</em>}' reference list.
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
	 * The cached value of the '{@link #getInitialAllocations() <em>Initial Allocations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<AllocationElement> initialAllocations;

	/**
	 * The cached value of the '{@link #getEntityTable() <em>Entity Table</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntityTable()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryADPEntityRow> entityTable;

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
	public EList<RelativeEntitlementElement> getRelativeEntitlements() {
		if (relativeEntitlements == null) {
			relativeEntitlements = new EObjectResolvingEList<RelativeEntitlementElement>(RelativeEntitlementElement.class, this, ADPPackage.INVENTORY_PROFILE__RELATIVE_ENTITLEMENTS);
		}
		return relativeEntitlements;
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
			generatedSlots = new EObjectResolvingEList<LoadSlot>(LoadSlot.class, this, ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS);
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
	public EList<AllocationElement> getInitialAllocations() {
		if (initialAllocations == null) {
			initialAllocations = new EObjectResolvingEList<AllocationElement>(AllocationElement.class, this, ADPPackage.INVENTORY_PROFILE__INITIAL_ALLOCATIONS);
		}
		return initialAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InventoryADPEntityRow> getEntityTable() {
		if (entityTable == null) {
			entityTable = new EObjectResolvingEList<InventoryADPEntityRow>(InventoryADPEntityRow.class, this, ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE);
		}
		return entityTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.INVENTORY_PROFILE__RELATIVE_ENTITLEMENTS:
				return getRelativeEntitlements();
			case ADPPackage.INVENTORY_PROFILE__INVENTORY:
				if (resolve) return getInventory();
				return basicGetInventory();
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				return getGeneratedSlots();
			case ADPPackage.INVENTORY_PROFILE__VOLUME:
				return getVolume();
			case ADPPackage.INVENTORY_PROFILE__INITIAL_ALLOCATIONS:
				return getInitialAllocations();
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				return getEntityTable();
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
			case ADPPackage.INVENTORY_PROFILE__RELATIVE_ENTITLEMENTS:
				getRelativeEntitlements().clear();
				getRelativeEntitlements().addAll((Collection<? extends RelativeEntitlementElement>)newValue);
				return;
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
			case ADPPackage.INVENTORY_PROFILE__INITIAL_ALLOCATIONS:
				getInitialAllocations().clear();
				getInitialAllocations().addAll((Collection<? extends AllocationElement>)newValue);
				return;
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				getEntityTable().clear();
				getEntityTable().addAll((Collection<? extends InventoryADPEntityRow>)newValue);
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
			case ADPPackage.INVENTORY_PROFILE__RELATIVE_ENTITLEMENTS:
				getRelativeEntitlements().clear();
				return;
			case ADPPackage.INVENTORY_PROFILE__INVENTORY:
				setInventory((Inventory)null);
				return;
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				getGeneratedSlots().clear();
				return;
			case ADPPackage.INVENTORY_PROFILE__VOLUME:
				setVolume(VOLUME_EDEFAULT);
				return;
			case ADPPackage.INVENTORY_PROFILE__INITIAL_ALLOCATIONS:
				getInitialAllocations().clear();
				return;
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				getEntityTable().clear();
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
			case ADPPackage.INVENTORY_PROFILE__RELATIVE_ENTITLEMENTS:
				return relativeEntitlements != null && !relativeEntitlements.isEmpty();
			case ADPPackage.INVENTORY_PROFILE__INVENTORY:
				return inventory != null;
			case ADPPackage.INVENTORY_PROFILE__GENERATED_SLOTS:
				return generatedSlots != null && !generatedSlots.isEmpty();
			case ADPPackage.INVENTORY_PROFILE__VOLUME:
				return volume != VOLUME_EDEFAULT;
			case ADPPackage.INVENTORY_PROFILE__INITIAL_ALLOCATIONS:
				return initialAllocations != null && !initialAllocations.isEmpty();
			case ADPPackage.INVENTORY_PROFILE__ENTITY_TABLE:
				return entityTable != null && !entityTable.isEmpty();
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
		result.append(')');
		return result.toString();
	}

} //InventoryProfileImpl
