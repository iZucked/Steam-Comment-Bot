/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.CustomSubProfileAttributes;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SubProfileConstraint;
import com.mmxlabs.models.lng.adp.ext.IDistributionModelFactory;
import com.mmxlabs.models.lng.adp.ext.ISubProfileConstraintFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.HoverActionHelper;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ValidationStatusWrapper;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.models.ui.impl.FeatureFilteringDetailDisplayComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class SubContractProfileTopLevelComposite extends DefaultTopLevelComposite {

	private ComboViewer distributionModelSelector;
	private Collection<ServiceReference<IDistributionModelFactory>> distributionFactoriesServiceReferences;
	private List<IDistributionModelFactory> distributionFactories;
	protected FeatureFilteringDetailDisplayComposite mainFeatures = null;
	protected FeatureFilteringDetailDisplayComposite windowFeatures = null;
	protected FeatureFilteringDetailDisplayComposite volumeFeatures = null;
	protected FeatureFilteringDetailDisplayComposite contractCodeLevel = null;

	// private Group constraintComposite;
	// private EClass eClass;
	private SubContractProfile<?, ?> contractProfile;
	private List<ISubProfileConstraintFactory> subProfileConstraintFactories;
	private Collection<ServiceReference<ISubProfileConstraintFactory>> subProfileConstraintFactoriesServiceReferences;

	public SubContractProfileTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
		addDisposeListener(e -> removeAdapter());
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		if (contractProfile != null) {
			contractProfile.eAdapters().remove(adapter);
			contractProfile = null;
		}

		initialiseFactories();

		// Grab this now, but set adapters etc later on
		if (object instanceof SubContractProfile<?, ?>) {
			contractProfile = (SubContractProfile<?, ?>) object;
		}

		{
			contractCodeLevel = new FeatureFilteringDetailDisplayComposite(this, SWT.NONE, toolkit);
			contractCodeLevel.includeFeature(ADPPackage.Literals.CONTRACT_PROFILE__CONTRACT_CODE);

			contractCodeLevel.setCommandHandler(commandHandler);
			contractCodeLevel.setEditorWrapper(editorWrapper);

			contractCodeLevel.display(dialogContext, root, contractProfile.eContainer(), range, dbc);
		}
		{
			mainFeatures = new FeatureFilteringDetailDisplayComposite(this, SWT.NONE, toolkit);
			mainFeatures.excludeFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE);
			mainFeatures.excludeFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS);

			mainFeatures.setLayoutProvider(new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONTRACT_TYPE) //
					.withFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL) //
					.makeRow() //
					.make() //
			);

			mainFeatures.setCommandHandler(commandHandler);
			mainFeatures.setEditorWrapper(editorWrapper);

			mainFeatures.display(dialogContext, root, object, range, dbc);
		}
		{
			windowFeatures = new FeatureFilteringDetailDisplayComposite(this, SWT.NONE, toolkit);
			windowFeatures.setLayoutProvider(new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withLabel("Window") //
					.withFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE) //
					.withFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS) //
					.makeRow() //
					.make() //
			);

			windowFeatures.includeFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE);
			windowFeatures.includeFeature(ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS);

			windowFeatures.setCommandHandler(commandHandler);
			windowFeatures.setEditorWrapper(editorWrapper);

			windowFeatures.display(dialogContext, root, object, range, dbc);
		}
		{

			volumeFeatures = new FeatureFilteringDetailDisplayComposite(this, SWT.NONE, toolkit) {
				@Override
				protected void initialize(EClass eClass) {
					// Override the default eClass to look up the specific super class we want as this is explicitly excluded now in the sub-classes.
					super.initialize(ADPPackage.eINSTANCE.getDistributionModel());
				}
			};
			volumeFeatures.setLayoutProvider(new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withLabel("Cargo volume") //
					.withFeature(ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_PER_CARGO) //
					.withFeature(ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_UNIT) //
					.makeRow() //
					.make()//
			);

			volumeFeatures.includeFeature(ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_PER_CARGO);
			volumeFeatures.includeFeature(ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_UNIT);

			volumeFeatures.setCommandHandler(commandHandler);
			volumeFeatures.setEditorWrapper(editorWrapper);

			volumeFeatures.display(dialogContext, root, ((SubContractProfile) object).getDistributionModel(), range, dbc);
		}
		
		setMargins(contractCodeLevel);
		setMargins(mainFeatures);
		setMargins(windowFeatures);
		setMargins(volumeFeatures);
	 
		{
			// Initialise child composites
			createChildOtherCompsiteSection(dialogContext, root, object, range, dbc, object.eClass(), this);
		}
		{
			Composite distributionComposite = this;

			{
				final Composite selectorComposite = toolkit.createComposite(distributionComposite);
				selectorComposite.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).margins(5, 0).create());

				toolkit.createLabel(selectorComposite, "Model:");
				distributionModelSelector = new ComboViewer(selectorComposite, SWT.DROP_DOWN);
				distributionModelSelector.setContentProvider(new ArrayContentProvider());
				distributionModelSelector.setLabelProvider(new DistributionModelFactoryLabelProvider());

				distributionModelValidationWrapper = ValidationStatusWrapper.createValidationDecorator(distributionModelSelector.getControl(), object,
						ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL);
				distributionModelSelector.getCombo().addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final ISelection selection = distributionModelSelector.getSelection();
						if (selection.isEmpty()) {
							return;
						}
						if (selection instanceof IStructuredSelection) {
							final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
							final Object firstElement = iStructuredSelection.getFirstElement();
							if (firstElement instanceof IDistributionModelFactory) {
								final IDistributionModelFactory factory = (IDistributionModelFactory) firstElement;
								final DistributionModel distributionModel = factory.createInstance();
								final Command command = SetCommand.create(getCommandHandler().getEditingDomain(), contractProfile, ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL,
										distributionModel);

								// This should trigger the adapter to re-display the content
								getCommandHandler().handleCommand(command, contractProfile, ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL);
							}
						}
					}
				});
			}
			final int numChildren = createChildDistributionCompsiteSection(dialogContext, root, object, range, dbc, object.eClass(), this);

			// We know there are n slots, so n columns
			distributionComposite.setLayout(new GridLayout(1, false));
			distributionComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

			setMargins(distributionComposite);
		}
		{

			// Initialise middle composite
			Group constraintComposite = new Group(this, SWT.NONE);
			constraintComposite.setText("Generation constraints");
			toolkit.adapt(constraintComposite);

			// final int numChildren =
			createChildConstraintsCompsiteSection(dialogContext, root, object, range, dbc, object.eClass(), constraintComposite);

			constraintComposite.setLayout(new GridLayout(1, false));
			constraintComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			// Create new constraint action
			{
				final Control addAction = HoverActionHelper.createAddAction(constraintComposite, helper -> {
					for (final ISubProfileConstraintFactory factory : subProfileConstraintFactories) {
						if (factory.validFor((ContractProfile<?, ?>) contractProfile.eContainer(), contractProfile)) {
							helper.addAction(new RunnableAction(factory.getName(), () -> {
								final SubProfileConstraint opt = factory.createInstance();
								final Command cmd = AddCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), contractProfile,
										ADPPackage.eINSTANCE.getSubContractProfile_Constraints(), opt);
								commandHandler.handleCommand(cmd, contractProfile, ADPPackage.eINSTANCE.getSubContractProfile_Constraints());
								dialogContext.getDialogController().rebuild(false);
							}));

						}
					}
				});
				addAction.setToolTipText("Add new constraint");
				toolkit.adapt(addAction, false, false);

			}
		}

		// Overrides default layout factory so we get a single column rather than multiple columns and one row
		// Set the spacing to zero as we have many child composites
		this.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 0).create());
		this.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (object instanceof SubContractProfile<?, ?>) {
			contractProfile = (SubContractProfile<?, ?>) object;
			distributionModelSelector.setInput(getFactoriesFor(contractProfile));

			final IDistributionModelFactory currentFactory = getCurrentFactory(contractProfile);
			if (currentFactory != null) {
				distributionModelSelector.setSelection(new StructuredSelection(currentFactory));
			}

			contractProfile.eAdapters().add(adapter);
		}
	}

	protected int createChildOtherCompsiteSection(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range,
			final EMFDataBindingContext dbc, final EClass eClass, final Composite parent) {
		final ChildCompositeContainer childContainer = new ChildCompositeContainer();
		for (final EReference ref : eClass.getEAllReferences()) {
			if (ref == ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS) {
				continue;
			}
			if (ref == ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL) {
				continue;
			}

			if (shouldDisplay(ref)) {
				if (ref.isMany()) {
					final List<?> values = (List<?>) object.eGet(ref);
					for (final Object o : values) {
						if (o instanceof EObject) {
							createChildArea(childContainer, root, object, parent, ref, (EObject) o);
						}
					}
				} else {
					final EObject value = (EObject) object.eGet(ref);
					createChildArea(childContainer, root, object, parent, ref, value);
				}
			}
		}

		final Iterator<IDisplayComposite> children = childContainer.childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childContainer.childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		childCompositeContainers.add(childContainer);
		return childContainer.childComposites.size();
	}

	protected int createChildConstraintsCompsiteSection(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range,
			final EMFDataBindingContext dbc, final EClass eClass, final Composite parent) {
		final ChildCompositeContainer childContainer = new ChildCompositeContainer();
		if (object instanceof SubContractProfile<?, ?>) {
			final SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>) object;
			for (final SubProfileConstraint constraint : subContractProfile.getConstraints()) {
				final String label = EditorUtils.unmangle(constraint);
				final IDisplayComposite c = createChildArea(childContainer, root, object, parent, ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS, label, constraint, (g) -> { // Create a toolbar
																																														// for
					// remove buttons
					final DetailToolbarManager removeButtonManager = new DetailToolbarManager(g, SWT.RIGHT);

					final Action action = new Action("Delete") {

						@Override
						public void run() {

							final Command remove = RemoveCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), subContractProfile,
									ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS, constraint);
							commandHandler.handleCommand(remove, subContractProfile, ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS);
							dialogContext.getDialogController().rebuild(true);

						}
					};
					CommonImages.setImageDescriptors(action, IconPaths.Delete);
					removeButtonManager.getToolbarManager().add(action);
					removeButtonManager.getToolbarManager().update(true);
				});
			}
		}

		final Iterator<IDisplayComposite> children = childContainer.childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childContainer.childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		childCompositeContainers.add(childContainer);
		return childContainer.childComposites.size();
	}

	protected int createChildDistributionCompsiteSection(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range,
			final EMFDataBindingContext dbc, final EClass eClass, final Composite parent) {
		final ChildCompositeContainer childContainer = new ChildCompositeContainer();
		if (object instanceof SubContractProfile<?, ?>) {
			final SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>) object;
			final DistributionModel distributionModel = subContractProfile.getDistributionModel();
			if (distributionModel != null) {
				createChildArea(childContainer, root, object, parent, ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, distributionModel);
			}
		}

		final Iterator<IDisplayComposite> children = childContainer.childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childContainer.childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		childCompositeContainers.add(childContainer);
		return childContainer.childComposites.size();
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref) //
				|| ref == ADPPackage.eINSTANCE.getSubContractProfile_CustomAttribs();
	}

	@Override
	protected IDisplayComposite createChildArea(final ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final EObject value) {
		final String label;
		if (value instanceof CustomSubProfileAttributes) {
			label = "Options";
		} else {
			label = EditorUtils.unmangle(ref.getName());
		}
		return createChildArea(childCompositeContainer, root, object, parent, ref, label, value);
	}

	private void initialiseFactories() {
		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		try {
			distributionFactoriesServiceReferences = bundleContext.getServiceReferences(IDistributionModelFactory.class, null);
		} catch (final InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		distributionFactories = new LinkedList<>();
		for (final ServiceReference<IDistributionModelFactory> ref : distributionFactoriesServiceReferences) {
			distributionFactories.add(bundleContext.getService(ref));

		}

		try {
			subProfileConstraintFactoriesServiceReferences = bundleContext.getServiceReferences(ISubProfileConstraintFactory.class, null);
		} catch (final InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		subProfileConstraintFactories = new LinkedList<>();
		for (final ServiceReference<ISubProfileConstraintFactory> ref : subProfileConstraintFactoriesServiceReferences) {
			subProfileConstraintFactories.add(bundleContext.getService(ref));

		}

	}

	void removeAdapter() {
		if (oldValue != null) {
			oldValue.eAdapters().remove(adapter);
			oldValue = null;
		}
	}

	EObject oldValue = null;
	final Adapter adapter = new SafeAdapterImpl() {
		@Override
		public void safeNotifyChanged(final Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			if (notification.getFeature() == ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL) {
				if (!isDisposed() && isVisible()) {
					if (distributionModelSelector != null && distributionModelSelector.getCombo().isDisposed() == false) {

						if (notification.getNotifier() instanceof SubContractProfile<?, ?>) {
							final SubContractProfile<?, ?> profile = (SubContractProfile<?, ?>) notification.getNotifier();
							distributionModelSelector.setInput(getFactoriesFor(profile));
							distributionModelSelector.setSelection(new StructuredSelection(getCurrentFactory(profile)));
						}

						distributionModelSelector.refresh();
						dialogContext.getDialogController().relayout();

					}
				} else {
					SubContractProfileTopLevelComposite.this.removeAdapter();
				}
			}
		}

	};
	private ValidationStatusWrapper distributionModelValidationWrapper;

	@Override
	public void dispose() {
		super.dispose();

		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		distributionFactories.clear();
		for (final ServiceReference<IDistributionModelFactory> ref : distributionFactoriesServiceReferences) {
			bundleContext.ungetService(ref);
		}
		subProfileConstraintFactories.clear();
		for (final ServiceReference<ISubProfileConstraintFactory> ref : subProfileConstraintFactoriesServiceReferences) {
			bundleContext.ungetService(ref);

		}
	}

	private IDistributionModelFactory getCurrentFactory(final SubContractProfile<?, ?> profile) {
		if (profile == null) {
			return null;
		}
		for (final IDistributionModelFactory ft : distributionFactories) {
			if (ft.isMatchForCurrent(profile)) {
				return ft;
			}
		}
		return null;
	}

	private List<IDistributionModelFactory> getFactoriesFor(final SubContractProfile<?, ?> profile) {

		final List<IDistributionModelFactory> l = new LinkedList<>();
		if (profile != null) {
			for (final IDistributionModelFactory ft : distributionFactories) {
				if (ft.validFor(profile)) {
					l.add(ft);
				}
			}
		}
		return l;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {

		if (mainFeatures != null) {
			mainFeatures.displayValidationStatus(status);
		}

		if (windowFeatures != null) {
			windowFeatures.displayValidationStatus(status);
		}

		if (contractCodeLevel != null) {
			contractCodeLevel.displayValidationStatus(status);
		}

		if (volumeFeatures != null) {
			volumeFeatures.displayValidationStatus(status);
		}

		super.displayValidationStatus(status);
		if (distributionModelValidationWrapper != null) {
			distributionModelValidationWrapper.processValidation(status);
		}
	}

	private void setMargins(Composite composite) {

		GridLayout newLayout = GridLayoutFactory.createFrom((GridLayout) composite.getLayout()) //
				.extendedMargins(0, 0, 0, 0) //
				.margins(5, 2) //
				.create();

		composite.setLayout(newLayout);

	}
}
