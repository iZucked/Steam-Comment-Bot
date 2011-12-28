/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import com.mmxlabs.scenario.service.model.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioServiceFactoryImpl extends EFactoryImpl implements ScenarioServiceFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ScenarioServiceFactory init() {
		try {
			ScenarioServiceFactory theScenarioServiceFactory = (ScenarioServiceFactory) EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.scenario.service/model/1");
			if (theScenarioServiceFactory != null) {
				return theScenarioServiceFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ScenarioServiceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioServiceFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case ScenarioServicePackage.SCENARIO_MODEL:
			return createScenarioModel();
		case ScenarioServicePackage.SCENARIO_SERVICE:
			return createScenarioService();
		case ScenarioServicePackage.SCENARIO_INSTANCE:
			return createScenarioInstance();
		case ScenarioServicePackage.PARAM_SET:
			return createParamSet();
		case ScenarioServicePackage.SOLUTION:
			return createSolution();
		case ScenarioServicePackage.METADATA:
			return createMetadata();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance createScenarioInstance() {
		ScenarioInstanceImpl scenarioInstance = new ScenarioInstanceImpl();
		return scenarioInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioModel createScenarioModel() {
		ScenarioModelImpl scenarioModel = new ScenarioModelImpl();
		return scenarioModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParamSet createParamSet() {
		ParamSetImpl paramSet = new ParamSetImpl();
		return paramSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Solution createSolution() {
		SolutionImpl solution = new SolutionImpl();
		return solution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioService createScenarioService() {
		ScenarioServiceImpl scenarioService = new ScenarioServiceImpl();
		return scenarioService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metadata createMetadata() {
		MetadataImpl metadata = new MetadataImpl();
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioServicePackage getScenarioServicePackage() {
		return (ScenarioServicePackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ScenarioServicePackage getPackage() {
		return ScenarioServicePackage.eINSTANCE;
	}

} //ScenarioServiceFactoryImpl
