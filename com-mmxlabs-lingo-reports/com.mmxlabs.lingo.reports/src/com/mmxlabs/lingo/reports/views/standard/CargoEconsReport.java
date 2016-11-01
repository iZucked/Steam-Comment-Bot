/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import javax.annotation.PreDestroy;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IElementCollector;

import com.mmxlabs.common.options.Options;
import com.mmxlabs.lingo.reports.views.standard.StandardEconsRowFactory.EconsOptions.MarginBy;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.application.BindSelectionListener;

/**
 * The {@link CargoEconsReport} is a vertical report similar in concept to the Properties View. This table is the transpose of most other tables. Columns represent the input data and rows are
 * pre-defined.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoEconsReport extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport";

	private CargoEconsReportComponent component;

	private IEclipseContext componentContext;

	@Override
	public void createPartControl(final Composite parent) {
		final IEclipseContext ctx = getSite().getService(IEclipseContext.class);
		componentContext = ctx.createChild();
		componentContext.set(Composite.class, parent);

		component = ContextInjectionFactory.make(CargoEconsReportComponent.class, componentContext);

		// componentContext.set(String.class, (String) null);
		ContextInjectionFactory.invoke(component, BindSelectionListener.class, componentContext);

		// component.listenToSelectionsFrom(null);

		GridTableViewer viewer = component.getViewer();

		{
			final Action volumeByPurchase = new RunnableAction("Margin by purchase volume", () -> {
				IEclipseContext actionCtx = EclipseContextFactory.create();
				actionCtx.set(MarginBy.class, MarginBy.PURCHASE_VOLUME);
				ContextInjectionFactory.invoke(component, SetEconsMarginMode.class, componentContext, actionCtx, null);
			});
			getViewSite().getActionBars().getMenuManager().add(volumeByPurchase);

			final Action volumeBySell = new RunnableAction("Margin by sales volume", () -> {
				IEclipseContext actionCtx = EclipseContextFactory.create();
				actionCtx.set(MarginBy.class, MarginBy.SALE_VOLUME);
				ContextInjectionFactory.invoke(component, SetEconsMarginMode.class, componentContext, actionCtx, null);
			});
			getViewSite().getActionBars().getMenuManager().add(volumeBySell);
		}

		final Action packAction = PackActionFactory.createPackColumnsAction(viewer);
		final Action copyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(viewer);
		getViewSite().getActionBars().getToolBarManager().add(packAction);
		getViewSite().getActionBars().getToolBarManager().add(copyAction);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

	}

	@PreDestroy
	public void dispose() {
		if (component != null) {
			ContextInjectionFactory.invoke(component, PreDestroy.class, componentContext);
			component = null;
		}
		if (componentContext != null) {
			componentContext.dispose();
			componentContext = null;
		}
	}

	@Focus
	public void setFocus() {
		ContextInjectionFactory.invoke(component, Focus.class, componentContext);
	}

}
