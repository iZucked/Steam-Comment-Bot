/**
 */
package com.mmxlabs.models.lng.analytics;

import java.time.ZonedDateTime;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marketability Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityEvent#getStart <em>Start</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityEvent()
 * @model
 * @generated
 */
public interface MarketabilityEvent extends EObject {

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(ZonedDateTime)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityEvent_Start()
	 * @model dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityEvent#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(ZonedDateTime value);
} // MarketabilityEvent
