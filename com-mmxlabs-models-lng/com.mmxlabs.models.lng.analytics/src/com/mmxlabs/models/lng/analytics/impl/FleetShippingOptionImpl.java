/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fleet Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.FleetShippingOptionImpl#isUseSafetyHeel <em>Use Safety Heel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FleetShippingOptionImpl extends ShippingOptionImpl implements FleetShippingOption {
	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The default value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected static final String HIRE_COST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected String hireCost = HIRE_COST_EDEFAULT;

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
	 * The default value of the '{@link #isUseSafetyHeel() <em>Use Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseSafetyHeel()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_SAFETY_HEEL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseSafetyHeel() <em>Use Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseSafetyHeel()
	 * @generated
	 * @ordered
	 */
	protected boolean useSafetyHeel = USE_SAFETY_HEEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetShippingOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.FLEET_SHIPPING_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.FLEET_SHIPPING_OPTION__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.FLEET_SHIPPING_OPTION__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHireCost() {
		return hireCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHireCost(String newHireCost) {
		String oldHireCost = hireCost;
		hireCost = newHireCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.FLEET_SHIPPING_OPTION__HIRE_COST, oldHireCost, hireCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (BaseLegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.FLEET_SHIPPING_OPTION__ENTITY, oldEntity, entity));
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
	public void setEntity(BaseLegalEntity newEntity) {
		BaseLegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.FLEET_SHIPPING_OPTION__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUseSafetyHeel() {
		return useSafetyHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseSafetyHeel(boolean newUseSafetyHeel) {
		boolean oldUseSafetyHeel = useSafetyHeel;
		useSafetyHeel = newUseSafetyHeel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL, oldUseSafetyHeel, useSafetyHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__HIRE_COST:
				return getHireCost();
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL:
				return isUseSafetyHeel();
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
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__HIRE_COST:
				setHireCost((String)newValue);
				return;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL:
				setUseSafetyHeel((Boolean)newValue);
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
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__VESSEL:
				setVessel((Vessel)null);
				return;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__HIRE_COST:
				setHireCost(HIRE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL:
				setUseSafetyHeel(USE_SAFETY_HEEL_EDEFAULT);
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
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__VESSEL:
				return vessel != null;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__HIRE_COST:
				return HIRE_COST_EDEFAULT == null ? hireCost != null : !HIRE_COST_EDEFAULT.equals(hireCost);
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__ENTITY:
				return entity != null;
			case AnalyticsPackage.FLEET_SHIPPING_OPTION__USE_SAFETY_HEEL:
				return useSafetyHeel != USE_SAFETY_HEEL_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (hireCost: ");
		result.append(hireCost);
		result.append(", useSafetyHeel: ");
		result.append(useSafetyHeel);
		result.append(')');
		return result.toString();
	}

} //FleetShippingOptionImpl
