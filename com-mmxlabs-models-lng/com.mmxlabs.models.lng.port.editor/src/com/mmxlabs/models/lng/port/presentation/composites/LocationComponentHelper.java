/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.timezone.ITimezoneProvider;
import com.mmxlabs.common.timezone.impl.GoogleTimezoneProvider;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.TimezoneInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Location instances
 *
 * @generated
 */
public class LocationComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public LocationComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public LocationComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.OTHER_NAMES_OBJECT));
	}
	
	/**
	 * add editors to a composite, using Location as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PortPackage.Literals.LOCATION);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_mmxIdEditor(detailComposite, topClass);
		add_timeZoneEditor(detailComposite, topClass);
		add_countryEditor(detailComposite, topClass);
		add_latEditor(detailComposite, topClass);
		add_lonEditor(detailComposite, topClass);
		add_otherIdentifiersEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the mmxId feature on Location
	 *
	 * @generated NOT
	 */
	protected void add_mmxIdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.LOCATION__MMX_ID));
	}

	/**
	 * Create the editor for the timeZone feature on Location
	 *
	 * @generated NOT
	 */
	protected void add_timeZoneEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TimezoneInlineEditor(PortPackage.Literals.LOCATION__TIME_ZONE) {
		@Override
		public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

			final Control control = super.createControl(parent, dbc, toolkit);

			/** Insert button to lookup timezone from lat/lon */

			final Button btn = new Button(control.getParent(), SWT.PUSH);
			btn.setText("Lookup");
			btn.setToolTipText("Attempt to lookup timezone from lat/lon co-ordinates");
			btn.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					final ITimezoneProvider tzProvider = new GoogleTimezoneProvider();

					if (input instanceof Port) {
						final Port port = (Port) input;
						final Location l = port.getLocation();
						if (l != null) {
							try {
								final TimeZone tz = tzProvider.findTimeZone((float) l.getLat(), (float) l.getLon());
								if (tz != null) {
									final EditingDomain ed = commandHandler.getEditingDomain();
										final String timeZoneId = tz.getID();
										if (timeZoneId != null && !timeZoneId.equals(l.getTimeZone())) {
											final Command cmd = SetCommand.create(ed, l, PortPackage.eINSTANCE.getLocation_TimeZone(), timeZoneId);
											commandHandler.handleCommand(cmd, l, PortPackage.eINSTANCE.getLocation_TimeZone());
										}
									}
							} catch (final Exception ex) {
								// Ignore - TODO - provide some feedback to user?
							}
						}
					}

				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {

				}
			});

			return control;
		}
	});

	}

	/**
	 * Create the editor for the country feature on Location
	 *
	 * @generated
	 */
	protected void add_countryEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.LOCATION__COUNTRY));
	}
	/**
	 * Create the editor for the lat feature on Location
	 *
	 * @generated
	 */
	protected void add_latEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.LOCATION__LAT));
	}
	/**
	 * Create the editor for the lon feature on Location
	 *
	 * @generated
	 */
	protected void add_lonEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.LOCATION__LON));
	}

	/**
	 * Create the editor for the otherIdentifiers feature on Location
	 *
	 * @generated NOT
	 */
	protected void add_otherIdentifiersEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.LOCATION__OTHER_IDENTIFIERS));
	}
}