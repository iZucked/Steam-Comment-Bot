/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.autocomplete;

import java.util.Collection;
import java.util.function.Function;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class AutoCompleteHelper {

	public static IMMXContentProposalProvider createControlProposalAdapter(final Control text, final EStructuralFeature attribute) {
		return createControlProposalAdapter(text, factory -> factory.create(attribute));
	}

	public static IMMXContentProposalProvider createControlProposalAdapter(final Control text, final String attribute) {
		return createControlProposalAdapter(text, factory -> factory.create(attribute));
	}

	public static IMMXContentProposalProvider createControlProposalAdapter(final Control control, final Function<IContentProposalFactory, IMMXContentProposalProvider> supplier) {

		IControlContentAdapter controlContentAdapter = null;
		if (control instanceof Text) {
			controlContentAdapter = new TextContentAdapter();
		} else if (control instanceof Combo) {
			controlContentAdapter = new ComboContentAdapter();
		} else if (control instanceof CCombo) {
			controlContentAdapter = new CComboContentAdapter();
		} else {
			return null;
		}

		try {
			final Bundle bundle = FrameworkUtil.getBundle(AutoCompleteHelper.class);
			final BundleContext bundleContext = bundle.getBundleContext();
			Collection<ServiceReference<IContentProposalFactory>> serviceReferences;
			serviceReferences = bundleContext.getServiceReferences(IContentProposalFactory.class, null);
			for (final ServiceReference<IContentProposalFactory> ref : serviceReferences) {
				final IContentProposalFactory factory = bundleContext.getService(ref);
				if (factory != null) {
					final IMMXContentProposalProvider proposalProvider = supplier.apply(factory);
					if (proposalProvider != null) {
						return createProposal(control, controlContentAdapter, proposalProvider);
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

	public static <T extends IContentProposalProvider> T createProposal(final Control control, IControlContentAdapter controlContentAdapter, final T proposalProvider) {
		final ContentProposalAdapter proposalAdapter = new ContentProposalAdapter(control, controlContentAdapter, proposalProvider, getActivationKeystroke(), getAutoactivationChars());
		proposalAdapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
		proposalAdapter.setPropagateKeys(true);
		return proposalProvider;
	}

	// this logic is from swt addons project
	public static char[] getAutoactivationChars() {

		final String LCL = "abcdefghijklmnopqrstuvwxy";
		final String UCL = LCL.toUpperCase();
		final String NUMS = "0123456789_+-*/%";
		// To enable content proposal on deleting a char
		final String delete = new String(new char[] { 8 });
		final String allChars = LCL + UCL + NUMS + delete;
		return allChars.toCharArray();
	}

	public static KeyStroke getActivationKeystroke() {
		final KeyStroke instance = KeyStroke.getInstance(Integer.valueOf(SWT.CTRL).intValue(), Integer.valueOf(' ').intValue());
		return instance;
	}
}
