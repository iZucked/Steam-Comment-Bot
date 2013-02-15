

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.spotmarkets;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel#getCharteringSpotMarkets <em>Chartering Spot Markets</em>}</li>
 * </ul>
 * </p>
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
	 * Returns the value of the '<em><b>Chartering Spot Markets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Chartering Spot Markets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Chartering Spot Markets</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketsModel_CharteringSpotMarkets()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterCostModel> getCharteringSpotMarkets();

} // end of  SpotMarketsModel

// finish type fixing
