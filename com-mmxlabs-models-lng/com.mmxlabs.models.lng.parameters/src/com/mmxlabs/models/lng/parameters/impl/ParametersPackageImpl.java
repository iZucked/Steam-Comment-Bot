/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.parameters.ActionPlanSettings;
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
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ParametersPackageImpl extends EPackageImpl implements ParametersPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass userSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass individualSolutionImprovementSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parametersModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optimiserSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectiveEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optimisationRangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annealingSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass argumentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass similaritySettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass similarityIntervalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actionPlanSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum similarityModeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ParametersPackageImpl() {
		super(eNS_URI, ParametersFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ParametersPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ParametersPackage init() {
		if (isInited) return (ParametersPackage)EPackage.Registry.INSTANCE.getEPackage(ParametersPackage.eNS_URI);

		// Obtain or create and register package
		ParametersPackageImpl theParametersPackage = (ParametersPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ParametersPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ParametersPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DateTimePackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theParametersPackage.createPackageContents();

		// Initialize created meta-data
		theParametersPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theParametersPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ParametersPackage.eNS_URI, theParametersPackage);
		return theParametersPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getUserSettings() {
		return userSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_PeriodStart() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_PeriodEnd() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_ShippingOnly() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_GenerateCharterOuts() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_BuildActionSets() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_SimilarityMode() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserSettings_FloatingDaysLimit() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIndividualSolutionImprovementSettings() {
		return individualSolutionImprovementSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndividualSolutionImprovementSettings_Iterations() {
		return (EAttribute)individualSolutionImprovementSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIndividualSolutionImprovementSettings_ImprovingSolutions() {
		return (EAttribute)individualSolutionImprovementSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getParametersModel() {
		return parametersModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getParametersModel_Settings() {
		return (EReference)parametersModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getParametersModel_ActiveSetting() {
		return (EReference)parametersModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOptimiserSettings() {
		return optimiserSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_Objectives() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_Constraints() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_Range() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_AnnealingSettings() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptimiserSettings_Seed() {
		return (EAttribute)optimiserSettingsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_Arguments() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptimiserSettings_GenerateCharterOuts() {
		return (EAttribute)optimiserSettingsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptimiserSettings_ShippingOnly() {
		return (EAttribute)optimiserSettingsEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_SimilaritySettings() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_SolutionImprovementSettings() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptimiserSettings_BuildActionSets() {
		return (EAttribute)optimiserSettingsEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOptimiserSettings_ActionPlanSettings() {
		return (EReference)optimiserSettingsEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptimiserSettings_FloatingDaysLimit() {
		return (EAttribute)optimiserSettingsEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getObjective() {
		return objectiveEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getObjective_Weight() {
		return (EAttribute)objectiveEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getObjective_Enabled() {
		return (EAttribute)objectiveEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConstraint() {
		return constraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConstraint_Enabled() {
		return (EAttribute)constraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOptimisationRange() {
		return optimisationRangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptimisationRange_OptimiseAfter() {
		return (EAttribute)optimisationRangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOptimisationRange_OptimiseBefore() {
		return (EAttribute)optimisationRangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAnnealingSettings() {
		return annealingSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAnnealingSettings_Iterations() {
		return (EAttribute)annealingSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAnnealingSettings_EpochLength() {
		return (EAttribute)annealingSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAnnealingSettings_Cooling() {
		return (EAttribute)annealingSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAnnealingSettings_InitialTemperature() {
		return (EAttribute)annealingSettingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAnnealingSettings_Restarting() {
		return (EAttribute)annealingSettingsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAnnealingSettings_RestartIterationsThreshold() {
		return (EAttribute)annealingSettingsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getArgument() {
		return argumentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getArgument_Name() {
		return (EAttribute)argumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getArgument_Value() {
		return (EAttribute)argumentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSimilaritySettings() {
		return similaritySettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSimilaritySettings_LowInterval() {
		return (EReference)similaritySettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSimilaritySettings_MedInterval() {
		return (EReference)similaritySettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSimilaritySettings_HighInterval() {
		return (EReference)similaritySettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSimilaritySettings_OutOfBoundsWeight() {
		return (EAttribute)similaritySettingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSimilarityInterval() {
		return similarityIntervalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSimilarityInterval_Weight() {
		return (EAttribute)similarityIntervalEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSimilarityInterval_Threshold() {
		return (EAttribute)similarityIntervalEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getActionPlanSettings() {
		return actionPlanSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getActionPlanSettings_TotalEvaluations() {
		return (EAttribute)actionPlanSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getActionPlanSettings_InRunEvaluations() {
		return (EAttribute)actionPlanSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getActionPlanSettings_SearchDepth() {
		return (EAttribute)actionPlanSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getSimilarityMode() {
		return similarityModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ParametersFactory getParametersFactory() {
		return (ParametersFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		userSettingsEClass = createEClass(USER_SETTINGS);
		createEAttribute(userSettingsEClass, USER_SETTINGS__PERIOD_START);
		createEAttribute(userSettingsEClass, USER_SETTINGS__PERIOD_END);
		createEAttribute(userSettingsEClass, USER_SETTINGS__SHIPPING_ONLY);
		createEAttribute(userSettingsEClass, USER_SETTINGS__GENERATE_CHARTER_OUTS);
		createEAttribute(userSettingsEClass, USER_SETTINGS__BUILD_ACTION_SETS);
		createEAttribute(userSettingsEClass, USER_SETTINGS__SIMILARITY_MODE);
		createEAttribute(userSettingsEClass, USER_SETTINGS__FLOATING_DAYS_LIMIT);

		individualSolutionImprovementSettingsEClass = createEClass(INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS);
		createEAttribute(individualSolutionImprovementSettingsEClass, INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS);
		createEAttribute(individualSolutionImprovementSettingsEClass, INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS);

		parametersModelEClass = createEClass(PARAMETERS_MODEL);
		createEReference(parametersModelEClass, PARAMETERS_MODEL__SETTINGS);
		createEReference(parametersModelEClass, PARAMETERS_MODEL__ACTIVE_SETTING);

		optimiserSettingsEClass = createEClass(OPTIMISER_SETTINGS);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__OBJECTIVES);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__CONSTRAINTS);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__RANGE);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__ANNEALING_SETTINGS);
		createEAttribute(optimiserSettingsEClass, OPTIMISER_SETTINGS__SEED);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__ARGUMENTS);
		createEAttribute(optimiserSettingsEClass, OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS);
		createEAttribute(optimiserSettingsEClass, OPTIMISER_SETTINGS__SHIPPING_ONLY);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__SIMILARITY_SETTINGS);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS);
		createEAttribute(optimiserSettingsEClass, OPTIMISER_SETTINGS__BUILD_ACTION_SETS);
		createEReference(optimiserSettingsEClass, OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS);
		createEAttribute(optimiserSettingsEClass, OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT);

		objectiveEClass = createEClass(OBJECTIVE);
		createEAttribute(objectiveEClass, OBJECTIVE__WEIGHT);
		createEAttribute(objectiveEClass, OBJECTIVE__ENABLED);

		constraintEClass = createEClass(CONSTRAINT);
		createEAttribute(constraintEClass, CONSTRAINT__ENABLED);

		optimisationRangeEClass = createEClass(OPTIMISATION_RANGE);
		createEAttribute(optimisationRangeEClass, OPTIMISATION_RANGE__OPTIMISE_AFTER);
		createEAttribute(optimisationRangeEClass, OPTIMISATION_RANGE__OPTIMISE_BEFORE);

		annealingSettingsEClass = createEClass(ANNEALING_SETTINGS);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__ITERATIONS);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__EPOCH_LENGTH);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__COOLING);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__INITIAL_TEMPERATURE);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__RESTARTING);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD);

		argumentEClass = createEClass(ARGUMENT);
		createEAttribute(argumentEClass, ARGUMENT__NAME);
		createEAttribute(argumentEClass, ARGUMENT__VALUE);

		similaritySettingsEClass = createEClass(SIMILARITY_SETTINGS);
		createEReference(similaritySettingsEClass, SIMILARITY_SETTINGS__LOW_INTERVAL);
		createEReference(similaritySettingsEClass, SIMILARITY_SETTINGS__MED_INTERVAL);
		createEReference(similaritySettingsEClass, SIMILARITY_SETTINGS__HIGH_INTERVAL);
		createEAttribute(similaritySettingsEClass, SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT);

		similarityIntervalEClass = createEClass(SIMILARITY_INTERVAL);
		createEAttribute(similarityIntervalEClass, SIMILARITY_INTERVAL__WEIGHT);
		createEAttribute(similarityIntervalEClass, SIMILARITY_INTERVAL__THRESHOLD);

		actionPlanSettingsEClass = createEClass(ACTION_PLAN_SETTINGS);
		createEAttribute(actionPlanSettingsEClass, ACTION_PLAN_SETTINGS__TOTAL_EVALUATIONS);
		createEAttribute(actionPlanSettingsEClass, ACTION_PLAN_SETTINGS__IN_RUN_EVALUATIONS);
		createEAttribute(actionPlanSettingsEClass, ACTION_PLAN_SETTINGS__SEARCH_DEPTH);

		// Create enums
		similarityModeEEnum = createEEnum(SIMILARITY_MODE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		parametersModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		optimiserSettingsEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		optimiserSettingsEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		objectiveEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		constraintEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());

		// Initialize classes and features; add operations and parameters
		initEClass(userSettingsEClass, UserSettings.class, "UserSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUserSettings_PeriodStart(), theDateTimePackage.getYearMonth(), "periodStart", null, 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_PeriodEnd(), theDateTimePackage.getYearMonth(), "periodEnd", null, 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_ShippingOnly(), ecorePackage.getEBoolean(), "shippingOnly", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_GenerateCharterOuts(), ecorePackage.getEBoolean(), "generateCharterOuts", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_BuildActionSets(), ecorePackage.getEBoolean(), "buildActionSets", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_SimilarityMode(), this.getSimilarityMode(), "similarityMode", "OFF", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_FloatingDaysLimit(), ecorePackage.getEInt(), "floatingDaysLimit", "15", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(individualSolutionImprovementSettingsEClass, IndividualSolutionImprovementSettings.class, "IndividualSolutionImprovementSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndividualSolutionImprovementSettings_Iterations(), ecorePackage.getEInt(), "iterations", null, 1, 1, IndividualSolutionImprovementSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndividualSolutionImprovementSettings_ImprovingSolutions(), ecorePackage.getEBoolean(), "improvingSolutions", null, 0, 1, IndividualSolutionImprovementSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parametersModelEClass, ParametersModel.class, "ParametersModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParametersModel_Settings(), this.getOptimiserSettings(), null, "settings", null, 0, -1, ParametersModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParametersModel_ActiveSetting(), this.getOptimiserSettings(), null, "activeSetting", null, 1, 1, ParametersModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optimiserSettingsEClass, OptimiserSettings.class, "OptimiserSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOptimiserSettings_Objectives(), this.getObjective(), null, "objectives", null, 0, -1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimiserSettings_Constraints(), this.getConstraint(), null, "constraints", null, 0, -1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimiserSettings_Range(), this.getOptimisationRange(), null, "range", null, 1, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimiserSettings_AnnealingSettings(), this.getAnnealingSettings(), null, "annealingSettings", null, 1, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptimiserSettings_Seed(), ecorePackage.getEInt(), "seed", null, 1, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimiserSettings_Arguments(), this.getArgument(), null, "arguments", null, 0, -1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptimiserSettings_GenerateCharterOuts(), ecorePackage.getEBoolean(), "generateCharterOuts", null, 0, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptimiserSettings_ShippingOnly(), ecorePackage.getEBoolean(), "shippingOnly", null, 0, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimiserSettings_SimilaritySettings(), this.getSimilaritySettings(), null, "similaritySettings", null, 1, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimiserSettings_SolutionImprovementSettings(), this.getIndividualSolutionImprovementSettings(), null, "solutionImprovementSettings", null, 1, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptimiserSettings_BuildActionSets(), ecorePackage.getEBoolean(), "buildActionSets", null, 0, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimiserSettings_ActionPlanSettings(), this.getActionPlanSettings(), null, "actionPlanSettings", null, 1, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptimiserSettings_FloatingDaysLimit(), ecorePackage.getEInt(), "floatingDaysLimit", "15", 0, 1, OptimiserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectiveEClass, Objective.class, "Objective", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getObjective_Weight(), ecorePackage.getEDouble(), "weight", null, 1, 1, Objective.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjective_Enabled(), ecorePackage.getEBoolean(), "enabled", null, 1, 1, Objective.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constraintEClass, Constraint.class, "Constraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstraint_Enabled(), ecorePackage.getEBoolean(), "enabled", null, 1, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optimisationRangeEClass, OptimisationRange.class, "OptimisationRange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOptimisationRange_OptimiseAfter(), theDateTimePackage.getYearMonth(), "optimiseAfter", null, 0, 1, OptimisationRange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOptimisationRange_OptimiseBefore(), theDateTimePackage.getYearMonth(), "optimiseBefore", null, 0, 1, OptimisationRange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(annealingSettingsEClass, AnnealingSettings.class, "AnnealingSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnealingSettings_Iterations(), ecorePackage.getEInt(), "iterations", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_EpochLength(), ecorePackage.getEInt(), "epochLength", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_Cooling(), ecorePackage.getEDouble(), "cooling", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_InitialTemperature(), ecorePackage.getEInt(), "initialTemperature", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_Restarting(), ecorePackage.getEBoolean(), "restarting", null, 0, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_RestartIterationsThreshold(), ecorePackage.getEInt(), "restartIterationsThreshold", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(argumentEClass, Argument.class, "Argument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArgument_Name(), ecorePackage.getEString(), "name", null, 1, 1, Argument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArgument_Value(), ecorePackage.getEString(), "value", null, 1, 1, Argument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(similaritySettingsEClass, SimilaritySettings.class, "SimilaritySettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSimilaritySettings_LowInterval(), this.getSimilarityInterval(), null, "lowInterval", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSimilaritySettings_MedInterval(), this.getSimilarityInterval(), null, "medInterval", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSimilaritySettings_HighInterval(), this.getSimilarityInterval(), null, "highInterval", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimilaritySettings_OutOfBoundsWeight(), ecorePackage.getEInt(), "outOfBoundsWeight", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(similarityIntervalEClass, SimilarityInterval.class, "SimilarityInterval", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSimilarityInterval_Weight(), ecorePackage.getEInt(), "weight", null, 1, 1, SimilarityInterval.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimilarityInterval_Threshold(), ecorePackage.getEInt(), "threshold", null, 1, 1, SimilarityInterval.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(actionPlanSettingsEClass, ActionPlanSettings.class, "ActionPlanSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getActionPlanSettings_TotalEvaluations(), ecorePackage.getEInt(), "totalEvaluations", null, 1, 1, ActionPlanSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActionPlanSettings_InRunEvaluations(), ecorePackage.getEInt(), "inRunEvaluations", null, 1, 1, ActionPlanSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActionPlanSettings_SearchDepth(), ecorePackage.getEInt(), "searchDepth", null, 1, 1, ActionPlanSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(similarityModeEEnum, SimilarityMode.class, "SimilarityMode");
		addEEnumLiteral(similarityModeEEnum, SimilarityMode.ALL);
		addEEnumLiteral(similarityModeEEnum, SimilarityMode.OFF);
		addEEnumLiteral(similarityModeEEnum, SimilarityMode.LOW);
		addEEnumLiteral(similarityModeEEnum, SimilarityMode.MEDIUM);
		addEEnumLiteral(similarityModeEEnum, SimilarityMode.HIGH);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.minimaxlabs.com/license/features/required
		createRequiredAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.minimaxlabs.com/license/features/required</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createRequiredAnnotations() {
		String source = "http://www.minimaxlabs.com/license/features/required";	
		addAnnotation
		  (getUserSettings_BuildActionSets(), 
		   source, 
		   new String[] {
			 "module", "actionplan"
		   });
	}

} //ParametersPackageImpl
