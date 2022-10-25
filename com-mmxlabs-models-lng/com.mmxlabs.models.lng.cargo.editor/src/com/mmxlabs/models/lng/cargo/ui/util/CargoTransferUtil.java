/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.util;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersFactory;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;

public class CargoTransferUtil {

	public static boolean isSlotReferencedByTransferRecord(final Slot<?> slot, final LNGScenarioModel scenarioModel) {
		return isSlotReferencedByTransferRecord(slot, getTransferRecords(scenarioModel));
	}
	
	private static boolean isSlotReferencedByTransferRecord(final Slot<?> slot, final List<TransferRecord> records) {
		return records.stream().anyMatch(tr -> tr.getLhs() == slot);
	}
	
	public static EObject getTransferRecordForSlot(final Slot<?> slot, final LNGScenarioModel scenarioModel) {
		final Optional<TransferRecord> optionalTransferRecord = getOptionalTransferRecordForSlot(slot, scenarioModel);
		if (optionalTransferRecord.isPresent()) {
			return optionalTransferRecord.get();
		}
		return null;
	}
	
	private static Optional<TransferRecord> getOptionalTransferRecordForSlot(final Slot<?> slot, final LNGScenarioModel scenarioModel) {
		return getTransferRecords(scenarioModel).stream().filter(tr -> tr.getLhs() == slot).findFirst();
	}
	
	private static List<TransferRecord> getTransferRecords(final LNGScenarioModel scenarioModel){
		if (scenarioModel != null) {
			final TransferModel tm = ScenarioModelUtil.getTransferModel(scenarioModel);
			if (tm != null) {
				return tm.getTransferRecords();
			}
		}
		return Collections.emptyList();
	}
	
	public static List<NamedObject> getTransferAgreementsForMenu(final LNGScenarioModel scenarioModel){
		if (scenarioModel != null) {
			final TransferModel tm = ScenarioModelUtil.getTransferModel(scenarioModel);
			if (tm != null) {
				return tm.getTransferAgreements().stream().collect(Collectors.toList());
			}
		}
		return Collections.emptyList();
	}
	
	public static List<Pair<CompoundCommand, EObject>> createTransferRecords(final String description, final LNGScenarioModel scenarioModel, //
			final EditingDomain editingDomain, final List<Slot<?>> slots, final EObject transferAgreement) {
		final List<Pair<CompoundCommand, EObject>> result = new LinkedList<>();
		
		for (final Slot<?> slot : slots) {
			if (isSimpleCargo(slot)) {
				Pair<CompoundCommand, EObject> pair = createTransferRecord(description, scenarioModel, editingDomain, slot, transferAgreement);
				if (!pair.getFirst().isEmpty() && pair.getSecond() != null) {
					result.add(pair);
				}
			}
		}
		
		return result;
	}
	
	// check that slot is not within a complex cargo
	private static boolean isSimpleCargo(final Slot<?> slot) {
		return (slot != null && slot.getCargo() != null && slot.getCargo().getSlots().size() > 2);
	}
	
	private static Pair<CompoundCommand, EObject> createTransferRecord(final String description, final LNGScenarioModel scenarioModel, //
			final EditingDomain editingDomain, final Slot<?> slot, final EObject transferAgreement) {
		final CompoundCommand cc = new CompoundCommand(description);
		
		final TransferRecord tr = TransfersFactory.eINSTANCE.createTransferRecord();
		tr.setCargoReleaseDate(LocalDate.now());
		int bufferDays = 0;
		if (transferAgreement instanceof TransferAgreement ta) {
			tr.setTransferAgreement(ta);
			bufferDays = getBufferDays(ta);
			tr.setName(String.format("%s_%s", slot.getName(), ta.getName()));
		}
		if (slot.getPricingDate() != null) {
			tr.setPricingDate(slot.getPricingDate().plusDays(bufferDays));
		} else if (slot.getSchedulingTimeWindow() != null && slot.getSchedulingTimeWindow().getStart() != null) {
			tr.setPricingDate(slot.getSchedulingTimeWindow().getStart().toLocalDate().plusDays(bufferDays));
		} else {
			tr.setPricingDate(slot.getWindowStart().plusDays(bufferDays));
		}
		tr.setLhs(slot);
		
		TransferModel tm = ScenarioModelUtil.getTransferModel(scenarioModel);
		if (tm == null) {
			tm = TransfersFactory.eINSTANCE.createTransferModel();
			tm.getTransferRecords().add(tr);
			cc.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_TransferModel(), tm));
		} else {
			cc.append(AddCommand.create(editingDomain, tm,
					TransfersPackage.Literals.TRANSFER_MODEL__TRANSFER_RECORDS, Collections.singleton(tr)));
		}
		
		return new Pair(cc, tr);
	}
	
	private static int getBufferDays(final TransferAgreement transferAgreement) {
		int bufferDays = 0;
		if (transferAgreement.isSetBufferDays()) {
			bufferDays = transferAgreement.getBufferDays();
		}
		return bufferDays;
	}
	
	public static Image joinImages(final String name, final @NonNull Image first, final @NonNull Image second) {

		// Define a key for this set of images

		final Image cache = Activator.getDefault().getImageRegistry().get(name);
		if (cache != null) {
			return cache;
		}

		final int imageWidth = 16 + 16;

		final Image image = new Image(Display.getDefault(), imageWidth, 16);
		final byte[] destArray = new byte[imageWidth * 16];

		final GC gc = new GC(image);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);

		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

		int xpos = 0;

		gc.drawImage(first, xpos, 0);
		xpos += 16;
		
		gc.drawImage(second, xpos, 0);
		
		// Create the alpha channel
		final byte[] dataArray = image.getImageData().data;
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < 16; y++) {
				final int destPos = imageWidth * y + x;
				final byte b = dataArray[destPos * 4 + 3];
				destArray[destPos] = b;
			}
		}

		// Create a new image merging in the alpha channel
		final ImageData data = image.getImageData();
		data.alphaData = destArray;

		gc.dispose();

		image.dispose();

		final Image result = new Image(Display.getDefault(), data);
		Activator.getDefault().getImageRegistry().put(name, result);
		return result;
	}
}
