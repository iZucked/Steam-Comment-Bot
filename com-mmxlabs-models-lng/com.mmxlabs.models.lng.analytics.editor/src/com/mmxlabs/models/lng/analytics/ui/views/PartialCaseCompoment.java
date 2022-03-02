/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VesselEventOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VoyageOptionsDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.AbstractSandboxComponent;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.SandboxUIHelper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.PartialCaseContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;

public class PartialCaseCompoment extends AbstractSandboxComponent<OptionModellerView, OptionAnalysisModel> {

	private GridTreeViewer partialCaseViewer;

	private ExpandableComposite expandable;

	private SandboxUIHelper sandboxHelper;

	private OptionModellerView optionModellerView;

	protected PartialCaseCompoment(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final OptionModellerView optionModellerView) {
		this.optionModellerView = optionModellerView;
		sandboxHelper = new SandboxUIHelper(parent);
		{
			expandable = wrapInExpandable(parent, "Options", this::createPartialCaseViewer, expandableCompo -> {

				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final PartialCaseDropTargetListener listener = new PartialCaseDropTargetListener(scenarioEditingLocation, partialCaseViewer);
				inputWants.add(listener::setOptionAnalysisModel);
				final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE | DND.DROP_LINK);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
			}, true);
			expandable.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 300).grab(false, true).create());

			hookOpenEditor(partialCaseViewer);

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final PartialCaseDropTargetListener listener = new PartialCaseDropTargetListener(scenarioEditingLocation, partialCaseViewer);
			inputWants.add(listener::setOptionAnalysisModel);
			partialCaseViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_LINK, types, listener);
			expandable.setExpanded(expanded);

			expandable.addExpansionListener(expansionListener);
		}
	}

	private Control createPartialCaseViewer(final Composite parent) {
		partialCaseViewer = new GridTreeViewer(parent, SWT.NONE | SWT.WRAP);
		ColumnViewerToolTipSupport.enableFor(partialCaseViewer);

		GridViewerHelper.configureLookAndFeel(partialCaseViewer);
		partialCaseViewer.getGrid().setHeaderVisible(true);
		partialCaseViewer.getGrid().setCellSelectionEnabled(true);

		partialCaseViewer.getGrid().setAutoHeight(true);
		partialCaseViewer.getGrid().setRowHeaderVisible(true);

		{
			final GridViewerColumn gvc = new GridViewerColumn(partialCaseViewer, SWT.CENTER | SWT.WRAP);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("Buy/Event");
			gvc.getColumn().setWidth(250);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.getColumn().setWordWrap(true);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(final ViewerCell cell) {

					final GridItem item = (GridItem) cell.getItem();
					item.setHeaderText("");
					item.setHeaderImage(null);
					cell.setBackground(null);

					final Object element = cell.getElement();

					final Set<Object> targetElements = getTargetElementsForWiringProvider(element);
					final IStatus s = sandboxHelper.getWorstStatus(targetElements, validationErrors);
					sandboxHelper.updateGridItem(cell, s);
					cell.setImage(sandboxHelper.getValidationImageForStatus(s));

					if (element instanceof BaseCaseRow || element instanceof PartialCaseRow) {
						if (validationErrors.containsKey(element)) {
							final IStatus status = validationErrors.get(element);
							sandboxHelper.updateGridHeaderItem(cell, status);
						}
					}

					if (cell.getElement() instanceof final PartialCaseRow row) {
						String lbl = "";
						if (!row.getBuyOptions().isEmpty()) {
							lbl = new BuyOptionDescriptionFormatter().render(row.getBuyOptions());
						}
						if (!row.getVesselEventOptions().isEmpty()) {
							if (!lbl.isEmpty()) {
								lbl += "\n";
							}
							lbl += new VesselEventOptionDescriptionFormatter().render(row.getVesselEventOptions());
						}
						cell.setText(lbl);
					} else {
						cell.setText("");
					}
				}

				@Override
				public String getToolTipText(final Object element) {
					// Should really be "Buy/Event" in the name field
					final Set<Object> targetElements = getTargetElementsForLabelProvider("Buy", element);

					final StringBuilder sb = new StringBuilder();
					sandboxHelper.extractValidationMessages(sb, targetElements, validationErrors);

					if (sb.length() > 0) {
						return sb.toString();
					}
					return super.getToolTipText(element);
				}
			});

		}
		PartialCaseWiringDiagram partialCaseDiagram;
		{
			final GridViewerColumn gvc = new GridViewerColumn(partialCaseViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringColumnLabelProvider());
			partialCaseDiagram = new PartialCaseWiringDiagram(partialCaseViewer.getGrid(), gvc);
		}

		createColumn(partialCaseViewer, "Sell", new SellOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS).getColumn().setWordWrap(true);
		createColumn(partialCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING).getColumn().setWordWrap(true);
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TEMP_SANDBOX_VOYAGE_OPTIONS)) {
			GridViewerColumn voyageOptsCol = createColumn(partialCaseViewer, "Options", new VoyageOptionsDescriptionFormatter(), false, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__OPTIONS);
			voyageOptsCol.getColumn().setWordWrap(true);
			// 122 is precalculated width of the rendered image. 8px is padding
			voyageOptsCol.getColumn().setWidth(122 + 8);
		}
		partialCaseViewer.setContentProvider(new PartialCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final PartialCaseContextMenuManager listener = new PartialCaseContextMenuManager(partialCaseViewer, scenarioEditingLocation, mgr);
		partialCaseViewer.getGrid().addMenuDetectListener(listener);
		inputWants.add(listener::setOptionAnalysisModel);

		inputWants.add(partialCaseViewer::setInput);
		inputWants.add(partialCaseDiagram::setRoot);

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(partialCaseViewer.getGrid(), grid -> grid.setEnabled(!locked)));

		hookDragSource(partialCaseViewer);

		{
			final Action deleteAction = new Action("Delete") {
				@Override
				public void run() {
					if (partialCaseViewer.getSelection() instanceof final IStructuredSelection selection) {

						final Collection<EObject> c = new LinkedList<>();
						selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));
						if (!c.isEmpty()) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c), null, null);
						}
					}
				}
			};
			partialCaseViewer.getControl().addFocusListener(new FocusListener() {

				@Override
				public void focusLost(final FocusEvent e) {
					optionModellerView.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
				}

				@Override
				public void focusGained(final FocusEvent e) {
					optionModellerView.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
				}
			});

		}

		return partialCaseViewer.getGrid();
	}

	@Override
	public void refresh() {
		partialCaseViewer.refresh();
		GridViewerHelper.recalculateRowHeights(partialCaseViewer.getGrid());
	}

	public void setVisible(final boolean visible) {
		expandable.setVisible(visible);
		((GridData) expandable.getLayoutData()).exclude = !visible;

	}
}
