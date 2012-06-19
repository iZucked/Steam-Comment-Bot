

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getBaseMarket <em>Base Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getConstant <em>Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getMultiplier <em>Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl#getShare <em>Share</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfitSharePurchaseContractImpl extends PurchaseContractImpl implements ProfitSharePurchaseContract {
	/**
	 * The cached value of the '{@link #getBaseMarket() <em>Base Market</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseMarket()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> baseMarket;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected AIndex index;

	/**
	 * The default value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected static final double CONSTANT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected double constant = CONSTANT_EDEFAULT;

	/**
	 * The default value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double MULTIPLIER_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double multiplier = MULTIPLIER_EDEFAULT;

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
	public EList<APortSet> getBaseMarket() {
		if (baseMarket == null) {
			baseMarket = new EObjectResolvingEList<APortSet>(APortSet.class, this, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET);
		}
		return baseMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex getIndex() {
		if (index != null && index.eIsProxy()) {
			InternalEObject oldIndex = (InternalEObject)index;
			index = (AIndex)eResolveProxy(oldIndex);
			if (index != oldIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__INDEX, oldIndex, index));
			}
		}
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex basicGetIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(AIndex newIndex) {
		AIndex oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getConstant() {
		return constant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstant(double newConstant) {
		double oldConstant = constant;
		constant = newConstant;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__CONSTANT, oldConstant, constant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplier(double newMultiplier) {
		double oldMultiplier = multiplier;
		multiplier = newMultiplier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MULTIPLIER, oldMultiplier, multiplier));
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET:
				return getBaseMarket();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__INDEX:
				if (resolve) return getIndex();
				return basicGetIndex();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__CONSTANT:
				return getConstant();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MULTIPLIER:
				return getMultiplier();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				return getShare();
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
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET:
				getBaseMarket().clear();
				getBaseMarket().addAll((Collection<? extends APortSet>)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__INDEX:
				setIndex((AIndex)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__CONSTANT:
				setConstant((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MULTIPLIER:
				setMultiplier((Double)newValue);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				setShare((Double)newValue);
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
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET:
				getBaseMarket().clear();
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__INDEX:
				setIndex((AIndex)null);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__CONSTANT:
				setConstant(CONSTANT_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MULTIPLIER:
				setMultiplier(MULTIPLIER_EDEFAULT);
				return;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				setShare(SHARE_EDEFAULT);
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
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET:
				return baseMarket != null && !baseMarket.isEmpty();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__INDEX:
				return index != null;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__CONSTANT:
				return constant != CONSTANT_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__MULTIPLIER:
				return multiplier != MULTIPLIER_EDEFAULT;
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE:
				return share != SHARE_EDEFAULT;
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
		result.append(" (constant: ");
		result.append(constant);
		result.append(", multiplier: ");
		result.append(multiplier);
		result.append(", share: ");
		result.append(share);
		result.append(')');
		return result.toString();
	}

} // end of ProfitSharePurchaseContractImpl

// finish type fixing
