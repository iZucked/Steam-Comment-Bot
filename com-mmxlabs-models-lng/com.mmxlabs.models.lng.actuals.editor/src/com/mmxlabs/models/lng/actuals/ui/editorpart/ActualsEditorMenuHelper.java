/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 */
public class ActualsEditorMenuHelper {

	private final Shell shell;

	private final IScenarioEditingLocation scenarioEditingLocation;

	private final ActualsEditingCommands cec;

	/**
	 */
	public ActualsEditorMenuHelper(final Shell shell, final IScenarioEditingLocation scenarioEditingLocation, final LNGScenarioModel scenarioModel) {
		this.shell = shell;
		this.scenarioEditingLocation = scenarioEditingLocation;
		cec = new ActualsEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel);
	}

	private final class EditAction extends Action {
		private final EObject target;

		private EditAction(final String text, final EObject target) {
			super(text);
			this.target = target;
		}

		@Override
		public void run() {
			DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, target);
		}
	}

	private void createDeleteSlotMenu(final IMenuManager newMenuManager, final CargoActuals cargoActuals) {
		final Action deleteAction = new Action("Delete") {
			@Override
			public void run() {

				final CompoundCommand currentWiringCommand = new CompoundCommand("Delete cargo actuals");
				currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), cargoActuals));
				// CargoActuals cargo = null;
				// if (slot instanceof LoadActuals) {
				// final LoadActuals loadSlot = (LoadActuals) slot;
				// // cargo = loadSlot.getCargo();
				// }
				// if (slot instanceof DischargeActuals) {
				// final DischargeActuals dischargeSlot = (DischargeActuals) slot;
				// // cargo = dischargeSlot.getCargo();
				// }
				// if (cargo != null) {
				// currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), cargo));
				// }
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			}
		};
		newMenuManager.add(new Separator());
		newMenuManager.add(deleteAction);

	}

	public IMenuListener createLoadSlotMenuListener(final List<LoadActuals> loadSlots, final int index, final ActualsModel actualsModel) {
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final LoadActuals loadSlot = loadSlots.get(index);
				createEditMenu(manager, loadSlot);
				
				final EObject container = loadSlot.eContainer();
				if (container instanceof CargoActuals) {
					final CargoActuals cargoActuals = (CargoActuals) container;
					createDeleteSlotMenu(manager, cargoActuals);
				}
			}

		};
		return l;

	}

	public IMenuListener createDischargeSlotMenuListener(final List<DischargeActuals> dischargeSlots, final int index, final ActualsModel actualsModel) {
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				final DischargeActuals dischargeSlot = dischargeSlots.get(index);

				createEditMenu(manager, dischargeSlot);
				
				final EObject container = dischargeSlot.eContainer();
				if (container instanceof CargoActuals) {
					final CargoActuals cargoActuals = (CargoActuals) container;
					createDeleteSlotMenu(manager, cargoActuals);
				}
			}

		};
		return l;

	}

	private void createEditMenu(final IMenuManager newMenuManager, final SlotActuals slot) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Slot", slot));
	}

//	private void createNewSlotMenu(final IMenuManager menuManager, final SlotActuals source, final ActualsModel actualsModel) {
//
//		if (source instanceof LoadActuals) {
//			menuManager.add(new CreateSlotAction("Discharge Actuals", source, false, actualsModel));
//		} else {
//			menuManager.add(new CreateSlotAction("Load Actuals", source, false, actualsModel));
//		}
//	}

//	private class CreateSlotAction extends Action {
//
//		private final SlotActuals source;
//		private final boolean sourceIsLoad;
//		private final boolean isDesPurchaseOrFobSale;
//		private ActualsModel actualsModel;
//
//		public CreateSlotAction(final String name, final SlotActuals source, final boolean isDesPurchaseOrFobSale, final ActualsModel actualsModel) {
//			super(name);
//			this.source = source;
//			this.actualsModel = actualsModel;
//			this.sourceIsLoad = source instanceof LoadSlot;
//			this.isDesPurchaseOrFobSale = isDesPurchaseOrFobSale;
//		}
//
//		@Override
//		public void run() {
//
//			final List<Command> setCommands = new LinkedList<Command>();
//			// final List<Command> deleteCommands = new LinkedList<Command>();
//
//			// when we create another slot, we rewire the cargoes
//			LoadActuals loadSlot;
//			DischargeActuals dischargeSlot;
//			if (sourceIsLoad) {
//				loadSlot = (LoadActuals) source;
//				dischargeSlot = cec.createNewDischarge(setCommands, actualsModel, isDesPurchaseOrFobSale);
//			} else {
//				dischargeSlot = (DischargeActuals) source;
//				loadSlot = cec.createNewLoad(setCommands, actualsModel, isDesPurchaseOrFobSale);
//			}
//
//			// make a compound command which adds and sets values before deleting anything
//			final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
//			// Process set before delete
//			for (final Command c : setCommands) {
//				currentWiringCommand.append(c);
//			}
//			// for (final Command c : deleteCommands) {
//			// currentWiringCommand.append(c);
//			// }
//
//			scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
//
//		}
//	}
}