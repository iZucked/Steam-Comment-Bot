

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics;
import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.mmxcore.MMXObject;
import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Shipping Cost Row</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getPlan <em>Plan</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCargoPrice <em>Cargo Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getDestinationType <em>Destination Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostRow()
 * @model
 * @generated
 */
public interface ShippingCostRow extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Plan</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Plan</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Plan</em>' container reference.
	 * @see #setPlan(ShippingCostPlan)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostRow_Plan()
	 * @see com.mmxlabs.models.lng.analytics.ShippingCostPlan#getRows
	 * @model opposite="rows" transient="false"
	 * @generated
	 */
	ShippingCostPlan getPlan();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getPlan <em>Plan</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Plan</em>' container reference.
	 * @see #getPlan()
	 * @generated
	 */
	void setPlan(ShippingCostPlan value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostRow_Port()
	 * @model
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostRow_Date()
	 * @model
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

	/**
	 * Returns the value of the '<em><b>Cargo Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Price</em>' attribute.
	 * @see #setCargoPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostRow_CargoPrice()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/mmbtu'"
	 * @generated
	 */
	double getCargoPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCargoPrice <em>Cargo Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Price</em>' attribute.
	 * @see #getCargoPrice()
	 * @generated
	 */
	void setCargoPrice(double value);

	/**
	 * Returns the value of the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv Value</em>' attribute.
	 * @see #isSetCvValue()
	 * @see #unsetCvValue()
	 * @see #setCvValue(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostRow_CvValue()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	double getCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCvValue <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv Value</em>' attribute.
	 * @see #isSetCvValue()
	 * @see #unsetCvValue()
	 * @see #getCvValue()
	 * @generated
	 */
	void setCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCvValue <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCvValue()
	 * @see #getCvValue()
	 * @see #setCvValue(double)
	 * @generated
	 */
	void unsetCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getCvValue <em>Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cv Value</em>' attribute is set.
	 * @see #unsetCvValue()
	 * @see #getCvValue()
	 * @see #setCvValue(double)
	 * @generated
	 */
	boolean isSetCvValue();

	/**
	 * Returns the value of the '<em><b>Destination Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.analytics.DestinationType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.analytics.DestinationType
	 * @see #setDestinationType(DestinationType)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostRow_DestinationType()
	 * @model
	 * @generated
	 */
	DestinationType getDestinationType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostRow#getDestinationType <em>Destination Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.analytics.DestinationType
	 * @see #getDestinationType()
	 * @generated
	 */
	void setDestinationType(DestinationType value);

} // end of  ShippingCostRow

// finish type fixing
