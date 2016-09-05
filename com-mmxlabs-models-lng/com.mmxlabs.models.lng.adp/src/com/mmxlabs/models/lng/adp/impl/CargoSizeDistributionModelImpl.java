/**
 */
package com.mmxlabs.models.lng.adp.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CargoSizeDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Size Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl#getCargoSize <em>Cargo Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl#isExact <em>Exact</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoSizeDistributionModelImpl extends EObjectImpl implements CargoSizeDistributionModel {
	/**
	 * The default value of the '{@link #getCargoSize() <em>Cargo Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoSize()
	 * @generated
	 * @ordered
	 */
	protected static final int CARGO_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCargoSize() <em>Cargo Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoSize()
	 * @generated
	 * @ordered
	 */
	protected int cargoSize = CARGO_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #isExact() <em>Exact</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExact()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExact() <em>Exact</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExact()
	 * @generated
	 * @ordered
	 */
	protected boolean exact = EXACT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoSizeDistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.CARGO_SIZE_DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCargoSize() {
		return cargoSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoSize(int newCargoSize) {
		int oldCargoSize = cargoSize;
		cargoSize = newCargoSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE, oldCargoSize, cargoSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isExact() {
		return exact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExact(boolean newExact) {
		boolean oldExact = exact;
		exact = newExact;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__EXACT, oldExact, exact));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE:
				return getCargoSize();
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__EXACT:
				return isExact();
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
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE:
				setCargoSize((Integer)newValue);
				return;
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__EXACT:
				setExact((Boolean)newValue);
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
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE:
				setCargoSize(CARGO_SIZE_EDEFAULT);
				return;
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__EXACT:
				setExact(EXACT_EDEFAULT);
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
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE:
				return cargoSize != CARGO_SIZE_EDEFAULT;
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL__EXACT:
				return exact != EXACT_EDEFAULT;
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
		result.append(" (cargoSize: ");
		result.append(cargoSize);
		result.append(", exact: ");
		result.append(exact);
		result.append(')');
		return result.toString();
	}

} //CargoSizeDistributionModelImpl
