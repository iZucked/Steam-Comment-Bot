/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Contract Fee Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CharterContractFeeDetails#getFee <em>Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.CharterContractFeeDetails#getMatchingContractDetails <em>Matching Contract Details</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCharterContractFeeDetails()
 * @model
 * @generated
 */
public interface CharterContractFeeDetails extends UUIDObject, GeneralPNLDetails {
	/**
	 * Returns the value of the '<em><b>Fee</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fee</em>' attribute.
	 * @see #setFee(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCharterContractFeeDetails_Fee()
	 * @model default="0" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	int getFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CharterContractFeeDetails#getFee <em>Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fee</em>' attribute.
	 * @see #getFee()
	 * @generated
	 */
	void setFee(int value);

	/**
	 * Returns the value of the '<em><b>Matching Contract Details</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Matching Contract Details</em>' containment reference.
	 * @see #setMatchingContractDetails(MatchingContractDetails)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCharterContractFeeDetails_MatchingContractDetails()
	 * @model containment="true"
	 * @generated
	 */
	MatchingContractDetails getMatchingContractDetails();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.CharterContractFeeDetails#getMatchingContractDetails <em>Matching Contract Details</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Matching Contract Details</em>' containment reference.
	 * @see #getMatchingContractDetails()
	 * @generated
	 */
	void setMatchingContractDetails(MatchingContractDetails value);

} // CharterContractFeeDetails
