/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model;

import org.eclipse.emf.ecore.EAttribute;
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
	 * The feature id for the '<em><b>Cargo Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__CARGO_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__SCHEDULE_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Actuals Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__ACTUALS_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Prompt Period Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__PROMPT_PERIOD_START = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Prompt Period End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__PROMPT_PERIOD_END = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Scheduling End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__SCHEDULING_END_DATE = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Reference Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__REFERENCE_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__USER_SETTINGS = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Analytics Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL__ANALYTICS_MODEL = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_SCENARIO_MODEL_FEATURE_COUNT = MMXCorePackage.MMX_ROOT_OBJECT_FEATURE_COUNT + 9;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl <em>LNG Reference Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl
	 * @see com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioPackageImpl#getLNGReferenceModel()
	 * @generated
	 */
	int LNG_REFERENCE_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Port Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__PORT_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fleet Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__FLEET_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Pricing Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__PRICING_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Commercial Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__COMMERCIAL_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Spot Markets Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Cost Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL__COST_MODEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>LNG Reference Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>LNG Reference Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_REFERENCE_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCargoModel <em>Cargo Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cargo Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCargoModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_CargoModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getScheduleModel <em>Schedule Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getScheduleModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_ScheduleModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getActualsModel <em>Actuals Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Actuals Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getActualsModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_ActualsModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart <em>Prompt Period Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prompt Period Start</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EAttribute getLNGScenarioModel_PromptPeriodStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd <em>Prompt Period End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prompt Period End</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EAttribute getLNGScenarioModel_PromptPeriodEnd();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSchedulingEndDate <em>Scheduling End Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scheduling End Date</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSchedulingEndDate()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EAttribute getLNGScenarioModel_SchedulingEndDate();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getReferenceModel <em>Reference Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reference Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getReferenceModel()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_ReferenceModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getUserSettings <em>User Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>User Settings</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getUserSettings()
	 * @see #getLNGScenarioModel()
	 * @generated
	 */
	EReference getLNGScenarioModel_UserSettings();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel <em>LNG Reference Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LNG Reference Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGReferenceModel
	 * @generated
	 */
	EClass getLNGReferenceModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPortModel <em>Port Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Port Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPortModel()
	 * @see #getLNGReferenceModel()
	 * @generated
	 */
	EReference getLNGReferenceModel_PortModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getFleetModel <em>Fleet Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fleet Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getFleetModel()
	 * @see #getLNGReferenceModel()
	 * @generated
	 */
	EReference getLNGReferenceModel_FleetModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPricingModel <em>Pricing Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Pricing Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getPricingModel()
	 * @see #getLNGReferenceModel()
	 * @generated
	 */
	EReference getLNGReferenceModel_PricingModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCommercialModel <em>Commercial Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Commercial Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCommercialModel()
	 * @see #getLNGReferenceModel()
	 * @generated
	 */
	EReference getLNGReferenceModel_CommercialModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getSpotMarketsModel <em>Spot Markets Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spot Markets Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getSpotMarketsModel()
	 * @see #getLNGReferenceModel()
	 * @generated
	 */
	EReference getLNGReferenceModel_SpotMarketsModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCostModel <em>Cost Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cost Model</em>'.
	 * @see com.mmxlabs.models.lng.scenario.model.LNGReferenceModel#getCostModel()
	 * @see #getLNGReferenceModel()
	 * @generated
	 */
	EReference getLNGReferenceModel_CostModel();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	LNGScenarioFactory getLNGScenarioFactory();

} //LNGScenarioPackage
