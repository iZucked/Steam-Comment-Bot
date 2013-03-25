/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.port.PortPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Discharge Slot</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl#isFOBSale <em>FOB Sale</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DischargeSlotImpl extends SlotImpl implements DischargeSlot {
	/**
	 * The default value of the '{@link #isFOBSale() <em>FOB Sale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFOBSale()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FOB_SALE_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isFOBSale() <em>FOB Sale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFOBSale()
	 * @generated
	 * @ordered
	 */
	protected boolean fobSale = FOB_SALE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPurchaseDeliveryType() <em>Purchase Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getPurchaseDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected static final CargoDeliveryType PURCHASE_DELIVERY_TYPE_EDEFAULT = CargoDeliveryType.ANY;

	/**
	 * The cached value of the '{@link #getPurchaseDeliveryType() <em>Purchase Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getPurchaseDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected CargoDeliveryType purchaseDeliveryType = PURCHASE_DELIVERY_TYPE_EDEFAULT;

	/**
	 * This is true if the Purchase Delivery Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean purchaseDeliveryTypeESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DischargeSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.DISCHARGE_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFOBSale() {
		return fobSale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFOBSale(boolean newFOBSale) {
		boolean oldFOBSale = fobSale;
		fobSale = newFOBSale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.DISCHARGE_SLOT__FOB_SALE, oldFOBSale, fobSale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoDeliveryType getPurchaseDeliveryType() {
		return purchaseDeliveryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPurchaseDeliveryType(CargoDeliveryType newPurchaseDeliveryType) {
		CargoDeliveryType oldPurchaseDeliveryType = purchaseDeliveryType;
		purchaseDeliveryType = newPurchaseDeliveryType == null ? PURCHASE_DELIVERY_TYPE_EDEFAULT : newPurchaseDeliveryType;
		boolean oldPurchaseDeliveryTypeESet = purchaseDeliveryTypeESet;
		purchaseDeliveryTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE, oldPurchaseDeliveryType, purchaseDeliveryType, !oldPurchaseDeliveryTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPurchaseDeliveryType() {
		CargoDeliveryType oldPurchaseDeliveryType = purchaseDeliveryType;
		boolean oldPurchaseDeliveryTypeESet = purchaseDeliveryTypeESet;
		purchaseDeliveryType = PURCHASE_DELIVERY_TYPE_EDEFAULT;
		purchaseDeliveryTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE, oldPurchaseDeliveryType, PURCHASE_DELIVERY_TYPE_EDEFAULT, oldPurchaseDeliveryTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPurchaseDeliveryType() {
		return purchaseDeliveryTypeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.DISCHARGE_SLOT__FOB_SALE:
				return isFOBSale();
			case CargoPackage.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE:
				return getPurchaseDeliveryType();
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
			case CargoPackage.DISCHARGE_SLOT__FOB_SALE:
				setFOBSale((Boolean)newValue);
				return;
			case CargoPackage.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE:
				setPurchaseDeliveryType((CargoDeliveryType)newValue);
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
			case CargoPackage.DISCHARGE_SLOT__FOB_SALE:
				setFOBSale(FOB_SALE_EDEFAULT);
				return;
			case CargoPackage.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE:
				unsetPurchaseDeliveryType();
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
			case CargoPackage.DISCHARGE_SLOT__FOB_SALE:
				return fobSale != FOB_SALE_EDEFAULT;
			case CargoPackage.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE:
				return isSetPurchaseDeliveryType();
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
		result.append(" (FOBSale: ");
		result.append(fobSale);
		result.append(", PurchaseDeliveryType: ");
		if (purchaseDeliveryTypeESet) result.append(purchaseDeliveryType); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		if (feature == CargoPackage.Literals.SLOT__DURATION) {
			return new DelegateInformation(CargoPackage.eINSTANCE.getSlot_Port(), PortPackage.eINSTANCE.getPort_DischargeDuration(), (Integer) 12);
		}
		
		else if (feature == CargoPackage.Literals.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE) {
			return new DelegateInformation(CargoPackage.eINSTANCE.getSlot_Contract(), CommercialPackage.Literals.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE, (Integer) 0);			
		}
		
		return super.getUnsetValueOrDelegate(feature);
	}
	
} // end of DischargeSlotImpl

// finish type fixing
