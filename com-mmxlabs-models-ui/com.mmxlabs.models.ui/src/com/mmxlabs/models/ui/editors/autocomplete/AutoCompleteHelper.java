/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.autocomplete;

import java.util.Collection;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class AutoCompleteHelper {

	public static IMMXContentProposalProvider createTextControlProposalAdapter(final Text text, final EStructuralFeature attribute) {

		try {
			final Bundle bundle = FrameworkUtil.getBundle(AutoCompleteHelper.class);
			final BundleContext bundleContext = bundle.getBundleContext();
			Collection<ServiceReference<IContentProposalFactory>> serviceReferences;
			serviceReferences = bundleContext.getServiceReferences(IContentProposalFactory.class, null);
			for (final ServiceReference<IContentProposalFactory> ref : serviceReferences) {
				final IContentProposalFactory factory = bundleContext.getService(ref);
				if (factory != null) {
					final IMMXContentProposalProvider proposalProvider = factory.create(attribute);
					if (proposalProvider != null) {
						final ContentProposalAdapter proposalAdapter = new ContentProposalAdapter(text, new TextContentAdapter(), proposalProvider, getActivationKeystroke(), getAutoactivationChars());
						proposalAdapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
						proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
						proposalAdapter.setPropagateKeys(true);
						return proposalProvider;
					}
				}
				try {
				} finally {
					bundleContext.ungetService(ref);
				}
			}
		} catch (final InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	// this logic is from swt addons project
	private static char[] getAutoactivationChars() {

		final String LCL = "abcdefghijklmnopqrstuvwxy";
		final String UCL = LCL.toUpperCase();
		final String NUMS = "0123456789_+-*/%";
		// To enable content proposal on deleting a char
		final String delete = new String(new char[] { 8 });
		final String allChars = LCL + UCL + NUMS + delete;
		return allChars.toCharArray();
	}

	private static KeyStroke getActivationKeystroke() {
		final KeyStroke instance = KeyStroke.getInstance(new Integer(SWT.CTRL).intValue(), new Integer(' ').intValue());
		return instance;
	}
}
