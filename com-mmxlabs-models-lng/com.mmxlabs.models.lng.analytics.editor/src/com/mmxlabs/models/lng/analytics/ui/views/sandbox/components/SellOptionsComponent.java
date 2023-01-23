/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView;
import com.mmxlabs.models.lng.analytics.ui.views.ResultsSetDeletionHelper;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.OptionMenuHelper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.OptionsViewerContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class SellOptionsComponent extends AbstractSandboxComponent<Object, AbstractAnalysisModel> {

	private GridTreeViewer sellOptionsViewer;
	private MenuManager mgr;
	private Object optionModellerView;

	public SellOptionsComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull Supplier<AbstractAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, Object optionModellerView) {
		this.optionModellerView = optionModellerView;
		ExpandableComposite expandable = wrapInExpandable(parent, "Sells", this::createSellOptionsViewer, expandableComposite -> {
			{
				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final SellsDropTargetListener listener = new SellsDropTargetListener(scenarioEditingLocation, sellOptionsViewer);
				inputWants.add(listener::setModel);
				final DropTarget dropTarget = new DropTarget(expandableComposite, DND.DROP_MOVE);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
			}

			final Label addSellButton = new Label(expandableComposite, SWT.NONE);
			addSellButton.setImage(sandboxUIHelper.image_grey_add);
			expandableComposite.setTextClient(addSellButton);
			addSellButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).grab(true, false).create());
			addSellButton.addMouseTrackListener(new MouseTrackAdapter() {

				@Override
				public void mouseExit(final MouseEvent e) {
					addSellButton.setImage(sandboxUIHelper.image_grey_add);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					addSellButton.setImage(sandboxUIHelper.image_add);
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
		// Temporary pending nebula grid bug fix
		createColumn_TempForNebulaBugFix(sellOptionsViewer, labelProvider, "Sell", false);

		sellOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS));
		hookOpenEditor(sellOptionsViewer);
		{
			mgr = new MenuManager();
			final SellOptionsContextMenuManager listener = new SellOptionsContextMenuManager(sellOptionsViewer, scenarioEditingLocation, mgr);
			inputWants.add(listener::setModel);
			sellOptionsViewer.getGrid().addMenuDetectListener(listener);
		}
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final SellsDropTargetListener listener = new SellsDropTargetListener(scenarioEditingLocation, sellOptionsViewer);
			inputWants.add(listener::setModel);
			sellOptionsViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		}
		inputWants.add(sellOptionsViewer::setInput);

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(sellOptionsViewer.getGrid(), grid -> grid.setEnabled(!locked)));

		if (optionModellerView instanceof OptionModellerView view) {
			Action deleteAction = new Action("Delete") {
				@Override
				public void run() {
					final Collection<EObject> c = new LinkedList<>();

					if (sellOptionsViewer.getSelection() instanceof IStructuredSelection selection) {

						selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));

						final CompoundCommand compoundCommand = new CompoundCommand("Delete sell option");
						if (!c.isEmpty()) {
							compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c));
						}
						if (sellOptionsViewer.getInput() instanceof OptionAnalysisModel model) {
							final Collection<EObject> linkedResults = ResultsSetDeletionHelper.getRelatedResultSets(c, model);
							if (!linkedResults.isEmpty()) {
								compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), linkedResults));
							}
						}
						if (!compoundCommand.isEmpty()) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(compoundCommand, null, null);
						}
					}
				}
			};
			sellOptionsViewer.getControl().addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					view.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
				}

				@Override
				public void focusGained(FocusEvent e) {
					view.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
				}
			});

		}

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
