/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getEntities <em>Entities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getSalesContracts <em>Sales Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getPurchaseContracts <em>Purchase Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getCharteringContracts <em>Chartering Contracts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommercialModelImpl extends UUIDObjectImpl implements CommercialModel {
	/**
	 * The cached value of the '{@link #getEntities() <em>Entities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntities()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseLegalEntity> entities;

	/**
	 * The cached value of the '{@link #getSalesContracts() <em>Sales Contracts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<SalesContract> salesContracts;

	/**
	 * The cached value of the '{@link #getPurchaseContracts() <em>Purchase Contracts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurchaseContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<PurchaseContract> purchaseContracts;

	/**
	 * The cached value of the '{@link #getCharteringContracts() <em>Chartering Contracts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharteringContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterContract> charteringContracts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommercialModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.COMMERCIAL_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BaseLegalEntity> getEntities() {
		if (entities == null) {
			entities = new EObjectContainmentEList<BaseLegalEntity>(BaseLegalEntity.class, this, CommercialPackage.COMMERCIAL_MODEL__ENTITIES);
		}
		return entities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SalesContract> getSalesContracts() {
		if (salesContracts == null) {
			salesContracts = new EObjectContainmentEList<SalesContract>(SalesContract.class, this, CommercialPackage.COMMERCIAL_MODEL__SALES_CONTRACTS);
		}
		return salesContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PurchaseContract> getPurchaseContracts() {
		if (purchaseContracts == null) {
			purchaseContracts = new EObjectContainmentEList<PurchaseContract>(PurchaseContract.class, this, CommercialPackage.COMMERCIAL_MODEL__PURCHASE_CONTRACTS);
		}
		return purchaseContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CharterContract> getCharteringContracts() {
		if (charteringContracts == null) {
			charteringContracts = new EObjectContainmentEList<CharterContract>(CharterContract.class, this, CommercialPackage.COMMERCIAL_MODEL__CHARTERING_CONTRACTS);
		}
		return charteringContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				return ((InternalEList<?>)getEntities()).basicRemove(otherEnd, msgs);
			case CommercialPackage.COMMERCIAL_MODEL__SALES_CONTRACTS:
				return ((InternalEList<?>)getSalesContracts()).basicRemove(otherEnd, msgs);
			case CommercialPackage.COMMERCIAL_MODEL__PURCHASE_CONTRACTS:
				return ((InternalEList<?>)getPurchaseContracts()).basicRemove(otherEnd, msgs);
			case CommercialPackage.COMMERCIAL_MODEL__CHARTERING_CONTRACTS:
				return ((InternalEList<?>)getCharteringContracts()).basicRemove(otherEnd, msgs);
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
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				return getEntities();
			case CommercialPackage.COMMERCIAL_MODEL__SALES_CONTRACTS:
				return getSalesContracts();
			case CommercialPackage.COMMERCIAL_MODEL__PURCHASE_CONTRACTS:
				return getPurchaseContracts();
			case CommercialPackage.COMMERCIAL_MODEL__CHARTERING_CONTRACTS:
				return getCharteringContracts();
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
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				getEntities().clear();
				getEntities().addAll((Collection<? extends BaseLegalEntity>)newValue);
				return;
			case CommercialPackage.COMMERCIAL_MODEL__SALES_CONTRACTS:
				getSalesContracts().clear();
				getSalesContracts().addAll((Collection<? extends SalesContract>)newValue);
				return;
			case CommercialPackage.COMMERCIAL_MODEL__PURCHASE_CONTRACTS:
				getPurchaseContracts().clear();
				getPurchaseContracts().addAll((Collection<? extends PurchaseContract>)newValue);
				return;
			case CommercialPackage.COMMERCIAL_MODEL__CHARTERING_CONTRACTS:
				getCharteringContracts().clear();
				getCharteringContracts().addAll((Collection<? extends CharterContract>)newValue);
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
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				getEntities().clear();
				return;
			case CommercialPackage.COMMERCIAL_MODEL__SALES_CONTRACTS:
				getSalesContracts().clear();
				return;
			case CommercialPackage.COMMERCIAL_MODEL__PURCHASE_CONTRACTS:
				getPurchaseContracts().clear();
				return;
			case CommercialPackage.COMMERCIAL_MODEL__CHARTERING_CONTRACTS:
				getCharteringContracts().clear();
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
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				return entities != null && !entities.isEmpty();
			case CommercialPackage.COMMERCIAL_MODEL__SALES_CONTRACTS:
				return salesContracts != null && !salesContracts.isEmpty();
			case CommercialPackage.COMMERCIAL_MODEL__PURCHASE_CONTRACTS:
				return purchaseContracts != null && !purchaseContracts.isEmpty();
			case CommercialPackage.COMMERCIAL_MODEL__CHARTERING_CONTRACTS:
				return charteringContracts != null && !charteringContracts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of CommercialModelImpl

// finish type fixing
