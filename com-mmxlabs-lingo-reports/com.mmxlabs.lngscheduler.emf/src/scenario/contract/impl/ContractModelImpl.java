/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.contract.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.ContractModelImpl#getPurchaseContracts <em>Purchase Contracts</em>}</li>
 *   <li>{@link scenario.contract.impl.ContractModelImpl#getSalesContracts <em>Sales Contracts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContractModelImpl extends EObjectImpl implements ContractModel {
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
	 * The cached value of the '{@link #getSalesContracts() <em>Sales Contracts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<SalesContract> salesContracts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.CONTRACT_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PurchaseContract> getPurchaseContracts() {
		if (purchaseContracts == null) {
			purchaseContracts = new EObjectContainmentEList<PurchaseContract>(PurchaseContract.class, this, ContractPackage.CONTRACT_MODEL__PURCHASE_CONTRACTS);
		}
		return purchaseContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SalesContract> getSalesContracts() {
		if (salesContracts == null) {
			salesContracts = new EObjectContainmentEList<SalesContract>(SalesContract.class, this, ContractPackage.CONTRACT_MODEL__SALES_CONTRACTS);
		}
		return salesContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ContractPackage.CONTRACT_MODEL__PURCHASE_CONTRACTS:
				return ((InternalEList<?>)getPurchaseContracts()).basicRemove(otherEnd, msgs);
			case ContractPackage.CONTRACT_MODEL__SALES_CONTRACTS:
				return ((InternalEList<?>)getSalesContracts()).basicRemove(otherEnd, msgs);
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
			case ContractPackage.CONTRACT_MODEL__PURCHASE_CONTRACTS:
				return getPurchaseContracts();
			case ContractPackage.CONTRACT_MODEL__SALES_CONTRACTS:
				return getSalesContracts();
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
			case ContractPackage.CONTRACT_MODEL__PURCHASE_CONTRACTS:
				getPurchaseContracts().clear();
				getPurchaseContracts().addAll((Collection<? extends PurchaseContract>)newValue);
				return;
			case ContractPackage.CONTRACT_MODEL__SALES_CONTRACTS:
				getSalesContracts().clear();
				getSalesContracts().addAll((Collection<? extends SalesContract>)newValue);
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
			case ContractPackage.CONTRACT_MODEL__PURCHASE_CONTRACTS:
				getPurchaseContracts().clear();
				return;
			case ContractPackage.CONTRACT_MODEL__SALES_CONTRACTS:
				getSalesContracts().clear();
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
			case ContractPackage.CONTRACT_MODEL__PURCHASE_CONTRACTS:
				return purchaseContracts != null && !purchaseContracts.isEmpty();
			case ContractPackage.CONTRACT_MODEL__SALES_CONTRACTS:
				return salesContracts != null && !salesContracts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ContractModelImpl
