/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.FreezeDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.OptioniseDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VesselEventOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VoyageOptionsDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.AbstractSandboxComponent;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.BaseCaseContentProvider;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;

public class BaseCaseComponent extends AbstractSandboxComponent<OptionModellerView, OptionAnalysisModel> {

	private GridTreeViewer baseCaseViewer;
	private BaseCaseWiringDiagram baseCaseDiagram;
	private GridViewerColumn optioniseCol;
//	private GridViewerColumn freezeCol;
	private GridViewerColumn optionsCol;

	protected BaseCaseComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final OptionModellerView optionModellerView) {
		final ExpandableComposite expandable = wrapInExpandable(parent, "Starting point", this::createBaseCaseViewer, expandableCompo -> {

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final BaseCaseDropTargetListener listener = new BaseCaseDropTargetListener(scenarioEditingLocation, baseCaseViewer);
			inputWants.add(listener::setOptionAnalysisModel);
			final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE | DND.DROP_LINK);
			dropTarget.setTransfer(types);
			dropTarget.addDropListener(listener);
		}, false);
		expandable.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 200).grab(false, true).create());
		hookOpenEditor(baseCaseViewer);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		final BaseCaseDropTargetListener listener = new BaseCaseDropTargetListener(scenarioEditingLocation, baseCaseViewer);
		inputWants.add(listener::setOptionAnalysisModel);
		baseCaseViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_LINK, types, listener);
		expandable.setExpanded(expanded);

		expandable.addExpansionListener(expansionListener);
	}

	private Control createBaseCaseViewer(final Composite parent) {
		baseCaseViewer = new GridTreeViewer(parent, SWT.NONE | SWT.SINGLE);
		ColumnViewerToolTipSupport.enableFor(baseCaseViewer);

		GridViewerHelper.configureLookAndFeel(baseCaseViewer);
		baseCaseViewer.getGrid().setHeaderVisible(true);
		baseCaseViewer.getGrid().setRowHeaderVisible(true);

		// Buy col
		{
			final GridViewerColumn gvc = new GridViewerColumn(baseCaseViewer, SWT.CENTER | SWT.WRAP);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("Buy/Event");
			gvc.getColumn().setWidth(250);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {
					final DataVisualizer dv = baseCaseViewer.getGrid().getDataVisualizer();
					final GridItem item = (GridItem) cell.getItem();
					// Reset any colouring
					item.setBackground(null);
					final int i = cell.getColumnIndex();
					dv.setColumnSpan(item, i, 1);

					if (cell.getElement() instanceof BaseCaseRow baseCaseRow) {
						if (baseCaseRow.getVesselEventOption() != null) {
							dv.setColumnSpan(item, i, 2);
							cell.setText(new VesselEventOptionDescriptionFormatter().render(baseCaseRow.getVesselEventOption()));
						} else {
							cell.setText(new BuyOptionDescriptionFormatter().render(baseCaseRow.getBuyOption()));
						}
					} else {
						cell.setText("");
					}
				}
			});
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(baseCaseViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringColumnLabelProvider());
			this.baseCaseDiagram = new BaseCaseWiringDiagram(baseCaseViewer.getGrid(), gvc);
		}

		createColumn(baseCaseViewer, "Sell", new SellOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
		createColumn(baseCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

		optioniseCol = createColumn(baseCaseViewer, "", new OptioniseDescriptionFormatter(), false);
		optioniseCol.getColumn().setAlignment(SWT.CENTER);
		CommonImages.setImage(optioniseCol.getColumn(), IconPaths.Play_16);
		
		optioniseCol.getColumn().setHeaderTooltip("Select rows to use of optionise targets. Other rows will be included in search scope");
		optioniseCol.getColumn().setWidth(30);
		optioniseCol.getColumn().setVisible(false);

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TEMP_SANDBOX_VOYAGE_OPTIONS)) {
			// Mode specific columns
			optionsCol = createColumn(baseCaseViewer, "Options", new VoyageOptionsDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__OPTIONS);

			// freezeCol = createColumn(baseCaseViewer, "Lock", new
			// FreezeDescriptionFormatter(), false);
			// freezeCol.getColumn().setWidth(40);

			// Hide by default
			optionsCol.getColumn().setVisible(false);
			// freezeCol.getColumn().setVisible(false);
		}

		baseCaseViewer.getGrid().setCellSelectionEnabled(true);

		baseCaseViewer.setContentProvider(new BaseCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final BaseCaseContextMenuManager listener = new BaseCaseContextMenuManager(baseCaseViewer, scenarioEditingLocation, mgr);
		baseCaseViewer.getGrid().addMenuDetectListener(listener);

		inputWants.add(listener::setOptionAnalysisModel);
		inputWants.add(baseCaseViewer::setInput);
		inputWants.add(baseCaseDiagram::setRoot);

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(baseCaseViewer.getGrid(), grid -> grid.setEnabled(!locked)));

		return baseCaseViewer.getGrid();
	}

	public void setMode(int mode) {

		optioniseCol.getColumn().setVisible(mode == SandboxModeConstants.MODE_OPTIONISE);

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TEMP_SANDBOX_VOYAGE_OPTIONS)) {
			if (mode == SandboxModeConstants.MODE_DERIVE) {
				optionsCol.getColumn().setVisible(true);
				// freezeCol.getColumn().setVisible(false);
			} else if (mode == SandboxModeConstants.MODE_OPTIMISE) {
				optionsCol.getColumn().setVisible(false);
				// freezeCol.getColumn().setVisible(true);
			} else if (mode == SandboxModeConstants.MODE_OPTIONISE) {
				optionsCol.getColumn().setVisible(false);
				// freezeCol.getColumn().setVisible(true);
			} else {
				optionsCol.getColumn().setVisible(false);
				// freezeCol.getColumn().setVisible(false);
			}
		}
	}

	@Override
	public void refresh() {
		baseCaseViewer.refresh();
	}
}
