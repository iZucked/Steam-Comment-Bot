/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FleetConstraint;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.ext.IFleetConstraintFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.HoverActionHelper;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.actions.RunnableAction;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class FleetProfileTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;
	private EClass eClass;
	private List<IFleetConstraintFactory> fleetConstraintFactories;
	private Collection<ServiceReference<IFleetConstraintFactory>> fleetConstraintFactoriesServiceReferences;

	public FleetProfileTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		final ImageDescriptor baseAdd = CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled);
		final ImageDescriptor image_grey_add = ImageDescriptor.createWithFlags(baseAdd, SWT.IMAGE_GRAY);

		this.eClass = object.eClass();
		initialiseFactories();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);

		int noCols = 1;
		String groupName = EditorUtils.unmangle(eClass.getName());
		// if (object instanceof ContractProfile<?>) {
		final FleetProfile fleetProfile = (FleetProfile) object;
		groupName = "Fleet";
		noCols = 1;
		// }
		if (groupName != null) {
			g.setText(groupName);
		}
		g.setLayout(new GridLayout(noCols, false));
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);
		topLevel.getComposite().setLayout(new GridLayout(1, false));
		setLayout(new GridLayout(1, false));

		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);
		{
			// Initialise middle composite
			middle = toolkit.createComposite(this);

			final int numChildren = createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, middle);

			// We know there are n slots, so n columns
			middle.setLayout(new GridLayout(numChildren + 2, false));
			middle.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		{
			// Initialise middle composite
			final Group constraintComposite = new Group(this, SWT.NONE);
			constraintComposite.setText("Constraints");
			toolkit.adapt(constraintComposite);
			constraintComposite.setLayout(new GridLayout(1, false));
			constraintComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
			{
				final Control addAction = HoverActionHelper.createAddAction(constraintComposite, helper -> {
					for (final IFleetConstraintFactory factory : fleetConstraintFactories) {

						{
							helper.addAction(new RunnableAction(factory.getName(), () -> {
								final FleetConstraint opt = factory.createInstance();
								final Command cmd = AddCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), fleetProfile, ADPPackage.eINSTANCE.getFleetProfile_Constraints(),
										opt);
								commandHandler.handleCommand(cmd, fleetProfile, ADPPackage.eINSTANCE.getFleetProfile_Constraints());
								dialogContext.getDialogController().rebuild(false);
							}));

						}
					}
				});
				addAction.setToolTipText("Create new constraint");
				toolkit.adapt(addAction, false, false);
			}

			for (final FleetConstraint constraint : fleetProfile.getConstraints()) {

				final Group g2 = new Group(constraintComposite, SWT.NONE);
				toolkit.adapt(g2);
				g2.setText(EditorUtils.unmangle(constraint));
				g2.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
				g2.setLayout(GridLayoutFactory.fillDefaults().create());

				final DefaultDetailComposite delegate1 = new DefaultDetailComposite(g2, 0, toolkit);
				delegate1.getComposite().setLayoutData(GridDataFactory.fillDefaults().create());
				delegate1.setCommandHandler(commandHandler);
				delegate1.display(dialogContext, root, constraint, range, dbc);
				{
					final DetailToolbarManager removeButtonManager = new DetailToolbarManager(g2, SWT.TOP);

					final Action action = new Action("Delete constraint") {
						@Override
						public void run() {
							final Command remove = RemoveCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), object, ADPPackage.Literals.FLEET_PROFILE__CONSTRAINTS,
									constraint);
							commandHandler.handleCommand(remove, object, ADPPackage.Literals.FLEET_PROFILE__CONSTRAINTS);
							dialogContext.getDialogController().rebuild(true);

						}
					};
					CommonImages.setImageDescriptors(action, IconPaths.Delete);
					removeButtonManager.getToolbarManager().add(action);
					removeButtonManager.getToolbarManager().update(true);
					toolkit.adapt(removeButtonManager.getToolbarManager().getControl());
				}

			}
		}
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref);// || true;
	}

	@Override
	protected IDisplayComposite createChildArea(final ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final EObject value) {
		String label;
		{
			label = EditorUtils.unmangle(ref.getName());
		}

		return createChildArea(childCompositeContainer, root, object, parent, ref, label, value);
	}

	private void initialiseFactories() {
		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();

		try {
			fleetConstraintFactoriesServiceReferences = bundleContext.getServiceReferences(IFleetConstraintFactory.class, null);
		} catch (final InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fleetConstraintFactories = new LinkedList<>();
		for (final ServiceReference<IFleetConstraintFactory> ref : fleetConstraintFactoriesServiceReferences) {
			fleetConstraintFactories.add(bundleContext.getService(ref));

		}

	}
}
