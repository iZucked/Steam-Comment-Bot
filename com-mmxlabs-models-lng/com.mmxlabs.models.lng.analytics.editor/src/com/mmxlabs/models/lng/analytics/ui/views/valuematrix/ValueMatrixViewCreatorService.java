package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.RunnerHelper;

public class ValueMatrixViewCreatorService {

	public static final String ValueMatrixViewCreatorService_Topic = "create-value-matrix-view";

	private static final Logger LOG = LoggerFactory.getLogger(ValueMatrixViewCreatorService.class);

	private final EventHandler eventHandler = event -> {
		try {
			final ValueMatrixScenario valueMatrixScenario = (ValueMatrixScenario) event.getProperty(IEventBroker.DATA);
			openView(valueMatrixScenario);
		} catch (Exception e) {
			LOG.error("Error handling create value matrix value event", e);
		}
	};

	private IEventBroker eventBroker;

	public void start() {
		new Thread() {
			@Override
			public void run() {
				while (!PlatformUI.isWorkbenchRunning()) {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
				RunnerHelper.asyncExec(() -> {
					eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
					eventBroker.subscribe(ValueMatrixViewCreatorService_Topic, eventHandler);
				});
			}
		}.start();;
	}

	public void stop() {
		if (eventBroker != null) {
			eventBroker.unsubscribe(eventHandler);
			eventBroker = null;
		}
	}

	private void openView(final ValueMatrixScenario valueMatrixScenario) {
		RunnerHelper.asyncExec(() -> {
			final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				final IViewPart part = activePage.showView(ValueMatrixModellerView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
				if (part instanceof @NonNull final ValueMatrixModellerView valueMatrixModellerView) {
					valueMatrixModellerView.openValueMatrixScenario(valueMatrixScenario);
				}
				return;
			} catch (final PartInitException e) {
				e.printStackTrace();
			}
		});
	}
}
