/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.internal;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * E4 model processor to remove e4 tool items.
 * 
 * TODO: Remove in 3.9.4 release + 2
 * 
 * @author Simon Goodall
 *
 */
public class FixActionSetsModelProcessor {

	@Execute
	public void execute(final MApplication application, final EModelService modelService) {
		{
			final MUIElement element = modelService.find("com.mmxlabs.lingo.reports.handledtoolitem.filterschedulechartbyselection", application);
			if (element != null) {
				element.getParent().getChildren().remove(element);
			}
		}
		{
			final MUIElement element = modelService.find("com.mmxlabs.lingo.reports.handledtoolitem.filterschedulesummarybyselection", application);
			if (element != null) {
				element.getParent().getChildren().remove(element);
			}
		}
	}
}
