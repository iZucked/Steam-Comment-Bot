/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationMapIO;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationRecord;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationRecordType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ToggleAnonymiseScenarioEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {
	protected IEditorPart targetEditor;
	protected IAction action;
	protected LNGScenarioModel currentModel;
	protected EditingDomain editingDomain;
	private static final Set<String> usedIDStrings = new HashSet<>();
	
	private static final String VN = "Vessel";
	private static final String VS = "VSL";
	private static final String LS = "Purchase";
	private static final String DS = "Sale";
	private static final String PC = "Purchase contract";
	private static final String SC = "Sales contract";

	protected AdapterImpl notificationAdapter = new SafeAdapterImpl() {

		@Override
		protected void safeNotifyChanged(Notification msg) {
			if (msg.isTouch()) {
				return;
			}
			if (msg.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_Anonymised()) {
				updateState();
			}
		}
	};

	@Override
	public synchronized void setActiveEditor(final IAction action, final IEditorPart targetEditor) {

		if (this.targetEditor != null) {
			// Remove notifications
		}
		if (currentModel != null) {
			currentModel.eAdapters().remove(notificationAdapter);
		}
		currentModel = null;
		usedIDStrings.clear();

		this.editingDomain = null;
		this.targetEditor = targetEditor;
		this.action = action;

		if (this.targetEditor != null) {
			final IEditorInput editorInput = targetEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();

				final @NonNull ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

				try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("ToggleAnonymiseScenarioEditorActionDelegate")) {
					this.currentModel = sdp.getTypedScenario(LNGScenarioModel.class);
					this.currentModel.eAdapters().add(notificationAdapter);
					this.editingDomain = sdp.getEditingDomain();
				}
			}
		}
		updateState();
	}

	private void updateState() {
		if (currentModel != null) {
			if (!currentModel.eAdapters().contains(notificationAdapter)) {
				currentModel.eAdapters().add(notificationAdapter);
			}
			action.setEnabled(true);
			action.setChecked(currentModel.isAnonymised());
		} else {
			action.setEnabled(false);
			action.setChecked(false);
		}
	}

	@Override
	public void dispose() {
		action = null;
		targetEditor = null;
		if (currentModel != null) {
			currentModel.eAdapters().remove(notificationAdapter);
		}
		currentModel = null;
	}

	@Override
	public synchronized void run(final IAction action) {
		if (editingDomain == null || currentModel == null) {
			return;
		}
		final LNGScenarioModel scenarioModel = currentModel;
		final EditingDomain ed = editingDomain;
		final List<AnonymisationRecord> records = AnonymisationMapIO.read(AnonymisationMapIO.anonyMapFile);
		if (records.isEmpty()) {
			//Show dialog which says that automatic naming will be applied
			final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			final MessageDialog dialog = new MessageDialog(shell, "Anonymisation", null, "Default naming generation will be applied!", 0, 0, "OK","Cancel") ;
			dialog.create();
			if (dialog.open() == 1) {
				return;
			}
		}
		usedIDStrings.clear();
		for (final AnonymisationRecord r : records) {
			usedIDStrings.add(r.newName);
		}
		final CompoundCommand cmd = new CompoundCommand("Toggle anonymised");
		final boolean foo = !currentModel.isAnonymised();
		cmd.append(SetCommand.create(ed, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_Anonymised(), foo));
		if (foo) {
			renameVessels(scenarioModel, ed, records, cmd, ToggleAnonymiseScenarioEditorActionDelegate::rename);
			renameSlots(scenarioModel, ed, records, cmd, ToggleAnonymiseScenarioEditorActionDelegate::rename);
			renameContracts(scenarioModel, ed, records, cmd, ToggleAnonymiseScenarioEditorActionDelegate::rename);
			AnonymisationMapIO.write(records, AnonymisationMapIO.anonyMapFile);
		} else {
			renameVessels(scenarioModel, ed, records, cmd, ToggleAnonymiseScenarioEditorActionDelegate::renameBW);
			renameSlots(scenarioModel, ed, records, cmd, ToggleAnonymiseScenarioEditorActionDelegate::renameBW);
			renameContracts(scenarioModel, ed, records, cmd, ToggleAnonymiseScenarioEditorActionDelegate::renameBW);
		}
		if (!cmd.isEmpty())
			ed.getCommandStack().execute(cmd);
	}
	
	private void renameVessels(final @NonNull LNGScenarioModel currentModel, final EditingDomain editingDomain, final List<AnonymisationRecord> records, //
			final CompoundCommand renameCommand, Function<RFEntry, Command> renameFunction) {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(currentModel);
		for (final Vessel v : fleetModel.getVessels()) {
			renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, v, v.getName(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, VN, AnonymisationRecordType.VN)));
			renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, v, v.getShortName(), FleetPackage.Literals.VESSEL__SHORT_NAME, VS, AnonymisationRecordType.VS)));
		}
	}
	
	private void renameSlots(final @NonNull LNGScenarioModel currentModel, final EditingDomain editingDomain, final List<AnonymisationRecord> records, //
			final CompoundCommand renameCommand, Function<RFEntry, Command> renameFunction) {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(currentModel);
		final Set<Slot<?>> usedSlots = new HashSet();
		for (final Cargo c : cargoModel.getCargoes()) {
			for(final Slot<?> s : c.getSlots()) {
				if (!(s instanceof SpotSlot)) {
					if (s instanceof LoadSlot) {
						renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, s, s.getName(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, LS, AnonymisationRecordType.LS)));
						usedSlots.add(s);
					} else {
						renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, s, s.getName(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, DS, AnonymisationRecordType.DS)));
						usedSlots.add(s);
					}
				}
			}
		}
		for (final Slot<?> s : cargoModel.getLoadSlots()) {
			if (!(s instanceof SpotSlot) && !(usedSlots.contains(s))) {
				renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, s, s.getName(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, LS, AnonymisationRecordType.LS)));
				usedSlots.add(s);
			}
		}
		for (final Slot<?> s : cargoModel.getDischargeSlots()) {
			if (!(s instanceof SpotSlot) && !(usedSlots.contains(s))) {
				renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, s, s.getName(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, DS, AnonymisationRecordType.DS)));
				usedSlots.add(s);
			}
		}
	}
	
	private void renameContracts(final @NonNull LNGScenarioModel currentModel, final EditingDomain editingDomain, final List<AnonymisationRecord> records, //
			final CompoundCommand renameCommand, Function<RFEntry, Command> renameFunction) {
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(currentModel);
		for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
			renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, c, c.getName(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, PC, AnonymisationRecordType.PC)));
		}
		for (final SalesContract c : commercialModel.getSalesContracts()) {
			renameCommand.append(renameFunction.apply(new RFEntry(editingDomain, records, c, c.getName(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, SC, AnonymisationRecordType.SC)));
		}
	}
	
	private static Command rename(RFEntry entry) {
		String name = getNewName(entry.records, entry.name, entry.type);
		if (name.isEmpty()) {
			name = suggestNewName(entry.prefix);
			entry.records.add(new AnonymisationRecord(entry.name, name, entry.type));
		}
		return SetCommand.create(entry.editingDomain, entry.renamee, entry.feature, name);
	}
	
	private static Command renameBW(RFEntry entry) {
		String name = getOldName(entry.records, entry.name, entry.type);
		if (!name.isEmpty()) {
			return SetCommand.create(entry.editingDomain, entry.renamee, entry.feature, name);
		}
		return null;
	}
	
	private static String getNewName(final List<AnonymisationRecord> records, final String oldName, final AnonymisationRecordType type) {
		final List<AnonymisationRecord> filtered = records.stream().filter(r -> r.oldName.equalsIgnoreCase(oldName) && r.type.equals(type)).collect(Collectors.toList());
		if (!filtered.isEmpty()) {
			return filtered.get(0).newName;
		}
		return "";
	}
	
	private static String getOldName(final List<AnonymisationRecord> records, final String newName, final AnonymisationRecordType type) {
		final List<AnonymisationRecord> filtered = records.stream().filter(r -> r.newName.equalsIgnoreCase(newName) && r.type.equals(type)).collect(Collectors.toList());
		if (!filtered.isEmpty()) {
			return filtered.get(0).oldName;
		}
		return "";
	}

	private static String suggestNewName(final String prefix) {
		int counter = 0;
		String suggestedName = prefix + counter++;
		while(usedIDStrings.contains(suggestedName)) {
			suggestedName = prefix + counter++;
		}
		usedIDStrings.add(suggestedName);
		return suggestedName;
	}
	
	private class RFEntry{
		public RFEntry(EditingDomain editingDomain, List<AnonymisationRecord> records, EObject renamee, String name, EStructuralFeature feature, String prefix, AnonymisationRecordType type) {
			super();
			this.editingDomain = editingDomain;
			this.records = records;
			this.renamee = renamee;
			this.name = name;
			this.feature = feature;
			this.prefix = prefix;
			this.type = type;
		}
		final EditingDomain editingDomain; 
		final List<AnonymisationRecord> records; 
		final EObject renamee; 
		final String name; 
		final EStructuralFeature feature; 
		final String prefix; 
		final AnonymisationRecordType type;
	}
}