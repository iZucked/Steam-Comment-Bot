/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.AssignableElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marketability Assignable Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement#getElement <em>Element</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityAssignableElement()
 * @model
 * @generated
 */
public interface MarketabilityAssignableElement extends MarketabilityEvent {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(AssignableElement)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityAssignableElement_Element()
	 * @model
	 * @generated
	 */
	AssignableElement getElement();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(AssignableElement value);

} // MarketabilityAssignableElement
