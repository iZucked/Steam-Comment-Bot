/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.rcp.common.actions.AbstractMenuLockableAction;

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

		@Nullable
		Collection<@NonNull EObject> getCurrentSelection();
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

	public final static Action create(final EClass eClass, final IAddContext context, final Action[] additionalActions) {
		return create(eClass, context, additionalActions, null);
	}
	
	/**
	 */
	public final static Action create(final EClass eClass, final IAddContext context, final Action[] additionalActions, Viewer viewer) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(eClass);

		if (factories.isEmpty())
			return null;
		else
			return new MenuAddAction(factories, context, additionalActions, viewer);

	}
}

class MenuAddAction extends AbstractMenuLockableAction {
	private final List<IModelFactory> factories;
	private final IAddContext context;
	private final Action[] additionalActions;
	private Viewer viewer = null;
	
	public MenuAddAction(final List<IModelFactory> factories, final IAddContext context) {
		this(factories, context, new Action[0]);
	}

	public MenuAddAction(final List<IModelFactory> factories, final IAddContext context, final Action[] additionalActions) {
		this(factories, context, additionalActions, null);
	}
	
	public MenuAddAction(final List<IModelFactory> factories, final IAddContext context, final Action[] additionalActions, Viewer viewer) {
		super("Create new element");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.context = context;
		this.factories = factories;
		this.additionalActions = additionalActions;
		this.viewer = viewer;
	}

	@Override
	protected void populate(final Menu menu) {
		for (final IModelFactory factory : factories) {
			addActionToMenu(new SingleAddAction(factory, context, viewer), menu);
		}
		for (final Action action : additionalActions) {
			addActionToMenu(action, menu);
		}
	}
}

class MultiAddContextAction extends AbstractMenuLockableAction {
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