/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.ShippingOption;

import com.mmxlabs.models.lng.adp.SubContractProfile;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binding Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.BindingRuleImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.BindingRuleImpl#getSubProfile <em>Sub Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.BindingRuleImpl#getFlowType <em>Flow Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.BindingRuleImpl#getShippingOption <em>Shipping Option</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BindingRuleImpl extends EObjectImpl implements BindingRule {
	/**
	 * The cached value of the '{@link #getProfile() <em>Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfile()
	 * @generated
	 * @ordered
	 */
	protected ContractProfile<?> profile;

	/**
	 * The cached value of the '{@link #getSubProfile() <em>Sub Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubProfile()
	 * @generated
	 * @ordered
	 */
	protected SubContractProfile<?> subProfile;

	/**
	 * The cached value of the '{@link #getFlowType() <em>Flow Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlowType()
	 * @generated
	 * @ordered
	 */
	protected FlowType flowType;

	/**
	 * The cached value of the '{@link #getShippingOption() <em>Shipping Option</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingOption()
	 * @generated
	 * @ordered
	 */
	protected ShippingOption shippingOption;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BindingRuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.BINDING_RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractProfile<?> getProfile() {
		if (profile != null && profile.eIsProxy()) {
			InternalEObject oldProfile = (InternalEObject)profile;
			profile = (ContractProfile<?>)eResolveProxy(oldProfile);
			if (profile != oldProfile) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.BINDING_RULE__PROFILE, oldProfile, profile));
			}
		}
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractProfile<?> basicGetProfile() {
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfile(ContractProfile<?> newProfile) {
		ContractProfile<?> oldProfile = profile;
		profile = newProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.BINDING_RULE__PROFILE, oldProfile, profile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SubContractProfile<?> getSubProfile() {
		if (subProfile != null && subProfile.eIsProxy()) {
			InternalEObject oldSubProfile = (InternalEObject)subProfile;
			subProfile = (SubContractProfile<?>)eResolveProxy(oldSubProfile);
			if (subProfile != oldSubProfile) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.BINDING_RULE__SUB_PROFILE, oldSubProfile, subProfile));
			}
		}
		return subProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SubContractProfile<?> basicGetSubProfile() {
		return subProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubProfile(SubContractProfile<?> newSubProfile) {
		SubContractProfile<?> oldSubProfile = subProfile;
		subProfile = newSubProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.BINDING_RULE__SUB_PROFILE, oldSubProfile, subProfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowType getFlowType() {
		if (flowType != null && flowType.eIsProxy()) {
			InternalEObject oldFlowType = (InternalEObject)flowType;
			flowType = (FlowType)eResolveProxy(oldFlowType);
			if (flowType != oldFlowType) {
				InternalEObject newFlowType = (InternalEObject)flowType;
				NotificationChain msgs = oldFlowType.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__FLOW_TYPE, null, null);
				if (newFlowType.eInternalContainer() == null) {
					msgs = newFlowType.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__FLOW_TYPE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.BINDING_RULE__FLOW_TYPE, oldFlowType, flowType));
			}
		}
		return flowType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowType basicGetFlowType() {
		return flowType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFlowType(FlowType newFlowType, NotificationChain msgs) {
		FlowType oldFlowType = flowType;
		flowType = newFlowType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.BINDING_RULE__FLOW_TYPE, oldFlowType, newFlowType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFlowType(FlowType newFlowType) {
		if (newFlowType != flowType) {
			NotificationChain msgs = null;
			if (flowType != null)
				msgs = ((InternalEObject)flowType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__FLOW_TYPE, null, msgs);
			if (newFlowType != null)
				msgs = ((InternalEObject)newFlowType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__FLOW_TYPE, null, msgs);
			msgs = basicSetFlowType(newFlowType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.BINDING_RULE__FLOW_TYPE, newFlowType, newFlowType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShippingOption getShippingOption() {
		if (shippingOption != null && shippingOption.eIsProxy()) {
			InternalEObject oldShippingOption = (InternalEObject)shippingOption;
			shippingOption = (ShippingOption)eResolveProxy(oldShippingOption);
			if (shippingOption != oldShippingOption) {
				InternalEObject newShippingOption = (InternalEObject)shippingOption;
				NotificationChain msgs = oldShippingOption.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__SHIPPING_OPTION, null, null);
				if (newShippingOption.eInternalContainer() == null) {
					msgs = newShippingOption.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__SHIPPING_OPTION, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.BINDING_RULE__SHIPPING_OPTION, oldShippingOption, shippingOption));
			}
		}
		return shippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShippingOption basicGetShippingOption() {
		return shippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetShippingOption(ShippingOption newShippingOption, NotificationChain msgs) {
		ShippingOption oldShippingOption = shippingOption;
		shippingOption = newShippingOption;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.BINDING_RULE__SHIPPING_OPTION, oldShippingOption, newShippingOption);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShippingOption(ShippingOption newShippingOption) {
		if (newShippingOption != shippingOption) {
			NotificationChain msgs = null;
			if (shippingOption != null)
				msgs = ((InternalEObject)shippingOption).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__SHIPPING_OPTION, null, msgs);
			if (newShippingOption != null)
				msgs = ((InternalEObject)newShippingOption).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.BINDING_RULE__SHIPPING_OPTION, null, msgs);
			msgs = basicSetShippingOption(newShippingOption, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.BINDING_RULE__SHIPPING_OPTION, newShippingOption, newShippingOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.BINDING_RULE__FLOW_TYPE:
				return basicSetFlowType(null, msgs);
			case ADPPackage.BINDING_RULE__SHIPPING_OPTION:
				return basicSetShippingOption(null, msgs);
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
			case ADPPackage.BINDING_RULE__PROFILE:
				if (resolve) return getProfile();
				return basicGetProfile();
			case ADPPackage.BINDING_RULE__SUB_PROFILE:
				if (resolve) return getSubProfile();
				return basicGetSubProfile();
			case ADPPackage.BINDING_RULE__FLOW_TYPE:
				if (resolve) return getFlowType();
				return basicGetFlowType();
			case ADPPackage.BINDING_RULE__SHIPPING_OPTION:
				if (resolve) return getShippingOption();
				return basicGetShippingOption();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ADPPackage.BINDING_RULE__PROFILE:
				setProfile((ContractProfile<?>)newValue);
				return;
			case ADPPackage.BINDING_RULE__SUB_PROFILE:
				setSubProfile((SubContractProfile<?>)newValue);
				return;
			case ADPPackage.BINDING_RULE__FLOW_TYPE:
				setFlowType((FlowType)newValue);
				return;
			case ADPPackage.BINDING_RULE__SHIPPING_OPTION:
				setShippingOption((ShippingOption)newValue);
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
			case ADPPackage.BINDING_RULE__PROFILE:
				setProfile((ContractProfile<?>)null);
				return;
			case ADPPackage.BINDING_RULE__SUB_PROFILE:
				setSubProfile((SubContractProfile<?>)null);
				return;
			case ADPPackage.BINDING_RULE__FLOW_TYPE:
				setFlowType((FlowType)null);
				return;
			case ADPPackage.BINDING_RULE__SHIPPING_OPTION:
				setShippingOption((ShippingOption)null);
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
			case ADPPackage.BINDING_RULE__PROFILE:
				return profile != null;
			case ADPPackage.BINDING_RULE__SUB_PROFILE:
				return subProfile != null;
			case ADPPackage.BINDING_RULE__FLOW_TYPE:
				return flowType != null;
			case ADPPackage.BINDING_RULE__SHIPPING_OPTION:
				return shippingOption != null;
		}
		return super.eIsSet(featureID);
	}

} //BindingRuleImpl
