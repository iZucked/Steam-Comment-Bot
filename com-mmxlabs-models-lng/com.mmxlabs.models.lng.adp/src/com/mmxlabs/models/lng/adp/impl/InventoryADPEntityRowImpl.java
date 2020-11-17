/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory ADP Entity Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryADPEntityRowImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryADPEntityRowImpl#getInitialAllocation <em>Initial Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.InventoryADPEntityRowImpl#getRelativeEntitlement <em>Relative Entitlement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryADPEntityRowImpl extends EObjectImpl implements InventoryADPEntityRow {
	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity entity;

	/**
	 * The default value of the '{@link #getInitialAllocation() <em>Initial Allocation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialAllocation()
	 * @generated
	 * @ordered
	 */
	protected static final String INITIAL_ALLOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInitialAllocation() <em>Initial Allocation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialAllocation()
	 * @generated
	 * @ordered
	 */
	protected String initialAllocation = INITIAL_ALLOCATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getRelativeEntitlement() <em>Relative Entitlement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelativeEntitlement()
	 * @generated
	 * @ordered
	 */
	protected static final double RELATIVE_ENTITLEMENT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRelativeEntitlement() <em>Relative Entitlement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelativeEntitlement()
	 * @generated
	 * @ordered
	 */
	protected double relativeEntitlement = RELATIVE_ENTITLEMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryADPEntityRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseLegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (BaseLegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.INVENTORY_ADP_ENTITY_ROW__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEntity(BaseLegalEntity newEntity) {
		BaseLegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.INVENTORY_ADP_ENTITY_ROW__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getInitialAllocation() {
		return initialAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInitialAllocation(String newInitialAllocation) {
		String oldInitialAllocation = initialAllocation;
		initialAllocation = newInitialAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.INVENTORY_ADP_ENTITY_ROW__INITIAL_ALLOCATION, oldInitialAllocation, initialAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getRelativeEntitlement() {
		return relativeEntitlement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRelativeEntitlement(double newRelativeEntitlement) {
		double oldRelativeEntitlement = relativeEntitlement;
		relativeEntitlement = newRelativeEntitlement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.INVENTORY_ADP_ENTITY_ROW__RELATIVE_ENTITLEMENT, oldRelativeEntitlement, relativeEntitlement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__INITIAL_ALLOCATION:
				return getInitialAllocation();
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				return getRelativeEntitlement();
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
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__INITIAL_ALLOCATION:
				setInitialAllocation((String)newValue);
				return;
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				setRelativeEntitlement((Double)newValue);
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
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__INITIAL_ALLOCATION:
				setInitialAllocation(INITIAL_ALLOCATION_EDEFAULT);
				return;
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				setRelativeEntitlement(RELATIVE_ENTITLEMENT_EDEFAULT);
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
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__ENTITY:
				return entity != null;
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__INITIAL_ALLOCATION:
				return INITIAL_ALLOCATION_EDEFAULT == null ? initialAllocation != null : !INITIAL_ALLOCATION_EDEFAULT.equals(initialAllocation);
			case ADPPackage.INVENTORY_ADP_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				return relativeEntitlement != RELATIVE_ENTITLEMENT_EDEFAULT;
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
		result.append(" (initialAllocation: ");
		result.append(initialAllocation);
		result.append(", relativeEntitlement: ");
		result.append(relativeEntitlement);
		result.append(')');
		return result.toString();
	}

} //InventoryADPEntityRowImpl
