/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.debug.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.rcp.common.debug.DebugOptionsLevel;
import com.mmxlabs.rcp.common.debug.DebugOptionsUtil;
import com.mmxlabs.rcp.common.debug.IDebugOptionsProvider;

public class DebugOptionsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private static final Logger LOG = LoggerFactory.getLogger(DebugOptionsPreferencePage.class);

	private ServiceReference<DebugOptions> serviceReference;

	private DebugOptions debugOptions;

	private List<Pair<IDebugOptionsProvider, DebugOptionsLevel>> content;
	private boolean debugEnabled;

	private final List<IDebugOptionsProvider> debugProviders = new LinkedList<>();

	@Override
	public void dispose() {
		debugOptions = null;
		if (serviceReference != null) {
			final BundleContext bundleContext = FrameworkUtil.getBundle(DebugOptionsPreferencePage.class).getBundleContext();
			bundleContext.ungetService(serviceReference);
		}
		debugProviders.clear();
		super.dispose();
	}

	@Override
	public void init(final IWorkbench workbench) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(DebugOptionsPreferencePage.class).getBundleContext();
		serviceReference = bundleContext.getServiceReference(DebugOptions.class);
		if (serviceReference == null) {
			throw new RuntimeException("DebugOptions service not found");
		}
		debugOptions = bundleContext.getService(serviceReference);

		debugProviders.addAll(DebugOptionsUtil.getDebugOptionsProviders());
		Collections.sort(debugProviders, (a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getName(), b.getName()));
	}

	@Override
	protected Control createContents(final Composite parent) {

		final Composite c = new Composite(parent, SWT.None);

		c.setLayout(new GridLayout(1, false));

		{
			final Label descLabel = new Label(c, SWT.NONE);
			descLabel.setText("These should normally be disabled and set to Off unless requested by Minimax Labs.");
			descLabel.setLayoutData(GridDataFactory.fillDefaults().create());

			// Just for inserting some space
			new Label(c, SWT.NONE);
		}
		List<Control> controls = new LinkedList<>();
		{
			final Composite g = new Composite(c, SWT.NONE);

			g.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());

			final Label l = new Label(g, SWT.NONE);
			l.setText("Enable");

			final Button btn = new Button(g, SWT.CHECK);
			debugEnabled = debugOptions.isDebugEnabled();
			btn.setSelection(debugEnabled);
			btn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
					debugEnabled = btn.getSelection();
					controls.forEach(ctl -> ctl.setEnabled(debugEnabled));

				}
			});
		}
		content = new LinkedList<>();
		for (final var debugProvider : debugProviders) {
			if (!debugProvider.isAvailable()) {
				continue;
			}

			final Composite g = new Composite(c, SWT.NONE);

			g.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());

			final Label l = new Label(g, SWT.NONE);
			l.setText(debugProvider.getName());
			l.setToolTipText(debugProvider.getDescription());

			final ComboViewer objectSelector = new ComboViewer(g, SWT.DROP_DOWN);
			objectSelector.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(150, SWT.DEFAULT).create());
			objectSelector.setContentProvider(new ArrayContentProvider());
			objectSelector.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(final Object element) {
					if (element instanceof final DebugOptionsLevel lvl) {
						switch (lvl) {
						case OFF:
							return "Off";
						case ON:
							return "On";
						case PARTIAL:
							return "Partial (externally configured)";
						}
						return lvl.toString();
					}
					return super.getText(element);
				}
			});
			final Pair<IDebugOptionsProvider, DebugOptionsLevel> p = new Pair<>(debugProvider, debugProvider.getCurrentLevel(debugOptions));
			final List<DebugOptionsLevel> lvlOptions = new LinkedList<>();
			lvlOptions.addAll(debugProvider.getSupportedLevels());
			if (p.getSecond() == DebugOptionsLevel.PARTIAL) {
				lvlOptions.add(p.getSecond());
			}
			objectSelector.setInput(lvlOptions);
			objectSelector.addSelectionChangedListener(evt -> {
				p.setSecond((DebugOptionsLevel) evt.getStructuredSelection().getFirstElement());
			});
			objectSelector.setSelection(new StructuredSelection(p.getSecond()));
			content.add(p);

			controls.add(objectSelector.getControl());
		}

		// GridDataFactory dataFactory = GridDataFactory.swtDefaults().minSize(300,
		// SWT.DEFAULT).hint(300, SWT.DEFAULT);
		if (content.isEmpty()) {
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("No debug logging options available");
		}

		controls.forEach(ctl -> ctl.setEnabled(debugEnabled));

		return c;
	}

	@Override
	public boolean performOk() {

		debugOptions.setDebugEnabled(debugEnabled);
		if (debugEnabled) {

			if (content != null) {
				for (final var p : content) {
					p.getFirst().apply(debugOptions, p.getSecond());
				}
			}
		}

		return super.performOk();
	}
}
