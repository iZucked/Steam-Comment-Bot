/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView;
import com.mmxlabs.models.lng.analytics.ui.views.ResultsSetDeletionHelper;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.ShippingOptionsContentProvider;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class ShippingOptionsComponent extends AbstractSandboxComponent<Object, AbstractAnalysisModel> {

	private GridTreeViewer shippingOptionsViewer;
	private MenuManager mgr;
	private Object optionModellerView;

	public ShippingOptionsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull final Supplier<AbstractAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final Object optionModellerView) {
		this.optionModellerView = optionModellerView;
		final ExpandableComposite expandableShipping = wrapInExpandable(parent, "Shipping", p -> createShippingOptionsViewer(p).getGrid(), expandableCompo -> {

			{
				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final ShippingOptionsDropTargetListener listener = new ShippingOptionsDropTargetListener(scenarioEditingLocation, shippingOptionsViewer);
				inputWants.add(model -> listener.setModel(model));
				final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_LINK);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
			}

			final Label addShipping = new Label(expandableCompo, SWT.NONE);
			expandableCompo.setTextClient(addShipping);
			addShipping.setImage(sandboxUIHelper.image_grey_add);

			addShipping.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
			addShipping.addMouseTrackListener(new MouseTrackAdapter() {

				@Override
				public void mouseExit(final MouseEvent e) {
					addShipping.setImage(sandboxUIHelper.image_grey_add);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					addShipping.setImage(sandboxUIHelper.image_add);
				}
			});

			addShipping.addMouseListener(new MouseAdapter() {

				LocalMenuHelper helper = new LocalMenuHelper(addShipping.getParent());

				void buildMenu() {
					boolean portfolioMode = false;
					{
						final AbstractAnalysisModel abstractAnalysisModel = modelProvider.get();
						if (abstractAnalysisModel instanceof final OptionAnalysisModel optionAnalysisModel) {
							portfolioMode = optionAnalysisModel.getBaseCase().isKeepExistingScenario();
						}

					}
					if (!portfolioMode) {
						helper.addAction(new RunnableAction("Nomination", () -> {
							final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();

							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
									modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

							DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
						}));
					}
					helper.addAction(new RunnableAction("Round trip", () -> {
						final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
								modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
					}));
					if (!portfolioMode) {
						helper.addAction(new RunnableAction("Simple Charter", () -> {
							final SimpleVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createOptionalSimpleVesselCharterOption();
							AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, opt);
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
									modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

							DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
						}));
						if (true) {
							helper.addAction(new RunnableAction("Full Charter", () -> {
								final FullVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createFullVesselCharterOption();
								final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
								vesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
								vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());
								opt.setVesselCharter(vesselCharter);
								AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, opt);
								scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
										AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
										modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

								DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
							}));
						}
					}
					if (portfolioMode) {
						helper.addAction(new RunnableAction("Existing Charter", () -> {
							final ExistingVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
							// opt.setVesselCharter(CargoFactory.eINSTANCE.createVesselCharter());
							// AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, opt);
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
									modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

							DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
						}));
					}
					if (portfolioMode) {
						helper.addAction(new RunnableAction("Existing Market Charter", () -> {
							final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
							final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
							opt.setCharterInMarket(market);
							opt.setSpotIndex(-1);
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
									modelProvider.get(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

							DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
						}));
					}
				}

				@Override
				public void mouseDown(final MouseEvent e) {
					if (modelProvider.get() != null) {
						helper.clearActions();
						buildMenu();
						helper.open();
					}
				}

			});
		}, false);

		expandableShipping.addExpansionListener(expansionListener);
		expandableShipping.setExpanded(expanded);

		// Failed attempt to set a minimum size on the table
		shippingOptionsViewer.getGrid().setLayoutData(GridDataFactory.swtDefaults().hint(SWT.DEFAULT, 150).create());

	}

	private GridTreeViewer createShippingOptionsViewer(final Composite vesselComposite) {

		shippingOptionsViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(shippingOptionsViewer);

		shippingOptionsViewer.getGrid().setHeaderVisible(false);
		// Temporary pending nebula grid bug fix
		createColumn_TempForNebulaBugFix(shippingOptionsViewer, "Templates", new ShippingOptionDescriptionFormatter(), false);
		shippingOptionsViewer.setContentProvider(new ShippingOptionsContentProvider(scenarioEditingLocation));
		hookOpenEditor(shippingOptionsViewer);

		{
			final Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final ShippingOptionsDropTargetListener listener = new ShippingOptionsDropTargetListener(scenarioEditingLocation, shippingOptionsViewer);
			shippingOptionsViewer.addDropSupport(DND.DROP_MOVE, transferTypes, listener);
			inputWants.add(listener::setModel);
		}

		mgr = new MenuManager();
		final ShippingOptionsContextMenuManager listener = new ShippingOptionsContextMenuManager(shippingOptionsViewer, scenarioEditingLocation, mgr);
		shippingOptionsViewer.getGrid().addMenuDetectListener(listener);

		hookDragSource(shippingOptionsViewer);

		inputWants.add(shippingOptionsViewer::setInput);
		inputWants.add(listener::setModel);
		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(shippingOptionsViewer.getGrid(), grid -> grid.setEnabled(!locked)));

		if (optionModellerView instanceof final OptionModellerView view) {
			final Action deleteAction = new Action("Delete") {
				@Override
				public void run() {

					if (shippingOptionsViewer.getSelection() instanceof final IStructuredSelection selection) {

						final Collection<EObject> c = new LinkedList<>();
						selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));

						final CompoundCommand compoundCommand = new CompoundCommand("Delete sell option");
						if (!c.isEmpty()) {
							compoundCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c));
						}
						if (shippingOptionsViewer.getInput() instanceof final OptionAnalysisModel model) {
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
			shippingOptionsViewer.getControl().addFocusListener(new FocusListener() {

				@Override
				public void focusLost(final FocusEvent e) {
					view.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
				}

				@Override
				public void focusGained(final FocusEvent e) {
					view.getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);

				}
			});
		}

		return shippingOptionsViewer;
	}

	@Override
	public void refresh() {
		shippingOptionsViewer.refresh();
	}

	@Override
	public void dispose() {
		mgr.dispose();
		super.dispose();
	}

}
