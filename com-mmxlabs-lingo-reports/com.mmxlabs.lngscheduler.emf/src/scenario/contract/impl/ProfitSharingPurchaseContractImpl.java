/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import scenario.contract.ContractPackage;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.market.Market;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profit Sharing Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getMarket <em>Market</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getReferenceMarket <em>Reference Market</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getAlpha <em>Alpha</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getBeta <em>Beta</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getGamma <em>Gamma</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfitSharingPurchaseContractImpl extends PurchaseContractImpl implements ProfitSharingPurchaseContract {
	/**
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected Market market;
	/**
	 * The cached value of the '{@link #getReferenceMarket() <em>Reference Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceMarket()
	 * @generated
	 * @ordered
	 */
	protected Market referenceMarket;
	/**
	 * The default value of the '{@link #getAlpha() <em>Alpha</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlpha()
	 * @generated
	 * @ordered
	 */
	protected static final float ALPHA_EDEFAULT = 0.0F;
	/**
	 * The cached value of the '{@link #getAlpha() <em>Alpha</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlpha()
	 * @generated
	 * @ordered
	 */
	protected float alpha = ALPHA_EDEFAULT;
	/**
	 * The default value of the '{@link #getBeta() <em>Beta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeta()
	 * @generated
	 * @ordered
	 */
	protected static final float BETA_EDEFAULT = 0.0F;
	/**
	 * The cached value of the '{@link #getBeta() <em>Beta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeta()
	 * @generated
	 * @ordered
	 */
	protected float beta = BETA_EDEFAULT;
	/**
	 * The default value of the '{@link #getGamma() <em>Gamma</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGamma()
	 * @generated
	 * @ordered
	 */
	protected static final float GAMMA_EDEFAULT = 0.0F;
	/**
	 * The cached value of the '{@link #getGamma() <em>Gamma</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGamma()
	 * @generated
	 * @ordered
	 */
	protected float gamma = GAMMA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfitSharingPurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.PROFIT_SHARING_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Market getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (Market)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Market basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarket(Market newMarket) {
		Market oldMarket = market;
		market = newMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__MARKET, oldMarket, market));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Market getReferenceMarket() {
		if (referenceMarket != null && referenceMarket.eIsProxy()) {
			InternalEObject oldReferenceMarket = (InternalEObject)referenceMarket;
			referenceMarket = (Market)eResolveProxy(oldReferenceMarket);
			if (referenceMarket != oldReferenceMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_MARKET, oldReferenceMarket, referenceMarket));
			}
		}
		return referenceMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Market basicGetReferenceMarket() {
		return referenceMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferenceMarket(Market newReferenceMarket) {
		Market oldReferenceMarket = referenceMarket;
		referenceMarket = newReferenceMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_MARKET, oldReferenceMarket, referenceMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getAlpha() {
		return alpha;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAlpha(float newAlpha) {
		float oldAlpha = alpha;
		alpha = newAlpha;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA, oldAlpha, alpha));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getBeta() {
		return beta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBeta(float newBeta) {
		float oldBeta = beta;
		beta = newBeta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA, oldBeta, beta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getGamma() {
		return gamma;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGamma(float newGamma) {
		float oldGamma = gamma;
		gamma = newGamma;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA, oldGamma, gamma));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__MARKET:
				if (resolve) return getMarket();
				return basicGetMarket();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_MARKET:
				if (resolve) return getReferenceMarket();
				return basicGetReferenceMarket();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				return getAlpha();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				return getBeta();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				return getGamma();
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
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__MARKET:
				setMarket((Market)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_MARKET:
				setReferenceMarket((Market)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				setAlpha((Float)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				setBeta((Float)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				setGamma((Float)newValue);
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
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__MARKET:
				setMarket((Market)null);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_MARKET:
				setReferenceMarket((Market)null);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				setAlpha(ALPHA_EDEFAULT);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				setBeta(BETA_EDEFAULT);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				setGamma(GAMMA_EDEFAULT);
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
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__MARKET:
				return market != null;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_MARKET:
				return referenceMarket != null;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				return alpha != ALPHA_EDEFAULT;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				return beta != BETA_EDEFAULT;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				return gamma != GAMMA_EDEFAULT;
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
		result.append(" (alpha: ");
		result.append(alpha);
		result.append(", beta: ");
		result.append(beta);
		result.append(", gamma: ");
		result.append(gamma);
		result.append(')');
		return result.toString();
	}

} //ProfitSharingPurchaseContractImpl
