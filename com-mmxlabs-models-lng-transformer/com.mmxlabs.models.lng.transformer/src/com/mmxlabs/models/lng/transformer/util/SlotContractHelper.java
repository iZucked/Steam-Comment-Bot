/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

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

	public static <T> T findDetailsAnnotation(@NonNull final IProfitAndLossSlotDetailsAnnotation annotation, String annotationKey, @NonNull final Class<T> cls) {
		if (annotation == null) {
			return null;
		}
		final IDetailTree detailTree = annotation.getDetails();
		if (detailTree != null) {
			for (final IDetailTree child : detailTree.getChildren()) {
				if (child.getKey() == annotationKey) {
					if (cls.isInstance(child.getValue())) {
						return cls.cast(child.getValue());
					}
				}
			}
		}
		return null;
	}

	public static <T> T findDetailsAnnotation(@NonNull final IProfitAndLossAnnotation annotation, String annotationKey, @NonNull final Class<T> cls) {
		for (final IProfitAndLossEntry e : annotation.getEntries()) {
			final IDetailTree detailTree = e.getDetails();
			if (detailTree != null) {
				for (final IDetailTree child : detailTree.getChildren()) {
					if (child.getKey() == annotationKey) {
						if (cls.isInstance(child.getValue())) {
							return cls.cast(child.getValue());
						}
					}
				}
			}
		}
		return null;
	}

	public static IEntity findDetailsAnnotationEntity(@NonNull final IProfitAndLossAnnotation annotation, String annotationKey) {
		for (final IProfitAndLossEntry e : annotation.getEntries()) {
			final IDetailTree detailTree = e.getDetails();
			if (detailTree != null) {
				for (final IDetailTree child : detailTree.getChildren()) {
					if (child.getKey() == annotationKey) {
						return e.getEntityBook().getEntity();
					}
				}
			}
		}
		return null;
	}
}
