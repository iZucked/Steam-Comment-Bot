/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.InventorySubprofile;

import com.mmxlabs.models.lng.cargo.Inventory;

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
 * An implementation of the model object '<em><b>Inventory Subprofile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventorySubprofileImpl#getInventory <em>Inventory</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventorySubprofileImpl#getEntityTable <em>Entity Table</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventorySubprofileImpl extends EObjectImpl implements InventorySubprofile {
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
	 * The cached value of the '{@link #getEntityTable() <em>Entity Table</em>}' containment reference list.
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
	protected InventorySubprofileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.INVENTORY_SUBPROFILE;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.INVENTORY_SUBPROFILE__INVENTORY, oldInventory, inventory));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.INVENTORY_SUBPROFILE__INVENTORY, oldInventory, inventory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InventoryADPEntityRow> getEntityTable() {
		if (entityTable == null) {
			entityTable = new EObjectContainmentEList.Resolving<InventoryADPEntityRow>(InventoryADPEntityRow.class, this, ADPPackage.INVENTORY_SUBPROFILE__ENTITY_TABLE);
		}
		return entityTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.INVENTORY_SUBPROFILE__ENTITY_TABLE:
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
			case ADPPackage.INVENTORY_SUBPROFILE__INVENTORY:
				if (resolve) return getInventory();
				return basicGetInventory();
			case ADPPackage.INVENTORY_SUBPROFILE__ENTITY_TABLE:
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
			case ADPPackage.INVENTORY_SUBPROFILE__INVENTORY:
				setInventory((Inventory)newValue);
				return;
			case ADPPackage.INVENTORY_SUBPROFILE__ENTITY_TABLE:
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
			case ADPPackage.INVENTORY_SUBPROFILE__INVENTORY:
				setInventory((Inventory)null);
				return;
			case ADPPackage.INVENTORY_SUBPROFILE__ENTITY_TABLE:
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
			case ADPPackage.INVENTORY_SUBPROFILE__INVENTORY:
				return inventory != null;
			case ADPPackage.INVENTORY_SUBPROFILE__ENTITY_TABLE:
				return entityTable != null && !entityTable.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //InventorySubprofileImpl
