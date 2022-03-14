/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;

public class DataBrowserLabelProvider extends ColumnLabelProvider implements IColorProvider {

	private final Set<Node> selectedNodes;
	private final Color background;
	private final AdapterFactoryLabelProvider lp;

	private Image img_published;
	private Image img_local;
	private Image img_bc;

	private IBaseCaseVersionsProvider baseCaseProvider;

	public IBaseCaseVersionsProvider getBaseCaseProvider() {
		return baseCaseProvider;
	}

	public void setBaseCaseProvider(IBaseCaseVersionsProvider baseCaseProvider) {
		this.baseCaseProvider = baseCaseProvider;
	}

	public DataBrowserLabelProvider(final AdapterFactory adapterFactory, final Set<Node> selectedNodes) {
		lp = new AdapterFactoryLabelProvider(adapterFactory);
		this.selectedNodes = selectedNodes;
		this.background = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GREEN);

		img_published = Activator.createScaledImage("published");
		img_local = Activator.createScaledImage("local");
		img_bc = CommonImages.getImageDescriptor(IconPaths.BaseFlag, IconMode.Enabled).createImage();
	}

	@Override
	public void dispose() {
		if (img_local != null) {
			img_local.dispose();
			img_local = null;
		}
		if (img_published != null) {
			img_published.dispose();
			img_published = null;
		}
		if (img_bc != null) {
			img_bc.dispose();
			img_bc = null;
		}
		super.dispose();
	}

	@Override
	public Image getImage(final Object element) {

		if (element instanceof CompositeNode) {
			// TODO: Select an image!
		} else if (element instanceof Node) {
			// figure out if published
			final Node node = (Node) element;

			if (node.eContainer() instanceof CompositeNode) {
				final CompositeNode compositeNode = (CompositeNode) node.eContainer();

				if (compositeNode.getType() != null) {
					if (baseCaseProvider != null && Objects.equals(node.getVersionIdentifier(), baseCaseProvider.getVersion(compositeNode.getType()))) {
						return img_bc;
					}
				}
			}

			if (node.isPublished()) {
				return img_published;
			} else {
				return img_local;
			}
		}
		return null;

	}

	@Override
	public String getText(final Object element) {
		if (element instanceof Node) {
			final Node node = (Node) element;
			String prefix = "";
			// if (node.eContainer() instanceof CompositeNode) {
			// final CompositeNode compositeNode = (CompositeNode) node.eContainer();
			//
			// if (compositeNode.getType() != null) {
			// if (baseCaseProvider != null && Objects.equals(node.getVersionIdentifier(), baseCaseProvider.getVersion(compositeNode.getType()))) {
			// prefix = "** ";
			//
			// }
			// }
			// }
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
