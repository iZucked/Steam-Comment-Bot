/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SpotMarketsProfile;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.TextualEnumAttributeManipulator;
import com.mmxlabs.rcp.common.actions.AbstractMenuLockableAction;

public class MarketsPage extends ADPComposite {

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private EmbeddedDetailComposite detailComposite;
	private Runnable releaseAdaptersRunnable = null;

	private ScenarioTableViewer previewViewer;

	private Group previewGroup;

	private Action deleteSlotAction;

	public MarketsPage(final Composite parent, int style, ADPEditorData editorData) {
		super(parent, style);
		this.editorData = editorData;
		this.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(0, 0).create());

		// Top Toolbar
		{
			final Composite toolbarComposite = new Composite(this, SWT.NONE);
			toolbarComposite.setLayout(GridLayoutFactory.fillDefaults() //
					.numColumns(5) //
					.equalWidth(false) //
					.create());

			if (false) {
				final Button btn = new Button(toolbarComposite, SWT.PUSH);
				btn.setText("Re-generate markets");
				btn.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						if (editorData.scenarioModel == null) {
							return;
						}
						ADPModel adpModel = editorData.adpModel;
						if (adpModel == null) {
							return;
						}
						CargoModel cargoModel = ScenarioModelUtil.getCargoModel(editorData.scenarioModel);
						EObject input = detailComposite.getInput();
						if (input instanceof SpotMarketsProfile) {
							SpotMarketsProfile spotMarketsProfile = (SpotMarketsProfile) input;

							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP markets");

							editorData.getDefaultCommandHandler().handleCommand(cmd, spotMarketsProfile, null);
						}
						updateDetailPaneInput(input);
					}
				});
			}
		}

		mainComposite = new SashForm(this, SWT.HORIZONTAL);
		mainComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY));
		mainComposite.setSashWidth(5);

		mainComposite.setLayoutData(GridDataFactory.fillDefaults()//
				.grab(true, true)//
				// .align(SWT.CENTER, SWT.TOP)//
				// .span(1, 1) //
				.create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(true) //
				.numColumns(2) //
				.spacing(0, 0) //
				.create());

		{
			detailComposite = new EmbeddedDetailComposite(mainComposite, editorData);
		}
		{

			rhsScrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.V_SCROLL);
			rhsScrolledComposite.setLayoutData(GridDataFactory.fillDefaults()//
					.grab(false, true)//
					.hint(200, SWT.DEFAULT) //
					// .span(1, 1) //
					.align(SWT.FILL, SWT.FILL).create());

			rhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			rhsScrolledComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			rhsScrolledComposite.setExpandHorizontal(true);
			rhsScrolledComposite.setExpandVertical(true);
			// lhsScrolledComposite.setMinSize(400, 400);
			rhsComposite = new Composite(rhsScrolledComposite, SWT.NONE);
			// centralComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			// centralComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setContent(rhsComposite);

			rhsComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			{
				// Preview Table with generated options
				{

					previewGroup = new Group(rhsComposite, SWT.NONE);
					previewGroup.setLayout(new GridLayout(1, false));
					previewGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));

					previewGroup.setText("Preview");
					// toolkit.adapt(previewGroup);

					if (editorData.getEditingDomain() != null) {
						// constructPreviewViewer(editorData, previewGroup);
					}

					final DetailToolbarManager buttonManager = new DetailToolbarManager(previewGroup, SWT.TOP);

					{
						final AbstractMenuLockableAction menu = new AbstractMenuLockableAction("New") {
							@Override
							public void runWithEvent(Event e) {
								final IMenuCreator mc = getMenuCreator();
								if (mc != null) {
									final Menu m = mc.getMenu(buttonManager.getToolbarManager().getControl());
									if (m != null) {
										// position the menu below the drop down item
										final Point point = buttonManager.getToolbarManager().getControl().toDisplay(new Point(e.x, e.y));
										m.setLocation(point.x, point.y);
										m.setVisible(true);
										return;
									}
								}
							}

							@Override
							protected void populate(final Menu menu) {

								{
									final Action action = new Action("FOB Purchase") {

										@Override
										public void run() {
											SpotMarket market = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
											market.setEnabled(true);
											market.setPriceInfo(CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters());
											CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
											if (commercialModel.getEntities().size() == 1) {
												market.setEntity(commercialModel.getEntities().get(0));
											}
											Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getAdpModel().getSpotMarketsProfile(),
													ADPPackage.Literals.SPOT_MARKETS_PROFILE__SPOT_MARKETS, market);
											editorData.getEditingDomain().getCommandStack().execute(c);
											DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, market, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
											if (previewViewer != null) {
												previewViewer.refresh();
											}
										}
									};
									final ActionContributionItem aci = new ActionContributionItem(action);
									aci.fill(menu, -1);
								}
								{
									final Action action = new Action("DES Purchase") {

										@Override
										public void run() {
											SpotMarket market = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
											market.setEnabled(true);
											market.setPriceInfo(CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters());
											CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
											if (commercialModel.getEntities().size() == 1) {
												market.setEntity(commercialModel.getEntities().get(0));
											}
											Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getAdpModel().getSpotMarketsProfile(),
													ADPPackage.Literals.SPOT_MARKETS_PROFILE__SPOT_MARKETS, market);
											editorData.getEditingDomain().getCommandStack().execute(c);
											DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, market, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
											if (previewViewer != null) {
												previewViewer.refresh();
											}
										}
									};
									final ActionContributionItem aci = new ActionContributionItem(action);
									aci.fill(menu, -1);
								}
								{
									final Action action = new Action("DES Sale") {

										@Override
										public void run() {
											SpotMarket market = SpotMarketsFactory.eINSTANCE.createDESSalesMarket();
											market.setEnabled(true);
											market.setPriceInfo(CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters());
											CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
											if (commercialModel.getEntities().size() == 1) {
												market.setEntity(commercialModel.getEntities().get(0));
											}
											Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getAdpModel().getSpotMarketsProfile(),
													ADPPackage.Literals.SPOT_MARKETS_PROFILE__SPOT_MARKETS, market);
											editorData.getEditingDomain().getCommandStack().execute(c);
											DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, market, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
											if (previewViewer != null) {
												previewViewer.refresh();
											}
										}
									};
									final ActionContributionItem aci = new ActionContributionItem(action);
									aci.fill(menu, -1);
								}
								{
									final Action action = new Action("FOB Sale") {

										@Override
										public void run() {
											SpotMarket market = SpotMarketsFactory.eINSTANCE.createFOBSalesMarket();
											market.setEnabled(true);
											market.setPriceInfo(CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters());
											CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
											if (commercialModel.getEntities().size() == 1) {
												market.setEntity(commercialModel.getEntities().get(0));
											}
											Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getAdpModel().getSpotMarketsProfile(),
													ADPPackage.Literals.SPOT_MARKETS_PROFILE__SPOT_MARKETS, market);
											editorData.getEditingDomain().getCommandStack().execute(c);
											DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, market, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
											if (previewViewer != null) {
												previewViewer.refresh();
											}
										}
									};
									final ActionContributionItem aci = new ActionContributionItem(action);
									aci.fill(menu, -1);
								}
							}
						};
						menu.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
						buttonManager.getToolbarManager().add(menu);
					}

					{

						deleteSlotAction = new Action("Delete") {
							@Override
							public void run() {
								if (previewViewer != null) {
									ISelection selection = previewViewer.getSelection();
									if (selection instanceof IStructuredSelection) {
										IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
										CompoundCommand c = new CompoundCommand();
										Iterator<Object> itr = iStructuredSelection.iterator();
										while (itr.hasNext()) {
											Object o = itr.next();
											c.append(RemoveCommand.create(editorData.getEditingDomain(), o));
										}
										editorData.getEditingDomain().getCommandStack().execute(c);
										previewViewer.refresh();
									}
								}
								;

							}
						};
						deleteSlotAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
						buttonManager.getToolbarManager().add(deleteSlotAction);
					}

					{
						Action packAction = new Action("Pack") {
							@Override
							public void run() {

								if (previewViewer != null && !previewViewer.getControl().isDisposed()) {
									final GridColumn[] columns = previewViewer.getGrid().getColumns();
									for (final GridColumn c : columns) {
										if (c.getResizeable()) {
											c.pack();
										}
									}
								}
							}
						};
						packAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.rcp.common", "/icons/pack.gif"));
						buttonManager.getToolbarManager().add(packAction);
					}

					buttonManager.getToolbarManager().update(true);
					// toolkit.adapt(removeButtonManager.getToolbarManager().getControl());
				}
			}
		}
	}

	private ScenarioTableViewer constructPreviewViewer(final ADPEditorData editorData, final Group previewGroup) {

		final ScenarioTableViewer previewViewer = new ScenarioTableViewer(previewGroup, SWT.NONE, editorData);
		previewViewer.init(editorData.getAdapterFactory(), editorData.getModelReference(), ADPPackage.Literals.SPOT_MARKETS_PROFILE__SPOT_MARKETS);
		GridViewerHelper.configureLookAndFeel(previewViewer);

		previewViewer.setStatusProvider(editorData.getStatusProvider());

		previewViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		ColumnViewerToolTipSupport.enableFor(previewViewer);

		previewViewer.getGrid().setHeaderVisible(true);

		// previewViewer.setContentProvider(new ArrayContentProvider());
		ReadOnlyManipulatorWrapper<BasicAttributeManipulator> nameManipulator = new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(
				new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Name", nameManipulator);
		previewViewer.addColumn("Type", new BaseFormatter() {
			@Override
			public @Nullable String render(Object object) {
				if (object instanceof DESSalesMarket) {
					return "DES Sale";
				}
				if (object instanceof FOBSalesMarket) {
					return "FOB Sale";
				}
				if (object instanceof DESPurchaseMarket) {
					return "DES Purchase";
				}
				if (object instanceof FOBPurchasesMarket) {
					return "FOB Purchase";
				}
				// TODO Auto-generated method stub
				return super.render(object);
			}
		}, null, new EReference[0]);

		previewViewer.addTypicalColumn("Price", new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getDateShiftExpressionPriceParameters_PriceExpression(), editorData.getEditingDomain()),
				SpotMarketsPackage.eINSTANCE.getSpotMarket_PriceInfo());

		// TODO: Groups
		GridColumnGroup quantityGroup = new GridColumnGroup(previewViewer.getGrid(), SWT.NONE);
		quantityGroup.setText("Quantity");
		GridViewerHelper.configureLookAndFeel(quantityGroup);
		previewViewer.addTypicalColumn("Min", quantityGroup, new VolumeAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotMarket_MinQuantity(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Max", quantityGroup, new VolumeAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotMarket_MaxQuantity(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Units", quantityGroup,
				new TextualEnumAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotMarket_VolumeLimitsUnit(), editorData.getEditingDomain(), (e) -> mapName((VolumeUnits) e)));

		//
		// previewViewer.addTypicalColumn("Fleet", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Fleet(), editorData.getEditingDomain()));
		//
		// previewViewer.addTypicalColumn("Optional", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Optional(), editorData.getEditingDomain()));
		//
		// previewViewer.addTypicalColumn("Charter", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate(), editorData.getEditingDomain()));

		// addTypicalColumn("Repositioning Fee", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_RepositioningFee(), editorData.getEditingDomain()) {
		// @Override
		// public boolean canEdit(Object object) {
		// if (object instanceof VesselAvailability) {
		// if (!((VesselAvailability) object).isFleet()) {
		// return true;
		// } else {
		// return false;
		// }
		// } else {
		// return super.canEdit(object);
		// }
		// }
		// });
		//
		// previewViewer.addTypicalColumn("Start Port",
		// new SingleReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAt(), editorData.getReferenceValueProviderCache(), editorData.getEditingDomain()));
		//
		// previewViewer.addTypicalColumn("Start After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAfter(), editorData.getEditingDomain()));
		//
		// previewViewer.addTypicalColumn("Start By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartBy(), editorData.getEditingDomain()));
		//
		// previewViewer.addTypicalColumn("End Port", new MultiplePortReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAt(), editorData.getReferenceValueProviderCache(),
		// editorData.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		//
		// previewViewer.addTypicalColumn("End After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAfter(), editorData.getEditingDomain()));
		//
		// previewViewer.addTypicalColumn("End By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndBy(), editorData.getEditingDomain()));

		previewViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(final DoubleClickEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection ss = (IStructuredSelection) selection;
					DetailCompositeDialogUtil.editSelection(editorData, ss);
					if (previewViewer != null) {
						previewViewer.refresh();
					}
				}
			}
		});
		previewViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				deleteSlotAction.setEnabled(!event.getSelection().isEmpty());
			}
		});
		deleteSlotAction.setEnabled(false);
		return previewViewer;
	}

	@Override
	public void refresh() {
		if (previewViewer != null) {
			previewViewer.refresh();
		}
	}

	@Override
	public synchronized void updateRootModel(@Nullable final LNGScenarioModel scenarioModel, @Nullable ADPModel adpModel) {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}
		if (previewViewer != null) {
			previewViewer.getControl().dispose();
			previewViewer.dispose();
			previewViewer = null;
		}
		if (scenarioModel != null) {
			previewViewer = constructPreviewViewer(editorData, previewGroup);
			previewGroup.layout();
		}

		// TODO: Attach adapter if needed.
		mainComposite.setVisible(adpModel != null);

		if (adpModel == null) {
			updateDetailPaneInput(null);
		} else {
			updateDetailPaneInput(adpModel.getSpotMarketsProfile());
			detailComposite.getComposite().pack();
		}
	}

	@Override
	public void dispose() {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}

		super.dispose();
	}

	private void updateDetailPaneInput(final EObject object) {
		EObject target = null;
		if (editorData.getAdpModel() != null) {
			target = editorData.getAdpModel().getSpotMarketsProfile();
			previewViewer.setInput(editorData.getAdpModel().getSpotMarketsProfile());
		}

		detailComposite.setInput(target);

	}

	private static String mapName(final VolumeUnits units) {

		switch (units) {
		case M3:
			return "m³";
		case MMBTU:
			return "mmBtu";
		}
		return units.getName();
	}
}
