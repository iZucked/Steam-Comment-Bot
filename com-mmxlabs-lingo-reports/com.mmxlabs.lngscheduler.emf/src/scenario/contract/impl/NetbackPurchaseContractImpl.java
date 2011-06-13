/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.contract.ContractPackage;
import scenario.contract.NetbackPurchaseContract;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Netback Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.NetbackPurchaseContractImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link scenario.contract.impl.NetbackPurchaseContractImpl#getBuyersMargin <em>Buyers Margin</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NetbackPurchaseContractImpl extends PurchaseContractImpl implements NetbackPurchaseContract {
	/**
	 * The default value of the '{@link #getLowerBound() <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowerBound()
	 * @generated
	 * @ordered
	 */
	protected static final int LOWER_BOUND_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLowerBound() <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowerBound()
	 * @generated
	 * @ordered
	 */
	protected int lowerBound = LOWER_BOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #getBuyersMargin() <em>Buyers Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuyersMargin()
	 * @generated
	 * @ordered
	 */
	protected static final float BUYERS_MARGIN_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getBuyersMargin() <em>Buyers Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuyersMargin()
	 * @generated
	 * @ordered
	 */
	protected float buyersMargin = BUYERS_MARGIN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NetbackPurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.NETBACK_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLowerBound() {
		return lowerBound;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLowerBound(int newLowerBound) {
		int oldLowerBound = lowerBound;
		lowerBound = newLowerBound;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.NETBACK_PURCHASE_CONTRACT__LOWER_BOUND, oldLowerBound, lowerBound));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getBuyersMargin() {
		return buyersMargin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBuyersMargin(float newBuyersMargin) {
		float oldBuyersMargin = buyersMargin;
		buyersMargin = newBuyersMargin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.NETBACK_PURCHASE_CONTRACT__BUYERS_MARGIN, oldBuyersMargin, buyersMargin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__LOWER_BOUND:
				return getLowerBound();
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__BUYERS_MARGIN:
				return getBuyersMargin();
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
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__LOWER_BOUND:
				setLowerBound((Integer)newValue);
				return;
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__BUYERS_MARGIN:
				setBuyersMargin((Float)newValue);
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
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__LOWER_BOUND:
				setLowerBound(LOWER_BOUND_EDEFAULT);
				return;
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__BUYERS_MARGIN:
				setBuyersMargin(BUYERS_MARGIN_EDEFAULT);
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
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__LOWER_BOUND:
				return lowerBound != LOWER_BOUND_EDEFAULT;
			case ContractPackage.NETBACK_PURCHASE_CONTRACT__BUYERS_MARGIN:
				return buyersMargin != BUYERS_MARGIN_EDEFAULT;
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
		result.append(" (lowerBound: ");
		result.append(lowerBound);
		result.append(", buyersMargin: ");
		result.append(buyersMargin);
		result.append(')');
		return result.toString();
	}

} //NetbackPurchaseContractImpl
