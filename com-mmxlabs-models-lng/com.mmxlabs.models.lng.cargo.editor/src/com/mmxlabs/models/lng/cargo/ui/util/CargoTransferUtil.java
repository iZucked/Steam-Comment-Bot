package com.mmxlabs.models.lng.cargo.ui.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.ui.Activator;

public class CargoTransferUtil {

	public static boolean isSlotTransferred(final Slot<?> slot, final LNGScenarioModel scenarioModel) {
		return isSlotTransferred(slot, getTransferRecords(scenarioModel));
	}
	
	private static boolean isSlotTransferred(final Slot<?> slot, final List<TransferRecord> records) {
		for(final TransferRecord tr: records) {
			if (slot == tr.getLhs()) {
				return true;
			}
		}
		return false;
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
