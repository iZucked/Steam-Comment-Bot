/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import java.time.YearMonth;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index Point</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.IndexPoint#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.IndexPoint#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getIndexPoint()
 * @model
 * @generated
 */
public interface IndexPoint<Value> extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(YearMonth)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getIndexPoint_Date()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.IndexPoint#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(YearMonth value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getIndexPoint_Value()
	 * @model required="true"
	 * @generated
	 */
	Value getValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.IndexPoint#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Value value);

} // end of  IndexPoint

// finish type fixing
