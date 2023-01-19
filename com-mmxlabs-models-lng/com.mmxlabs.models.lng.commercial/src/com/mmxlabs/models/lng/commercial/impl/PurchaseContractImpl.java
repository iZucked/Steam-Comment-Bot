/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.types.DESPurchaseDealType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl#getSalesDeliveryType <em>Sales Delivery Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl#getDesPurchaseDealType <em>Des Purchase Deal Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl#getCargoCV <em>Cargo CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl#getUpstreamEmissionRate <em>Upstream Emission Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl#getPipelineEmissionRate <em>Pipeline Emission Rate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PurchaseContractImpl extends ContractImpl implements PurchaseContract {
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
	 * The default value of the '{@link #getDesPurchaseDealType() <em>Des Purchase Deal Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesPurchaseDealType()
	 * @generated
	 * @ordered
	 */
	protected static final DESPurchaseDealType DES_PURCHASE_DEAL_TYPE_EDEFAULT = DESPurchaseDealType.DEST_ONLY;
	/**
	 * The cached value of the '{@link #getDesPurchaseDealType() <em>Des Purchase Deal Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesPurchaseDealType()
	 * @generated
	 * @ordered
	 */
	protected DESPurchaseDealType desPurchaseDealType = DES_PURCHASE_DEAL_TYPE_EDEFAULT;
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
	 * The default value of the '{@link #getUpstreamEmissionRate() <em>Upstream Emission Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpstreamEmissionRate()
	 * @generated
	 * @ordered
	 */
	protected static final double UPSTREAM_EMISSION_RATE_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getUpstreamEmissionRate() <em>Upstream Emission Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpstreamEmissionRate()
	 * @generated
	 * @ordered
	 */
	protected double upstreamEmissionRate = UPSTREAM_EMISSION_RATE_EDEFAULT;
	/**
	 * This is true if the Upstream Emission Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean upstreamEmissionRateESet;
	/**
	 * The default value of the '{@link #getPipelineEmissionRate() <em>Pipeline Emission Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPipelineEmissionRate()
	 * @generated
	 * @ordered
	 */
	protected static final double PIPELINE_EMISSION_RATE_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getPipelineEmissionRate() <em>Pipeline Emission Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPipelineEmissionRate()
	 * @generated
	 * @ordered
	 */
	protected double pipelineEmissionRate = PIPELINE_EMISSION_RATE_EDEFAULT;
	/**
	 * This is true if the Pipeline Emission Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pipelineEmissionRateESet;
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
	@Override
	public double getCargoCV() {
		return cargoCV;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
	public boolean isSetCargoCV() {
		return cargoCVESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getUpstreamEmissionRate() {
		return upstreamEmissionRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUpstreamEmissionRate(double newUpstreamEmissionRate) {
		double oldUpstreamEmissionRate = upstreamEmissionRate;
		upstreamEmissionRate = newUpstreamEmissionRate;
		boolean oldUpstreamEmissionRateESet = upstreamEmissionRateESet;
		upstreamEmissionRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE, oldUpstreamEmissionRate, upstreamEmissionRate, !oldUpstreamEmissionRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetUpstreamEmissionRate() {
		double oldUpstreamEmissionRate = upstreamEmissionRate;
		boolean oldUpstreamEmissionRateESet = upstreamEmissionRateESet;
		upstreamEmissionRate = UPSTREAM_EMISSION_RATE_EDEFAULT;
		upstreamEmissionRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE, oldUpstreamEmissionRate, UPSTREAM_EMISSION_RATE_EDEFAULT, oldUpstreamEmissionRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetUpstreamEmissionRate() {
		return upstreamEmissionRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getPipelineEmissionRate() {
		return pipelineEmissionRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPipelineEmissionRate(double newPipelineEmissionRate) {
		double oldPipelineEmissionRate = pipelineEmissionRate;
		pipelineEmissionRate = newPipelineEmissionRate;
		boolean oldPipelineEmissionRateESet = pipelineEmissionRateESet;
		pipelineEmissionRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE, oldPipelineEmissionRate, pipelineEmissionRate, !oldPipelineEmissionRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPipelineEmissionRate() {
		double oldPipelineEmissionRate = pipelineEmissionRate;
		boolean oldPipelineEmissionRateESet = pipelineEmissionRateESet;
		pipelineEmissionRate = PIPELINE_EMISSION_RATE_EDEFAULT;
		pipelineEmissionRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE, oldPipelineEmissionRate, PIPELINE_EMISSION_RATE_EDEFAULT, oldPipelineEmissionRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPipelineEmissionRate() {
		return pipelineEmissionRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoDeliveryType getSalesDeliveryType() {
		return salesDeliveryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public DESPurchaseDealType getDesPurchaseDealType() {
		return desPurchaseDealType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDesPurchaseDealType(DESPurchaseDealType newDesPurchaseDealType) {
		DESPurchaseDealType oldDesPurchaseDealType = desPurchaseDealType;
		desPurchaseDealType = newDesPurchaseDealType == null ? DES_PURCHASE_DEAL_TYPE_EDEFAULT : newDesPurchaseDealType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE, oldDesPurchaseDealType, desPurchaseDealType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				return getSalesDeliveryType();
			case CommercialPackage.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE:
				return getDesPurchaseDealType();
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				return getCargoCV();
			case CommercialPackage.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE:
				return getUpstreamEmissionRate();
			case CommercialPackage.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE:
				return getPipelineEmissionRate();
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
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				setSalesDeliveryType((CargoDeliveryType)newValue);
				return;
			case CommercialPackage.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE:
				setDesPurchaseDealType((DESPurchaseDealType)newValue);
				return;
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				setCargoCV((Double)newValue);
				return;
			case CommercialPackage.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE:
				setUpstreamEmissionRate((Double)newValue);
				return;
			case CommercialPackage.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE:
				setPipelineEmissionRate((Double)newValue);
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
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				setSalesDeliveryType(SALES_DELIVERY_TYPE_EDEFAULT);
				return;
			case CommercialPackage.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE:
				setDesPurchaseDealType(DES_PURCHASE_DEAL_TYPE_EDEFAULT);
				return;
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				unsetCargoCV();
				return;
			case CommercialPackage.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE:
				unsetUpstreamEmissionRate();
				return;
			case CommercialPackage.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE:
				unsetPipelineEmissionRate();
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
			case CommercialPackage.PURCHASE_CONTRACT__SALES_DELIVERY_TYPE:
				return salesDeliveryType != SALES_DELIVERY_TYPE_EDEFAULT;
			case CommercialPackage.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE:
				return desPurchaseDealType != DES_PURCHASE_DEAL_TYPE_EDEFAULT;
			case CommercialPackage.PURCHASE_CONTRACT__CARGO_CV:
				return isSetCargoCV();
			case CommercialPackage.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE:
				return isSetUpstreamEmissionRate();
			case CommercialPackage.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE:
				return isSetPipelineEmissionRate();
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
		result.append(" (salesDeliveryType: ");
		result.append(salesDeliveryType);
		result.append(", desPurchaseDealType: ");
		result.append(desPurchaseDealType);
		result.append(", cargoCV: ");
		if (cargoCVESet) result.append(cargoCV); else result.append("<unset>");
		result.append(", upstreamEmissionRate: ");
		if (upstreamEmissionRateESet) result.append(upstreamEmissionRate); else result.append("<unset>");
		result.append(", pipelineEmissionRate: ");
		if (pipelineEmissionRateESet) result.append(pipelineEmissionRate); else result.append("<unset>");
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
		} else if (feature == CommercialPackage.Literals.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE) {
			return new DelegateInformation(CommercialPackage.Literals.CONTRACT__PREFERRED_PORT, PortPackage.Literals.PORT__PIPELINE_EMISSION_RATE, null);
		} else if (feature == CommercialPackage.Literals.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE) {
			return new DelegateInformation(CommercialPackage.Literals.CONTRACT__PREFERRED_PORT, PortPackage.Literals.PORT__UPSTREAM_EMISSION_RATE, null);
		} 
		return super.getUnsetValueOrDelegate(feature);
	}
} // end of PurchaseContractImpl

// finish type fixing
