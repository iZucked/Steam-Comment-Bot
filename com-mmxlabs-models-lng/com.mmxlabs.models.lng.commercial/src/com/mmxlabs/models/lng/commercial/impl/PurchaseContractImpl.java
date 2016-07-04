/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.CargoDeliveryType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl#getCargoCV <em>Cargo CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl#getSalesDeliveryType <em>Sales Delivery Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PurchaseContractImpl extends ContractImpl implements PurchaseContract {
	/**
	 * The default value of the '{@link #getCargoCV() <em>Cargo CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCV()
	 * @generated
	 * @ordered
	 */
	protected static final double CARGO_CV_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getCargoCV() <em>Cargo CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCV()
	 * @generated
	 * @ordered
	 */
	protected double cargoCV = CARGO_CV_EDEFAULT;
	/**
	 * This is true if the Cargo CV attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cargoCVESet;

	/**
	 * The default value of the '{@link #getSalesDeliveryType() <em>Sales Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected static final CargoDeliveryType SALES_DELIVERY_TYPE_EDEFAULT = CargoDeliveryType.ANY;
	/**
	 * The cached value of the '{@link #getSalesDeliveryType() <em>Sales Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected CargoDeliveryType salesDeliveryType = SALES_DELIVERY_TYPE_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCargoCV() {
		return cargoCV;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoCV(double newCargoCV) {
		double oldCargoCV = cargoCV;
		cargoCV = newCargoCV;
		boolean oldCargoCVESet = cargoCVESet;
		cargoCVESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PURCHASE_CONTRACT__CARGO_CV, oldCargoCV, cargoCV, !oldCargoCVESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCargoCV() {
		double oldCargoCV = cargoCV;
		boolean oldCargoCVESet = cargoCVESet;
		cargoCV = CARGO_CV_EDEFAULT;
		cargoCVESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.PURCHASE_CONTRACT__CARGO_CV, oldCargoCV, CARGO_CV_EDEFAULT, oldCargoCVESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCargoCV() {
		return cargoCVESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoDeliveryType getSalesDeliveryType() {
		return salesDeliveryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSalesDeliveryType(CargoDeliveryType newSalesDeliveryType) {
		CargoDeliveryType oldSalesDeliveryType = salesDeliveryType;
		salesDeliveryType = newSalesDeliveryType == null ? SALES_DELIVERY_TYPE_EDEFAULT : newSalesDeliveryType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE, oldSalesDeliveryType, salesDeliveryType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				return getCargoCV();
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				return getSalesDeliveryType();
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
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				setCargoCV((Double)newValue);
				return;
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				setSalesDeliveryType((CargoDeliveryType)newValue);
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
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				unsetCargoCV();
				return;
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				setSalesDeliveryType(SALES_DELIVERY_TYPE_EDEFAULT);
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
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				return isSetCargoCV();
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				return salesDeliveryType != SALES_DELIVERY_TYPE_EDEFAULT;
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
		result.append(" (cargoCV: ");
		if (cargoCVESet) result.append(cargoCV); else result.append("<unset>");
		result.append(", salesDeliveryType: ");
		result.append(salesDeliveryType);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		if (feature == CommercialPackage.Literals.PURCHASE_CONTRACT__CARGO_CV) {
			return new DelegateInformation(CommercialPackage.Literals.CONTRACT__PREFERRED_PORT, PortPackage.Literals.PORT__CV_VALUE, null);
		} 
		return super.getUnsetValueOrDelegate(feature);
	}
} // end of PurchaseContractImpl

// finish type fixing
