package com.mmxlabs.rcp.common;

import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
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
}
