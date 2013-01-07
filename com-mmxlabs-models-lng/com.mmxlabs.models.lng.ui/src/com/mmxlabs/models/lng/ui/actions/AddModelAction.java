/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * Action
 * 
 * @author hinton
 * 
 */
public final class AddModelAction {
	public interface IAddContext {
		public ICommandHandler getCommandHandler();

		public EObject getContainer();

		public EReference getContainment();

		public MMXRootObject getRootObject();

		public IScenarioEditingLocation getEditorPart();

		ISelection getCurrentSelection();
	}

	public final static Action create(final EClass eClass, final IAddContext context) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(eClass);

		if (factories.isEmpty())
			return null;
		else if (factories.size() == 1)
			return new SingleAddAction(factories.get(0), context);
		else
			return new MenuAddAction(factories, context);
	}
}

class SingleAddAction extends LockableAction {
	private final IAddContext context;
	private final IModelFactory factory;

	/**
	 * @param iModelFactory
	 * @param context
	 */
	public SingleAddAction(final IModelFactory factory, final IAddContext context) {
		super("Create new " + factory.getLabel(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.factory = factory;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final Collection<? extends ISetting> settings = factory.createInstance(context.getRootObject(), context.getContainer(), context.getContainment(), context.getCurrentSelection());
		if (settings.isEmpty() == false) {

			final CompoundCommand add = new CompoundCommand();
			for (final ISetting setting : settings) {
				add.append(AddCommand.create(context.getCommandHandler().getEditingDomain(), setting.getContainer(), setting.getContainment(), setting.getInstance()));
			}
			final CommandStack commandStack = context.getCommandHandler().getEditingDomain().getCommandStack();
			commandStack.execute(add);

			final DetailCompositeDialog editor = new DetailCompositeDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), context.getCommandHandler());

			final IScenarioEditingLocation editorPart = context.getEditorPart();
			final ScenarioLock editorLock = editorPart.getEditorLock();
			try {
				editorLock.claim();
				editorPart.setDisableUpdates(true);
				if (editor.open(editorPart, context.getRootObject(), Collections.singletonList(settings.iterator().next().getInstance())) != Window.OK) {
					// Revert state
					assert commandStack.getUndoCommand() == add;
					commandStack.undo();
				} else {
				}
			} finally {
				editorPart.setDisableUpdates(false);
				editorLock.release();
			}

		}
	}
}

class MenuAddAction extends AbstractMenuAction {
	private final List<IModelFactory> factories;
	private final IAddContext context;

	public MenuAddAction(final List<IModelFactory> factories, final IAddContext context) {
		super("Create new element");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.context = context;
		this.factories = factories;
	}

	@Override
	protected void populate(final Menu menu) {
		for (final IModelFactory factory : factories) {
			addActionToMenu(new SingleAddAction(factory, context), menu);
		}
	}
}
