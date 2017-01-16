/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Provisional Cargo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getBuy <em>Buy</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getSell <em>Sell</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getPortfolioModel <em>Portfolio Model</em>}</li>
 * </ul>
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

	/**
	 * Returns the value of the '<em><b>Portfolio Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Portfolio Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Portfolio Model</em>' containment reference.
	 * @see #setPortfolioModel(EObject)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getProvisionalCargo_PortfolioModel()
	 * @model containment="true" transient="true"
	 * @generated
	 */
	EObject getPortfolioModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ProvisionalCargo#getPortfolioModel <em>Portfolio Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Portfolio Model</em>' containment reference.
	 * @see #getPortfolioModel()
	 * @generated
	 */
	void setPortfolioModel(EObject value);

} // end of  ProvisionalCargo

// finish type fixing
