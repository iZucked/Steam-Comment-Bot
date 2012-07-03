package com.mmxlabs.models.lng.analytics.ui.liveeval;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.analytics.presentation.AnalyticsEditorPlugin;

public class LiveEvaluatorStateCommandHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final Command command = event.getCommand();
		final boolean oldValue = HandlerUtil.toggleCommandState(command);
		// use the old value and perform the operation

		final BundleContext bundleContext = AnalyticsEditorPlugin.getPlugin().getBundle().getBundleContext();
		final ServiceReference<ILiveEvaluatorService> serviceReference = bundleContext.getServiceReference(ILiveEvaluatorService.class);

		final ILiveEvaluatorService service = bundleContext.getService(serviceReference);

		if (service != null) {
			service.setLiveEvaluatorEnabled(oldValue);
		}
		return null;
	}

}
