package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public interface IMoveHelper {

	boolean legacyCheckResource(ISequenceElement sequenceElement, IResource resource);

	boolean checkResource(@NonNull ISequenceElement element, @Nullable IResource resource);

	@NonNull
	Collection<@NonNull IResource> getAllowedResources(@NonNull ISequenceElement element);

	@NonNull
	IResource getDESPurchaseResource(@NonNull ISequenceElement desPurchase);

	@NonNull
	IResource getFOBSaleResource(@NonNull ISequenceElement fobSale);

	boolean isLockedToVessel(@NonNull ISequenceElement element);

	boolean isLoadSlot(@NonNull ISequenceElement element);

	boolean isDischargeSlot(@NonNull ISequenceElement element);

	boolean isFOBPurchase(@NonNull ISequenceElement element);

	boolean isFOBSale(@NonNull ISequenceElement element);

	boolean isDESPurchase(@NonNull ISequenceElement element);

	boolean isDESSale(@NonNull ISequenceElement element);

	boolean isVesselEvent(@NonNull ISequenceElement element);

	boolean isDryDockEvent(@NonNull ISequenceElement element);

	boolean isMaintenanceEvent(@NonNull ISequenceElement element);

	@NonNull
	Collection<@NonNull IResource> getAllVesselResources();

	boolean isStartOrEndSlot(@NonNull ISequenceElement element);

	boolean isOptional(@NonNull ISequenceElement element);

	boolean isCharterOutEvent(@NonNull ISequenceElement element);

	// TODO: -- How to tell?
	boolean isRelocatedCharterOutEvent(@NonNull ISequenceElement element);

	boolean isSimpleCharterOutEvent(@NonNull ISequenceElement element);
}