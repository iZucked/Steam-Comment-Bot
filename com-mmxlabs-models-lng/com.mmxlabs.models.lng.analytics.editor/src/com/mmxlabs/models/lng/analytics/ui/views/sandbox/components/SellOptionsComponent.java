/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.OptionMenuHelper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.OptionsViewerContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;

public class SellOptionsComponent extends AbstractSandboxComponent<Object, AbstractAnalysisModel> {

	private GridTreeViewer sellOptionsViewer;
	private MenuManager mgr;

	public SellOptionsComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull Supplier<AbstractAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, Object optionModellerView) {
		ExpandableComposite expandable = wrapInExpandable(parent, "Sells", p -> createSellOptionsViewer(p), expandableComposite -> {

			{
				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final SellsDropTargetListener listener = new SellsDropTargetListener(scenarioEditingLocation, sellOptionsViewer);
				inputWants.add(model -> listener.setModel(model));
				// Control control = getControl();
				final DropTarget dropTarget = new DropTarget(expandableComposite, DND.DROP_MOVE);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
				// expandableComposite.addDropSupport(DND.DROP_MOVE, types, listener);
			}

			final Label addSellButton = new Label(expandableComposite, SWT.NONE);
			addSellButton.setImage(sandboxUIHelper.image_grey_add);
			expandableComposite.setTextClient(addSellButton);
			addSellButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).grab(true, false).create());
			addSellButton.addMouseTrackListener(new MouseTrackListener() {

				@Override
				public void mouseHover(final MouseEvent e) {

				}

				@Override
				public void mouseExit(final MouseEvent e) {
					addSellButton.setImage(sandboxUIHelper.image_grey_add);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					addSellButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
				}
			});
			addSellButton.addMouseListener(OptionMenuHelper.createNewSellOptionMenuListener(addSellButton.getParent(), scenarioEditingLocation, modelProvider));

		}, false);
		sellOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 400).create());

		hookDragSource(sellOptionsViewer);
		expandable.setExpanded(expanded);

		expandable.addExpansionListener(expansionListener);
	}

	private Control createSellOptionsViewer(final Composite sellComposite) {

		sellOptionsViewer = new GridTreeViewer(sellComposite, SWT.NONE | SWT.MULTI);
		ColumnViewerToolTipSupport.enableFor(sellOptionsViewer);

		GridViewerHelper.configureLookAndFeel(sellOptionsViewer);
		sellOptionsViewer.getGrid().setHeaderVisible(false);

		CellFormatterLabelProvider labelProvider = new BuysSellsLabelProvider(sandboxUIHelper, new SellOptionDescriptionFormatter(), validationErrors, "Sell");
		createColumn(sellOptionsViewer, labelProvider, "Sell", new SellOptionDescriptionFormatter(), false);

		sellOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS));
		hookOpenEditor(sellOptionsViewer);
		{
			mgr = new MenuManager();
			final SellOptionsContextMenuManager listener = new SellOptionsContextMenuManager(sellOptionsViewer, scenarioEditingLocation, mgr);
			inputWants.add(model -> listener.setModel(model));
			sellOptionsViewer.getGrid().addMenuDetectListener(listener);
		}
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final SellsDropTargetListener listener = new SellsDropTargetListener(scenarioEditingLocation, sellOptionsViewer);
			inputWants.add(model -> listener.setModel(model));
			sellOptionsViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		}
		inputWants.add(model -> sellOptionsViewer.setInput(model));

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(sellOptionsViewer.getGrid(), grid -> grid.setEnabled(!locked)));

		return sellOptionsViewer.getControl();
	}

	@Override
	public void refresh() {
		sellOptionsViewer.refresh();
	}

	@Override
	public List<Consumer<AbstractAnalysisModel>> getInputWants() {
		return inputWants;
	}

	@Override
	public void dispose() {
		mgr.dispose();
		super.dispose();
	}
}
