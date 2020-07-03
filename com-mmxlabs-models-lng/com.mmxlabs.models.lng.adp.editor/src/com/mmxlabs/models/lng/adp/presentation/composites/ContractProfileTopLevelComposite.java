/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
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

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.CustomSubProfileAttributes;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileConstraintFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.HoverActionHelper;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.rcp.common.actions.RunnableAction;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ContractProfileTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;
	private EClass eClass;
	private List<IProfileConstraintFactory> profileConstraintFactories;
	private Collection<ServiceReference<IProfileConstraintFactory>> profileConstraintFactoriesServiceReferences;
	private final List<IDisplayComposite> extraDislpayComposites = new LinkedList<>();

	public ContractProfileTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		final ImageDescriptor baseAdd = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD);
		final ImageDescriptor image_grey_add = ImageDescriptor.createWithFlags(baseAdd, SWT.IMAGE_GRAY);

		this.eClass = object.eClass();
		initialiseFactories();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);

		int noCols = 1;
		String groupName = EditorUtils.unmangle(object);
		// if (object instanceof ContractProfile<?>) {
		final ContractProfile<?, ?> contractProfile = (ContractProfile<?, ?>) object;
		groupName = null;// "Contract";
		noCols = 1;
		// }
		if (groupName != null) {
			g.setText(groupName);
		}
		g.setLayout(new GridLayout(noCols, false));
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);

		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);

		{
			// Initialise middle composite
			final Group constraintComposite = new Group(this, SWT.NONE);
			constraintComposite.setText("Contract constraints");
			toolkit.adapt(constraintComposite);
			constraintComposite.setLayout(new GridLayout(1, false));
			constraintComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
			{
				final Control addAction = HoverActionHelper.createAddAction(constraintComposite, helper -> {

					final List<RunnableAction> actions = new LinkedList<>();
					for (final IProfileConstraintFactory factory : profileConstraintFactories) {
						if (factory.validFor(contractProfile))

						{
							actions.add(new RunnableAction(factory.getName(), () -> {
								final ProfileConstraint opt = factory.createInstance();
								final Command cmd = AddCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), contractProfile,
										ADPPackage.eINSTANCE.getContractProfile_Constraints(), opt);
								commandHandler.handleCommand(cmd, contractProfile, ADPPackage.eINSTANCE.getContractProfile_Constraints());
								dialogContext.getDialogController().rebuild(false);
							}));

						}
					}

					actions.stream() //
							.sorted((a, b) -> a.getText().compareTo(b.getText())) //
							.forEach(a -> helper.addAction(a));

				});
				addAction.setToolTipText("Create new constraint");
				toolkit.adapt(addAction, false, false);
			}

			for (final ProfileConstraint constraint : contractProfile.getConstraints()) {

				final Group g2 = new Group(constraintComposite, SWT.NONE);
				toolkit.adapt(g2);
				g2.setText(EditorUtils.unmangle(constraint));
				g2.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
				g2.setLayout(GridLayoutFactory.fillDefaults().create());

				final IDisplayCompositeFactory displayCompositeFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(constraint.eClass());
				final IDisplayComposite delegate1 = displayCompositeFactory.createSublevelComposite(g2, constraint.eClass(), dialogContext, toolkit);
				delegate1.getComposite().setLayoutData(GridDataFactory.fillDefaults().create());
				delegate1.setCommandHandler(commandHandler);
				delegate1.display(dialogContext, root, constraint, range, dbc);
				{
					final DetailToolbarManager removeButtonManager = new DetailToolbarManager(g2, SWT.TOP);

					final Action action = new Action("Delete constraint") {
						@Override
						public void run() {
							final Command remove = RemoveCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), object, ADPPackage.Literals.CONTRACT_PROFILE__CONSTRAINTS,
									constraint);
							commandHandler.handleCommand(remove, object, ADPPackage.Literals.CONTRACT_PROFILE__CONSTRAINTS);
							dialogContext.getDialogController().rebuild(true);

						}
					};
					action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
					removeButtonManager.getToolbarManager().add(action);
					removeButtonManager.getToolbarManager().update(true);
					toolkit.adapt(removeButtonManager.getToolbarManager().getControl());

					extraDislpayComposites.add(delegate1);
				}

			}
		}

		{
			// Initialise middle composite
			middle = toolkit.createComposite(this);

			final int numChildren = createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, middle);

			boolean displayAddButtons = false;

			// We know there are n slots, so n columns
			middle.setLayout(new GridLayout(numChildren + (displayAddButtons ? 2 : 0), false));
			middle.setLayoutData(new GridData(GridData.FILL_BOTH));
			if (displayAddButtons) { // Create a toolbar for remove buttons
				final DetailToolbarManager addButtonManager = new DetailToolbarManager(middle, SWT.RIGHT);
				toolkit.adapt(addButtonManager.getToolbarManager().getControl());
				final Action action = new Action("New volume allocation") {
					@Override
					public void run() {

						final SubContractProfile<?, ?> p2 = ADPFactory.eINSTANCE.createSubContractProfile();
						final Command remove = AddCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), object, ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES, p2);
						commandHandler.handleCommand(remove, object, ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES);
						// Object tree has changed, request a relayout
						dialogContext.getDialogController().rebuild(true);

					}

				};
				action.setHoverImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
				action.setImageDescriptor(image_grey_add);

				addButtonManager.getToolbarManager().add(action);
				addButtonManager.getToolbarManager().update(true);

			}

		}
		// Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(1, false));
		this.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref) //
				|| ref == ADPPackage.eINSTANCE.getContractProfile_SubProfiles() //
				|| ref == ADPPackage.eINSTANCE.getSubContractProfile_CustomAttribs();
	}

	@Override
	protected IDisplayComposite createChildArea(final ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final EObject value) {
		String label;
		if (value instanceof CustomSubProfileAttributes) {
			label = "Options";
		} else if (value instanceof ProfileConstraint) {
			label = EditorUtils.unmangle(value.eClass());
		} else {
			label = EditorUtils.unmangle(ref.getName());
		}
		if (value instanceof SubContractProfile<?, ?>) {
			final IDisplayComposite sub = new SubContractProfileTopLevelComposite(parent, SWT.NONE, dialogContext, toolkit);
			sub.setCommandHandler(commandHandler);
			sub.setEditorWrapper(editorWrapper);
			childCompositeContainer.childReferences.add(ref);
			childCompositeContainer.childComposites.add(sub);
			childCompositeContainer.childObjects.add(value);

			return sub;
		}
		return createChildArea(childCompositeContainer, root, object, parent, ref, label, value);
	}

	private void initialiseFactories() {
		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();

		try {
			profileConstraintFactoriesServiceReferences = bundleContext.getServiceReferences(IProfileConstraintFactory.class, null);
		} catch (final InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		profileConstraintFactories = new LinkedList<>();
		for (final ServiceReference<IProfileConstraintFactory> ref : profileConstraintFactoriesServiceReferences) {
			profileConstraintFactories.add(bundleContext.getService(ref));

		}
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		super.displayValidationStatus(status);
		for (final IDisplayComposite d : extraDislpayComposites) {
			d.displayValidationStatus(status);
		}
	}
}
