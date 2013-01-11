/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract;
import com.mmxlabs.models.lng.types.AIndex;
import com.mmxlabs.models.lng.types.APortSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profit Share Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getBaseMarketPorts <em>Base Market Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getBaseMarketIndex <em>Base Market Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getBaseMarketMultiplier <em>Base Market Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getBaseMarketConstant <em>Base Market Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getRefMarketIndex <em>Ref Market Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getRefMarketMultiplier <em>Ref Market Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getRefMarketConstant <em>Ref Market Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getShare <em>Share</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getMargin <em>Margin</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getSalesMultiplier <em>Sales Multiplier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfitSharePurchaseContractImpl extends PurchaseContractImpl implements ProfitSharePurchaseContract {
	/**
	 * The cached value of the '{@link #getBaseMarketPorts() <em>Base Market Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseMarketPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> baseMarketPorts;

	/**
	 * The cached value of the '{@link #getBaseMarketIndex() <em>Base Market Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseMarketIndex()
	 * @generated
	 * @ordered
	 */
	protected AIndex baseMarketIndex;

	/**
	 * The default value of the '{@link #getBaseMarketMultiplier() <em>Base Market Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseMarketMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double BASE_MARKET_MULTIPLIER_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getBaseMarketMultiplier() <em>Base Market Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseMarketMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double baseMarketMultiplier = BASE_MARKET_MULTIPLIER_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseMarketConstant() <em>Base Market Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseMarketConstant()
	 * @generated
	 * @ordered
	 */
	protected static final double BASE_MARKET_CONSTANT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBaseMarketConstant() <em>Base Market Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseMarketConstant()
	 * @generated
	 * @ordered
	 */
	protected double baseMarketConstant = BASE_MARKET_CONSTANT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRefMarketIndex() <em>Ref Market Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefMarketIndex()
	 * @generated
	 * @ordered
	 */
	protected AIndex refMarketIndex;

	/**
	 * The default value of the '{@link #getRefMarketMultiplier() <em>Ref Market Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefMarketMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double REF_MARKET_MULTIPLIER_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getRefMarketMultiplier() <em>Ref Market Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefMarketMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double refMarketMultiplier = REF_MARKET_MULTIPLIER_EDEFAULT;

	/**
	 * The default value of the '{@link #getRefMarketConstant() <em>Ref Market Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefMarketConstant()
	 * @generated
	 * @ordered
	 */
	protected static final double REF_MARKET_CONSTANT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRefMarketConstant() <em>Ref Market Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefMarketConstant()
	 * @generated
	 * @ordered
	 */
	protected double refMarketConstant = REF_MARKET_CONSTANT_EDEFAULT;

	/**
	 * The default value of the '{@link #getShare() <em>Share</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShare()
	 * @generated
	 * @ordered
	 */
	protected static final double SHARE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getShare() <em>Share</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShare()
	 * @generated
	 * @ordered
	 */
	protected double share = SHARE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMargin() <em>Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMargin()
	 * @generated
	 * @ordered
	 */
	protected static final double MARGIN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMargin() <em>Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMargin()
	 * @generated
	 * @ordered
	 */
	protected double margin = MARGIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getSalesMultiplier() <em>Sales Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getSalesMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double SALES_MULTIPLIER_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSalesMultiplier() <em>Sales Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getSalesMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double salesMultiplier = SALES_MULTIPLIER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfitSharePurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getBaseMarketPorts() {
		if (baseMarketPorts == null) {
			baseMarketPorts = new EObjectResolvingEList<APortSet>(APortSet.class, this, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS);
		}
		return baseMarketPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex getBaseMarketIndex() {
		if (baseMarketIndex != null && baseMarketIndex.eIsProxy()) {
			InternalEObject oldBaseMarketIndex = (InternalEObject)baseMarketIndex;
			baseMarketIndex = (AIndex)eResolveProxy(oldBaseMarketIndex);
			if (baseMarketIndex != oldBaseMarketIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX, oldBaseMarketIndex, baseMarketIndex));
			}
		}
		return baseMarketIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex basicGetBaseMarketIndex() {
		return baseMarketIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseMarketIndex(AIndex newBaseMarketIndex) {
		AIndex oldBaseMarketIndex = baseMarketIndex;
		baseMarketIndex = newBaseMarketIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX, oldBaseMarketIndex, baseMarketIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getBaseMarketConstant() {
		return baseMarketConstant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseMarketConstant(double newBaseMarketConstant) {
		double oldBaseMarketConstant = baseMarketConstant;
		baseMarketConstant = newBaseMarketConstant;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT, oldBaseMarketConstant, baseMarketConstant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getBaseMarketMultiplier() {
		return baseMarketMultiplier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseMarketMultiplier(double newBaseMarketMultiplier) {
		double oldBaseMarketMultiplier = baseMarketMultiplier;
		baseMarketMultiplier = newBaseMarketMultiplier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER, oldBaseMarketMultiplier, baseMarketMultiplier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex getRefMarketIndex() {
		if (refMarketIndex != null && refMarketIndex.eIsProxy()) {
			InternalEObject oldRefMarketIndex = (InternalEObject)refMarketIndex;
			refMarketIndex = (AIndex)eResolveProxy(oldRefMarketIndex);
			if (refMarketIndex != oldRefMarketIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX, oldRefMarketIndex, refMarketIndex));
			}
		}
		return refMarketIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex basicGetRefMarketIndex() {
		return refMarketIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRefMarketIndex(AIndex newRefMarketIndex) {
		AIndex oldRefMarketIndex = refMarketIndex;
		refMarketIndex = newRefMarketIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX, oldRefMarketIndex, refMarketIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getRefMarketConstant() {
		return refMarketConstant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRefMarketConstant(double newRefMarketConstant) {
		double oldRefMarketConstant = refMarketConstant;
		refMarketConstant = newRefMarketConstant;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT, oldRefMarketConstant, refMarketConstant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getRefMarketMultiplier() {
		return refMarketMultiplier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRefMarketMultiplier(double newRefMarketMultiplier) {
		double oldRefMarketMultiplier = refMarketMultiplier;
		refMarketMultiplier = newRefMarketMultiplier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER, oldRefMarketMultiplier, refMarketMultiplier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getShare() {
		return share;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShare(double newShare) {
		double oldShare = share;
		share = newShare;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE, oldShare, share));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMargin() {
		return margin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMargin(double newMargin) {
		double oldMargin = margin;
		margin = newMargin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN, oldMargin, margin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getSalesMultiplier() {
		return salesMultiplier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSalesMultiplier(double newSalesMultiplier) {
		double oldSalesMultiplier = salesMultiplier;
		salesMultiplier = newSalesMultiplier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER, oldSalesMultiplier, salesMultiplier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS:
				return getBaseMarketPorts();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX:
				if (resolve) return getBaseMarketIndex();
				return basicGetBaseMarketIndex();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER:
				return getBaseMarketMultiplier();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT:
				return getBaseMarketConstant();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX:
				if (resolve) return getRefMarketIndex();
				return basicGetRefMarketIndex();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER:
				return getRefMarketMultiplier();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT:
				return getRefMarketConstant();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				return getShare();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN:
				return getMargin();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER:
				return getSalesMultiplier();
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
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS:
				getBaseMarketPorts().clear();
				getBaseMarketPorts().addAll((Collection<? extends APortSet>)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX:
				setBaseMarketIndex((AIndex)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER:
				setBaseMarketMultiplier((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT:
				setBaseMarketConstant((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX:
				setRefMarketIndex((AIndex)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER:
				setRefMarketMultiplier((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT:
				setRefMarketConstant((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				setShare((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN:
				setMargin((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER:
				setSalesMultiplier((Double)newValue);
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
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS:
				getBaseMarketPorts().clear();
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX:
				setBaseMarketIndex((AIndex)null);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER:
				setBaseMarketMultiplier(BASE_MARKET_MULTIPLIER_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT:
				setBaseMarketConstant(BASE_MARKET_CONSTANT_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX:
				setRefMarketIndex((AIndex)null);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER:
				setRefMarketMultiplier(REF_MARKET_MULTIPLIER_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT:
				setRefMarketConstant(REF_MARKET_CONSTANT_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				setShare(SHARE_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN:
				setMargin(MARGIN_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER:
				setSalesMultiplier(SALES_MULTIPLIER_EDEFAULT);
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
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS:
				return baseMarketPorts != null && !baseMarketPorts.isEmpty();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX:
				return baseMarketIndex != null;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER:
				return baseMarketMultiplier != BASE_MARKET_MULTIPLIER_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT:
				return baseMarketConstant != BASE_MARKET_CONSTANT_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX:
				return refMarketIndex != null;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER:
				return refMarketMultiplier != REF_MARKET_MULTIPLIER_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT:
				return refMarketConstant != REF_MARKET_CONSTANT_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				return share != SHARE_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN:
				return margin != MARGIN_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER:
				return salesMultiplier != SALES_MULTIPLIER_EDEFAULT;
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
		result.append(" (baseMarketMultiplier: ");
		result.append(baseMarketMultiplier);
		result.append(", baseMarketConstant: ");
		result.append(baseMarketConstant);
		result.append(", refMarketMultiplier: ");
		result.append(refMarketMultiplier);
		result.append(", refMarketConstant: ");
		result.append(refMarketConstant);
		result.append(", share: ");
		result.append(share);
		result.append(", margin: ");
		result.append(margin);
		result.append(", salesMultiplier: ");
		result.append(salesMultiplier);
		result.append(')');
		return result.toString();
	}

} // end of ProfitSharePurchaseContractImpl

// finish type fixing
