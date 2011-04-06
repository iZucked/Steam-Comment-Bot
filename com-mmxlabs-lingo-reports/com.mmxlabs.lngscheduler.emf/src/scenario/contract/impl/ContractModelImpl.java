/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.contract.impl;

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

import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.Entity;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.contract.TotalVolumeLimit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.ContractModelImpl#getPurchaseContracts <em>Purchase Contracts</em>}</li>
 *   <li>{@link scenario.contract.impl.ContractModelImpl#getSalesContracts <em>Sales Contracts</em>}</li>
 *   <li>{@link scenario.contract.impl.ContractModelImpl#getVolumeConstraints <em>Volume Constraints</em>}</li>
 *   <li>{@link scenario.contract.impl.ContractModelImpl#getEntities <em>Entities</em>}</li>
 *   <li>{@link scenario.contract.impl.ContractModelImpl#getShippingEntity <em>Shipping Entity</em>}</li>
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
	 * The cached value of the '{@link #getVolumeConstraints() <em>Volume Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<TotalVolumeLimit> volumeConstraints;

	/**
	 * The cached value of the '{@link #getEntities() <em>Entities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntities()
	 * @generated
	 * @ordered
	 */
	protected EList<Entity> entities;

	/**
	 * The cached value of the '{@link #getShippingEntity() <em>Shipping Entity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingEntity()
	 * @generated
	 * @ordered
	 */
	protected Entity shippingEntity;

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
	public EList<TotalVolumeLimit> getVolumeConstraints() {
		if (volumeConstraints == null) {
			volumeConstraints = new EObjectContainmentEList<TotalVolumeLimit>(TotalVolumeLimit.class, this, ContractPackage.CONTRACT_MODEL__VOLUME_CONSTRAINTS);
		}
		return volumeConstraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Entity> getEntities() {
		if (entities == null) {
			entities = new EObjectContainmentEList<Entity>(Entity.class, this, ContractPackage.CONTRACT_MODEL__ENTITIES);
		}
		return entities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Entity getShippingEntity() {
		return shippingEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetShippingEntity(Entity newShippingEntity, NotificationChain msgs) {
		Entity oldShippingEntity = shippingEntity;
		shippingEntity = newShippingEntity;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY, oldShippingEntity, newShippingEntity);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShippingEntity(Entity newShippingEntity) {
		if (newShippingEntity != shippingEntity) {
			NotificationChain msgs = null;
			if (shippingEntity != null)
				msgs = ((InternalEObject)shippingEntity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY, null, msgs);
			if (newShippingEntity != null)
				msgs = ((InternalEObject)newShippingEntity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY, null, msgs);
			msgs = basicSetShippingEntity(newShippingEntity, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY, newShippingEntity, newShippingEntity));
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
			case ContractPackage.CONTRACT_MODEL__VOLUME_CONSTRAINTS:
				return ((InternalEList<?>)getVolumeConstraints()).basicRemove(otherEnd, msgs);
			case ContractPackage.CONTRACT_MODEL__ENTITIES:
				return ((InternalEList<?>)getEntities()).basicRemove(otherEnd, msgs);
			case ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY:
				return basicSetShippingEntity(null, msgs);
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
			case ContractPackage.CONTRACT_MODEL__VOLUME_CONSTRAINTS:
				return getVolumeConstraints();
			case ContractPackage.CONTRACT_MODEL__ENTITIES:
				return getEntities();
			case ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY:
				return getShippingEntity();
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
			case ContractPackage.CONTRACT_MODEL__VOLUME_CONSTRAINTS:
				getVolumeConstraints().clear();
				getVolumeConstraints().addAll((Collection<? extends TotalVolumeLimit>)newValue);
				return;
			case ContractPackage.CONTRACT_MODEL__ENTITIES:
				getEntities().clear();
				getEntities().addAll((Collection<? extends Entity>)newValue);
				return;
			case ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY:
				setShippingEntity((Entity)newValue);
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
			case ContractPackage.CONTRACT_MODEL__VOLUME_CONSTRAINTS:
				getVolumeConstraints().clear();
				return;
			case ContractPackage.CONTRACT_MODEL__ENTITIES:
				getEntities().clear();
				return;
			case ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY:
				setShippingEntity((Entity)null);
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
			case ContractPackage.CONTRACT_MODEL__VOLUME_CONSTRAINTS:
				return volumeConstraints != null && !volumeConstraints.isEmpty();
			case ContractPackage.CONTRACT_MODEL__ENTITIES:
				return entities != null && !entities.isEmpty();
			case ContractPackage.CONTRACT_MODEL__SHIPPING_ENTITY:
				return shippingEntity != null;
		}
		return super.eIsSet(featureID);
	}

} //ContractModelImpl
