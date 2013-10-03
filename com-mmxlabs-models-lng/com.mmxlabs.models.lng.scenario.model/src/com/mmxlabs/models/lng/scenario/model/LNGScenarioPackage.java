/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory
 * @model kind="package"
 * @generated
 */
public interface LNGScenarioPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/scenario/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.scenario.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	LNGScenarioPackage eINSTANCE = com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl
	 * @see com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioPackageImpl#getLNGScenarioModel()
	 * @generated
	 */
	int LNG_SCENARIO_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__EXTENSIONS = MMXCorePackage.MMX_ROOT_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__UUID = MMXCorePackage.MMX_ROOT_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Port Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__PORT_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fleet Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__FLEET_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Pricing Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__PRICING_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Commercial Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__COMMERCIAL_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Spot Markets Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Parameters Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__PARAMETERS_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Analytics Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__ANALYTICS_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Portfolio Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__PORTFOLIO_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL_FEATURE_COUNT = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_ROOT_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_ROOT_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL___ECONTAINER_OP = MMXCorePackage.MMX_ROOT_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL_OPERATION_COUNT = MMXCorePackage.MMX_ROOT_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl <em>LNG Portfolio Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl
	 * @see com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioPackageImpl#getLNGPortfolioModel()
	 * @generated
	 */
	int LNG_PORTFOLIO_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Scenario Fleet Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cargo Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL__CARGO_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Assignment Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL__PARAMETERS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>LNG Portfolio Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>LNG Portfolio Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PORTFOLIO_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel
	 * @generated
	 */
	EClass getLNGScenarioModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortModel <em>Port Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Port Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_PortModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getFleetModel <em>Fleet Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fleet Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getFleetModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_FleetModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPricingModel <em>Pricing Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Pricing Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPricingModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_PricingModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCommercialModel <em>Commercial Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Commercial Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCommercialModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_CommercialModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSpotMarketsModel <em>Spot Markets Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spot Markets Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSpotMarketsModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_SpotMarketsModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getParametersModel <em>Parameters Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Parameters Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getParametersModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_ParametersModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAnalyticsModel <em>Analytics Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Analytics Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAnalyticsModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_AnalyticsModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortfolioModel <em>Portfolio Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Portfolio Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPortfolioModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_PortfolioModel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel <em>LNG Portfolio Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LNG Portfolio Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel
	 * @generated
	 */
	EClass getLNGPortfolioModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScenarioFleetModel <em>Scenario Fleet Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Scenario Fleet Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScenarioFleetModel()
	 * @see #getLNGPortfolioModel()
	 * @generated
	 */
	EReference getLNGPortfolioModel_ScenarioFleetModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getCargoModel <em>Cargo Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cargo Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getCargoModel()
	 * @see #getLNGPortfolioModel()
	 * @generated
	 */
	EReference getLNGPortfolioModel_CargoModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getAssignmentModel <em>Assignment Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Assignment Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getAssignmentModel()
	 * @see #getLNGPortfolioModel()
	 * @generated
	 */
	EReference getLNGPortfolioModel_AssignmentModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScheduleModel <em>Schedule Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScheduleModel()
	 * @see #getLNGPortfolioModel()
	 * @generated
	 */
	EReference getLNGPortfolioModel_ScheduleModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Parameters</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getParameters()
	 * @see #getLNGPortfolioModel()
	 * @generated
	 */
	EReference getLNGPortfolioModel_Parameters();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	LNGScenarioFactory getLNGScenarioFactory();

} //LNGScenarioPackage
