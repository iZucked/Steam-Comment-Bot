/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Matching Contract Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.MatchingContractDetails#getMatchedPort <em>Matched Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getMatchingContractDetails()
 * @model abstract="true"
 * @generated
 */
public interface MatchingContractDetails extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Matched Port</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Matched Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Matched Port</em>' attribute.
	 * @see #setMatchedPort(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getMatchingContractDetails_MatchedPort()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='basefuel'"
	 * @generated
	 */
	String getMatchedPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.MatchingContractDetails#getMatchedPort <em>Matched Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Matched Port</em>' attribute.
	 * @see #getMatchedPort()
	 * @generated
	 */
	void setMatchedPort(String value);

} // MatchingContractDetails
