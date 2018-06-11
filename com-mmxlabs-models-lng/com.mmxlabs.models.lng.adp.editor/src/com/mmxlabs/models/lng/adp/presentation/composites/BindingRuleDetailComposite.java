/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ext.IFlowTypeFactory;
import com.mmxlabs.models.lng.adp.presentation.wizard.ADPModelUtil;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class BindingRuleDetailComposite extends Composite implements IDisplayComposite {
	private IDisplayComposite delegate;
	private ICommandHandler commandHandler;
	private LNGScenarioModel rootObject;
	private final ComboViewer flowTypeSelector;
	private IDialogEditingContext dialogContext;

	public BindingRuleDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);

		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				removeAdapter();
			}
		});
		setLayout(new GridLayout(2, false));
		toolkit.adapt(this);

		delegate = new DefaultDetailComposite(this, style, toolkit);
		delegate.getComposite().setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		toolkit.createLabel(this, "Flow:");
		flowTypeSelector = new ComboViewer(this, SWT.DROP_DOWN);
		flowTypeSelector.setContentProvider(new ArrayContentProvider());
		flowTypeSelector.setLabelProvider(new FlowTypeFactoryLabelProvider());
		flowTypeSelector.getCombo().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection selection = flowTypeSelector.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					final Object firstElement = iStructuredSelection.getFirstElement();
					if (firstElement instanceof IFlowTypeFactory) {
						final IFlowTypeFactory factory = (IFlowTypeFactory) firstElement;

						bindingRule.setFlowType(factory.createInstance());
						dialogContext.getDialogController().rebuild(false);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		try {
			serviceReferences = bundleContext.getServiceReferences(IFlowTypeFactory.class, null);
		} catch (final InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		factories = new LinkedList<>();
		for (final ServiceReference<IFlowTypeFactory> ref : serviceReferences) {
			factories.add(bundleContext.getService(ref));

		}
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject value, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		if (bindingRule != null) {
			bindingRule.eAdapters().remove(adapter);
		}

		this.dialogContext = dialogContext;
		delegate.display(dialogContext, root, value, range, dbc);
		rootObject = (LNGScenarioModel) root;
		bindingRule = null;
		if (value instanceof BindingRule) {
			bindingRule = (BindingRule) value;
			flowTypeSelector.setInput(getFactoriesFor(bindingRule));
			IFlowTypeFactory currentFactory = getCurrentFactory((BindingRule) value);
			if (currentFactory != null) {
				flowTypeSelector.setSelection(new StructuredSelection(currentFactory));
			}

			bindingRule.eAdapters().add(adapter);
		}
	}

	private List<IFlowTypeFactory> getFactoriesFor(final BindingRule bindingRule) {

		final List<IFlowTypeFactory> l = new LinkedList<>();

		for (final IFlowTypeFactory ft : factories) {
			if (ft.validFor(bindingRule.getProfile())) {
				l.add(ft);
			}
		}
		return l;
	}

	private IFlowTypeFactory getCurrentFactory(final BindingRule bindingRule) {

		for (final IFlowTypeFactory ft : factories) {
			if (ft.isMatchForCurrent(bindingRule)) {
				return ft;
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		super.dispose();

		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		factories.clear();
		for (final ServiceReference<IFlowTypeFactory> ref : serviceReferences) {
			bundleContext.ungetService(ref);

		}
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		delegate.setCommandHandler(commandHandler);
		this.commandHandler = commandHandler;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		delegate.displayValidationStatus(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editors.IDisplayComposite#setEditorWrapper(com.mmxlabs.models.ui.editors.IInlineEditorWrapper)
	 */
	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		delegate.setEditorWrapper(wrapper);
	}

	@Override
	public boolean checkVisibility(final IDialogEditingContext context) {
		return delegate.checkVisibility(context);
	}

	EObject oldValue = null;
	final Adapter adapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			if (notification.getFeature() == ADPPackage.Literals.BINDING_RULE__PROFILE) {
				if (!isDisposed() && isVisible()) {
					if (flowTypeSelector != null && flowTypeSelector.getCombo().isDisposed() == false)

						if (notification.getNotifier() instanceof BindingRule) {
							final BindingRule bindingRule = (BindingRule) notification.getNotifier();
							flowTypeSelector.setInput(getFactoriesFor(bindingRule));
							IFlowTypeFactory currentFactory = getCurrentFactory(bindingRule);
							if (currentFactory != null) {
								flowTypeSelector.setSelection(new StructuredSelection(currentFactory));
							}
						}

					flowTypeSelector.refresh();
				} else {
					BindingRuleDetailComposite.this.removeAdapter();
				}
			}
		}

	};
	private BindingRule bindingRule;
	private Collection<ServiceReference<IFlowTypeFactory>> serviceReferences;
	private List<IFlowTypeFactory> factories;

	void removeAdapter() {
		if (oldValue != null) {
			oldValue.eAdapters().remove(adapter);
			oldValue = null;
		}
	}

}
