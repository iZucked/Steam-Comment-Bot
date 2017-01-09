/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LNG Reference Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPricingModel <em>Pricing Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCommercialModel <em>Commercial Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getSpotMarketsModel <em>Spot Markets Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCostModel <em>Cost Model</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGReferenceModel()
 * @model
 * @generated
 */
public interface LNGReferenceModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Port Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Model</em>' containment reference.
	 * @see #setPortModel(PortModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGReferenceModel_PortModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	PortModel getPortModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPortModel <em>Port Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Model</em>' containment reference.
	 * @see #getPortModel()
	 * @generated
	 */
	void setPortModel(PortModel value);

	/**
	 * Returns the value of the '<em><b>Fleet Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet Model</em>' containment reference.
	 * @see #setFleetModel(FleetModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGReferenceModel_FleetModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	FleetModel getFleetModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getFleetModel <em>Fleet Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fleet Model</em>' containment reference.
	 * @see #getFleetModel()
	 * @generated
	 */
	void setFleetModel(FleetModel value);

	/**
	 * Returns the value of the '<em><b>Pricing Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pricing Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Model</em>' containment reference.
	 * @see #setPricingModel(PricingModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGReferenceModel_PricingModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	PricingModel getPricingModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPricingModel <em>Pricing Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Model</em>' containment reference.
	 * @see #getPricingModel()
	 * @generated
	 */
	void setPricingModel(PricingModel value);

	/**
	 * Returns the value of the '<em><b>Commercial Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commercial Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commercial Model</em>' containment reference.
	 * @see #setCommercialModel(CommercialModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGReferenceModel_CommercialModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	CommercialModel getCommercialModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCommercialModel <em>Commercial Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Commercial Model</em>' containment reference.
	 * @see #getCommercialModel()
	 * @generated
	 */
	void setCommercialModel(CommercialModel value);

	/**
	 * Returns the value of the '<em><b>Spot Markets Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Markets Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Markets Model</em>' containment reference.
	 * @see #setSpotMarketsModel(SpotMarketsModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGReferenceModel_SpotMarketsModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	SpotMarketsModel getSpotMarketsModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getSpotMarketsModel <em>Spot Markets Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Markets Model</em>' containment reference.
	 * @see #getSpotMarketsModel()
	 * @generated
	 */
	void setSpotMarketsModel(SpotMarketsModel value);

	/**
	 * Returns the value of the '<em><b>Cost Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cost Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cost Model</em>' containment reference.
	 * @see #setCostModel(CostModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGReferenceModel_CostModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	CostModel getCostModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCostModel <em>Cost Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cost Model</em>' containment reference.
	 * @see #getCostModel()
	 * @generated
	 */
	void setCostModel(CostModel value);

} // LNGReferenceModel
