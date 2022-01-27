/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
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

	private final Font boldFont;

	public ScenarioServiceLabelProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
		selectionProviderTracker.open();
		showEnabledImage = CommonImages.getImageDescriptor(IconPaths.Console, IconMode.Enabled).createImage(); 
		showDisabledImage = CommonImages.getImageDescriptor(IconPaths.Console, IconMode.Disabled).createImage(); 
		pinImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/pin_scenario.png").createImage();

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

			if (object instanceof ScenarioInstance scenarioInstance) {
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
				if (modelRecord != null) {
					// We can be blocked on migration if we are loading when checking for a reference
					if (modelRecord.isLoaded()) {
						try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("ScenarioServiceLabelProvider")) {
							if (ref != null && ref.isDirty()) {
								text = "* " + text;
							}
						}
					}
				}

				final IScenarioService ss = SSDataManager.Instance.findScenarioService(scenarioInstance);
				if (ss != null && !ss.getServiceModel().isLocal()) {
					final Metadata metadata = scenarioInstance.getMetadata();
					if (metadata != null) {
						final Date created = metadata.getCreated();
						if (created != null) {
							final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							format.setTimeZone(TimeZone.getTimeZone("UTC"));
							text += " [" + format.format(created) + "]" ;// [" + metadata.getCreator() + "]";
						}
					}
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
			} else if (object instanceof ScenarioFragment) {
				// ScenarioFragment scenarioFragment = (ScenarioFragment) object;
				// final IScenarioServiceSelectionProvider service = selectionProviderTracker.getService();
				// if (service != null) {
				// if (service.isPinned((ScenarioFragment) object)) {
				// return pinImage;
				// } else if (service.isSelected((ScenarioFragment) object)) {
				// return showEnabledImage;
				// } else {
				// return showDisabledImage;
				// }
				// }
			}
			return null;
		case ScenarioServiceNavigator.COLUMN_NAME_IDX:
			return super.getColumnImage(object, columnIndex);
		}
		return null;
	}

	@Override
	public Color getForeground(final Object object) {

		if (object instanceof ScenarioService) {
			return Grey;
		}
		return super.getForeground(object);
	}

	@Override
	public Font getFont(final Object object) {

		if (object instanceof ScenarioService) {
			return boldFont;
		}
		return super.getFont(object);
	}
}
