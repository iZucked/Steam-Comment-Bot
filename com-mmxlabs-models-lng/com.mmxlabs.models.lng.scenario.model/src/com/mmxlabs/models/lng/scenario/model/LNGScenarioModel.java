/**
 */
package com.mmxlabs.models.lng.scenario.model;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.parameters.ParametersModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPricingModel <em>Pricing Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCommercialModel <em>Commercial Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSpotMarketsModel <em>Spot Markets Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getParametersModel <em>Parameters Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAnalyticsModel <em>Analytics Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortfolioModel <em>Portfolio Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel()
 * @model
 * @generated
 */
public interface LNGScenarioModel extends MMXRootObject {
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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_PortModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	PortModel getPortModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortModel <em>Port Model</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_FleetModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	FleetModel getFleetModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getFleetModel <em>Fleet Model</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_PricingModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	PricingModel getPricingModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPricingModel <em>Pricing Model</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_CommercialModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	CommercialModel getCommercialModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCommercialModel <em>Commercial Model</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_SpotMarketsModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	SpotMarketsModel getSpotMarketsModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSpotMarketsModel <em>Spot Markets Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Markets Model</em>' containment reference.
	 * @see #getSpotMarketsModel()
	 * @generated
	 */
	void setSpotMarketsModel(SpotMarketsModel value);

	/**
	 * Returns the value of the '<em><b>Parameters Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters Model</em>' containment reference.
	 * @see #setParametersModel(ParametersModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_ParametersModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ParametersModel getParametersModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getParametersModel <em>Parameters Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameters Model</em>' containment reference.
	 * @see #getParametersModel()
	 * @generated
	 */
	void setParametersModel(ParametersModel value);

	/**
	 * Returns the value of the '<em><b>Analytics Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Analytics Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Analytics Model</em>' containment reference.
	 * @see #setAnalyticsModel(AnalyticsModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_AnalyticsModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	AnalyticsModel getAnalyticsModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAnalyticsModel <em>Analytics Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Analytics Model</em>' containment reference.
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	void setAnalyticsModel(AnalyticsModel value);

	/**
	 * Returns the value of the '<em><b>Portfolio Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Portfolio Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Portfolio Model</em>' containment reference.
	 * @see #setPortfolioModel(LNGPortfolioModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_PortfolioModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	LNGPortfolioModel getPortfolioModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortfolioModel <em>Portfolio Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Portfolio Model</em>' containment reference.
	 * @see #getPortfolioModel()
	 * @generated
	 */
	void setPortfolioModel(LNGPortfolioModel value);

} // LNGScenarioModel
