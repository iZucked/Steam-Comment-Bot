package com.mmxlabs.models.lng.transformer.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;

/**
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Simon Goodall
 * 
 */
public final class SlotContractHelper {

	public static @Nullable
	<T> T findSlotContractExtension(@NonNull final Slot slot, @NonNull final Class<T> cls) {
		for (final EObject object : slot.getExtensions()) {
			if (cls.isInstance(object)) {
				return cls.cast(object);
			}
		}
		return null;
	}
}
