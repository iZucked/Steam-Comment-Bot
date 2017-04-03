/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ballast Bonus Fee Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails#getFee <em>Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails#getMatchingBallastBonusContractDetails <em>Matching Ballast Bonus Contract Details</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBallastBonusFeeDetails()
 * @model
 * @generated
 */
public interface BallastBonusFeeDetails extends UUIDObject, GeneralPNLDetails {
	/**
	 * Returns the value of the '<em><b>Fee</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fee</em>' attribute.
	 * @see #setFee(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBallastBonusFeeDetails_Fee()
	 * @model default="0" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	int getFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails#getFee <em>Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fee</em>' attribute.
	 * @see #getFee()
	 * @generated
	 */
	void setFee(int value);

	/**
	 * Returns the value of the '<em><b>Matching Ballast Bonus Contract Details</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Matching Ballast Bonus Contract Details</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Matching Ballast Bonus Contract Details</em>' reference.
	 * @see #setMatchingBallastBonusContractDetails(MatchingContractDetails)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBallastBonusFeeDetails_MatchingBallastBonusContractDetails()
	 * @model
	 * @generated
	 */
	MatchingContractDetails getMatchingBallastBonusContractDetails();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails#getMatchingBallastBonusContractDetails <em>Matching Ballast Bonus Contract Details</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Matching Ballast Bonus Contract Details</em>' reference.
	 * @see #getMatchingBallastBonusContractDetails()
	 * @generated
	 */
	void setMatchingBallastBonusContractDetails(MatchingContractDetails value);

} // BallastBonusFeeDetails
