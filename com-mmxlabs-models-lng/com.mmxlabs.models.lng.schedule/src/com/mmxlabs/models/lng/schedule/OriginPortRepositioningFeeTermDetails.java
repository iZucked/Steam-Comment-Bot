/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Origin Port Repositioning Fee Term Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails#getOriginPort <em>Origin Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getOriginPortRepositioningFeeTermDetails()
 * @model
 * @generated
 */
public interface OriginPortRepositioningFeeTermDetails extends NotionalJourneyDetails {
	/**
	 * Returns the value of the '<em><b>Origin Port</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Port</em>' attribute.
	 * @see #setOriginPort(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getOriginPortRepositioningFeeTermDetails_OriginPort()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='basefuel'"
	 * @generated
	 */
	String getOriginPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails#getOriginPort <em>Origin Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Port</em>' attribute.
	 * @see #getOriginPort()
	 * @generated
	 */
	void setOriginPort(String value);

} // OriginPortRepositioningFeeTermDetails
