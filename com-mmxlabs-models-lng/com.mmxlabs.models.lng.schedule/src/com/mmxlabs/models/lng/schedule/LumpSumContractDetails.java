/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lump Sum Contract Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.LumpSumContractDetails#getLumpSum <em>Lump Sum</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getLumpSumContractDetails()
 * @model
 * @generated
 */
public interface LumpSumContractDetails extends MatchingContractDetails {
	/**
	 * Returns the value of the '<em><b>Lump Sum</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lump Sum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lump Sum</em>' attribute.
	 * @see #setLumpSum(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getLumpSumContractDetails_LumpSum()
	 * @model default="0" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	int getLumpSum();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.LumpSumContractDetails#getLumpSum <em>Lump Sum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lump Sum</em>' attribute.
	 * @see #getLumpSum()
	 * @generated
	 */
	void setLumpSum(int value);

} // LumpSumContractDetails
