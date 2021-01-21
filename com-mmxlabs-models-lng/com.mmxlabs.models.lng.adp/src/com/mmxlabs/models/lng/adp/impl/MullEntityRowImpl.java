/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import com.mmxlabs.models.lng.port.Port;

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
 * An implementation of the model object '<em><b>Mull Entity Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl#getInitialAllocation <em>Initial Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl#getRelativeEntitlement <em>Relative Entitlement</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl#getDesSalesMarketAllocationRows <em>Des Sales Market Allocation Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl#getSalesContractAllocationRows <em>Sales Contract Allocation Rows</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MullEntityRowImpl extends EObjectImpl implements MullEntityRow {
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
	 * The cached value of the '{@link #getDesSalesMarketAllocationRows() <em>Des Sales Market Allocation Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesSalesMarketAllocationRows()
	 * @generated
	 * @ordered
	 */
	protected EList<DESSalesMarketAllocationRow> desSalesMarketAllocationRows;

	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> ports;

	/**
	 * The cached value of the '{@link #getSalesContractAllocationRows() <em>Sales Contract Allocation Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesContractAllocationRows()
	 * @generated
	 * @ordered
	 */
	protected EList<SalesContractAllocationRow> salesContractAllocationRows;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MullEntityRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.MULL_ENTITY_ROW;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.MULL_ENTITY_ROW__ENTITY, oldEntity, entity));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULL_ENTITY_ROW__ENTITY, oldEntity, entity));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULL_ENTITY_ROW__INITIAL_ALLOCATION, oldInitialAllocation, initialAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT, oldRelativeEntitlement, relativeEntitlement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DESSalesMarketAllocationRow> getDesSalesMarketAllocationRows() {
		if (desSalesMarketAllocationRows == null) {
			desSalesMarketAllocationRows = new EObjectContainmentEList.Resolving<DESSalesMarketAllocationRow>(DESSalesMarketAllocationRow.class, this, ADPPackage.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS);
		}
		return desSalesMarketAllocationRows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Port> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<Port>(Port.class, this, ADPPackage.MULL_ENTITY_ROW__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SalesContractAllocationRow> getSalesContractAllocationRows() {
		if (salesContractAllocationRows == null) {
			salesContractAllocationRows = new EObjectContainmentEList.Resolving<SalesContractAllocationRow>(SalesContractAllocationRow.class, this, ADPPackage.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS);
		}
		return salesContractAllocationRows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS:
				return ((InternalEList<?>)getDesSalesMarketAllocationRows()).basicRemove(otherEnd, msgs);
			case ADPPackage.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS:
				return ((InternalEList<?>)getSalesContractAllocationRows()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.MULL_ENTITY_ROW__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case ADPPackage.MULL_ENTITY_ROW__INITIAL_ALLOCATION:
				return getInitialAllocation();
			case ADPPackage.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				return getRelativeEntitlement();
			case ADPPackage.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS:
				return getDesSalesMarketAllocationRows();
			case ADPPackage.MULL_ENTITY_ROW__PORTS:
				return getPorts();
			case ADPPackage.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS:
				return getSalesContractAllocationRows();
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
			case ADPPackage.MULL_ENTITY_ROW__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case ADPPackage.MULL_ENTITY_ROW__INITIAL_ALLOCATION:
				setInitialAllocation((String)newValue);
				return;
			case ADPPackage.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				setRelativeEntitlement((Double)newValue);
				return;
			case ADPPackage.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS:
				getDesSalesMarketAllocationRows().clear();
				getDesSalesMarketAllocationRows().addAll((Collection<? extends DESSalesMarketAllocationRow>)newValue);
				return;
			case ADPPackage.MULL_ENTITY_ROW__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends Port>)newValue);
				return;
			case ADPPackage.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS:
				getSalesContractAllocationRows().clear();
				getSalesContractAllocationRows().addAll((Collection<? extends SalesContractAllocationRow>)newValue);
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
			case ADPPackage.MULL_ENTITY_ROW__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case ADPPackage.MULL_ENTITY_ROW__INITIAL_ALLOCATION:
				setInitialAllocation(INITIAL_ALLOCATION_EDEFAULT);
				return;
			case ADPPackage.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				setRelativeEntitlement(RELATIVE_ENTITLEMENT_EDEFAULT);
				return;
			case ADPPackage.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS:
				getDesSalesMarketAllocationRows().clear();
				return;
			case ADPPackage.MULL_ENTITY_ROW__PORTS:
				getPorts().clear();
				return;
			case ADPPackage.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS:
				getSalesContractAllocationRows().clear();
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
			case ADPPackage.MULL_ENTITY_ROW__ENTITY:
				return entity != null;
			case ADPPackage.MULL_ENTITY_ROW__INITIAL_ALLOCATION:
				return INITIAL_ALLOCATION_EDEFAULT == null ? initialAllocation != null : !INITIAL_ALLOCATION_EDEFAULT.equals(initialAllocation);
			case ADPPackage.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT:
				return relativeEntitlement != RELATIVE_ENTITLEMENT_EDEFAULT;
			case ADPPackage.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS:
				return desSalesMarketAllocationRows != null && !desSalesMarketAllocationRows.isEmpty();
			case ADPPackage.MULL_ENTITY_ROW__PORTS:
				return ports != null && !ports.isEmpty();
			case ADPPackage.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS:
				return salesContractAllocationRows != null && !salesContractAllocationRows.isEmpty();
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

} //MullEntityRowImpl
