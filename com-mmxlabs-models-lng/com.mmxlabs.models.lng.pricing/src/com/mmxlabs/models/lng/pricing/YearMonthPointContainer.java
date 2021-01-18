/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import java.time.YearMonth;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Year Month Point Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.YearMonthPointContainer#getPoints <em>Points</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getYearMonthPointContainer()
 * @model
 * @generated
 */
public interface YearMonthPointContainer extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Points</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.YearMonthPoint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Points</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Points</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getYearMonthPointContainer_Points()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<YearMonthPoint> getPoints();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model monthDataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	Double valueForMonth(YearMonth month);

} // YearMonthPointContainer
