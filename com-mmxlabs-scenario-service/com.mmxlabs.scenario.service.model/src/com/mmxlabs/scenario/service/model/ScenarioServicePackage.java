/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.scenario.service.model.ScenarioServiceFactory
 * @model kind="package"
 * @generated
 */
public interface ScenarioServicePackage extends EPackage {
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
	String eNS_URI = "http://com.mmxlabs.scenario.service/model/1";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.scenario.service";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScenarioServicePackage eINSTANCE = com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl <em>Scenario Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioInstance()
	 * @generated
	 */
	int SCENARIO_INSTANCE = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl <em>Scenario Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioModel()
	 * @generated
	 */
	int SCENARIO_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Scenario Services</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_MODEL__SCENARIO_SERVICES = 0;

	/**
	 * The number of structural features of the '<em>Scenario Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ParamSetImpl <em>Param Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ParamSetImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getParamSet()
	 * @generated
	 */
	int PARAM_SET = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.SolutionImpl <em>Solution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.SolutionImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getSolution()
	 * @generated
	 */
	int SOLUTION = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl <em>Scenario Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioService()
	 * @generated
	 */
	int SCENARIO_SERVICE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Scenarios</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__SCENARIOS = 2;

	/**
	 * The number of structural features of the '<em>Scenario Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__UUID = 1;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__URI = 2;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOCKED = 3;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__METADATA = 4;

	/**
	 * The feature id for the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__ARCHIVED = 5;

	/**
	 * The feature id for the '<em><b>Initial Solution</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__INITIAL_SOLUTION = 6;

	/**
	 * The feature id for the '<em><b>Variations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__VARIATIONS = 7;

	/**
	 * The feature id for the '<em><b>Parameter Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__PARAMETER_SETS = 8;

	/**
	 * The number of structural features of the '<em>Scenario Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE_FEATURE_COUNT = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_SET__NAME = 0;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_SET__UUID = 1;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_SET__URI = 2;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_SET__METADATA = 3;

	/**
	 * The feature id for the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_SET__ARCHIVED = 4;

	/**
	 * The feature id for the '<em><b>Solutions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_SET__SOLUTIONS = 5;

	/**
	 * The number of structural features of the '<em>Param Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_SET_FEATURE_COUNT = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__UUID = 1;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__URI = 2;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__METADATA = 3;

	/**
	 * The feature id for the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__ARCHIVED = 4;

	/**
	 * The feature id for the '<em><b>Param Set</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__PARAM_SET = 5;

	/**
	 * The number of structural features of the '<em>Solution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl <em>Metadata</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.MetadataImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getMetadata()
	 * @generated
	 */
	int METADATA = 5;

