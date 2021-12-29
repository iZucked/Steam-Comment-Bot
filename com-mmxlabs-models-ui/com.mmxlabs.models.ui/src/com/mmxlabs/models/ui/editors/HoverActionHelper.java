/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.function.Consumer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class HoverActionHelper {
	private HoverActionHelper() {
	}

	public static Label createAddAction(final Composite parent, final Consumer<LocalMenuHelper> menuCreator) {

		final Label c = new Label(parent, SWT.NONE);

		c.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).hint(16, 16).grab(true, false).create());
		final ImageDescriptor baseAdd = CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled);
		final Image imageAdd = baseAdd.createImage();  
		parent.addDisposeListener(e -> imageAdd.dispose());
		final Image imageGreyAdd = ImageDescriptor.createWithFlags(baseAdd, SWT.IMAGE_GRAY).createImage();
		c.setImage(imageGreyAdd);
		parent.addDisposeListener(e -> imageGreyAdd.dispose());

		c.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseExit(final MouseEvent e) {
				c.setImage(imageGreyAdd);
			}

			@Override
			public void mouseEnter(final MouseEvent e) {
				
				c.setImage(imageAdd);
			}
		});
		c.addMouseListener(new MouseAdapter() {
			LocalMenuHelper helper = new LocalMenuHelper(c.getParent());

			@Override
			public void mouseDown(final MouseEvent e) {

				helper.clearActions();
				menuCreator.accept(helper);
				helper.open();
			}

		});
		return c;
	}
}
