/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getAllowedPorts <em>Allowed Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPreferredPort <em>Preferred Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getRestrictedContracts <em>Restricted Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getRestrictedPorts <em>Restricted Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl#getPriceInfo <em>Price Info</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContractImpl extends UUIDObjectImpl implements Contract {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected LegalEntity entity;

	/**
	 * The cached value of the '{@link #getAllowedPorts() <em>Allowed Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> allowedPorts;
	/**
	 * The cached value of the '{@link #getPreferredPort() <em>Preferred Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreferredPort()
	 * @generated
	 * @ordered
	 */
	protected Port preferredPort;

	/**
	 * The default value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinQuantity() <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinQuantity()
	 * @generated
	 * @ordered
	 */
	protected int minQuantity = MIN_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxQuantity() <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxQuantity()
	 * @generated
	 * @ordered
	 */
	protected int maxQuantity = MAX_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #isRestrictedListsArePermissive() <em>Restricted Lists Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #isRestrictedListsArePermissive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRestrictedListsArePermissive() <em>Restricted Lists Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #isRestrictedListsArePermissive()
	 * @generated
	 * @ordered
	 */
	protected boolean restrictedListsArePermissive = RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRestrictedContracts() <em>Restricted Contracts</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getRestrictedContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<Contract> restrictedContracts;

	/**
	 * The cached value of the '{@link #getRestrictedPorts() <em>Restricted Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getRestrictedPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> restrictedPorts;

	/**
	 * The cached value of the '{@link #getPriceInfo() <em>Price Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getPriceInfo()
	 * @generated
	 * @ordered
	 */
	protected LNGPriceCalculatorParameters priceInfo;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (LegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.CONTRACT__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegalEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(LegalEntity newEntity) {
		LegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getAllowedPorts() {
		if (allowedPorts == null) {
			allowedPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.CONTRACT__ALLOWED_PORTS);
		}
		return allowedPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getPreferredPort() {
		if (preferredPort != null && preferredPort.eIsProxy()) {
			InternalEObject oldPreferredPort = (InternalEObject)preferredPort;
			preferredPort = (Port)eResolveProxy(oldPreferredPort);
			if (preferredPort != oldPreferredPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.CONTRACT__PREFERRED_PORT, oldPreferredPort, preferredPort));
			}
		}
		return preferredPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPreferredPort() {
		return preferredPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPreferredPort(Port newPreferredPort) {
		Port oldPreferredPort = preferredPort;
		preferredPort = newPreferredPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PREFERRED_PORT, oldPreferredPort, preferredPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinQuantity() {
		return minQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinQuantity(int newMinQuantity) {
		int oldMinQuantity = minQuantity;
		minQuantity = newMinQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__MIN_QUANTITY, oldMinQuantity, minQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxQuantity() {
		return maxQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxQuantity(int newMaxQuantity) {
		int oldMaxQuantity = maxQuantity;
		maxQuantity = newMaxQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__MAX_QUANTITY, oldMaxQuantity, maxQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRestrictedListsArePermissive() {
		return restrictedListsArePermissive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRestrictedListsArePermissive(boolean newRestrictedListsArePermissive) {
		boolean oldRestrictedListsArePermissive = restrictedListsArePermissive;
		restrictedListsArePermissive = newRestrictedListsArePermissive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE, oldRestrictedListsArePermissive, restrictedListsArePermissive));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Contract> getRestrictedContracts() {
		if (restrictedContracts == null) {
			restrictedContracts = new EObjectResolvingEList<Contract>(Contract.class, this, CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS);
		}
		return restrictedContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getRestrictedPorts() {
		if (restrictedPorts == null) {
			restrictedPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.CONTRACT__RESTRICTED_PORTS);
		}
		return restrictedPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGPriceCalculatorParameters getPriceInfo() {
		return priceInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPriceInfo(LNGPriceCalculatorParameters newPriceInfo, NotificationChain msgs) {
		LNGPriceCalculatorParameters oldPriceInfo = priceInfo;
		priceInfo = newPriceInfo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PRICE_INFO, oldPriceInfo, newPriceInfo);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriceInfo(LNGPriceCalculatorParameters newPriceInfo) {
		if (newPriceInfo != priceInfo) {
			NotificationChain msgs = null;
			if (priceInfo != null)
				msgs = ((InternalEObject)priceInfo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.CONTRACT__PRICE_INFO, null, msgs);
			if (newPriceInfo != null)
				msgs = ((InternalEObject)newPriceInfo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.CONTRACT__PRICE_INFO, null, msgs);
			msgs = basicSetPriceInfo(newPriceInfo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.CONTRACT__PRICE_INFO, newPriceInfo, newPriceInfo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.CONTRACT__PRICE_INFO:
				return basicSetPriceInfo(null, msgs);
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
			case CommercialPackage.CONTRACT__NAME:
				return getName();
			case CommercialPackage.CONTRACT__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				return getAllowedPorts();
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				if (resolve) return getPreferredPort();
				return basicGetPreferredPort();
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				return getMinQuantity();
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				return getMaxQuantity();
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				return isRestrictedListsArePermissive();
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				return getRestrictedContracts();
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				return getRestrictedPorts();
			case CommercialPackage.CONTRACT__PRICE_INFO:
				return getPriceInfo();
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
			case CommercialPackage.CONTRACT__NAME:
				setName((String)newValue);
				return;
			case CommercialPackage.CONTRACT__ENTITY:
				setEntity((LegalEntity)newValue);
				return;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				getAllowedPorts().clear();
				getAllowedPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				setPreferredPort((Port)newValue);
				return;
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				setMinQuantity((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				setMaxQuantity((Integer)newValue);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				setRestrictedListsArePermissive((Boolean)newValue);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				getRestrictedContracts().clear();
				getRestrictedContracts().addAll((Collection<? extends Contract>)newValue);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				getRestrictedPorts().clear();
				getRestrictedPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CommercialPackage.CONTRACT__PRICE_INFO:
				setPriceInfo((LNGPriceCalculatorParameters)newValue);
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
			case CommercialPackage.CONTRACT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__ENTITY:
				setEntity((LegalEntity)null);
				return;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				getAllowedPorts().clear();
				return;
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				setPreferredPort((Port)null);
				return;
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				setMinQuantity(MIN_QUANTITY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				setMaxQuantity(MAX_QUANTITY_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				setRestrictedListsArePermissive(RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT);
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				getRestrictedContracts().clear();
				return;
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				getRestrictedPorts().clear();
				return;
			case CommercialPackage.CONTRACT__PRICE_INFO:
				setPriceInfo((LNGPriceCalculatorParameters)null);
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
			case CommercialPackage.CONTRACT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CommercialPackage.CONTRACT__ENTITY:
				return entity != null;
			case CommercialPackage.CONTRACT__ALLOWED_PORTS:
				return allowedPorts != null && !allowedPorts.isEmpty();
			case CommercialPackage.CONTRACT__PREFERRED_PORT:
				return preferredPort != null;
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
				return minQuantity != MIN_QUANTITY_EDEFAULT;
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
				return maxQuantity != MAX_QUANTITY_EDEFAULT;
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
				return restrictedListsArePermissive != RESTRICTED_LISTS_ARE_PERMISSIVE_EDEFAULT;
			case CommercialPackage.CONTRACT__RESTRICTED_CONTRACTS:
				return restrictedContracts != null && !restrictedContracts.isEmpty();
			case CommercialPackage.CONTRACT__RESTRICTED_PORTS:
				return restrictedPorts != null && !restrictedPorts.isEmpty();
			case CommercialPackage.CONTRACT__PRICE_INFO:
				return priceInfo != null;
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
				case CommercialPackage.CONTRACT__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
				case MMXCorePackage.NAMED_OBJECT__NAME: return CommercialPackage.CONTRACT__NAME;
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
		result.append(", minQuantity: ");
		result.append(minQuantity);
		result.append(", maxQuantity: ");
		result.append(maxQuantity);
		result.append(", restrictedListsArePermissive: ");
		result.append(restrictedListsArePermissive);
		result.append(')');
		return result.toString();
	}

} // end of ContractImpl

// finish type fixing
