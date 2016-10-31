/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import java.time.LocalDate;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Optional Availability Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getBallastBonus <em>Ballast Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getRepositioningFee <em>Repositioning Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStart <em>Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEnd <em>End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStartPort <em>Start Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEndPort <em>End Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionalAvailabilityShippingOption()
 * @model
 * @generated
 */
public interface OptionalAvailabilityShippingOption extends FleetShippingOption {
	/**
	 * Returns the value of the '<em><b>Ballast Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Bonus</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus</em>' attribute.
	 * @see #setBallastBonus(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionalAvailabilityShippingOption_BallastBonus()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getBallastBonus();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getBallastBonus <em>Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus</em>' attribute.
	 * @see #getBallastBonus()
	 * @generated
	 */
	void setBallastBonus(String value);

	/**
	 * Returns the value of the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositioning Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #setRepositioningFee(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionalAvailabilityShippingOption_RepositioningFee()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getRepositioningFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #getRepositioningFee()
	 * @generated
	 */
	void setRepositioningFee(String value);

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionalAvailabilityShippingOption_Start()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(LocalDate value);

	/**
	 * Returns the value of the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End</em>' attribute.
	 * @see #setEnd(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionalAvailabilityShippingOption_End()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEnd <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End</em>' attribute.
	 * @see #getEnd()
	 * @generated
	 */
	void setEnd(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Start Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Port</em>' reference.
	 * @see #setStartPort(Port)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionalAvailabilityShippingOption_StartPort()
	 * @model
	 * @generated
	 */
	Port getStartPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getStartPort <em>Start Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Port</em>' reference.
	 * @see #getStartPort()
	 * @generated
	 */
	void setStartPort(Port value);

	/**
	 * Returns the value of the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Port</em>' reference.
	 * @see #setEndPort(Port)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionalAvailabilityShippingOption_EndPort()
	 * @model
	 * @generated
	 */
	Port getEndPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Port</em>' reference.
	 * @see #getEndPort()
	 * @generated
	 */
	void setEndPort(Port value);

} // OptionalAvailabilityShippingOption
