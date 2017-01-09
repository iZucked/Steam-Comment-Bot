/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.application.BindSelectionListener;
import com.mmxlabs.rcp.common.application.IInjectableE4ComponentFactory;

public class EmbeddedReportComponent extends AbstractSandboxComponent {
	private IEclipseContext reportContext;
	private Object reportObject;
	private String componentId;
	private String componentName;
	private IViewSite viewSite;
	private Consumer<IEclipseContext> contextConsumer;

	public EmbeddedReportComponent(@NonNull IScenarioEditingLocation scenarioEditingLocation, Map<Object, IStatus> validationErrors, @NonNull Supplier<OptionAnalysisModel> modelProvider,
			String componentId, String componentName, IViewSite viewSite, final Consumer<IEclipseContext> contextConsumer) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
		this.componentId = componentId;
		this.componentName = componentName;
		this.viewSite = viewSite;
		this.contextConsumer = contextConsumer;
	}

	@Override
	public void createControls(final Composite parent, boolean expanded, final IExpansionListener expansionListener, OptionModellerView optionModellerView) {
		final Pair<Object, IEclipseContext> p = createReportControl(componentId, componentName, parent, expansionListener, expanded);
		this.reportObject = p.getFirst();
		this.reportContext = p.getSecond();

	}

	private Pair<Object, IEclipseContext> createReportControl(final String componentId, final String title, final Composite parent, IExpansionListener l, boolean expanded) {

		final Pair<Object, IEclipseContext> result = new Pair<>();

		final String filter = String.format("(partId=%s)", componentId);
		ServiceHelper.withAllServices(IInjectableE4ComponentFactory.class, filter, service -> {
			if (service != null) {
				final IEclipseContext ctx = viewSite.getService(IEclipseContext.class);

				final ExpandableComposite expandable = wrapInExpandable(parent, title, (p) -> {
					final Composite componentComposite = new Composite(p, SWT.NONE);
					componentComposite.setLayout(new GridLayout(1, true));
					componentComposite.setLayoutData(GridDataFactory.fillDefaults()//
							.grab(true, true)//
							.hint(200, SWT.DEFAULT) //
							// .span(1, 1) //
							.align(SWT.FILL, SWT.FILL).create());

					final IEclipseContext componentContext = ctx.createChild();
					componentContext.set(Composite.class, componentComposite);

					contextConsumer.accept(componentContext);

					final Object component = ContextInjectionFactory.make(service.getComponentClass(), componentContext);

					componentContext.set(String.class, viewSite.getId());
					ContextInjectionFactory.invoke(component, BindSelectionListener.class, componentContext);

					result.setBoth(component, componentContext);

					return componentComposite;
				});
				expandable.setExpanded(expanded);
				expandable.addExpansionListener(l);
				return false;
			}
			return true;
		});

		if (result.getFirst() != null) {
			return result;
		}
		return null;
	}

	@Override
	public void refresh() {

	}

	@Override
	public void dispose() {
		if (reportObject != null) {
			ContextInjectionFactory.invoke(reportObject, PreDestroy.class, reportContext);
			reportObject = null;
		}
		if (reportContext != null) {
			reportContext.dispose();
			reportContext = null;
		}

		super.dispose();
	}

}
