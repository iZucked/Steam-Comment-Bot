/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

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

	public ContractProfileTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		this.eClass = object.eClass();

		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);
		//
		// boolean createSubProfileButtons = false;
		// boolean createSlotButtons = false;

		// Simple (single composite), or complex (top and a middle)
		boolean simpleComposite = false;

		int noCols = 1;
		String groupName = EditorUtils.unmangle(eClass.getName());
		if (object instanceof ContractProfile<?>) {
			groupName = "Profile";
			// createSubProfileButtons = false;
			noCols = 2;
		} else if (object instanceof SubContractProfile<?>) {
			// createSlotButtons = true;
			final SubContractProfile subProfile = (SubContractProfile) object;
			groupName = subProfile.getName();
			noCols = 3;
		}
		// final ToolBarManager removeButtonManager;
		// if (createSlotButtons) {
		// // Create a toolbar for remove buttons
		// removeButtonManager = new ToolBarManager(SWT.NO_BACKGROUND | SWT.RIGHT);
		// {
		// final ToolBar removeToolbar = removeButtonManager.createControl(this);
		//
		// // This code places the remove button in the top right of the display composite on top of the Group border.
		// GridData layoutData = new GridData();
		// // This tells the grid layout to ignore this control
		// layoutData.exclude = true;
		// removeToolbar.setLayoutData(layoutData);
		// // Make sure we are above the Group
		// removeToolbar.moveAbove(g);
		//
		// // This listener adjusts the control position to keep in the top right corner
		// this.addControlListener(new ControlListener() {
		//
		// @Override
		// public void controlResized(ControlEvent e) {
		// removeToolbar.setLocation(ContractProfileTopLevelComposite.this.getSize().x - removeToolbar.getSize().x, 0);
		// }
		//
		// @Override
		// public void controlMoved(ControlEvent e) {
		// removeToolbar.setLocation(ContractProfileTopLevelComposite.this.getSize().x - removeToolbar.getSize().x, 0);
		// }
		// });
		// }
		// } else {
		// removeButtonManager = null;
		// }
		if (groupName != null) {
			g.setText(groupName);
		}
		g.setLayout(new GridLayout(noCols, false));
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);

		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		// ToolBarManager addButtonManager = null;
		// if (createSubProfileButtons) {
		// addButtonManager = new ToolBarManager(SWT.RIGHT);
		// }
		if (!simpleComposite) {

			// Initialise middle composite
			middle = toolkit.createComposite(this);

			createChildComposites(root, object, eClass, middle);

			GridData layoutData2 = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
			// layoutData2.exclude = true;

			// if (addButtonManager != null) {
			// final ToolBar createControl2 = addButtonManager.createControl(middle);
			// createControl2.setLayoutData(layoutData2);
			// }

			// We know there are n slots, so n columns
			middle.setLayout(new GridLayout(childObjects.size() + 1, false));
			middle.setLayoutData(new GridData(GridData.FILL_BOTH));
			// middle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

			topLevel.display(dialogContext, root, object, range, dbc);

			final Iterator<IDisplayComposite> children = childComposites.iterator();
			final Iterator<EObject> childObjectsItr = childObjects.iterator();

			while (childObjectsItr.hasNext()) {
				children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
			}

			// Overrides default layout factory so we get a single column rather than multiple columns and one row
			this.setLayout(new GridLayout(1, false));

		} else {

			// if (addButtonManager != null) {
			// GridData layoutData2 = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
			// // layoutData2.exclude = true;
			// final ToolBar createControl2 = addButtonManager.createControl(topLevel.getComposite());
			// createControl2.setLayoutData(layoutData2);
			// }

			topLevel.display(dialogContext, root, object, range, dbc);
			this.setLayout(new GridLayout(2, false));

		}

		// if (createSubProfileButtons) {
		// assert addButtonManager != null;
		//
		// final Action action = new Action("Add new diversion entry") {
		//
		// @Override
		// public void run() {
		//
		// if (object instanceof ContractProfile<?>) {
		// final ContractProfile profile = (ContractProfile) object;
		//
		// final SubContractProfile<?> subProfile = ADPFactory.eINSTANCE.createSubContractProfile();
		//
		// final Command add = AddCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), profile, ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES, subProfile);
		//
		// commandHandler.handleCommand(add, profile, ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES);
		//
		// // Object tree has changed, request a relayout
		// dialogContext.getDialogController().relayout();
		// }
		// }
		// };
		// action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		// addButtonManager.add(action);
		//
		// }
		//
		// if (createSlotButtons && removeButtonManager != null) {
		//
		// final Action action = new Action("Remove sub profile") {
		// @Override
		// public void run() {
		//
		// if (object instanceof SubContractProfile) {
		// final SubContractProfile subProfile = (SubContractProfile) object;
		//
		// final Command remove = RemoveCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), subProfile.eContainer(),
		// ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES, subProfile);
		//
		// commandHandler.handleCommand(remove, subProfile.eContainer(), ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES);
		//
		// // Object tree has changed, request a relayout
		// dialogContext.getDialogController().relayout();
		// }
		//
		// }
		// };
		// action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		// removeButtonManager.add(action);
		//
		// }

		// if (addButtonManager != null) {
		// addButtonManager.update(true);
		// }
		// if (removeButtonManager != null) {
		// removeButtonManager.update(true);
		// }
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref) //
				|| ref == ADPPackage.eINSTANCE.getContractProfile_SubProfiles() //
				|| ref == ADPPackage.eINSTANCE.getSubContractProfile_CustomAttribs();
	}

	@Override
	protected void createChildComposites(final MMXRootObject root, final EObject object, final EClass eClass, final Composite parent) {
		for (final EReference ref : eClass.getEAllReferences()) {
			if (shouldDisplay(ref)) {
				if (ref.isMany()) {
					final List<?> values = (List<?>) object.eGet(ref);

					for (final Object o : values) {
						if (o instanceof EObject) {
							createChildArea(root, object, parent, ref, (EObject) o);
						}
					}

				} else {
					final EObject value = (EObject) object.eGet(ref);

					createChildArea(root, object, parent, ref, value);
				}
			}
		}

	}

	@Override
	protected IDisplayComposite createChildArea(final MMXRootObject root, final EObject object, final Composite parent, final EReference ref, final EObject value) {
		if (value != null) {
			final IDisplayComposite sub;
			if (value instanceof SubContractProfile<?>) {
				SubContractProfile<?> subContractProfile = (SubContractProfile<?>) value;
				sub = new ContractProfileTopLevelComposite(parent, SWT.NONE, dialogContext, toolkit);
			} else {
				final Group g2 = new Group(parent, SWT.NONE);
				toolkit.adapt(g2);
				g2.setText(EditorUtils.unmangle(ref.getName()));
				g2.setLayout(new FillLayout());
				g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));
				sub = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(value.eClass()).createSublevelComposite(g2, value.eClass(), dialogContext, toolkit);
			}
			sub.setCommandHandler(commandHandler);
			sub.setEditorWrapper(editorWrapper);

			childReferences.add(ref);
			childComposites.add(sub);
			childObjects.add(value);

			return sub;
		}
		return null;
	}

}
