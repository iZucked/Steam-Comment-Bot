/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.application;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jdt.annotation.NonNull;

public class E4ModelHelper {

	public static void removePerspective(@NonNull final String perspectiveId, @NonNull final MApplication application, @NonNull final EModelService modelService) {

		final List<MWindow> windows = modelService.findElements(application, null, MWindow.class, null);
		final List<MPerspective> perspectives = modelService.findElements(application, perspectiveId, MPerspective.class, null);
		for (final MPerspective perspective : perspectives) {
			for (final MWindow window : windows) {
				modelService.removePerspectiveModel(perspective, window);
			}
		}
	}

	public static void removeViewPart(@NonNull final String viewId, @NonNull final MApplication application, @NonNull final EModelService modelService) {

		final List<MWindow> windows = modelService.findElements(application, null, MWindow.class, null);

		for (final MWindow window : windows) {
			final List<MUIElement> elementsToRemove = new LinkedList<>();
			for (final MUIElement element : window.getSharedElements()) {
				if (viewId.equals(element.getElementId())) {
					elementsToRemove.add(element);
				}
			}
			for (final MUIElement element : elementsToRemove) {
				MPlaceholder placeholder = modelService.findPlaceholderFor(window, element);
				while (placeholder != null) {
					final MElementContainer<MUIElement> parent = placeholder.getParent();
					parent.getChildren().remove(placeholder);
					if (parent.getSelectedElement() == placeholder) {
						parent.setSelectedElement(null);
					}

					placeholder = modelService.findPlaceholderFor(window, element);
				}
				// See http://www.eclipse.org/forums/index.php/t/757211/

				// API not yet defined
				// modelService.delete(element);

				// Tom Shindl's suggestion -- not available in injection / eclipse context at this point in time
				// presentationEngine.removeGui(element);

				// Another suggestion
				// Probably not needed
				element.setToBeRendered(false);
				// Possible covered in the removeGui call
				window.getSharedElements().remove(element);
			}
		}

		// Remove part descriptor
		final Iterator<MPartDescriptor> itr = application.getDescriptors().iterator();
		while (itr.hasNext()) {
			final MPartDescriptor descriptor = itr.next();
			if (viewId.equals(descriptor.getElementId())) {
				itr.remove();
			}
		}
	}
}
