package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ICharterLengthElementProviderEditor;

@NonNullByDefault
public class DefaultCharterLengthElementProviderEditor implements ICharterLengthElementProviderEditor {

	private final HashMap<IPort, Map<ICharterLengthEvent, ISequenceElement>> charterLengthElements = new HashMap<>();
	private final HashMap<ICharterLengthEventPortSlot, ICharterLengthEventPortSlot> originalPortSlotMap = new HashMap<>();
	
	@Override
	public @Nullable ISequenceElement getCharterLengthLocationElement(@NonNull ICharterLengthEvent event, @NonNull IPort port) {
		Map<ICharterLengthEvent, ISequenceElement> eventElementMap = charterLengthElements.get(port);
		return eventElementMap == null ? null : eventElementMap.get(event);
	}

	@Override
	public void setCharterLengthLocationElement(@NonNull ICharterLengthEvent event, @NonNull IPort port, @NonNull ISequenceElement element) {
		charterLengthElements.merge(port, CollectionsUtil.makeHashMap(event, element), //
				(k, v) -> {
					v.put(event, element);
					return v;
				});
	}

	@Override
	public @Nullable ICharterLengthEventPortSlot getOriginalCharterLengthSlot(ICharterLengthEventPortSlot portSlot) {
		return originalPortSlotMap.get(portSlot);
	}

	@Override
	public void setOriginalCharterLengthPortSlot(ICharterLengthEventPortSlot original, ICharterLengthEventPortSlot newPortSlot) {
		originalPortSlotMap.put( newPortSlot, original);
	}

}
