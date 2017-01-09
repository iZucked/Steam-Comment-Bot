/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityPart;

/**
 * A helper class to convert e4 selection API parameters to e3 equivalents where possible.
 * 
 * @author Simon Goodall
 *
 */
@SuppressWarnings("restriction")
public final class SelectionHelper {

	@NonNull
	public static ISelection adaptSelection(@Nullable final Object selectionObject) {
		if (selectionObject == null) {
			return new StructuredSelection();
		} else if (selectionObject instanceof ISelection) {
			return (ISelection) selectionObject;
		} else if (selectionObject instanceof List<?>) {
			final List<?> list = (List<?>) selectionObject;
			return new StructuredSelection(list);
		} else if (selectionObject instanceof Object[]) {
			final Object[] objects = (Object[]) selectionObject;
			return new StructuredSelection(objects);
		} else {
			return new StructuredSelection(selectionObject);
		}
	}

	@Nullable
	public static IWorkbenchPart getE3Part(@Nullable final MPart e4Part) {
		if (e4Part == null) {
			return null;
		}
		final Object object = e4Part.getObject();
		if (object instanceof CompatibilityPart) {
			final CompatibilityPart compatibilityView = (CompatibilityPart) object;
			return compatibilityView.getPart();
		}
		return null;
	}

	/**
	 * Convert the given {@link ISelection} to a filtered list of elements of the given input {@link Class} type. May return null or an empty list.
	 */
	public static <T> @Nullable List<@NonNull T> convertToList(@Nullable final ISelection selection, final Class<T> cls) {
		if (selection instanceof IStructuredSelection) {
			final List<?> l = ((IStructuredSelection) selection) //
					.toList();
			return l.stream() //
					.filter(e -> cls.isInstance(e)) //
					.map(e -> cls.cast(e)) //
					.collect(Collectors.toList());
		}
		return null;
	}
}
