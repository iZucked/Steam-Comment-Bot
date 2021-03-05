/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notional Journey Ballast Bonus Term Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails#getReturnPort <em>Return Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyBallastBonusTermDetails()
 * @model
 * @generated
 */
public interface NotionalJourneyBallastBonusTermDetails extends NotionalJourneyDetails {
	/**
	 * Returns the value of the '<em><b>Return Port</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Port</em>' attribute.
	 * @see #setReturnPort(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyBallastBonusTermDetails_ReturnPort()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='basefuel'"
	 * @generated
	 */
	String getReturnPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails#getReturnPort <em>Return Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Port</em>' attribute.
	 * @see #getReturnPort()
	 * @generated
	 */
	void setReturnPort(String value);

} // NotionalJourneyBallastBonusTermDetails
