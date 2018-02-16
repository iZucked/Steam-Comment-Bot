package com.mmxlabs.lngdataserver.browser.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;

public class DataBrowserLabelProvider extends ColumnLabelProvider implements IColorProvider {

	private final Set<Node> selectedNodes;
	private final Color background;
	private final AdapterFactoryLabelProvider lp;

	public DataBrowserLabelProvider(final AdapterFactory adapterFactory, final Set<Node> selectedNodes) {
		lp = new AdapterFactoryLabelProvider(adapterFactory);
		this.selectedNodes = selectedNodes;
		this.background = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GREEN);
	}

	@Override
	public Image getImage(final Object element) {

		if (element instanceof CompositeNode) {
			// TODO: add icons for all data types?
			return new Image(PlatformUI.getWorkbench().getDisplay(), 16, 16);
		} else if (element instanceof Node) {

			// figure out if published

			String imageType;
			if (((Node) element).isPublished()) {
				imageType = "published";
			} else {
				imageType = "local";
			}

			final Bundle bundle = FrameworkUtil.getBundle(Activator.class);
			ImageDataProvider imageDataProvider;
			try {
				final String basePath = new File(FileLocator.toFileURL(bundle.getResource("/icons/")).toURI()).getAbsolutePath();

				imageDataProvider = zoom -> {
					switch (zoom) {
					case 150:
						return new ImageData(basePath + "/" + imageType + "_24.png");
					case 200:
						return new ImageData(basePath + "/" + imageType + "_32.png");
					default:
						return new ImageData(basePath + "/" + imageType + "_16.png");
					}
				};
			} catch (URISyntaxException | IOException e) {
				throw new RuntimeException(e);
			}

			return new Image(PlatformUI.getWorkbench().getDisplay(), imageDataProvider);
		} else {
			throw new RuntimeException("Unknown data element");
		}
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof Node) {
			final Node node = (Node) element;
			String prefix = "";
			if (node.eContainer() instanceof CompositeNode) {
				final CompositeNode compositeNode = (CompositeNode) node.eContainer();
				if (compositeNode.getLatest() == node) {
					prefix = "** ";
				}
			}
			return prefix + node.getDisplayName();
		}
		return "Error displaying label";
	}

	@Override
	public Color getBackground(final Object object) {

		if (selectedNodes.contains(object)) {
			return background;
		}

		return super.getBackground(object);
	}
}
