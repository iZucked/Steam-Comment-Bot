/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ChildCreationExtenderManager;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import org.eclipse.jdt.annotation.Nullable;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.util.ParametersAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ParametersItemProviderAdapterFactory extends ParametersAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable, IChildCreationExtender {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This helps manage the child creation extenders.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChildCreationExtenderManager childCreationExtenderManager = new ChildCreationExtenderManager(ParametersEditPlugin.INSTANCE, ParametersPackage.eNS_URI);

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParametersItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.UserSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UserSettingsItemProvider userSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.UserSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUserSettingsAdapter() {
		if (userSettingsItemProvider == null) {
			userSettingsItemProvider = new UserSettingsItemProvider(this);
		}

		return userSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.Objective} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ObjectiveItemProvider objectiveItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.Objective}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createObjectiveAdapter() {
		if (objectiveItemProvider == null) {
			objectiveItemProvider = new ObjectiveItemProvider(this);
		}

		return objectiveItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.Constraint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConstraintItemProvider constraintItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.Constraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createConstraintAdapter() {
		if (constraintItemProvider == null) {
			constraintItemProvider = new ConstraintItemProvider(this);
		}

		return constraintItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.AnnealingSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnnealingSettingsItemProvider annealingSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.AnnealingSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createAnnealingSettingsAdapter() {
		if (annealingSettingsItemProvider == null) {
			annealingSettingsItemProvider = new AnnealingSettingsItemProvider(this);
		}

		return annealingSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.SimilaritySettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimilaritySettingsItemProvider similaritySettingsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.SimilaritySettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSimilaritySettingsAdapter() {
		if (similaritySettingsItemProvider == null) {
			similaritySettingsItemProvider = new SimilaritySettingsItemProvider(this);
		}

		return similaritySettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.SimilarityInterval} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimilarityIntervalItemProvider similarityIntervalItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.SimilarityInterval}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSimilarityIntervalAdapter() {
		if (similarityIntervalItemProvider == null) {
			similarityIntervalItemProvider = new SimilarityIntervalItemProvider(this);
		}

		return similarityIntervalItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParallisableOptimisationStageItemProvider parallisableOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createParallisableOptimisationStageAdapter() {
		if (parallisableOptimisationStageItemProvider == null) {
			parallisableOptimisationStageItemProvider = new ParallisableOptimisationStageItemProvider(this);
		}

		return parallisableOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParallelOptimisationStageItemProvider parallelOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createParallelOptimisationStageAdapter() {
		if (parallelOptimisationStageItemProvider == null) {
			parallelOptimisationStageItemProvider = new ParallelOptimisationStageItemProvider(this);
		}

		return parallelOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CleanStateOptimisationStageItemProvider cleanStateOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCleanStateOptimisationStageAdapter() {
		if (cleanStateOptimisationStageItemProvider == null) {
			cleanStateOptimisationStageItemProvider = new CleanStateOptimisationStageItemProvider(this);
		}

		return cleanStateOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalSearchOptimisationStageItemProvider localSearchOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLocalSearchOptimisationStageAdapter() {
		if (localSearchOptimisationStageItemProvider == null) {
			localSearchOptimisationStageItemProvider = new LocalSearchOptimisationStageItemProvider(this);
		}

		return localSearchOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HillClimbOptimisationStageItemProvider hillClimbOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createHillClimbOptimisationStageAdapter() {
		if (hillClimbOptimisationStageItemProvider == null) {
			hillClimbOptimisationStageItemProvider = new HillClimbOptimisationStageItemProvider(this);
		}

		return hillClimbOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ActionPlanOptimisationStageItemProvider actionPlanOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createActionPlanOptimisationStageAdapter() {
		if (actionPlanOptimisationStageItemProvider == null) {
			actionPlanOptimisationStageItemProvider = new ActionPlanOptimisationStageItemProvider(this);
		}

		return actionPlanOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResetInitialSequencesStageItemProvider resetInitialSequencesStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createResetInitialSequencesStageAdapter() {
		if (resetInitialSequencesStageItemProvider == null) {
			resetInitialSequencesStageItemProvider = new ResetInitialSequencesStageItemProvider(this);
		}

		return resetInitialSequencesStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.InsertionOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InsertionOptimisationStageItemProvider insertionOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.InsertionOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInsertionOptimisationStageAdapter() {
		if (insertionOptimisationStageItemProvider == null) {
			insertionOptimisationStageItemProvider = new InsertionOptimisationStageItemProvider(this);
		}

		return insertionOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BreakEvenOptimisationStageItemProvider breakEvenOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBreakEvenOptimisationStageAdapter() {
		if (breakEvenOptimisationStageItemProvider == null) {
			breakEvenOptimisationStageItemProvider = new BreakEvenOptimisationStageItemProvider(this);
		}

		return breakEvenOptimisationStageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConstraintAndFitnessSettingsItemProvider constraintAndFitnessSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createConstraintAndFitnessSettingsAdapter() {
		if (constraintAndFitnessSettingsItemProvider == null) {
			constraintAndFitnessSettingsItemProvider = new ConstraintAndFitnessSettingsItemProvider(this);
		}

		return constraintAndFitnessSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.OptimisationPlan} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptimisationPlanItemProvider optimisationPlanItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.OptimisationPlan}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createOptimisationPlanAdapter() {
		if (optimisationPlanItemProvider == null) {
			optimisationPlanItemProvider = new OptimisationPlanItemProvider(this);
		}

		return optimisationPlanItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.SolutionBuilderSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SolutionBuilderSettingsItemProvider solutionBuilderSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.SolutionBuilderSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSolutionBuilderSettingsAdapter() {
		if (solutionBuilderSettingsItemProvider == null) {
			solutionBuilderSettingsItemProvider = new SolutionBuilderSettingsItemProvider(this);
		}

		return solutionBuilderSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MultipleSolutionSimilarityOptimisationStageItemProvider multipleSolutionSimilarityOptimisationStageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMultipleSolutionSimilarityOptimisationStageAdapter() {
		if (multipleSolutionSimilarityOptimisationStageItemProvider == null) {
			multipleSolutionSimilarityOptimisationStageItemProvider = new MultipleSolutionSimilarityOptimisationStageItemProvider(this);
		}

		return multipleSolutionSimilarityOptimisationStageItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<IChildCreationExtender> getChildCreationExtenders() {
		return childCreationExtenderManager.getChildCreationExtenders();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain) {
		return childCreationExtenderManager.getNewChildDescriptors(object, editingDomain);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return childCreationExtenderManager;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void dispose() {
		if (userSettingsItemProvider != null) userSettingsItemProvider.dispose();
		if (objectiveItemProvider != null) objectiveItemProvider.dispose();
		if (constraintItemProvider != null) constraintItemProvider.dispose();
		if (annealingSettingsItemProvider != null) annealingSettingsItemProvider.dispose();
		if (similaritySettingsItemProvider != null) similaritySettingsItemProvider.dispose();
		if (similarityIntervalItemProvider != null) similarityIntervalItemProvider.dispose();
		if (optimisationPlanItemProvider != null) optimisationPlanItemProvider.dispose();
		if (constraintAndFitnessSettingsItemProvider != null) constraintAndFitnessSettingsItemProvider.dispose();
		if (parallisableOptimisationStageItemProvider != null) parallisableOptimisationStageItemProvider.dispose();
		if (parallelOptimisationStageItemProvider != null) parallelOptimisationStageItemProvider.dispose();
		if (cleanStateOptimisationStageItemProvider != null) cleanStateOptimisationStageItemProvider.dispose();
		if (localSearchOptimisationStageItemProvider != null) localSearchOptimisationStageItemProvider.dispose();
		if (hillClimbOptimisationStageItemProvider != null) hillClimbOptimisationStageItemProvider.dispose();
		if (actionPlanOptimisationStageItemProvider != null) actionPlanOptimisationStageItemProvider.dispose();
		if (resetInitialSequencesStageItemProvider != null) resetInitialSequencesStageItemProvider.dispose();
		if (insertionOptimisationStageItemProvider != null) insertionOptimisationStageItemProvider.dispose();
		if (breakEvenOptimisationStageItemProvider != null) breakEvenOptimisationStageItemProvider.dispose();
		if (solutionBuilderSettingsItemProvider != null) solutionBuilderSettingsItemProvider.dispose();
		if (multipleSolutionSimilarityOptimisationStageItemProvider != null) multipleSolutionSimilarityOptimisationStageItemProvider.dispose();
	}

}