	/**
	 * The feature id for the '<em><b>Creator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__CREATOR = 0;

	/**
	 * The feature id for the '<em><b>Created</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__CREATED = 1;

	/**
	 * The feature id for the '<em><b>Last Modified</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__LAST_MODIFIED = 2;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__COMMENT = 3;

	/**
	 * The feature id for the '<em><b>Last Modified By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__LAST_MODIFIED_BY = 4;

	/**
	 * The number of structural features of the '<em>Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_FEATURE_COUNT = 5;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Instance</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance
	 * @generated
	 */
	EClass getScenarioInstance();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getVariations <em>Variations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Variations</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getVariations()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_Variations();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getParameterSets <em>Parameter Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameter Sets</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getParameterSets()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_ParameterSets();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getUri()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Uri();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getName()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Name();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInitialSolution <em>Initial Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Initial Solution</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getInitialSolution()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_InitialSolution();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Uuid();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked <em>Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locked</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Locked();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isArchived <em>Archived</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Archived</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#isArchived()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Archived();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_Metadata();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioModel <em>Scenario Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Model</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioModel
	 * @generated
	 */
	EClass getScenarioModel();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.scenario.service.model.ScenarioModel#getScenarioServices <em>Scenario Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Scenario Services</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioModel#getScenarioServices()
	 * @see #getScenarioModel()
	 * @generated
	 */
	EReference getScenarioModel_ScenarioServices();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ParamSet <em>Param Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Param Set</em>'.
	 * @see com.mmxlabs.scenario.service.model.ParamSet
	 * @generated
	 */
	EClass getParamSet();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.ParamSet#getSolutions <em>Solutions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Solutions</em>'.
	 * @see com.mmxlabs.scenario.service.model.ParamSet#getSolutions()
	 * @see #getParamSet()
	 * @generated
	 */
	EReference getParamSet_Solutions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ParamSet#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see com.mmxlabs.scenario.service.model.ParamSet#getUri()
	 * @see #getParamSet()
	 * @generated
	 */
	EAttribute getParamSet_Uri();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ParamSet#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.ParamSet#getName()
	 * @see #getParamSet()
	 * @generated
	 */
	EAttribute getParamSet_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ParamSet#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see com.mmxlabs.scenario.service.model.ParamSet#getUuid()
	 * @see #getParamSet()
	 * @generated
	 */
	EAttribute getParamSet_Uuid();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ParamSet#isArchived <em>Archived</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Archived</em>'.
	 * @see com.mmxlabs.scenario.service.model.ParamSet#isArchived()
	 * @see #getParamSet()
	 * @generated
	 */
	EAttribute getParamSet_Archived();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.scenario.service.model.ParamSet#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata</em>'.
	 * @see com.mmxlabs.scenario.service.model.ParamSet#getMetadata()
	 * @see #getParamSet()
	 * @generated
	 */
	EReference getParamSet_Metadata();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.Solution <em>Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Solution</em>'.
	 * @see com.mmxlabs.scenario.service.model.Solution
	 * @generated
	 */
	EClass getSolution();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Solution#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see com.mmxlabs.scenario.service.model.Solution#getUri()
	 * @see #getSolution()
	 * @generated
	 */
	EAttribute getSolution_Uri();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Solution#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.Solution#getName()
	 * @see #getSolution()
	 * @generated
	 */
	EAttribute getSolution_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Solution#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see com.mmxlabs.scenario.service.model.Solution#getUuid()
	 * @see #getSolution()
	 * @generated
	 */
	EAttribute getSolution_Uuid();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Solution#isArchived <em>Archived</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Archived</em>'.
	 * @see com.mmxlabs.scenario.service.model.Solution#isArchived()
	 * @see #getSolution()
	 * @generated
	 */
	EAttribute getSolution_Archived();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.scenario.service.model.Solution#getParamSet <em>Param Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Param Set</em>'.
	 * @see com.mmxlabs.scenario.service.model.Solution#getParamSet()
	 * @see #getSolution()
	 * @generated
	 */
	EReference getSolution_ParamSet();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.scenario.service.model.Solution#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata</em>'.
	 * @see com.mmxlabs.scenario.service.model.Solution#getMetadata()
	 * @see #getSolution()
	 * @generated
	 */
	EReference getSolution_Metadata();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioService <em>Scenario Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Service</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService
	 * @generated
	 */
	EClass getScenarioService();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getName()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.ScenarioService#getScenarios <em>Scenarios</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Scenarios</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getScenarios()
	 * @see #getScenarioService()
	 * @generated
	 */
	EReference getScenarioService_Scenarios();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getDescription()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_Description();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata
	 * @generated
	 */
	EClass getMetadata();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getCreator <em>Creator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Creator</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getCreator()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Creator();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getCreated <em>Created</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Created</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getCreated()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Created();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getLastModified <em>Last Modified</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Modified</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getLastModified()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_LastModified();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getComment()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Comment();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getLastModifiedBy <em>Last Modified By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Modified By</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getLastModifiedBy()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_LastModifiedBy();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ScenarioServiceFactory getScenarioServiceFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl <em>Scenario Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioInstance()
		 * @generated
		 */
		EClass SCENARIO_INSTANCE = eINSTANCE.getScenarioInstance();

