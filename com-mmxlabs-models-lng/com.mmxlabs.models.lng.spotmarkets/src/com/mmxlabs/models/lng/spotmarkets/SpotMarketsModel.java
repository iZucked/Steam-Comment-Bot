/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getCharterOutStartDate <em>Charter Out Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getCharterInMarkets <em>Charter In Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getCharterOutMarkets <em>Charter Out Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDefaultNominalMarket <em>Default Nominal Market</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel()
 * @model
 * @generated
 */
public interface SpotMarketsModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Des Purchase Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Des Purchase Spot Market</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Purchase Spot Market</em>' containment reference.
	 * @see #setDesPurchaseSpotMarket(SpotMarketGroup)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_DesPurchaseSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getDesPurchaseSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Des Purchase Spot Market</em>' containment reference.
	 * @see #getDesPurchaseSpotMarket()
	 * @generated
	 */
	void setDesPurchaseSpotMarket(SpotMarketGroup value);

	/**
	 * Returns the value of the '<em><b>Des Sales Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Des Sales Spot Market</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Sales Spot Market</em>' containment reference.
	 * @see #setDesSalesSpotMarket(SpotMarketGroup)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_DesSalesSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getDesSalesSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Des Sales Spot Market</em>' containment reference.
	 * @see #getDesSalesSpotMarket()
	 * @generated
	 */
	void setDesSalesSpotMarket(SpotMarketGroup value);

	/**
	 * Returns the value of the '<em><b>Fob Purchases Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fob Purchases Spot Market</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fob Purchases Spot Market</em>' containment reference.
	 * @see #setFobPurchasesSpotMarket(SpotMarketGroup)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_FobPurchasesSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getFobPurchasesSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fob Purchases Spot Market</em>' containment reference.
	 * @see #getFobPurchasesSpotMarket()
	 * @generated
	 */
	void setFobPurchasesSpotMarket(SpotMarketGroup value);

	/**
	 * Returns the value of the '<em><b>Fob Sales Spot Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fob Sales Spot Market</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fob Sales Spot Market</em>' containment reference.
	 * @see #setFobSalesSpotMarket(SpotMarketGroup)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_FobSalesSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getFobSalesSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fob Sales Spot Market</em>' containment reference.
	 * @see #getFobSalesSpotMarket()
	 * @generated
	 */
	void setFobSalesSpotMarket(SpotMarketGroup value);

	/**
	 * Returns the value of the '<em><b>Charter Out Start Date</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Start Date</em>' containment reference.
	 * @see #setCharterOutStartDate(CharterOutStartDate)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_CharterOutStartDate()
	 * @model containment="true"
	 * @generated
	 */
	CharterOutStartDate getCharterOutStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getCharterOutStartDate <em>Charter Out Start Date</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Start Date</em>' containment reference.
	 * @see #getCharterOutStartDate()
	 * @generated
	 */
	void setCharterOutStartDate(CharterOutStartDate value);

	/**
	 * Returns the value of the '<em><b>Charter In Markets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Markets</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Markets</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_CharterInMarkets()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterInMarket> getCharterInMarkets();

	/**
	 * Returns the value of the '<em><b>Charter Out Markets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out Markets</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Markets</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_CharterOutMarkets()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterOutMarket> getCharterOutMarkets();

	/**
	 * Returns the value of the '<em><b>Default Nominal Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Nominal Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Nominal Market</em>' reference.
	 * @see #setDefaultNominalMarket(CharterInMarket)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_DefaultNominalMarket()
	 * @model
	 * @generated
	 */
	CharterInMarket getDefaultNominalMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDefaultNominalMarket <em>Default Nominal Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Nominal Market</em>' reference.
	 * @see #getDefaultNominalMarket()
	 * @generated
	 */
	void setDefaultNominalMarket(CharterInMarket value);

} // end of  SpotMarketsModel

// finish type fixing
