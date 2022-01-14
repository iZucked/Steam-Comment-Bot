/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.anonymisation;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class AnonymisationUtils {
	
	public static CompoundCommand createAnonymisationCommand(final @NonNull LNGScenarioModel scenarioModel, final EditingDomain ed, final Set<String> usedIDStrings, //
			final List<AnonymisationRecord> records, final boolean anonymise) {
		final AnonymisationHandler handler = new AnonymisationHandler();
		return handler.createAnonymisationCommand(scenarioModel, ed, usedIDStrings, records, anonymise, AnonymisationMapIO.anonyMapFile);
	}
	
	public static CompoundCommand createAnonymisationCommand(final @NonNull LNGScenarioModel scenarioModel, final EditingDomain ed, final Set<String> usedIDStrings, //
			final List<AnonymisationRecord> records, final boolean anonymise, final @NonNull File anonyMap) {
		final AnonymisationHandler handler = new AnonymisationHandler();
		return handler.createAnonymisationCommand(scenarioModel, ed, usedIDStrings, records, anonymise, anonyMap);
	}
	
	public static Command rename(AnonymisationEntry entry) {
		String name = getNewName(entry.records, entry.name, entry.type);
		if (name.isEmpty()) {
			name = suggestNewName(entry);
			entry.records.add(new AnonymisationRecord(entry.name, name, entry.type));
		}
		return SetCommand.create(entry.editingDomain, entry.renamee, entry.feature, name);
	}

	public static Command renameToOriginal(AnonymisationEntry entry) {
		String name = getOldName(entry.records, entry.name, entry.type);
		if (!name.isEmpty() || entry.type == AnonymisationRecordType.VesselShortID) {
			return SetCommand.create(entry.editingDomain, entry.renamee, entry.feature, name);
		}
		return null;
	}

	private static String getNewName(final List<AnonymisationRecord> records, final String oldName, final AnonymisationRecordType type) {
		if (oldName == null || type == null || records == null || records.isEmpty() || (type == AnonymisationRecordType.VesselShortID && oldName.isEmpty()))
			return "";
		final List<AnonymisationRecord> filtered = records.stream().filter(r -> r.type == type && oldName.equalsIgnoreCase(r.oldName)).collect(Collectors.toList());
		if (!filtered.isEmpty()) {
			final AnonymisationRecord result = filtered.get(0);
			if (result != null && result.newName != null) {
				return result.newName;
			} else {
				return "";
			}
		}
		return "";
	}

	private static @NonNull String getOldName(final List<AnonymisationRecord> records, final String newName, final AnonymisationRecordType type) {
		if (newName == null || type == null || records == null || records.isEmpty())
			return "";
		final List<AnonymisationRecord> filtered = records.stream().filter(r -> r.type == type && newName.equalsIgnoreCase(r.newName)).collect(Collectors.toList());
		if (!filtered.isEmpty()) {
			final AnonymisationRecord result = filtered.get(0);
			if (result != null && result.oldName != null) {
				return result.oldName;
			} else {
				return "";
			}
		}
		return "";
	}

	public static String suggestNewName(final AnonymisationEntry entry) {
		int counter = 0;
		String suggestedName = entry.prefix;
		String prefix = entry.prefix;
		if (entry.renamee instanceof Slot) {
			final Slot<?> slot = (Slot<?>) entry.renamee;
			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			suggestedName = String.format("%s-%s-%s-%d", getSlotPrefix(slot), slot.getPort().getName(), slot.getWindowStart().format(dtf), slot.getWindowStartTime());
			prefix = suggestedName;
		}
		if (entry.renamee instanceof Vessel) {
			final Vessel v = (Vessel) entry.renamee;
			String capacity = String.format("%dK", (int) (v.getCapacity() / 1_000.0));
			String type = v.getType();
			String vPrefix = "Vessel";
			if (entry.type == AnonymisationRecordType.VesselShortID) {
				vPrefix = "VSL";
			}
			suggestedName = String.format("%s-%s-%s", vPrefix, capacity, type);
			prefix = suggestedName;
		}
		while (entry.usedIDStrings.contains(suggestedName)) {
			suggestedName = String.format("%s-%d", prefix, counter++);
		}
		entry.usedIDStrings.add(suggestedName);
		return suggestedName;
	}

	private static String getSlotPrefix(final Slot<?> slot) {
		if (slot instanceof LoadSlot) {
			final LoadSlot ls = (LoadSlot) slot;
			if (ls.isDESPurchase()) {
				return "DP";
			} else {
				return "FP";
			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot ds = (DischargeSlot) slot;
			if (ds.isFOBSale()) {
				return "FS";
			} else {
				return "DS";
			}
		}
		return "UD";
	}
}
