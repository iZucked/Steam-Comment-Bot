/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCommodityIndices <em>Commodity Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCharterIndices <em>Charter Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getFleetCost <em>Fleet Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getRouteCosts <em>Route Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getPortCosts <em>Port Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCooldownPrices <em>Cooldown Prices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel()
 * @model
 * @generated
 */
public interface PricingModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Commodity Indices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.Index}&lt;java.lang.Double>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commodity Indices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commodity Indices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CommodityIndices()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EDoubleObject>" containment="true"
	 * @generated
	 */
	EList<Index<Double>> getCommodityIndices();

	/**
	 * Returns the value of the '<em><b>Charter Indices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.Index}&lt;java.lang.Integer>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Indices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Indices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CharterIndices()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" containment="true"
	 * @generated
	 */
	EList<Index<Integer>> getCharterIndices();

	/**
	 * Returns the value of the '<em><b>Fleet Cost</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet Cost</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet Cost</em>' containment reference.
	 * @see #setFleetCost(FleetCostModel)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_FleetCost()
	 * @model containment="true" required="true"
	 * @generated
	 */
	FleetCostModel getFleetCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingModel#getFleetCost <em>Fleet Cost</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fleet Cost</em>' containment reference.
	 * @see #getFleetCost()
	 * @generated
	 */
	void setFleetCost(FleetCostModel value);

	/**
	 * Returns the value of the '<em><b>Route Costs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.RouteCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Costs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_RouteCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<RouteCost> getRouteCosts();

	/**
	 * Returns the value of the '<em><b>Port Costs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PortCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Costs</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Costs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_PortCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortCost> getPortCosts();

	/**
	 * Returns the value of the '<em><b>Cooldown Prices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CooldownPrice}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooldown Prices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooldown Prices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CooldownPrices()
	 * @model containment="true"
	 * @generated
	 */
	EList<CooldownPrice> getCooldownPrices();

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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_DesPurchaseSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getDesPurchaseSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingModel#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_DesSalesSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getDesSalesSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingModel#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_FobPurchasesSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getFobPurchasesSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingModel#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_FobSalesSpotMarket()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotMarketGroup getFobSalesSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingModel#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fob Sales Spot Market</em>' containment reference.
	 * @see #getFobSalesSpotMarket()
	 * @generated
	 */
	void setFobSalesSpotMarket(SpotMarketGroup value);

} // end of  PricingModel

// finish type fixing