		/**
		 * The meta object literal for the '<em><b>Variations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__VARIATIONS = eINSTANCE.getScenarioInstance_Variations();

		/**
		 * The meta object literal for the '<em><b>Parameter Sets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__PARAMETER_SETS = eINSTANCE.getScenarioInstance_ParameterSets();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__URI = eINSTANCE.getScenarioInstance_Uri();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__NAME = eINSTANCE.getScenarioInstance_Name();

		/**
		 * The meta object literal for the '<em><b>Initial Solution</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__INITIAL_SOLUTION = eINSTANCE.getScenarioInstance_InitialSolution();

		/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__UUID = eINSTANCE.getScenarioInstance_Uuid();

		/**
		 * The meta object literal for the '<em><b>Locked</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__LOCKED = eINSTANCE.getScenarioInstance_Locked();

		/**
		 * The meta object literal for the '<em><b>Archived</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__ARCHIVED = eINSTANCE.getScenarioInstance_Archived();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__METADATA = eINSTANCE.getScenarioInstance_Metadata();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl <em>Scenario Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioModel()
		 * @generated
		 */
		EClass SCENARIO_MODEL = eINSTANCE.getScenarioModel();

		/**
		 * The meta object literal for the '<em><b>Scenario Services</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_MODEL__SCENARIO_SERVICES = eINSTANCE.getScenarioModel_ScenarioServices();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ParamSetImpl <em>Param Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ParamSetImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getParamSet()
		 * @generated
		 */
		EClass PARAM_SET = eINSTANCE.getParamSet();

		/**
		 * The meta object literal for the '<em><b>Solutions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAM_SET__SOLUTIONS = eINSTANCE.getParamSet_Solutions();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_SET__URI = eINSTANCE.getParamSet_Uri();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_SET__NAME = eINSTANCE.getParamSet_Name();

		/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_SET__UUID = eINSTANCE.getParamSet_Uuid();

		/**
		 * The meta object literal for the '<em><b>Archived</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_SET__ARCHIVED = eINSTANCE.getParamSet_Archived();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAM_SET__METADATA = eINSTANCE.getParamSet_Metadata();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.SolutionImpl <em>Solution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.SolutionImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getSolution()
		 * @generated
		 */
		EClass SOLUTION = eINSTANCE.getSolution();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOLUTION__URI = eINSTANCE.getSolution_Uri();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOLUTION__NAME = eINSTANCE.getSolution_Name();

		/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOLUTION__UUID = eINSTANCE.getSolution_Uuid();

		/**
		 * The meta object literal for the '<em><b>Archived</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOLUTION__ARCHIVED = eINSTANCE.getSolution_Archived();

		/**
		 * The meta object literal for the '<em><b>Param Set</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION__PARAM_SET = eINSTANCE.getSolution_ParamSet();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION__METADATA = eINSTANCE.getSolution_Metadata();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl <em>Scenario Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioService()
		 * @generated
		 */
		EClass SCENARIO_SERVICE = eINSTANCE.getScenarioService();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_SERVICE__NAME = eINSTANCE.getScenarioService_Name();

		/**
		 * The meta object literal for the '<em><b>Scenarios</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_SERVICE__SCENARIOS = eINSTANCE.getScenarioService_Scenarios();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_SERVICE__DESCRIPTION = eINSTANCE.getScenarioService_Description();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl <em>Metadata</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.MetadataImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getMetadata()
		 * @generated
		 */
		EClass METADATA = eINSTANCE.getMetadata();

		/**
		 * The meta object literal for the '<em><b>Creator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__CREATOR = eINSTANCE.getMetadata_Creator();

		/**
		 * The meta object literal for the '<em><b>Created</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__CREATED = eINSTANCE.getMetadata_Created();

		/**
		 * The meta object literal for the '<em><b>Last Modified</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__LAST_MODIFIED = eINSTANCE.getMetadata_LastModified();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__COMMENT = eINSTANCE.getMetadata_Comment();

		/**
		 * The meta object literal for the '<em><b>Last Modified By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__LAST_MODIFIED_BY = eINSTANCE.getMetadata_LastModifiedBy();

	}

} //ScenarioServicePackage
