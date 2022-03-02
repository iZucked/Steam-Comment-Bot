/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class LimitWidgetHeightListener implements Listener {
	private final Composite pparent;
	private final Button btn;

	public LimitWidgetHeightListener(final Composite pparent, final Button btn) {
		this.pparent = pparent;
		this.btn = btn;
	}

	@Override
	public void handleEvent(final Event arg0) {
		// This should be the toolbar this control set is contained in.
		final Composite toolbarComposite = pparent.getParent();
		// Get the height of the composite.
		int toolbarHeight = toolbarComposite.getSize().y;
		// Fix issue in workbench. On opening the application, the toolbar height is 44 allowing large buttons. However the real height is only 22 (and this is correct when opening an editor after
		// the application has opened). This will probably cause issues on e.g. Alex's machine when running at high resolution (3840x2160 -- icons are already tiny on this mode).
		if (toolbarHeight > 35) {
			toolbarHeight = 35;
		}
		final Point size = btn.getSize();

		// Clamp to toolbar height.
		if (size.y > toolbarHeight) {
			btn.setSize(size.x, toolbarHeight);
		}
	}
}