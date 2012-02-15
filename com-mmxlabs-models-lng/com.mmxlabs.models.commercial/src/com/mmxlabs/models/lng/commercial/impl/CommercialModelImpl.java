

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial.impl;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LegalEntity;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getEntities <em>Entities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getContracts <em>Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl#getShippingEntity <em>Shipping Entity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CommercialModelImpl extends UUIDObjectImpl implements CommercialModel {
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
	 * The cached value of the '{@link #getEntities() <em>Entities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntities()
	 * @generated
	 * @ordered
	 */
	protected EList<LegalEntity> entities;

	/**
	 * The cached value of the '{@link #getContracts() <em>Contracts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<Contract> contracts;

	/**
	 * The cached value of the '{@link #getShippingEntity() <em>Shipping Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingEntity()
	 * @generated
	 * @ordered
	 */
	protected LegalEntity shippingEntity;

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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.COMMERCIAL_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LegalEntity> getEntities() {
		if (entities == null) {
			entities = new EObjectContainmentEList<LegalEntity>(LegalEntity.class, this, CommercialPackage.COMMERCIAL_MODEL__ENTITIES);
		}
		return entities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Contract> getContracts() {
		if (contracts == null) {
			contracts = new EObjectContainmentEList<Contract>(Contract.class, this, CommercialPackage.COMMERCIAL_MODEL__CONTRACTS);
		}
		return contracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegalEntity getShippingEntity() {
		if (shippingEntity != null && shippingEntity.eIsProxy()) {
			InternalEObject oldShippingEntity = (InternalEObject)shippingEntity;
			shippingEntity = (LegalEntity)eResolveProxy(oldShippingEntity);
			if (shippingEntity != oldShippingEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.COMMERCIAL_MODEL__SHIPPING_ENTITY, oldShippingEntity, shippingEntity));
			}
		}
		return shippingEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegalEntity basicGetShippingEntity() {
		return shippingEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShippingEntity(LegalEntity newShippingEntity) {
		LegalEntity oldShippingEntity = shippingEntity;
		shippingEntity = newShippingEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.COMMERCIAL_MODEL__SHIPPING_ENTITY, oldShippingEntity, shippingEntity));
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
			case CommercialPackage.COMMERCIAL_MODEL__CONTRACTS:
				return ((InternalEList<?>)getContracts()).basicRemove(otherEnd, msgs);
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
			case CommercialPackage.COMMERCIAL_MODEL__NAME:
				return getName();
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				return getEntities();
			case CommercialPackage.COMMERCIAL_MODEL__CONTRACTS:
				return getContracts();
			case CommercialPackage.COMMERCIAL_MODEL__SHIPPING_ENTITY:
				if (resolve) return getShippingEntity();
				return basicGetShippingEntity();
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
			case CommercialPackage.COMMERCIAL_MODEL__NAME:
				setName((String)newValue);
				return;
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				getEntities().clear();
				getEntities().addAll((Collection<? extends LegalEntity>)newValue);
				return;
			case CommercialPackage.COMMERCIAL_MODEL__CONTRACTS:
				getContracts().clear();
				getContracts().addAll((Collection<? extends Contract>)newValue);
				return;
			case CommercialPackage.COMMERCIAL_MODEL__SHIPPING_ENTITY:
				setShippingEntity((LegalEntity)newValue);
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
			case CommercialPackage.COMMERCIAL_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				getEntities().clear();
				return;
			case CommercialPackage.COMMERCIAL_MODEL__CONTRACTS:
				getContracts().clear();
				return;
			case CommercialPackage.COMMERCIAL_MODEL__SHIPPING_ENTITY:
				setShippingEntity((LegalEntity)null);
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
			case CommercialPackage.COMMERCIAL_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
				return entities != null && !entities.isEmpty();
			case CommercialPackage.COMMERCIAL_MODEL__CONTRACTS:
				return contracts != null && !contracts.isEmpty();
			case CommercialPackage.COMMERCIAL_MODEL__SHIPPING_ENTITY:
				return shippingEntity != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case CommercialPackage.COMMERCIAL_MODEL__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return CommercialPackage.COMMERCIAL_MODEL__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} // end of CommercialModelImpl

// finish type fixing
