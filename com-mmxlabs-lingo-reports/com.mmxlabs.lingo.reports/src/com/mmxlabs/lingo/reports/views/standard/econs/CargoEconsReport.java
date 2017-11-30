/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.views.standard.econs.StandardEconsRowFactory.EconsOptions.MarginBy;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
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

			final Action showOnlyDiff = new RunnableAction("Δ", () -> {
				component.toggleShowDiffOnly();
				component.rebuild();
			});
			getViewSite().getActionBars().getToolBarManager().add(showOnlyDiff);
		
			
		}

		Runnable preOperation = () -> {
			component.setIncludedUnit(false);
			ViewerHelper.refresh(component.getViewer(), true);
		};

		Runnable postOperation = () -> {
			component.setIncludedUnit(true);
			ViewerHelper.refresh(component.getViewer(), true);
		};

		final Action packAction = PackActionFactory.createPackColumnsAction(viewer);
		final Action copyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(viewer, preOperation, postOperation);
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

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (component instanceof IAdaptable) {
			T result = ((IAdaptable) component).getAdapter(adapter);
			if (result != null) {
				return result;
			}
		}

		return super.getAdapter(adapter);
	}

}
