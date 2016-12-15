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
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
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
	private EClass annealingSettingsEClass = null;

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
	private EClass optimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constraintsAndFitnessSettingsStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parallisableOptimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parallelOptimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cleanStateOptimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass localSearchOptimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass hillClimbOptimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actionPlanOptimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resetInitialSequencesStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass breakEvenOptimisationStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constraintAndFitnessSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass optimisationPlanEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass solutionBuilderSettingsEClass = null;

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
	public EAttribute getUserSettings_WithSpotCargoMarkets() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_BuildActionSets() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUserSettings_SimilarityMode() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserSettings_CleanStateOptimisation() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserSettings_FloatingDaysLimit() {
		return (EAttribute)userSettingsEClass.getEStructuralFeatures().get(8);
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
	public EClass getOptimisationStage() {
		return optimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOptimisationStage_Name() {
		return (EAttribute)optimisationStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstraintsAndFitnessSettingsStage() {
		return constraintsAndFitnessSettingsStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConstraintsAndFitnessSettingsStage_ConstraintAndFitnessSettings() {
		return (EReference)constraintsAndFitnessSettingsStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParallisableOptimisationStage() {
		return parallisableOptimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParallelOptimisationStage() {
		return parallelOptimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParallelOptimisationStage_JobCount() {
		return (EAttribute)parallelOptimisationStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParallelOptimisationStage_Template() {
		return (EReference)parallelOptimisationStageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCleanStateOptimisationStage() {
		return cleanStateOptimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCleanStateOptimisationStage_Seed() {
		return (EAttribute)cleanStateOptimisationStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCleanStateOptimisationStage_AnnealingSettings() {
		return (EReference)cleanStateOptimisationStageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLocalSearchOptimisationStage() {
		return localSearchOptimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocalSearchOptimisationStage_Seed() {
		return (EAttribute)localSearchOptimisationStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLocalSearchOptimisationStage_AnnealingSettings() {
		return (EReference)localSearchOptimisationStageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHillClimbOptimisationStage() {
		return hillClimbOptimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHillClimbOptimisationStage_Seed() {
		return (EAttribute)hillClimbOptimisationStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getHillClimbOptimisationStage_AnnealingSettings() {
		return (EReference)hillClimbOptimisationStageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getActionPlanOptimisationStage() {
		return actionPlanOptimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getActionPlanOptimisationStage_TotalEvaluations() {
		return (EAttribute)actionPlanOptimisationStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getActionPlanOptimisationStage_InRunEvaluations() {
		return (EAttribute)actionPlanOptimisationStageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getActionPlanOptimisationStage_SearchDepth() {
		return (EAttribute)actionPlanOptimisationStageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResetInitialSequencesStage() {
		return resetInitialSequencesStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBreakEvenOptimisationStage() {
		return breakEvenOptimisationStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBreakEvenOptimisationStage_TargetProfitAndLoss() {
		return (EAttribute)breakEvenOptimisationStageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstraintAndFitnessSettings() {
		return constraintAndFitnessSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConstraintAndFitnessSettings_Objectives() {
		return (EReference)constraintAndFitnessSettingsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConstraintAndFitnessSettings_Constraints() {
		return (EReference)constraintAndFitnessSettingsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstraintAndFitnessSettings_FloatingDaysLimit() {
		return (EAttribute)constraintAndFitnessSettingsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConstraintAndFitnessSettings_SimilaritySettings() {
		return (EReference)constraintAndFitnessSettingsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOptimisationPlan() {
		return optimisationPlanEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptimisationPlan_UserSettings() {
		return (EReference)optimisationPlanEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptimisationPlan_Stages() {
		return (EReference)optimisationPlanEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOptimisationPlan_SolutionBuilderSettings() {
		return (EReference)optimisationPlanEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSolutionBuilderSettings() {
		return solutionBuilderSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSolutionBuilderSettings_ConstraintAndFitnessSettings() {
		return (EReference)solutionBuilderSettingsEClass.getEStructuralFeatures().get(0);
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
		createEAttribute(userSettingsEClass, USER_SETTINGS__WITH_SPOT_CARGO_MARKETS);
		createEAttribute(userSettingsEClass, USER_SETTINGS__BUILD_ACTION_SETS);
		createEAttribute(userSettingsEClass, USER_SETTINGS__SIMILARITY_MODE);
		createEAttribute(userSettingsEClass, USER_SETTINGS__CLEAN_STATE_OPTIMISATION);
		createEAttribute(userSettingsEClass, USER_SETTINGS__FLOATING_DAYS_LIMIT);

		objectiveEClass = createEClass(OBJECTIVE);
		createEAttribute(objectiveEClass, OBJECTIVE__WEIGHT);
		createEAttribute(objectiveEClass, OBJECTIVE__ENABLED);

		constraintEClass = createEClass(CONSTRAINT);
		createEAttribute(constraintEClass, CONSTRAINT__ENABLED);

		annealingSettingsEClass = createEClass(ANNEALING_SETTINGS);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__ITERATIONS);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__EPOCH_LENGTH);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__COOLING);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__INITIAL_TEMPERATURE);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__RESTARTING);
		createEAttribute(annealingSettingsEClass, ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD);

		similaritySettingsEClass = createEClass(SIMILARITY_SETTINGS);
		createEReference(similaritySettingsEClass, SIMILARITY_SETTINGS__LOW_INTERVAL);
		createEReference(similaritySettingsEClass, SIMILARITY_SETTINGS__MED_INTERVAL);
		createEReference(similaritySettingsEClass, SIMILARITY_SETTINGS__HIGH_INTERVAL);
		createEAttribute(similaritySettingsEClass, SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT);

		similarityIntervalEClass = createEClass(SIMILARITY_INTERVAL);
		createEAttribute(similarityIntervalEClass, SIMILARITY_INTERVAL__WEIGHT);
		createEAttribute(similarityIntervalEClass, SIMILARITY_INTERVAL__THRESHOLD);

		optimisationPlanEClass = createEClass(OPTIMISATION_PLAN);
		createEReference(optimisationPlanEClass, OPTIMISATION_PLAN__USER_SETTINGS);
		createEReference(optimisationPlanEClass, OPTIMISATION_PLAN__STAGES);
		createEReference(optimisationPlanEClass, OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS);

		constraintAndFitnessSettingsEClass = createEClass(CONSTRAINT_AND_FITNESS_SETTINGS);
		createEReference(constraintAndFitnessSettingsEClass, CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES);
		createEReference(constraintAndFitnessSettingsEClass, CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS);
		createEAttribute(constraintAndFitnessSettingsEClass, CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT);
		createEReference(constraintAndFitnessSettingsEClass, CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS);

		optimisationStageEClass = createEClass(OPTIMISATION_STAGE);
		createEAttribute(optimisationStageEClass, OPTIMISATION_STAGE__NAME);

		constraintsAndFitnessSettingsStageEClass = createEClass(CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE);
		createEReference(constraintsAndFitnessSettingsStageEClass, CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS);

		parallisableOptimisationStageEClass = createEClass(PARALLISABLE_OPTIMISATION_STAGE);

		parallelOptimisationStageEClass = createEClass(PARALLEL_OPTIMISATION_STAGE);
		createEAttribute(parallelOptimisationStageEClass, PARALLEL_OPTIMISATION_STAGE__JOB_COUNT);
		createEReference(parallelOptimisationStageEClass, PARALLEL_OPTIMISATION_STAGE__TEMPLATE);

		cleanStateOptimisationStageEClass = createEClass(CLEAN_STATE_OPTIMISATION_STAGE);
		createEAttribute(cleanStateOptimisationStageEClass, CLEAN_STATE_OPTIMISATION_STAGE__SEED);
		createEReference(cleanStateOptimisationStageEClass, CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS);

		localSearchOptimisationStageEClass = createEClass(LOCAL_SEARCH_OPTIMISATION_STAGE);
		createEAttribute(localSearchOptimisationStageEClass, LOCAL_SEARCH_OPTIMISATION_STAGE__SEED);
		createEReference(localSearchOptimisationStageEClass, LOCAL_SEARCH_OPTIMISATION_STAGE__ANNEALING_SETTINGS);

		hillClimbOptimisationStageEClass = createEClass(HILL_CLIMB_OPTIMISATION_STAGE);
		createEAttribute(hillClimbOptimisationStageEClass, HILL_CLIMB_OPTIMISATION_STAGE__SEED);
		createEReference(hillClimbOptimisationStageEClass, HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS);

		actionPlanOptimisationStageEClass = createEClass(ACTION_PLAN_OPTIMISATION_STAGE);
		createEAttribute(actionPlanOptimisationStageEClass, ACTION_PLAN_OPTIMISATION_STAGE__TOTAL_EVALUATIONS);
		createEAttribute(actionPlanOptimisationStageEClass, ACTION_PLAN_OPTIMISATION_STAGE__IN_RUN_EVALUATIONS);
		createEAttribute(actionPlanOptimisationStageEClass, ACTION_PLAN_OPTIMISATION_STAGE__SEARCH_DEPTH);

		resetInitialSequencesStageEClass = createEClass(RESET_INITIAL_SEQUENCES_STAGE);

		breakEvenOptimisationStageEClass = createEClass(BREAK_EVEN_OPTIMISATION_STAGE);
		createEAttribute(breakEvenOptimisationStageEClass, BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS);

		solutionBuilderSettingsEClass = createEClass(SOLUTION_BUILDER_SETTINGS);
		createEReference(solutionBuilderSettingsEClass, SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS);

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
		ETypeParameter parallelOptimisationStageEClass_T = addETypeParameter(parallelOptimisationStageEClass, "T");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(this.getParallisableOptimisationStage());
		parallelOptimisationStageEClass_T.getEBounds().add(g1);

		// Add supertypes to classes
		objectiveEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		constraintEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		constraintsAndFitnessSettingsStageEClass.getESuperTypes().add(this.getOptimisationStage());
		parallisableOptimisationStageEClass.getESuperTypes().add(this.getOptimisationStage());
		parallelOptimisationStageEClass.getESuperTypes().add(this.getOptimisationStage());
		cleanStateOptimisationStageEClass.getESuperTypes().add(this.getParallisableOptimisationStage());
		cleanStateOptimisationStageEClass.getESuperTypes().add(this.getConstraintsAndFitnessSettingsStage());
		localSearchOptimisationStageEClass.getESuperTypes().add(this.getParallisableOptimisationStage());
		localSearchOptimisationStageEClass.getESuperTypes().add(this.getConstraintsAndFitnessSettingsStage());
		hillClimbOptimisationStageEClass.getESuperTypes().add(this.getParallisableOptimisationStage());
		hillClimbOptimisationStageEClass.getESuperTypes().add(this.getConstraintsAndFitnessSettingsStage());
		actionPlanOptimisationStageEClass.getESuperTypes().add(this.getConstraintsAndFitnessSettingsStage());
		resetInitialSequencesStageEClass.getESuperTypes().add(this.getOptimisationStage());
		breakEvenOptimisationStageEClass.getESuperTypes().add(this.getOptimisationStage());

		// Initialize classes and features; add operations and parameters
		initEClass(userSettingsEClass, UserSettings.class, "UserSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUserSettings_PeriodStart(), theDateTimePackage.getYearMonth(), "periodStart", null, 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_PeriodEnd(), theDateTimePackage.getYearMonth(), "periodEnd", null, 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_ShippingOnly(), ecorePackage.getEBoolean(), "shippingOnly", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_GenerateCharterOuts(), ecorePackage.getEBoolean(), "generateCharterOuts", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_WithSpotCargoMarkets(), ecorePackage.getEBoolean(), "withSpotCargoMarkets", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_BuildActionSets(), ecorePackage.getEBoolean(), "buildActionSets", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_SimilarityMode(), this.getSimilarityMode(), "similarityMode", "OFF", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_CleanStateOptimisation(), ecorePackage.getEBoolean(), "cleanStateOptimisation", "false", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUserSettings_FloatingDaysLimit(), ecorePackage.getEInt(), "floatingDaysLimit", "15", 0, 1, UserSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectiveEClass, Objective.class, "Objective", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getObjective_Weight(), ecorePackage.getEDouble(), "weight", null, 1, 1, Objective.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjective_Enabled(), ecorePackage.getEBoolean(), "enabled", null, 1, 1, Objective.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constraintEClass, Constraint.class, "Constraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstraint_Enabled(), ecorePackage.getEBoolean(), "enabled", null, 1, 1, Constraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(annealingSettingsEClass, AnnealingSettings.class, "AnnealingSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnealingSettings_Iterations(), ecorePackage.getEInt(), "iterations", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_EpochLength(), ecorePackage.getEInt(), "epochLength", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_Cooling(), ecorePackage.getEDouble(), "cooling", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_InitialTemperature(), ecorePackage.getEInt(), "initialTemperature", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_Restarting(), ecorePackage.getEBoolean(), "restarting", null, 0, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnealingSettings_RestartIterationsThreshold(), ecorePackage.getEInt(), "restartIterationsThreshold", null, 1, 1, AnnealingSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(similaritySettingsEClass, SimilaritySettings.class, "SimilaritySettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSimilaritySettings_LowInterval(), this.getSimilarityInterval(), null, "lowInterval", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSimilaritySettings_MedInterval(), this.getSimilarityInterval(), null, "medInterval", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSimilaritySettings_HighInterval(), this.getSimilarityInterval(), null, "highInterval", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimilaritySettings_OutOfBoundsWeight(), ecorePackage.getEInt(), "outOfBoundsWeight", null, 1, 1, SimilaritySettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(similarityIntervalEClass, SimilarityInterval.class, "SimilarityInterval", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSimilarityInterval_Weight(), ecorePackage.getEInt(), "weight", null, 1, 1, SimilarityInterval.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSimilarityInterval_Threshold(), ecorePackage.getEInt(), "threshold", null, 1, 1, SimilarityInterval.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optimisationPlanEClass, OptimisationPlan.class, "OptimisationPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOptimisationPlan_UserSettings(), this.getUserSettings(), null, "userSettings", null, 0, 1, OptimisationPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimisationPlan_Stages(), this.getOptimisationStage(), null, "stages", null, 0, -1, OptimisationPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOptimisationPlan_SolutionBuilderSettings(), this.getSolutionBuilderSettings(), null, "solutionBuilderSettings", null, 0, 1, OptimisationPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constraintAndFitnessSettingsEClass, ConstraintAndFitnessSettings.class, "ConstraintAndFitnessSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConstraintAndFitnessSettings_Objectives(), this.getObjective(), null, "objectives", null, 0, -1, ConstraintAndFitnessSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConstraintAndFitnessSettings_Constraints(), this.getConstraint(), null, "constraints", null, 0, -1, ConstraintAndFitnessSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConstraintAndFitnessSettings_FloatingDaysLimit(), ecorePackage.getEInt(), "floatingDaysLimit", "15", 0, 1, ConstraintAndFitnessSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConstraintAndFitnessSettings_SimilaritySettings(), this.getSimilaritySettings(), null, "similaritySettings", null, 1, 1, ConstraintAndFitnessSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(optimisationStageEClass, OptimisationStage.class, "OptimisationStage", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOptimisationStage_Name(), ecorePackage.getEString(), "name", null, 0, 1, OptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constraintsAndFitnessSettingsStageEClass, ConstraintsAndFitnessSettingsStage.class, "ConstraintsAndFitnessSettingsStage", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConstraintsAndFitnessSettingsStage_ConstraintAndFitnessSettings(), this.getConstraintAndFitnessSettings(), null, "constraintAndFitnessSettings", null, 0, 1, ConstraintsAndFitnessSettingsStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parallisableOptimisationStageEClass, ParallisableOptimisationStage.class, "ParallisableOptimisationStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(parallelOptimisationStageEClass, ParallelOptimisationStage.class, "ParallelOptimisationStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParallelOptimisationStage_JobCount(), ecorePackage.getEInt(), "jobCount", null, 0, 1, ParallelOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(parallelOptimisationStageEClass_T);
		initEReference(getParallelOptimisationStage_Template(), g1, null, "template", null, 0, 1, ParallelOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cleanStateOptimisationStageEClass, CleanStateOptimisationStage.class, "CleanStateOptimisationStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCleanStateOptimisationStage_Seed(), ecorePackage.getEInt(), "seed", null, 1, 1, CleanStateOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCleanStateOptimisationStage_AnnealingSettings(), this.getAnnealingSettings(), null, "annealingSettings", null, 1, 1, CleanStateOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(localSearchOptimisationStageEClass, LocalSearchOptimisationStage.class, "LocalSearchOptimisationStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLocalSearchOptimisationStage_Seed(), ecorePackage.getEInt(), "seed", null, 1, 1, LocalSearchOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLocalSearchOptimisationStage_AnnealingSettings(), this.getAnnealingSettings(), null, "annealingSettings", null, 1, 1, LocalSearchOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(hillClimbOptimisationStageEClass, HillClimbOptimisationStage.class, "HillClimbOptimisationStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHillClimbOptimisationStage_Seed(), ecorePackage.getEInt(), "seed", null, 1, 1, HillClimbOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getHillClimbOptimisationStage_AnnealingSettings(), this.getAnnealingSettings(), null, "annealingSettings", null, 1, 1, HillClimbOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(actionPlanOptimisationStageEClass, ActionPlanOptimisationStage.class, "ActionPlanOptimisationStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getActionPlanOptimisationStage_TotalEvaluations(), ecorePackage.getEInt(), "totalEvaluations", null, 1, 1, ActionPlanOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActionPlanOptimisationStage_InRunEvaluations(), ecorePackage.getEInt(), "inRunEvaluations", null, 1, 1, ActionPlanOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActionPlanOptimisationStage_SearchDepth(), ecorePackage.getEInt(), "searchDepth", null, 1, 1, ActionPlanOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resetInitialSequencesStageEClass, ResetInitialSequencesStage.class, "ResetInitialSequencesStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(breakEvenOptimisationStageEClass, BreakEvenOptimisationStage.class, "BreakEvenOptimisationStage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBreakEvenOptimisationStage_TargetProfitAndLoss(), ecorePackage.getELong(), "targetProfitAndLoss", null, 0, 1, BreakEvenOptimisationStage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(solutionBuilderSettingsEClass, SolutionBuilderSettings.class, "SolutionBuilderSettings", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSolutionBuilderSettings_ConstraintAndFitnessSettings(), this.getConstraintAndFitnessSettings(), null, "constraintAndFitnessSettings", null, 0, 1, SolutionBuilderSettings.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
