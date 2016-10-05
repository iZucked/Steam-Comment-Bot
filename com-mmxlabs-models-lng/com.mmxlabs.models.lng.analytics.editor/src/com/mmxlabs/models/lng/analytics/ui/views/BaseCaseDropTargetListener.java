package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.emf.common.command.CompoundCommand;
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
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;

public class BaseCaseDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private OptionAnalysisModel optionAnalysisModel;

	private @NonNull final Runnable refreshCallback;

	private @NonNull GridTreeViewer viewer;

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final Runnable refreshCallback,
			@NonNull GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.optionAnalysisModel = optionAnalysisModel;
		this.refreshCallback = refreshCallback;
		this.viewer = viewer;
	}

	public BaseCaseDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final Runnable refreshCallback, @NonNull GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.refreshCallback = refreshCallback;
		this.viewer = viewer;

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
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

					}
					refreshCallback.run();
				} else if (o instanceof SellOption) {
					final SellOption sellOption = (SellOption) o;
					if (existing != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption), existing,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					} else {
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption));
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
					}
					refreshCallback.run();
				} else if (o instanceof Vessel) {

					if (existing != null) {
						ShippingOption opt = null;
						if (AnalyticsBuilder.isNonShipped(existing)) {
							final NominatedShippingOption so = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
							so.setNominatedVessel((Vessel) o);

							opt = so;
						} else {
							final FleetShippingOption so = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
							so.setVessel((Vessel) o);

							opt = so;
						}
						assert opt != null;
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);

						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
					}
					refreshCallback.run();
				} else if (o instanceof VesselClass) {
					final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
					opt.setVesselClass((VesselClass) o);
					if (existing != null) {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));

					}
					refreshCallback.run();
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
}
