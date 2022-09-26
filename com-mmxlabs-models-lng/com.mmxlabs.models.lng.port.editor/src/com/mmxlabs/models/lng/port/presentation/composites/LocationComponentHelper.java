/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import java.util.TimeZone;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.timezone.ITimezoneProvider;
import com.mmxlabs.common.timezone.impl.GoogleTimezoneProvider;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TimezoneInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for Location instances
 *
 * @generated NOT
 */
public class LocationComponentHelper extends DefaultComponentHelper {

	public LocationComponentHelper() {
		super(PortPackage.Literals.LOCATION);

		ignoreFeatures.add(PortPackage.Literals.LOCATION__MMX_ID);

		addEditor(PortPackage.Literals.LOCATION__TIME_ZONE, createTimeZoneEditor());

	}

	/**
	 * Create the editor for the timeZone feature on Location
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createTimeZoneEditor() {
		return topClass -> new TimezoneInlineEditor(PortPackage.Literals.LOCATION__TIME_ZONE) {
			@Override
			public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

				final Control control = super.createControl(parent, dbc, toolkit);

				/** Insert button to lookup timezone from lat/lon */

				final Button btn = new Button(control.getParent(), SWT.PUSH);
				btn.setText("Lookup");
				btn.setToolTipText("Attempt to lookup timezone from lat/lon co-ordinates");
				btn.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final ITimezoneProvider tzProvider = new GoogleTimezoneProvider();

						if (input instanceof Port port) {
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
				});
				return control;
			}
		};
	}
}