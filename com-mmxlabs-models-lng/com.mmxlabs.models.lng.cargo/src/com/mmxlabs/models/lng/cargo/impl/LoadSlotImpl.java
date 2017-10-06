/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.CargoDeliveryType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl#getCargoCV <em>Cargo CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl#isArriveCold <em>Arrive Cold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl#isDESPurchase <em>DES Purchase</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl#getTransferFrom <em>Transfer From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl#getSalesDeliveryType <em>Sales Delivery Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LoadSlotImpl extends SlotImpl implements LoadSlot {
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
	 * The default value of the '{@link #isArriveCold() <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isArriveCold()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ARRIVE_COLD_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isArriveCold() <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isArriveCold()
	 * @generated
	 * @ordered
	 */
	protected boolean arriveCold = ARRIVE_COLD_EDEFAULT;

	/**
	 * This is true if the Arrive Cold attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean arriveColdESet;

	/**
	 * The default value of the '{@link #isDESPurchase() <em>DES Purchase</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDESPurchase()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DES_PURCHASE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDESPurchase() <em>DES Purchase</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDESPurchase()
	 * @generated
	 * @ordered
	 */
	protected boolean desPurchase = DES_PURCHASE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTransferFrom() <em>Transfer From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferFrom()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot transferFrom;

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
	 * This is true if the Sales Delivery Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean salesDeliveryTypeESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LoadSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.LOAD_SLOT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__CARGO_CV, oldCargoCV, cargoCV, !oldCargoCVESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.LOAD_SLOT__CARGO_CV, oldCargoCV, CARGO_CV_EDEFAULT, oldCargoCVESet));
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
	public boolean isArriveCold() {
		return arriveCold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArriveCold(boolean newArriveCold) {
		boolean oldArriveCold = arriveCold;
		arriveCold = newArriveCold;
		boolean oldArriveColdESet = arriveColdESet;
		arriveColdESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__ARRIVE_COLD, oldArriveCold, arriveCold, !oldArriveColdESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetArriveCold() {
		boolean oldArriveCold = arriveCold;
		boolean oldArriveColdESet = arriveColdESet;
		arriveCold = ARRIVE_COLD_EDEFAULT;
		arriveColdESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.LOAD_SLOT__ARRIVE_COLD, oldArriveCold, ARRIVE_COLD_EDEFAULT, oldArriveColdESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetArriveCold() {
		return arriveColdESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDESPurchase() {
		return desPurchase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDESPurchase(boolean newDESPurchase) {
		boolean oldDESPurchase = desPurchase;
		desPurchase = newDESPurchase;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__DES_PURCHASE, oldDESPurchase, desPurchase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot getTransferFrom() {
		if (transferFrom != null && transferFrom.eIsProxy()) {
			InternalEObject oldTransferFrom = (InternalEObject)transferFrom;
			transferFrom = (DischargeSlot)eResolveProxy(oldTransferFrom);
			if (transferFrom != oldTransferFrom) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.LOAD_SLOT__TRANSFER_FROM, oldTransferFrom, transferFrom));
			}
		}
		return transferFrom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetTransferFrom() {
		return transferFrom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTransferFrom(DischargeSlot newTransferFrom, NotificationChain msgs) {
		DischargeSlot oldTransferFrom = transferFrom;
		transferFrom = newTransferFrom;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__TRANSFER_FROM, oldTransferFrom, newTransferFrom);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransferFrom(DischargeSlot newTransferFrom) {
		if (newTransferFrom != transferFrom) {
			NotificationChain msgs = null;
			if (transferFrom != null)
				msgs = ((InternalEObject)transferFrom).eInverseRemove(this, CargoPackage.DISCHARGE_SLOT__TRANSFER_TO, DischargeSlot.class, msgs);
			if (newTransferFrom != null)
				msgs = ((InternalEObject)newTransferFrom).eInverseAdd(this, CargoPackage.DISCHARGE_SLOT__TRANSFER_TO, DischargeSlot.class, msgs);
			msgs = basicSetTransferFrom(newTransferFrom, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__TRANSFER_FROM, newTransferFrom, newTransferFrom));
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
		boolean oldSalesDeliveryTypeESet = salesDeliveryTypeESet;
		salesDeliveryTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__SALES_DELIVERY_TYPE, oldSalesDeliveryType, salesDeliveryType, !oldSalesDeliveryTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSalesDeliveryType() {
		CargoDeliveryType oldSalesDeliveryType = salesDeliveryType;
		boolean oldSalesDeliveryTypeESet = salesDeliveryTypeESet;
		salesDeliveryType = SALES_DELIVERY_TYPE_EDEFAULT;
		salesDeliveryTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.LOAD_SLOT__SALES_DELIVERY_TYPE, oldSalesDeliveryType, SALES_DELIVERY_TYPE_EDEFAULT, oldSalesDeliveryTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSalesDeliveryType() {
		return salesDeliveryTypeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getSlotOrDelegatedCV() {
		return (Double) eGetWithDefault(CargoPackage.Literals.LOAD_SLOT__CARGO_CV);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public CargoDeliveryType getSlotOrContractDeliveryType() {
		return (CargoDeliveryType) eGetWithDefault(CargoPackage.Literals.LOAD_SLOT__SALES_DELIVERY_TYPE);	
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.LOAD_SLOT__TRANSFER_FROM:
				if (transferFrom != null)
					msgs = ((InternalEObject)transferFrom).eInverseRemove(this, CargoPackage.DISCHARGE_SLOT__TRANSFER_TO, DischargeSlot.class, msgs);
				return basicSetTransferFrom((DischargeSlot)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.LOAD_SLOT__TRANSFER_FROM:
				return basicSetTransferFrom(null, msgs);
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
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				return getCargoCV();
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				return isArriveCold();
			case CargoPackage.LOAD_SLOT__DES_PURCHASE:
				return isDESPurchase();
			case CargoPackage.LOAD_SLOT__TRANSFER_FROM:
				if (resolve) return getTransferFrom();
				return basicGetTransferFrom();
			case CargoPackage.LOAD_SLOT__SALES_DELIVERY_TYPE:
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
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				setCargoCV((Double)newValue);
				return;
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				setArriveCold((Boolean)newValue);
				return;
			case CargoPackage.LOAD_SLOT__DES_PURCHASE:
				setDESPurchase((Boolean)newValue);
				return;
			case CargoPackage.LOAD_SLOT__TRANSFER_FROM:
				setTransferFrom((DischargeSlot)newValue);
				return;
			case CargoPackage.LOAD_SLOT__SALES_DELIVERY_TYPE:
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
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				unsetCargoCV();
				return;
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				unsetArriveCold();
				return;
			case CargoPackage.LOAD_SLOT__DES_PURCHASE:
				setDESPurchase(DES_PURCHASE_EDEFAULT);
				return;
			case CargoPackage.LOAD_SLOT__TRANSFER_FROM:
				setTransferFrom((DischargeSlot)null);
				return;
			case CargoPackage.LOAD_SLOT__SALES_DELIVERY_TYPE:
				unsetSalesDeliveryType();
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
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				return isSetCargoCV();
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				return isSetArriveCold();
			case CargoPackage.LOAD_SLOT__DES_PURCHASE:
				return desPurchase != DES_PURCHASE_EDEFAULT;
			case CargoPackage.LOAD_SLOT__TRANSFER_FROM:
				return transferFrom != null;
			case CargoPackage.LOAD_SLOT__SALES_DELIVERY_TYPE:
				return isSetSalesDeliveryType();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.LOAD_SLOT___GET_SLOT_OR_DELEGATED_CV:
				return getSlotOrDelegatedCV();
			case CargoPackage.LOAD_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE:
				return getSlotOrContractDeliveryType();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(", arriveCold: ");
		if (arriveColdESet) result.append(arriveCold); else result.append("<unset>");
		result.append(", DESPurchase: ");
		result.append(desPurchase);
		result.append(", salesDeliveryType: ");
		if (salesDeliveryTypeESet) result.append(salesDeliveryType); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		if (feature == CargoPackage.Literals.SLOT__DURATION) {
			return new DelegateInformation(CargoPackage.Literals.SLOT__PORT, PortPackage.Literals.PORT__LOAD_DURATION, (Integer) 12);
		} else if (feature == CargoPackage.Literals.LOAD_SLOT__CARGO_CV) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SLOT__CONTRACT || changedFeature == CargoPackage.Literals.SLOT__PORT);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					final PurchaseContract purchaseContract = (PurchaseContract) getContract();
					if (purchaseContract != null) {
						// Get value if set, otherwise we want to fall back to current load port, not contract fallback.
						if (purchaseContract.eIsSet(CommercialPackage.Literals.PURCHASE_CONTRACT__CARGO_CV)) {
							result = purchaseContract.eGet(CommercialPackage.Literals.PURCHASE_CONTRACT__CARGO_CV);
						}
					}
					if (result == null && port != null) {
						result =  port.eGetWithDefault(PortPackage.Literals.PORT__CV_VALUE);
					}
					if (result == null) {
						result = (Double) 0.0;
					}
					return result;
					
				}				
			};
		} else if (feature == CargoPackage.Literals.LOAD_SLOT__ARRIVE_COLD) {
			return new DelegateInformation(CargoPackage.Literals.SLOT__PORT, PortPackage.Literals.PORT__ALLOW_COOLDOWN, Boolean.TRUE);
		} else if (feature == CargoPackage.Literals.LOAD_SLOT__SALES_DELIVERY_TYPE) {
			return new DelegateInformation(CargoPackage.eINSTANCE.getSlot_Contract(), CommercialPackage.Literals.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE, CargoDeliveryType.ANY);
		} else if (feature == CargoPackage.Literals.SLOT__PRICING_EVENT) {
			return new DelegateInformation(CargoPackage.eINSTANCE.getSlot_Contract(), CommercialPackage.Literals.CONTRACT__PRICING_EVENT, PricingEvent.START_LOAD);
		}
		return super.getUnsetValueOrDelegate(feature);
	}

	
} // end of LoadSlotImpl

// finish type fixing
