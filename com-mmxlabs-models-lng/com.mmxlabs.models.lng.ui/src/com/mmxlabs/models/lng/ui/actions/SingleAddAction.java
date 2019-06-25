/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.util.Collection;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class SingleAddAction extends LockableAction {
	protected final IAddContext context;
	protected final IModelFactory factory;
	protected final Viewer viewer;
	
	public SingleAddAction(final IModelFactory factory, final IAddContext context) {
		this(factory, context, null);
	}
	
	/**
	 * @param iModelFactory
	 * @param context
	 * @param viewer
	 */
	public SingleAddAction(final IModelFactory factory, final IAddContext context, @Nullable Viewer viewer) {
		super(factory.getLabel(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		setToolTipText("Create new " + factory.getLabel());
		this.factory = factory;
		this.context = context;
		this.viewer = viewer;
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
			EObject obj = settings.iterator().next().getInstance();
			int ret = DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(context.getEditorPart(), obj, commandStack.getMostRecentCommand());				
			if (ret == Window.OK) {
				viewer.setSelection(new StructuredSelection(obj));
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