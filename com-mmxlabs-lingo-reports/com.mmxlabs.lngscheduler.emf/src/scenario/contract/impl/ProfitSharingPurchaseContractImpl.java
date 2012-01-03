/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.contract.ContractPackage;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.market.Index;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profit Sharing Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getAlpha <em>Alpha</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getBeta <em>Beta</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getGamma <em>Gamma</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl#getReferenceIndex <em>Reference Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfitSharingPurchaseContractImpl extends PurchaseContractImpl implements ProfitSharingPurchaseContract {
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
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected Index index;

	/**
	 * The cached value of the '{@link #getReferenceIndex() <em>Reference Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceIndex()
	 * @generated
	 * @ordered
	 */
	protected Index referenceIndex;

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
	public Index getIndex() {
		if (index != null && index.eIsProxy()) {
			InternalEObject oldIndex = (InternalEObject)index;
			index = (Index)eResolveProxy(oldIndex);
			if (index != oldIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__INDEX, oldIndex, index));
			}
		}
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index basicGetIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(Index newIndex) {
		Index oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index getReferenceIndex() {
		if (referenceIndex != null && referenceIndex.eIsProxy()) {
			InternalEObject oldReferenceIndex = (InternalEObject)referenceIndex;
			referenceIndex = (Index)eResolveProxy(oldReferenceIndex);
			if (referenceIndex != oldReferenceIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX, oldReferenceIndex, referenceIndex));
			}
		}
		return referenceIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index basicGetReferenceIndex() {
		return referenceIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferenceIndex(Index newReferenceIndex) {
		Index oldReferenceIndex = referenceIndex;
		referenceIndex = newReferenceIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX, oldReferenceIndex, referenceIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getAlpha() {
		return alpha;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public float getBeta() {
		return beta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public float getGamma() {
		return gamma;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				return getAlpha();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				return getBeta();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				return getGamma();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__INDEX:
				if (resolve) return getIndex();
				return basicGetIndex();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX:
				if (resolve) return getReferenceIndex();
				return basicGetReferenceIndex();
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
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				setAlpha((Float)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				setBeta((Float)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				setGamma((Float)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__INDEX:
				setIndex((Index)newValue);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX:
				setReferenceIndex((Index)newValue);
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
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				setAlpha(ALPHA_EDEFAULT);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				setBeta(BETA_EDEFAULT);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				setGamma(GAMMA_EDEFAULT);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__INDEX:
				setIndex((Index)null);
				return;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX:
				setReferenceIndex((Index)null);
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
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA:
				return alpha != ALPHA_EDEFAULT;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__BETA:
				return beta != BETA_EDEFAULT;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA:
				return gamma != GAMMA_EDEFAULT;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__INDEX:
				return index != null;
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX:
				return referenceIndex != null;
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
