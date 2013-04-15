

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Provisional Cargo</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getBuy <em>Buy</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getSell <em>Sell</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getCostLine <em>Cost Line</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getVessel <em>Vessel</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getProvisionalCargo()
 * @model
 * @generated
 */
public interface ProvisionalCargo extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Buy</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Buy</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy</em>' containment reference.
	 * @see #setBuy(BuyOpportunity)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getProvisionalCargo_Buy()
	 * @model containment="true"
	 * @generated
	 */
	BuyOpportunity getBuy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getBuy <em>Buy</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Buy</em>' containment reference.
	 * @see #getBuy()
	 * @generated
	 */
	void setBuy(BuyOpportunity value);

	/**
	 * Returns the value of the '<em><b>Sell</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sell</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell</em>' containment reference.
	 * @see #setSell(SellOpportunity)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getProvisionalCargo_Sell()
	 * @model containment="true"
	 * @generated
	 */
	SellOpportunity getSell();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getSell <em>Sell</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sell</em>' containment reference.
	 * @see #getSell()
	 * @generated
	 */
	void setSell(SellOpportunity value);

	/**
	 * Returns the value of the '<em><b>Cost Line</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cost Line</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cost Line</em>' containment reference.
	 * @see #setCostLine(UnitCostLine)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getProvisionalCargo_CostLine()
	 * @model containment="true"
	 * @generated
	 */
	UnitCostLine getCostLine();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getCostLine <em>Cost Line</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cost Line</em>' containment reference.
	 * @see #getCostLine()
	 * @generated
	 */
	void setCostLine(UnitCostLine value);

	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(Vessel)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getProvisionalCargo_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

} // end of  ProvisionalCargo

// finish type fixing
