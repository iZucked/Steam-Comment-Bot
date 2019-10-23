/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.pnlcalcs;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToExcelMSClipboardAction;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.application.BindSelectionListener;

/**
 * The {@link PNLCalcsReport} is a vertical report similar in concept to the Properties View. This table is the transpose of most other tables. Columns represent the input data and rows are
 * pre-defined.
 * 
 * @author Simon Goodall
 * 
 */
public class PNLCalcsReport extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.views.PNLConsReport";

	private PNLCalcsReportComponent component;

	private IEclipseContext componentContext;

	@Override
	public void createPartControl(final Composite parent) {
		final IEclipseContext ctx = getSite().getService(IEclipseContext.class);
		componentContext = ctx.createChild();
		componentContext.set(Composite.class, parent);

		component = ContextInjectionFactory.make(PNLCalcsReportComponent.class, componentContext);

		ContextInjectionFactory.invoke(component, BindSelectionListener.class, componentContext);

		final GridTableViewer viewer = component.getViewer();

		{
			final Action showOnlyDiff = new RunnableAction("Δ", () -> {
				if (component!= null) {
					component.toggleShowDiffOnly();
					component.rebuild();
				}
			});
			getViewSite().getActionBars().getToolBarManager().add(showOnlyDiff);

		}

		final Runnable preOperation = () -> {
			component.setIncludedUnit(false);
			ViewerHelper.refresh(component.getViewer(), true);
		};

		final Runnable postOperation = () -> {
			component.setIncludedUnit(true);
			ViewerHelper.refresh(component.getViewer(), true);
		};

		final Action packAction = PackActionFactory.createPackColumnsAction(viewer);
		final CopyGridToExcelMSClipboardAction copyAction = new CopyGridToExcelMSClipboardAction(viewer.getGrid(), false, preOperation, postOperation);
		copyAction.setAdditionalAttributeProvider(new IAdditionalAttributeProvider() {

			@Override
			public Object getTypedValue(final GridItem item, final int i) {
				final String text = item.getText(i);
				try {
					return DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter().parse(text);
				} catch (final Exception e) {

				}
				try {
					return NumberFormat.getInstance().parseObject(text);
				} catch (final Exception e) {

				}
				return null;
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
			final T result = component.getAdapter(adapter);
			if (result != null) {
				return result;
			}
		}

		return super.getAdapter(adapter);
	}

}
