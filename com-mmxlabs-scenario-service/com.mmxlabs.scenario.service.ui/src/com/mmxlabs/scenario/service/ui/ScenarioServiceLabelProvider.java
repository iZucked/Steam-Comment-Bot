/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.internal.Activator;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceComposedAdapterFactory;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceNavigator;

public class ScenarioServiceLabelProvider extends AdapterFactoryLabelProvider implements IFontProvider, IColorProvider {

	private final Color Grey = new Color(Display.getCurrent(), new RGB(64, 64, 64));

	private final ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> selectionProviderTracker = new ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider>(
			Activator.getDefault().getBundle().getBundleContext(), IScenarioServiceSelectionProvider.class, null);

	private final Image showEnabledImage;
	private final Image showDisabledImage;

	private final Image pinImage;

	private Font boldFont;

	public ScenarioServiceLabelProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
		selectionProviderTracker.open();
		showEnabledImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/console_view.gif").createImage();
		showDisabledImage = ImageDescriptor.createWithFlags(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/console_view.gif"), SWT.IMAGE_DISABLE).createImage();
		pinImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/pin_editor.gif").createImage();

		final Font systemFont = Display.getDefault().getSystemFont();
		// Clone the font data
		final FontData fd = new FontData(systemFont.getFontData()[0].toString());
		// Set the bold bit.
		fd.setStyle(fd.getStyle() | SWT.BOLD);
		boldFont = new Font(Display.getDefault(), fd);
	}

	@Override
	public void dispose() {
		selectionProviderTracker.close();

		showEnabledImage.dispose();
		showDisabledImage.dispose();
		pinImage.dispose();

		boldFont.dispose();

		super.dispose();
	}

	@Override
	public String getColumnText(final Object object, final int columnIndex) {
		switch (columnIndex) {
		case ScenarioServiceNavigator.COLUMN_NAME_IDX:
			String text = super.getColumnText(object, columnIndex);

			if (object instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) object;
				if (scenarioInstance.isDirty()) {
					text = "* " + text;
				}
			}

			return text;
		default:
			return "";
		}
	}

	@Override
	public Image getColumnImage(final Object object, final int columnIndex) {
		switch (columnIndex) {
		case ScenarioServiceNavigator.COLUMN_SHOW_IDX:
			// virtual checkbox
			if (object instanceof ScenarioInstance) {
				final IScenarioServiceSelectionProvider service = selectionProviderTracker.getService();
				if (service != null) {
					if (service.isPinned((ScenarioInstance) object)) {
						return pinImage;
					} else if (service.isSelected((ScenarioInstance) object)) {
						return showEnabledImage;
					} else {
						return showDisabledImage;
					}
				}
			}
			return null;
		case ScenarioServiceNavigator.COLUMN_NAME_IDX:
			return super.getColumnImage(object, columnIndex);
		}
		return null;
	}

	@Override
	public Color getForeground(Object object) {

		if (object instanceof ScenarioService) {
			return Grey;
		}
		return super.getForeground(object);
	}

	@Override
	public Font getFont(Object object) {

		if (object instanceof ScenarioService) {
			return boldFont;
		}
		return super.getFont(object);
	}
}
