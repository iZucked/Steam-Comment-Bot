/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixShippedCargoResult;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Shipped Cargo Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixShippedCargoResultImpl#getShippingCost <em>Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixShippedCargoResultImpl#getLoadVolume <em>Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixShippedCargoResultImpl#getDischargeVolume <em>Discharge Volume</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SwapValueMatrixShippedCargoResultImpl extends SwapValueMatrixCargoResultImpl implements SwapValueMatrixShippedCargoResult {
	/**
	 * The default value of the '{@link #getShippingCost() <em>Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingCost()
	 * @generated
	 * @ordered
	 */
	protected static final long SHIPPING_COST_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getShippingCost() <em>Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingCost()
	 * @generated
	 * @ordered
	 */
	protected long shippingCost = SHIPPING_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getLoadVolume() <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int LOAD_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLoadVolume() <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected int loadVolume = LOAD_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int DISCHARGE_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected int dischargeVolume = DISCHARGE_VOLUME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixShippedCargoResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getShippingCost() {
		return shippingCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShippingCost(long newShippingCost) {
		long oldShippingCost = shippingCost;
		shippingCost = newShippingCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SHIPPING_COST, oldShippingCost, shippingCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getLoadVolume() {
		return loadVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadVolume(int newLoadVolume) {
		int oldLoadVolume = loadVolume;
		loadVolume = newLoadVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_VOLUME, oldLoadVolume, loadVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDischargeVolume() {
		return dischargeVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeVolume(int newDischargeVolume) {
		int oldDischargeVolume = dischargeVolume;
		dischargeVolume = newDischargeVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_VOLUME, oldDischargeVolume, dischargeVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SHIPPING_COST:
				return getShippingCost();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_VOLUME:
				return getLoadVolume();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_VOLUME:
				return getDischargeVolume();
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SHIPPING_COST:
				setShippingCost((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_VOLUME:
				setLoadVolume((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_VOLUME:
				setDischargeVolume((Integer)newValue);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SHIPPING_COST:
				setShippingCost(SHIPPING_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_VOLUME:
				setLoadVolume(LOAD_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_VOLUME:
				setDischargeVolume(DISCHARGE_VOLUME_EDEFAULT);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__SHIPPING_COST:
				return shippingCost != SHIPPING_COST_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__LOAD_VOLUME:
				return loadVolume != LOAD_VOLUME_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_SHIPPED_CARGO_RESULT__DISCHARGE_VOLUME:
				return dischargeVolume != DISCHARGE_VOLUME_EDEFAULT;
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
		result.append(" (shippingCost: ");
		result.append(shippingCost);
		result.append(", loadVolume: ");
		result.append(loadVolume);
		result.append(", dischargeVolume: ");
		result.append(dischargeVolume);
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixShippedCargoResultImpl
