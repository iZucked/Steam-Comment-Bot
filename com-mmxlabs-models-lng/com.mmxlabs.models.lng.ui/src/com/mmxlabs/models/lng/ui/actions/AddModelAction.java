/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
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

	/**
	 */
	public final static Action create(final List<Pair<EClass, IAddContext>> items, final Action[] additionalActions) {

		final List<Pair<IModelFactory, IAddContext>> factoryPairs = new LinkedList<>();
		for (final Pair<EClass, IAddContext> p : items) {
			final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(p.getFirst());
			for (final IModelFactory f : factories) {
				factoryPairs.add(new Pair<>(f, p.getSecond()));
			}
		}
		return new MultiAddContextAction(factoryPairs, additionalActions);
	}

	/**
	 */
	public final static Action create(final EClass eClass, final IAddContext context, final Action[] additionalActions) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(eClass);

		if (factories.isEmpty())
			return null;
		else
			return new MenuAddAction(factories, context, additionalActions);

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
		super(factory.getLabel(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		setToolTipText("Create new " + factory.getLabel());
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
				add.append(AddCommand.create(context.getCommandHandler().getEditingDomain(), setting.getContainer(), setting.getContainment(), setting.getInstance(), getInsertionIndex(setting)));
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

	/**
	 * Overridable method to customise {@link AddCommand} insertion index location. Default is {@link CommandParameter#NO_INDEX}
	 * 
	 * @param setting
	 * @return
	 */
	protected int getInsertionIndex(final ISetting setting) {
		return CommandParameter.NO_INDEX;
	}
}

class MenuAddAction extends AbstractMenuAction {
	private final List<IModelFactory> factories;
	private final IAddContext context;
	private final Action[] additionalActions;

	public MenuAddAction(final List<IModelFactory> factories, final IAddContext context) {
		this(factories, context, new Action[0]);
	}

	public MenuAddAction(final List<IModelFactory> factories, final IAddContext context, final Action[] additionalActions) {
		super("Create new element");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.context = context;
		this.factories = factories;
		this.additionalActions = additionalActions;
	}

	@Override
	protected void populate(final Menu menu) {
		for (final IModelFactory factory : factories) {
			addActionToMenu(new SingleAddAction(factory, context), menu);
		}
		for (final Action action : additionalActions) {
			addActionToMenu(action, menu);
		}
	}
}

class MultiAddContextAction extends AbstractMenuAction {
	private final List<Pair<IModelFactory, IAddContext>> factories;
	private final Action[] additionalActions;

	public MultiAddContextAction(final List<Pair<IModelFactory, IAddContext>> factories) {
		this(factories, new Action[0]);
	}

	public MultiAddContextAction(final List<Pair<IModelFactory, IAddContext>> factories, final Action[] additionalActions) {
		super("Create new element");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.factories = factories;
		this.additionalActions = additionalActions;
	}

	@Override
	protected void populate(final Menu menu) {
		for (final Pair<IModelFactory, IAddContext> p : factories) {
			addActionToMenu(new SingleAddAction(p.getFirst(), p.getSecond()), menu);
		}
		for (final Action action : additionalActions) {
			addActionToMenu(action, menu);
		}
	}
}