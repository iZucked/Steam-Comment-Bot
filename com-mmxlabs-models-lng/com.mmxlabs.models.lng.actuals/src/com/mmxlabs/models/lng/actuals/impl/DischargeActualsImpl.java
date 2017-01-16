/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.DischargeActuals;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Discharge Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl#getDeliveryType <em>Delivery Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl#getEndHeelM3 <em>End Heel M3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl#getEndHeelMMBTu <em>End Heel MMB Tu</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DischargeActualsImpl extends SlotActualsImpl implements DischargeActuals {
	/**
	 * The default value of the '{@link #getDeliveryType() <em>Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected static final String DELIVERY_TYPE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDeliveryType() <em>Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected String deliveryType = DELIVERY_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndHeelM3() <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelM3()
	 * @generated
	 * @ordered
	 */
	protected static final double END_HEEL_M3_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getEndHeelM3() <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelM3()
	 * @generated
	 * @ordered
	 */
	protected double endHeelM3 = END_HEEL_M3_EDEFAULT;
	/**
	 * The default value of the '{@link #getEndHeelMMBTu() <em>End Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelMMBTu()
	 * @generated
	 * @ordered
	 */
	protected static final double END_HEEL_MMB_TU_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getEndHeelMMBTu() <em>End Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeelMMBTu()
	 * @generated
	 * @ordered
	 */
	protected double endHeelMMBTu = END_HEEL_MMB_TU_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DischargeActualsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActualsPackage.Literals.DISCHARGE_ACTUALS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDeliveryType() {
		return deliveryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDeliveryType(String newDeliveryType) {
		String oldDeliveryType = deliveryType;
		deliveryType = newDeliveryType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.DISCHARGE_ACTUALS__DELIVERY_TYPE, oldDeliveryType, deliveryType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getEndHeelM3() {
		return endHeelM3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndHeelM3(double newEndHeelM3) {
		double oldEndHeelM3 = endHeelM3;
		endHeelM3 = newEndHeelM3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_M3, oldEndHeelM3, endHeelM3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getEndHeelMMBTu() {
		return endHeelMMBTu;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndHeelMMBTu(double newEndHeelMMBTu) {
		double oldEndHeelMMBTu = endHeelMMBTu;
		endHeelMMBTu = newEndHeelMMBTu;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_MMB_TU, oldEndHeelMMBTu, endHeelMMBTu));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActualsPackage.DISCHARGE_ACTUALS__DELIVERY_TYPE:
				return getDeliveryType();
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_M3:
				return getEndHeelM3();
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_MMB_TU:
				return getEndHeelMMBTu();
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
			case ActualsPackage.DISCHARGE_ACTUALS__DELIVERY_TYPE:
				setDeliveryType((String)newValue);
				return;
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_M3:
				setEndHeelM3((Double)newValue);
				return;
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_MMB_TU:
				setEndHeelMMBTu((Double)newValue);
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
			case ActualsPackage.DISCHARGE_ACTUALS__DELIVERY_TYPE:
				setDeliveryType(DELIVERY_TYPE_EDEFAULT);
				return;
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_M3:
				setEndHeelM3(END_HEEL_M3_EDEFAULT);
				return;
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_MMB_TU:
				setEndHeelMMBTu(END_HEEL_MMB_TU_EDEFAULT);
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
			case ActualsPackage.DISCHARGE_ACTUALS__DELIVERY_TYPE:
				return DELIVERY_TYPE_EDEFAULT == null ? deliveryType != null : !DELIVERY_TYPE_EDEFAULT.equals(deliveryType);
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_M3:
				return endHeelM3 != END_HEEL_M3_EDEFAULT;
			case ActualsPackage.DISCHARGE_ACTUALS__END_HEEL_MMB_TU:
				return endHeelMMBTu != END_HEEL_MMB_TU_EDEFAULT;
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
		result.append(" (deliveryType: ");
		result.append(deliveryType);
		result.append(", endHeelM3: ");
		result.append(endHeelM3);
		result.append(", endHeelMMBTu: ");
		result.append(endHeelMMBTu);
		result.append(')');
		return result.toString();
	}

} //DischargeActualsImpl
