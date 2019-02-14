/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CustomSubProfileAttributes;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.SubContractProfile;

import com.mmxlabs.models.lng.adp.SubProfileConstraint;
import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.VolumeUnits;
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
 * An implementation of the model object '<em><b>Sub Contract Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getContractType <em>Contract Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getDistributionModel <em>Distribution Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getSlotTemplateId <em>Slot Template Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getNominatedVessel <em>Nominated Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getShippingDays <em>Shipping Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getCustomAttribs <em>Custom Attribs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SubContractProfileImpl<T extends Slot<U>, U extends Contract> extends EObjectImpl implements SubContractProfile<T, U> {
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
	 * The default value of the '{@link #getContractType() <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractType()
	 * @generated
	 * @ordered
	 */
	protected static final ContractType CONTRACT_TYPE_EDEFAULT = ContractType.BOTH;

	/**
	 * The cached value of the '{@link #getContractType() <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractType()
	 * @generated
	 * @ordered
	 */
	protected ContractType contractType = CONTRACT_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDistributionModel() <em>Distribution Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistributionModel()
	 * @generated
	 * @ordered
	 */
	protected DistributionModel distributionModel;

	/**
	 * The default value of the '{@link #getSlotTemplateId() <em>Slot Template Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotTemplateId()
	 * @generated
	 * @ordered
	 */
	protected static final String SLOT_TEMPLATE_ID_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getSlotTemplateId() <em>Slot Template Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotTemplateId()
	 * @generated
	 * @ordered
	 */
	protected String slotTemplateId = SLOT_TEMPLATE_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getNominatedVessel() <em>Nominated Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominatedVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel nominatedVessel;

	/**
	 * The default value of the '{@link #getShippingDays() <em>Shipping Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingDays()
	 * @generated
	 * @ordered
	 */
	protected static final int SHIPPING_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getShippingDays() <em>Shipping Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingDays()
	 * @generated
	 * @ordered
	 */
	protected int shippingDays = SHIPPING_DAYS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCustomAttribs() <em>Custom Attribs</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCustomAttribs()
	 * @generated
	 * @ordered
	 */
	protected CustomSubProfileAttributes customAttribs;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<SubProfileConstraint> constraints;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SubContractProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.SUB_CONTRACT_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractType getContractType() {
		return contractType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContractType(ContractType newContractType) {
		ContractType oldContractType = contractType;
		contractType = newContractType == null ? CONTRACT_TYPE_EDEFAULT : newContractType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__CONTRACT_TYPE, oldContractType, contractType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public DistributionModel getDistributionModel() {
		if (distributionModel != null && distributionModel.eIsProxy()) {
			InternalEObject oldDistributionModel = (InternalEObject)distributionModel;
			distributionModel = (DistributionModel)eResolveProxy(oldDistributionModel);
			if (distributionModel != oldDistributionModel) {
				InternalEObject newDistributionModel = (InternalEObject)distributionModel;
				NotificationChain msgs = oldDistributionModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, null);
				if (newDistributionModel.eInternalContainer() == null) {
					msgs = newDistributionModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, oldDistributionModel, distributionModel));
			}
		}
		return distributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DistributionModel basicGetDistributionModel() {
		return distributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDistributionModel(DistributionModel newDistributionModel, NotificationChain msgs) {
		DistributionModel oldDistributionModel = distributionModel;
		distributionModel = newDistributionModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, oldDistributionModel, newDistributionModel);
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
	public void setDistributionModel(DistributionModel newDistributionModel) {
		if (newDistributionModel != distributionModel) {
			NotificationChain msgs = null;
			if (distributionModel != null)
				msgs = ((InternalEObject)distributionModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, msgs);
			if (newDistributionModel != null)
				msgs = ((InternalEObject)newDistributionModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, msgs);
			msgs = basicSetDistributionModel(newDistributionModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, newDistributionModel, newDistributionModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSlotTemplateId() {
		return slotTemplateId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSlotTemplateId(String newSlotTemplateId) {
		String oldSlotTemplateId = slotTemplateId;
		slotTemplateId = newSlotTemplateId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID, oldSlotTemplateId, slotTemplateId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CustomSubProfileAttributes getCustomAttribs() {
		if (customAttribs != null && customAttribs.eIsProxy()) {
			InternalEObject oldCustomAttribs = (InternalEObject)customAttribs;
			customAttribs = (CustomSubProfileAttributes)eResolveProxy(oldCustomAttribs);
			if (customAttribs != oldCustomAttribs) {
				InternalEObject newCustomAttribs = (InternalEObject)customAttribs;
				NotificationChain msgs = oldCustomAttribs.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS, null, null);
				if (newCustomAttribs.eInternalContainer() == null) {
					msgs = newCustomAttribs.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS, oldCustomAttribs, customAttribs));
			}
		}
		return customAttribs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CustomSubProfileAttributes basicGetCustomAttribs() {
		return customAttribs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCustomAttribs(CustomSubProfileAttributes newCustomAttribs, NotificationChain msgs) {
		CustomSubProfileAttributes oldCustomAttribs = customAttribs;
		customAttribs = newCustomAttribs;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS, oldCustomAttribs, newCustomAttribs);
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
	public void setCustomAttribs(CustomSubProfileAttributes newCustomAttribs) {
		if (newCustomAttribs != customAttribs) {
			NotificationChain msgs = null;
			if (customAttribs != null)
				msgs = ((InternalEObject)customAttribs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS, null, msgs);
			if (newCustomAttribs != null)
				msgs = ((InternalEObject)newCustomAttribs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS, null, msgs);
			msgs = basicSetCustomAttribs(newCustomAttribs, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS, newCustomAttribs, newCustomAttribs));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getNominatedVessel() {
		if (nominatedVessel != null && nominatedVessel.eIsProxy()) {
			InternalEObject oldNominatedVessel = (InternalEObject)nominatedVessel;
			nominatedVessel = (Vessel)eResolveProxy(oldNominatedVessel);
			if (nominatedVessel != oldNominatedVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL, oldNominatedVessel, nominatedVessel));
			}
		}
		return nominatedVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetNominatedVessel() {
		return nominatedVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNominatedVessel(Vessel newNominatedVessel) {
		Vessel oldNominatedVessel = nominatedVessel;
		nominatedVessel = newNominatedVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL, oldNominatedVessel, nominatedVessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getShippingDays() {
		return shippingDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShippingDays(int newShippingDays) {
		int oldShippingDays = shippingDays;
		shippingDays = newShippingDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUB_CONTRACT_PROFILE__SHIPPING_DAYS, oldShippingDays, shippingDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SubProfileConstraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList.Resolving<SubProfileConstraint>(SubProfileConstraint.class, this, ADPPackage.SUB_CONTRACT_PROFILE__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				return basicSetDistributionModel(null, msgs);
			case ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS:
				return basicSetCustomAttribs(null, msgs);
			case ADPPackage.SUB_CONTRACT_PROFILE__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.SUB_CONTRACT_PROFILE__NAME:
				return getName();
			case ADPPackage.SUB_CONTRACT_PROFILE__CONTRACT_TYPE:
				return getContractType();
			case ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				if (resolve) return getDistributionModel();
				return basicGetDistributionModel();
			case ADPPackage.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID:
				return getSlotTemplateId();
			case ADPPackage.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL:
				if (resolve) return getNominatedVessel();
				return basicGetNominatedVessel();
			case ADPPackage.SUB_CONTRACT_PROFILE__SHIPPING_DAYS:
				return getShippingDays();
			case ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS:
				if (resolve) return getCustomAttribs();
				return basicGetCustomAttribs();
			case ADPPackage.SUB_CONTRACT_PROFILE__CONSTRAINTS:
				return getConstraints();
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
			case ADPPackage.SUB_CONTRACT_PROFILE__NAME:
				setName((String)newValue);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__CONTRACT_TYPE:
				setContractType((ContractType)newValue);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				setDistributionModel((DistributionModel)newValue);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID:
				setSlotTemplateId((String)newValue);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL:
				setNominatedVessel((Vessel)newValue);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__SHIPPING_DAYS:
				setShippingDays((Integer)newValue);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS:
				setCustomAttribs((CustomSubProfileAttributes)newValue);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends SubProfileConstraint>)newValue);
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
			case ADPPackage.SUB_CONTRACT_PROFILE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__CONTRACT_TYPE:
				setContractType(CONTRACT_TYPE_EDEFAULT);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				setDistributionModel((DistributionModel)null);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID:
				setSlotTemplateId(SLOT_TEMPLATE_ID_EDEFAULT);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL:
				setNominatedVessel((Vessel)null);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__SHIPPING_DAYS:
				setShippingDays(SHIPPING_DAYS_EDEFAULT);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS:
				setCustomAttribs((CustomSubProfileAttributes)null);
				return;
			case ADPPackage.SUB_CONTRACT_PROFILE__CONSTRAINTS:
				getConstraints().clear();
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
			case ADPPackage.SUB_CONTRACT_PROFILE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ADPPackage.SUB_CONTRACT_PROFILE__CONTRACT_TYPE:
				return contractType != CONTRACT_TYPE_EDEFAULT;
			case ADPPackage.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				return distributionModel != null;
			case ADPPackage.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID:
				return SLOT_TEMPLATE_ID_EDEFAULT == null ? slotTemplateId != null : !SLOT_TEMPLATE_ID_EDEFAULT.equals(slotTemplateId);
			case ADPPackage.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL:
				return nominatedVessel != null;
			case ADPPackage.SUB_CONTRACT_PROFILE__SHIPPING_DAYS:
				return shippingDays != SHIPPING_DAYS_EDEFAULT;
			case ADPPackage.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS:
				return customAttribs != null;
			case ADPPackage.SUB_CONTRACT_PROFILE__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", contractType: ");
		result.append(contractType);
		result.append(", slotTemplateId: ");
		result.append(slotTemplateId);
		result.append(", shippingDays: ");
		result.append(shippingDays);
		result.append(')');
		return result.toString();
	}

} //SubContractProfileImpl
