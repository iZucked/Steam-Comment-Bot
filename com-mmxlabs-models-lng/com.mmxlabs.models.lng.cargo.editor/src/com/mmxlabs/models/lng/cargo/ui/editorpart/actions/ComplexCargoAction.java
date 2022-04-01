/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.editors.ldd.ComplexCargoEditor;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public final class ComplexCargoAction extends LockableAction {

	/**
	 * 
	 */
	private final IScenarioEditingLocation scenarioEditingLocation;
	private Shell shell;

	public ComplexCargoAction(final String text, IScenarioEditingLocation scenarioEditingLocation, Shell shell) {
		super(text);
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.shell = shell;
	}

	@Override
	public void run() {

		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		editorLock.lock();
		try {
			scenarioEditingLocation.setDisableUpdates(true);
			final ComplexCargoEditor editor = new ComplexCargoEditor(shell, scenarioEditingLocation, true);
			// editor.setBlockOnOpen(true);

			final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

			final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
			final DischargeSlot discharge1 = CargoFactory.eINSTANCE.createDischargeSlot();
			final DischargeSlot discharge2 = CargoFactory.eINSTANCE.createDischargeSlot();

			discharge1.setWindowStart(LocalDate.now());
			discharge2.setWindowStart(LocalDate.now());

			cargo.getSlots().addAll(Lists.newArrayList(load, discharge1, discharge2));
			final int ret = editor.open(cargo);
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
			if (ret == Window.OK) {

				final CargoModel cargomodel = ScenarioModelUtil.getCargoModel(scenarioEditingLocation.getScenarioDataProvider());

				final CompoundCommand cmd = new CompoundCommand("New LDD Cargo");
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), Collections.singleton(cargo)));
				for (final Slot s : cargo.getSlots()) {

					if (s.eContainer() == null) {

						if (s instanceof LoadSlot) {
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), Collections.singleton(s)));
						} else {
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), Collections.singleton(s)));
						}
					}
				}

				commandStack.execute(cmd);
			} else {
				final Iterator<Command> itr = new LinkedList<Command>(editor.getExecutedCommands()).descendingIterator();
				while (itr.hasNext()) {
					final Command cmd = itr.next();
					if (commandStack.getUndoCommand() == cmd) {
						commandStack.undo();
					} else {
						throw new IllegalStateException("Unable to cancel edit - command stack history is corrupt");
					}
				}

			}
		} finally {
			scenarioEditingLocation.setDisableUpdates(false);
			editorLock.unlock();
		}
	}
}