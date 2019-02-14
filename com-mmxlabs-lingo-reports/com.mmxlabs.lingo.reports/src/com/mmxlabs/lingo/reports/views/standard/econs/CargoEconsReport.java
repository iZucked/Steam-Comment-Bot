/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.views.standard.econs.EconsOptions.MarginBy;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToExcelMSClipboardAction;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
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

		final GridTableViewer viewer = component.getViewer();

		{
			final Map<MarginBy, Action> actionMap = new EnumMap<>(MarginBy.class);
			final Consumer<MarginBy> changeSelectedAction = mode -> {
				for (final Map.Entry<MarginBy, Action> e : actionMap.entrySet()) {
					e.getValue().setChecked(e.getKey() == mode);
				}
			};
			final Action volumeByPurchase = new RunnableAction("Per mmBtu by purchase volume", SWT.TOGGLE, () -> {
				final IEclipseContext actionCtx = EclipseContextFactory.create();
				actionCtx.set(MarginBy.class, MarginBy.PURCHASE_VOLUME);
				ContextInjectionFactory.invoke(component, SetEconsMarginMode.class, componentContext, actionCtx, null);
				changeSelectedAction.accept(MarginBy.PURCHASE_VOLUME);
			});
			getViewSite().getActionBars().getMenuManager().add(volumeByPurchase);

			final Action volumeBySell = new RunnableAction("Per mmBtu by sales volume", SWT.TOGGLE, () -> {
				final IEclipseContext actionCtx = EclipseContextFactory.create();
				actionCtx.set(MarginBy.class, MarginBy.SALE_VOLUME);
				ContextInjectionFactory.invoke(component, SetEconsMarginMode.class, componentContext, actionCtx, null);
				changeSelectedAction.accept(MarginBy.SALE_VOLUME);
			});
			actionMap.put(MarginBy.PURCHASE_VOLUME, volumeByPurchase);
			actionMap.put(MarginBy.SALE_VOLUME, volumeBySell);

			getViewSite().getActionBars().getMenuManager().add(volumeBySell);

			final Action showOnlyDiff = new RunnableAction("Δ", () -> {
				component.toggleShowDiffOnly();
				component.rebuild();
			});
			getViewSite().getActionBars().getToolBarManager().add(showOnlyDiff);

			// Default to sales volume
			volumeBySell.run();
		}

		final Runnable preOperation = () -> {
			component.setCopyPasteMode(true);
			ViewerHelper.refresh(component.getViewer(), true);
		};

		final Runnable postOperation = () -> {
			component.setCopyPasteMode(false);
			ViewerHelper.refresh(component.getViewer(), true);
		};

		final Action packAction = PackActionFactory.createPackColumnsAction(viewer);
		final CopyGridToExcelMSClipboardAction copyAction = new CopyGridToExcelMSClipboardAction(viewer.getGrid(), true, preOperation, postOperation);
		copyAction.setAdditionalAttributeProvider(new IAdditionalAttributeProvider() {

			@Override
			public Object getTypedValue(final GridItem item, final int i) {
				final String text = item.getText(i);
				try {
					return NumberFormat.getInstance().parseObject(text);
				} catch (final Exception e) {

				}
				return String.class;
			}

			@Override
			public @NonNull String getTopLeftCellUpperText() {
				return "";
			}

			@Override
			public @NonNull String getTopLeftCellText() {
				return "";
			}

			@Override
			public @NonNull String getTopLeftCellLowerText() {
				return "";
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalRowHeaderAttributes(@NonNull final GridItem item) {
				return null;
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalPreRows() {
				return null;
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalHeaderAttributes(final GridColumn column) {
				return null;
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalAttributes(@NonNull final GridItem item, final int columnIdx) {

				return null;
			}

			@Override
			public int getBorders(final GridItem item, final int i) {
				return 0;
			}
		});
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
			final T result = ((IAdaptable) component).getAdapter(adapter);
			if (result != null) {
				return result;
			}
		}

		return super.getAdapter(adapter);
	}

}
