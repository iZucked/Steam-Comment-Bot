package com.mmxlabs.models.ui.editors;

import java.util.function.Consumer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class HoverActionHelper {

	public static Control createAddAction(final Composite parent, final Consumer<LocalMenuHelper> menuCreator) {

		final Label c = new Label(parent, SWT.NONE);
		final ImageDescriptor baseAdd = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD);
		final Image image_grey_add = ImageDescriptor.createWithFlags(baseAdd, SWT.IMAGE_GRAY).createImage();
		c.setImage(image_grey_add);

		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				image_grey_add.dispose();
			}
		});

		c.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).hint(16, 16).grab(true, false).create());
		c.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(final MouseEvent e) {

			}

			@Override
			public void mouseExit(final MouseEvent e) {
				c.setImage(image_grey_add);
			}

			@Override
			public void mouseEnter(final MouseEvent e) {
				c.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
			}
		});
		c.addMouseListener(new MouseListener() {
			LocalMenuHelper helper = new LocalMenuHelper(c.getParent());

			@Override
			public void mouseUp(final MouseEvent e) {

			}

			@Override
			public void mouseDown(final MouseEvent e) {

				helper.clearActions();

				menuCreator.accept(helper);

				helper.open();
			}

			@Override
			public void mouseDoubleClick(final MouseEvent e) {

			}
		});
		return c;
	}
}
