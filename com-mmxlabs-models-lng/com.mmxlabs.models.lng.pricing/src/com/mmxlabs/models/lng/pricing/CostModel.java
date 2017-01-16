/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cost Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CostModel#getRouteCosts <em>Route Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CostModel#getPortCosts <em>Port Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CostModel#getCooldownCosts <em>Cooldown Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CostModel#getBaseFuelCosts <em>Base Fuel Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CostModel#getPanamaCanalTariff <em>Panama Canal Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CostModel#getSuezCanalTariff <em>Suez Canal Tariff</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCostModel()
 * @model
 * @generated
 */
public interface CostModel extends UUIDObject {
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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCostModel_RouteCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<RouteCost> getRouteCosts();

	/**
	 * Returns the value of the '<em><b>Port Costs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PortCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Costs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCostModel_PortCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortCost> getPortCosts();

	/**
	 * Returns the value of the '<em><b>Cooldown Costs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CooldownPrice}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooldown Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooldown Costs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCostModel_CooldownCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<CooldownPrice> getCooldownCosts();

	/**
	 * Returns the value of the '<em><b>Base Fuel Costs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.BaseFuelCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Costs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCostModel_BaseFuelCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<BaseFuelCost> getBaseFuelCosts();

	/**
	 * Returns the value of the '<em><b>Panama Canal Tariff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Panama Canal Tariff</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Panama Canal Tariff</em>' containment reference.
	 * @see #setPanamaCanalTariff(PanamaCanalTariff)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCostModel_PanamaCanalTariff()
	 * @model containment="true"
	 * @generated
	 */
	PanamaCanalTariff getPanamaCanalTariff();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CostModel#getPanamaCanalTariff <em>Panama Canal Tariff</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Panama Canal Tariff</em>' containment reference.
	 * @see #getPanamaCanalTariff()
	 * @generated
	 */
	void setPanamaCanalTariff(PanamaCanalTariff value);

	/**
	 * Returns the value of the '<em><b>Suez Canal Tariff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Suez Canal Tariff</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Suez Canal Tariff</em>' containment reference.
	 * @see #setSuezCanalTariff(SuezCanalTariff)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCostModel_SuezCanalTariff()
	 * @model containment="true"
	 * @generated
	 */
	SuezCanalTariff getSuezCanalTariff();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CostModel#getSuezCanalTariff <em>Suez Canal Tariff</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Suez Canal Tariff</em>' containment reference.
	 * @see #getSuezCanalTariff()
	 * @generated
	 */
	void setSuezCanalTariff(SuezCanalTariff value);

} // CostModel
