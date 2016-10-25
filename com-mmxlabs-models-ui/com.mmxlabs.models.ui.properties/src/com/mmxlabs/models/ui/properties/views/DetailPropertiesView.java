/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.views;

import javax.annotation.PreDestroy;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.application.BindSelectionListener;

public abstract class DetailPropertiesView<T extends DetailPropertiesViewComponent> extends ViewPart {
	private Action packColumnsAction;

	protected T component;
	protected IEclipseContext componentContext;

	private Options options;

	protected DetailPropertiesView(Options options) {
		this.options = options;
	}

	public DetailPropertiesView(String category, String helpContextId, boolean showUnitsInSeparateColumn) {
		this(new Options(category, helpContextId, showUnitsInSeparateColumn));
	}

	protected Class<T> getComponentClass() {
		return (Class<T>) DetailPropertiesViewComponent.class;
	}

	@Override
	public void createPartControl(final Composite parent) {

		final IEclipseContext ctx = getSite().getService(IEclipseContext.class);
		componentContext = ctx.createChild();
		componentContext.set(Composite.class, parent);
		componentContext.set(Options.class, options);

		component = ContextInjectionFactory.make(getComponentClass(), componentContext);
		ContextInjectionFactory.invoke(component, BindSelectionListener.class, componentContext);
		

		GridTreeViewer viewer = component.getViewer();

		makeActions(viewer);
	}

	protected void makeActions(GridTreeViewer viewer) {
		packColumnsAction = PackActionFactory.createPackColumnsAction(viewer);
		final Action copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());

		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);
		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	@Override
	public void dispose() {
		ContextInjectionFactory.invoke(component, PreDestroy.class, componentContext);
		super.dispose();
	}

	@Override
	public void setFocus() {
		ContextInjectionFactory.invoke(component, Focus.class, componentContext);
	}
}
