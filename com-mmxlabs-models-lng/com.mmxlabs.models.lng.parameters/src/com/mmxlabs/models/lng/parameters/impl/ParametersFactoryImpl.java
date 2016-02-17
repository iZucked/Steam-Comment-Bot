/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.Argument;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersModel;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ParametersFactoryImpl extends EFactoryImpl implements ParametersFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ParametersFactory init() {
		try {
			ParametersFactory theParametersFactory = (ParametersFactory)EPackage.Registry.INSTANCE.getEFactory(ParametersPackage.eNS_URI);
			if (theParametersFactory != null) {
				return theParametersFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ParametersFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParametersFactoryImpl() {
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
			case ParametersPackage.USER_SETTINGS: return createUserSettings();
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS: return createIndividualSolutionImprovementSettings();
			case ParametersPackage.PARAMETERS_MODEL: return createParametersModel();
			case ParametersPackage.OPTIMISER_SETTINGS: return createOptimiserSettings();
			case ParametersPackage.OBJECTIVE: return createObjective();
			case ParametersPackage.CONSTRAINT: return createConstraint();
			case ParametersPackage.OPTIMISATION_RANGE: return createOptimisationRange();
			case ParametersPackage.ANNEALING_SETTINGS: return createAnnealingSettings();
			case ParametersPackage.ARGUMENT: return createArgument();
			case ParametersPackage.SIMILARITY_SETTINGS: return createSimilaritySettings();
			case ParametersPackage.SIMILARITY_INTERVAL: return createSimilarityInterval();
			case ParametersPackage.ACTION_PLAN_SETTINGS: return createActionPlanSettings();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ParametersPackage.SIMILARITY_MODE:
				return createSimilarityModeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ParametersPackage.SIMILARITY_MODE:
				return convertSimilarityModeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UserSettings createUserSettings() {
		UserSettingsImpl userSettings = new UserSettingsImpl();
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IndividualSolutionImprovementSettings createIndividualSolutionImprovementSettings() {
		IndividualSolutionImprovementSettingsImpl individualSolutionImprovementSettings = new IndividualSolutionImprovementSettingsImpl();
		return individualSolutionImprovementSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ParametersModel createParametersModel() {
		ParametersModelImpl parametersModel = new ParametersModelImpl();
		return parametersModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptimiserSettings createOptimiserSettings() {
		OptimiserSettingsImpl optimiserSettings = new OptimiserSettingsImpl();
		return optimiserSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Objective createObjective() {
		ObjectiveImpl objective = new ObjectiveImpl();
		return objective;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Constraint createConstraint() {
		ConstraintImpl constraint = new ConstraintImpl();
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptimisationRange createOptimisationRange() {
		OptimisationRangeImpl optimisationRange = new OptimisationRangeImpl();
		return optimisationRange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnnealingSettings createAnnealingSettings() {
		AnnealingSettingsImpl annealingSettings = new AnnealingSettingsImpl();
		return annealingSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Argument createArgument() {
		ArgumentImpl argument = new ArgumentImpl();
		return argument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilaritySettings createSimilaritySettings() {
		SimilaritySettingsImpl similaritySettings = new SimilaritySettingsImpl();
		return similaritySettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilarityInterval createSimilarityInterval() {
		SimilarityIntervalImpl similarityInterval = new SimilarityIntervalImpl();
		return similarityInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActionPlanSettings createActionPlanSettings() {
		ActionPlanSettingsImpl actionPlanSettings = new ActionPlanSettingsImpl();
		return actionPlanSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimilarityMode createSimilarityModeFromString(EDataType eDataType, String initialValue) {
		SimilarityMode result = SimilarityMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSimilarityModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ParametersPackage getParametersPackage() {
		return (ParametersPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ParametersPackage getPackage() {
		return ParametersPackage.eINSTANCE;
	}

} //ParametersFactoryImpl
