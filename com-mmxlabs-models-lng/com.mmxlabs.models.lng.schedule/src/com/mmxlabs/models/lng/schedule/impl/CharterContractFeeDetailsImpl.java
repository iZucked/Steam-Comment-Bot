/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.MatchingContractDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Contract Fee Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CharterContractFeeDetailsImpl#getFee <em>Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CharterContractFeeDetailsImpl#getMatchingContractDetails <em>Matching Contract Details</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterContractFeeDetailsImpl extends UUIDObjectImpl implements CharterContractFeeDetails {
	/**
	 * The default value of the '{@link #getFee() <em>Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFee()
	 * @generated
	 * @ordered
	 */
	protected static final int FEE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFee() <em>Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFee()
	 * @generated
	 * @ordered
	 */
	protected int fee = FEE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMatchingContractDetails() <em>Matching Contract Details</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMatchingContractDetails()
	 * @generated
	 * @ordered
	 */
	protected MatchingContractDetails matchingContractDetails;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterContractFeeDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CHARTER_CONTRACT_FEE_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFee() {
		return fee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFee(int newFee) {
		int oldFee = fee;
		fee = newFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__FEE, oldFee, fee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MatchingContractDetails getMatchingContractDetails() {
		return matchingContractDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMatchingContractDetails(MatchingContractDetails newMatchingContractDetails, NotificationChain msgs) {
		MatchingContractDetails oldMatchingContractDetails = matchingContractDetails;
		matchingContractDetails = newMatchingContractDetails;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS, oldMatchingContractDetails, newMatchingContractDetails);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMatchingContractDetails(MatchingContractDetails newMatchingContractDetails) {
		if (newMatchingContractDetails != matchingContractDetails) {
			NotificationChain msgs = null;
			if (matchingContractDetails != null)
				msgs = ((InternalEObject)matchingContractDetails).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS, null, msgs);
			if (newMatchingContractDetails != null)
				msgs = ((InternalEObject)newMatchingContractDetails).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS, null, msgs);
			msgs = basicSetMatchingContractDetails(newMatchingContractDetails, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS, newMatchingContractDetails, newMatchingContractDetails));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS:
				return basicSetMatchingContractDetails(null, msgs);
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
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__FEE:
				return getFee();
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS:
				return getMatchingContractDetails();
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
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__FEE:
				setFee((Integer)newValue);
				return;
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS:
				setMatchingContractDetails((MatchingContractDetails)newValue);
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
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__FEE:
				setFee(FEE_EDEFAULT);
				return;
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS:
				setMatchingContractDetails((MatchingContractDetails)null);
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
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__FEE:
				return fee != FEE_EDEFAULT;
			case SchedulePackage.CHARTER_CONTRACT_FEE_DETAILS__MATCHING_CONTRACT_DETAILS:
				return matchingContractDetails != null;
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
		result.append(" (fee: ");
		result.append(fee);
		result.append(')');
		return result.toString();
	}

} //CharterContractFeeDetailsImpl
