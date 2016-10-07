package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder.ShippingType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.LocalMenuHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class BaseCaseDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private OptionAnalysisModel optionAnalysisModel;

	private @NonNull final GridTreeViewer viewer;

	private final LocalMenuHelper menuHelper;

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.optionAnalysisModel = optionAnalysisModel;
		this.viewer = viewer;
		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				menuHelper.dispose();
			}
		});
	}

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		this(scenarioEditingLocation, null, viewer);
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {

	}

	@Override
	public void drop(final DropTargetEvent event) {
		final OptionAnalysisModel optionAnalysisModel = this.optionAnalysisModel;
		if (optionAnalysisModel == null) {
			return;
		}
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() == 1) {
				final Object o = selection.getFirstElement();

				BaseCaseRow existing = null;
				if (event.item instanceof GridItem) {
					final GridItem gridItem = (GridItem) event.item;
					final Point cell = viewer.getGrid().getCell(viewer.getGrid().toControl(event.x, event.y));

					final Object d = gridItem.getData();
					if (d instanceof BaseCaseRow) {
						existing = (BaseCaseRow) d;
					}
				}

				if (o instanceof BuyOption) {
					final BuyOption buyOption = (BuyOption) o;
					if (existing != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption), existing,
								AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					} else {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

					}
				} else if (o instanceof SellOption) {
					final SellOption sellOption = (SellOption) o;
					if (existing != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption), existing,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					} else {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
				} else if (o instanceof Vessel) {
					final Vessel vessel = (Vessel) o;

					if (existing != null) {
						if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.NonShipped) {
							final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
							opt.setNominatedVessel(vessel);

							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
									AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

							DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
						} else if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.Shipped) {
							final BaseCaseRow pExisting = existing;
							menuHelper.clearActions();
							menuHelper.addAction(new RunnableAction("Create RT", () -> {
								final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
								opt.setVesselClass(vessel.getVesselClass());
								scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
										SetCommand.create(scenarioEditingLocation.getEditingDomain(), pExisting, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), pExisting,
										AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
								DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
							}));
							menuHelper.addAction(new RunnableAction("Create fleet", () -> {
								final FleetShippingOption opt = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
								opt.setVessel(vessel);
								AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, opt);
								scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
										SetCommand.create(scenarioEditingLocation.getEditingDomain(), pExisting, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), pExisting,
										AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
								DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
							}));

							menuHelper.open();
						}
					}
				} else if (o instanceof VesselClass) {

					final BaseCaseRow pExisting = existing;

					final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
					opt.setVesselClass((VesselClass) o);
					if (pExisting != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), pExisting, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), pExisting,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
					}
				} else if (o instanceof ShippingOption) {
					if (existing != null) {
						ShippingOption opt = null;
						if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.NonShipped) {
							if (o instanceof NominatedShippingOption) {
								opt = (ShippingOption) EcoreUtil.copy((ShippingOption) o);
							}
						} else if (AnalyticsBuilder.isNonShipped(existing) == ShippingType.Shipped) {

							if (o instanceof RoundTripShippingOption || o instanceof FleetShippingOption) {
								opt = (ShippingOption) EcoreUtil.copy((ShippingOption) o);
							}
						}
						if (opt != null) {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
									AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						}
					}
				} else if (o instanceof CargoModelRowTransformer.RowData) {
					final CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) o;
					final CompoundCommand cmd = new CompoundCommand();

					final LoadSlot loadSlot = rowData.getLoadSlot();
					final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, cmd);

					final DischargeSlot dischargeSlot = rowData.getDischargeSlot();
					final SellOption sellRef = AnalyticsBuilder.getOrCreateSellOption(dischargeSlot, optionAnalysisModel, scenarioEditingLocation, cmd);

					final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
					row.setBuyOption(buyRef);
					row.setSellOption(sellRef);

					final ShippingOption shippingOption = AnalyticsBuilder.getOrCreateShippingOption(rowData.getCargo(), loadSlot, dischargeSlot, optionAnalysisModel, scenarioEditingLocation, cmd);
					row.setShipping(shippingOption);

					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));

					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);

					return;
				}
			}
		}
	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() == 1) {
				final Object o = selection.getFirstElement();
				if (o instanceof BuyOption || o instanceof SellOption) {
					event.operations = DND.DROP_MOVE;
					return;
				}
				if (o instanceof Vessel || o instanceof VesselClass) {
					event.operations = DND.DROP_MOVE;
					return;
				}
				if (o instanceof ShippingOption) {
					event.operations = DND.DROP_MOVE;
					return;
				}
				if (o instanceof CargoModelRowTransformer.RowData) {
					event.operations = DND.DROP_MOVE;
					return;
				}
			}
		}
		event.operations = DND.DROP_NONE;
	}

	@Override
	public void dragOperationChanged(final DropTargetEvent event) {

	}

	@Override
	public void dragLeave(final DropTargetEvent event) {

	}

	@Override
	public void dragEnter(final DropTargetEvent event) {

	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}
}
