/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;

import com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails;
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
 * An implementation of the model object '<em><b>Ballast Bonus Fee Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BallastBonusFeeDetailsImpl#getFee <em>Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.BallastBonusFeeDetailsImpl#getMatchingBallastBonusContractDetails <em>Matching Ballast Bonus Contract Details</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BallastBonusFeeDetailsImpl extends UUIDObjectImpl implements BallastBonusFeeDetails {
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
	 * The cached value of the '{@link #getMatchingBallastBonusContractDetails() <em>Matching Ballast Bonus Contract Details</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMatchingBallastBonusContractDetails()
	 * @generated
	 * @ordered
	 */
	protected MatchingContractDetails matchingBallastBonusContractDetails;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BallastBonusFeeDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.BALLAST_BONUS_FEE_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFee() {
		return fee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFee(int newFee) {
		int oldFee = fee;
		fee = newFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BALLAST_BONUS_FEE_DETAILS__FEE, oldFee, fee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MatchingContractDetails getMatchingBallastBonusContractDetails() {
		return matchingBallastBonusContractDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMatchingBallastBonusContractDetails(MatchingContractDetails newMatchingBallastBonusContractDetails, NotificationChain msgs) {
		MatchingContractDetails oldMatchingBallastBonusContractDetails = matchingBallastBonusContractDetails;
		matchingBallastBonusContractDetails = newMatchingBallastBonusContractDetails;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS, oldMatchingBallastBonusContractDetails, newMatchingBallastBonusContractDetails);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMatchingBallastBonusContractDetails(MatchingContractDetails newMatchingBallastBonusContractDetails) {
		if (newMatchingBallastBonusContractDetails != matchingBallastBonusContractDetails) {
			NotificationChain msgs = null;
			if (matchingBallastBonusContractDetails != null)
				msgs = ((InternalEObject)matchingBallastBonusContractDetails).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS, null, msgs);
			if (newMatchingBallastBonusContractDetails != null)
				msgs = ((InternalEObject)newMatchingBallastBonusContractDetails).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS, null, msgs);
			msgs = basicSetMatchingBallastBonusContractDetails(newMatchingBallastBonusContractDetails, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS, newMatchingBallastBonusContractDetails, newMatchingBallastBonusContractDetails));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS:
				return basicSetMatchingBallastBonusContractDetails(null, msgs);
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
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__FEE:
				return getFee();
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS:
				return getMatchingBallastBonusContractDetails();
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
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__FEE:
				setFee((Integer)newValue);
				return;
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS:
				setMatchingBallastBonusContractDetails((MatchingContractDetails)newValue);
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
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__FEE:
				setFee(FEE_EDEFAULT);
				return;
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS:
				setMatchingBallastBonusContractDetails((MatchingContractDetails)null);
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
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__FEE:
				return fee != FEE_EDEFAULT;
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS__MATCHING_BALLAST_BONUS_CONTRACT_DETAILS:
				return matchingBallastBonusContractDetails != null;
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
		result.append(" (fee: ");
		result.append(fee);
		result.append(')');
		return result.toString();
	}

} //BallastBonusFeeDetailsImpl
