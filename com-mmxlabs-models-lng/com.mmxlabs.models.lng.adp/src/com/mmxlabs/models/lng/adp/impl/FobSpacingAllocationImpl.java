/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;

import com.mmxlabs.models.lng.commercial.SalesContract;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fob Spacing Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FobSpacingAllocationImpl#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FobSpacingAllocationImpl#getMinSpacing <em>Min Spacing</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FobSpacingAllocationImpl#getMaxSpacing <em>Max Spacing</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FobSpacingAllocationImpl#getCargoCount <em>Cargo Count</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FobSpacingAllocationImpl extends EObjectImpl implements FobSpacingAllocation {
	/**
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected SalesContract contract;

	/**
	 * The default value of the '{@link #getMinSpacing() <em>Min Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_SPACING_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinSpacing() <em>Min Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpacing()
	 * @generated
	 * @ordered
	 */
	protected int minSpacing = MIN_SPACING_EDEFAULT;

	/**
	 * This is true if the Min Spacing attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minSpacingESet;

	/**
	 * The default value of the '{@link #getMaxSpacing() <em>Max Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_SPACING_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxSpacing() <em>Max Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpacing()
	 * @generated
	 * @ordered
	 */
	protected int maxSpacing = MAX_SPACING_EDEFAULT;

	/**
	 * This is true if the Max Spacing attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxSpacingESet;

	/**
	 * The default value of the '{@link #getCargoCount() <em>Cargo Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCount()
	 * @generated
	 * @ordered
	 */
	protected static final int CARGO_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCargoCount() <em>Cargo Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCount()
	 * @generated
	 * @ordered
	 */
	protected int cargoCount = CARGO_COUNT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FobSpacingAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.FOB_SPACING_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SalesContract getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (SalesContract)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.FOB_SPACING_ALLOCATION__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SalesContract basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContract(SalesContract newContract) {
		SalesContract oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FOB_SPACING_ALLOCATION__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinSpacing() {
		return minSpacing;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinSpacing(int newMinSpacing) {
		int oldMinSpacing = minSpacing;
		minSpacing = newMinSpacing;
		boolean oldMinSpacingESet = minSpacingESet;
		minSpacingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FOB_SPACING_ALLOCATION__MIN_SPACING, oldMinSpacing, minSpacing, !oldMinSpacingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinSpacing() {
		int oldMinSpacing = minSpacing;
		boolean oldMinSpacingESet = minSpacingESet;
		minSpacing = MIN_SPACING_EDEFAULT;
		minSpacingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.FOB_SPACING_ALLOCATION__MIN_SPACING, oldMinSpacing, MIN_SPACING_EDEFAULT, oldMinSpacingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinSpacing() {
		return minSpacingESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxSpacing() {
		return maxSpacing;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxSpacing(int newMaxSpacing) {
		int oldMaxSpacing = maxSpacing;
		maxSpacing = newMaxSpacing;
		boolean oldMaxSpacingESet = maxSpacingESet;
		maxSpacingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FOB_SPACING_ALLOCATION__MAX_SPACING, oldMaxSpacing, maxSpacing, !oldMaxSpacingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxSpacing() {
		int oldMaxSpacing = maxSpacing;
		boolean oldMaxSpacingESet = maxSpacingESet;
		maxSpacing = MAX_SPACING_EDEFAULT;
		maxSpacingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.FOB_SPACING_ALLOCATION__MAX_SPACING, oldMaxSpacing, MAX_SPACING_EDEFAULT, oldMaxSpacingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxSpacing() {
		return maxSpacingESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCargoCount() {
		return cargoCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoCount(int newCargoCount) {
		int oldCargoCount = cargoCount;
		cargoCount = newCargoCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FOB_SPACING_ALLOCATION__CARGO_COUNT, oldCargoCount, cargoCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.FOB_SPACING_ALLOCATION__CONTRACT:
				if (resolve) return getContract();
				return basicGetContract();
			case ADPPackage.FOB_SPACING_ALLOCATION__MIN_SPACING:
				return getMinSpacing();
			case ADPPackage.FOB_SPACING_ALLOCATION__MAX_SPACING:
				return getMaxSpacing();
			case ADPPackage.FOB_SPACING_ALLOCATION__CARGO_COUNT:
				return getCargoCount();
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
			case ADPPackage.FOB_SPACING_ALLOCATION__CONTRACT:
				setContract((SalesContract)newValue);
				return;
			case ADPPackage.FOB_SPACING_ALLOCATION__MIN_SPACING:
				setMinSpacing((Integer)newValue);
				return;
			case ADPPackage.FOB_SPACING_ALLOCATION__MAX_SPACING:
				setMaxSpacing((Integer)newValue);
				return;
			case ADPPackage.FOB_SPACING_ALLOCATION__CARGO_COUNT:
				setCargoCount((Integer)newValue);
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
			case ADPPackage.FOB_SPACING_ALLOCATION__CONTRACT:
				setContract((SalesContract)null);
				return;
			case ADPPackage.FOB_SPACING_ALLOCATION__MIN_SPACING:
				unsetMinSpacing();
				return;
			case ADPPackage.FOB_SPACING_ALLOCATION__MAX_SPACING:
				unsetMaxSpacing();
				return;
			case ADPPackage.FOB_SPACING_ALLOCATION__CARGO_COUNT:
				setCargoCount(CARGO_COUNT_EDEFAULT);
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
			case ADPPackage.FOB_SPACING_ALLOCATION__CONTRACT:
				return contract != null;
			case ADPPackage.FOB_SPACING_ALLOCATION__MIN_SPACING:
				return isSetMinSpacing();
			case ADPPackage.FOB_SPACING_ALLOCATION__MAX_SPACING:
				return isSetMaxSpacing();
			case ADPPackage.FOB_SPACING_ALLOCATION__CARGO_COUNT:
				return cargoCount != CARGO_COUNT_EDEFAULT;
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
		result.append(" (minSpacing: ");
		if (minSpacingESet) result.append(minSpacing); else result.append("<unset>");
		result.append(", maxSpacing: ");
		if (maxSpacingESet) result.append(maxSpacing); else result.append("<unset>");
		result.append(", cargoCount: ");
		result.append(cargoCount);
		result.append(')');
		return result.toString();
	}

} //FobSpacingAllocationImpl
