/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mmxlabs.lngdataserver.server.api.IDatasetEditor;
import com.mmxlabs.lngdataserver.server.api.IReportPublisher;
import com.mmxlabs.lngdataserver.server.editors.CargoEditor;
import com.mmxlabs.lngdataserver.server.editors.ContractEditor;
import com.mmxlabs.lngdataserver.server.reports.CargoReportPublisher;
import com.mmxlabs.lngdataserver.server.reports.NominationsReportPublisher;
import com.mmxlabs.lngdataserver.server.reports.ScheduleChartPublisher;
import com.mmxlabs.lngdataserver.server.reports.SourceToDestSankeyPublisher;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 * A simple servlet to support a WEB API for LiNGO. It serves up content based on the active editor.
 * 
 * The bundles "org.eclipse.equinox.http.jetty" and "org.eclipse.equinox.http.registry" need to be started to start the web server and register any handlers.
 * Handlers need to be registered via plugin.xml - see web-api-plugin.xml. This also points to a classpath folder (web_files) which can be used to service static content.
 * 
 * The following code can determine the port of the embedded server (once it is started).
 * <pre>
 * 		Bundle bundle = FrameworkUtil.getBundle(getClass());
		ServiceTracker<HttpServiceRuntime, HttpServiceRuntime> tracker = new ServiceTracker<>(bundle.getBundleContext(), HttpServiceRuntime.class, null);
		tracker.open();
		ServiceReference<HttpServiceRuntime>[] serviceReferences = tracker.getServiceReferences();
		if (serviceReferences != null) {
			for (var ref : serviceReferences) {
				System.out.println(ref.getProperty("http.port"));
			}
		}
		tracker.close();
	</pre>
 * 
 * @author Simon Goodall
 *
 */
public class APIServlet implements Servlet {

	private Map<String, IReportPublisher> reportHandlerMap = new HashMap<>();
	private Map<String, IDatasetEditor> editorHandlerMap = new HashMap<>();
	private WebSelectionHandler selectionHandler;

	@Override
	public void init(final ServletConfig config) throws ServletException {

		// Handler to respond to selection requests through API
		selectionHandler = new WebSelectionHandler();

		// Reports
		register(new SourceToDestSankeyPublisher());
		register(new ScheduleChartPublisher());
		register(new CargoReportPublisher());
		register(new NominationsReportPublisher());
		
		// Example editors
		register(new CargoEditor());
		register(new ContractEditor());
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {
		if (selectionHandler != null) {
			selectionHandler.dispose();
		}
	}

	private void register(Object o) {
		if (o instanceof IReportPublisher p) {
			reportHandlerMap.put(p.getReportName(), p);
		}
		if (o instanceof IDatasetEditor p) {
			editorHandlerMap.put(p.getEditorName(), p);
		}
	}

	@Override
	public void service(final ServletRequest req, final ServletResponse res) throws ServletException, IOException {

		if (res instanceof HttpServletResponse httpServletResponse) {
			httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			httpServletResponse.setHeader("Pragma", "no-cache");
			httpServletResponse.setHeader("Expires", "0");
			httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
			httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,PATCH");
			httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");

			// Okay by default
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);

			if (req.getParameter("selection") != null) {
				selectionHandler.handleSelection(req);
				return;
			}

			if (req.getParameter("report") != null) {
				var handler = reportHandlerMap.get(req.getParameter("report"));
				if (handler != null) {
					ServiceHelper.withCheckedServiceConsumer(com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider.class, ss -> {
						ScenarioResult pinnedInstance = ss.getPinned();
						if (pinnedInstance == null) {
							if (ss.getSelection().isEmpty()) {
								try (ServletOutputStream outputStream = res.getOutputStream()) {
									outputStream.println("[]");
								}
								return;
							}
							pinnedInstance = ss.getSelection().iterator().next();
						}
						if (pinnedInstance == null) {
							try (ServletOutputStream outputStream = res.getOutputStream()) {
								outputStream.println("[]");
							}
							return;
						}

						try (ServletOutputStream outputStream = res.getOutputStream()) {
							String json = handler.getJSONData(pinnedInstance, req);
							outputStream.print(json);
						} catch (final Exception e) {
							e.printStackTrace();
							httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						}
					});
				}
			} else if (req.getParameter("editor") != null) {
				var handler = editorHandlerMap.get(req.getParameter("editor"));
				if (handler != null) {
					ServiceHelper.withCheckedServiceConsumer(com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider.class, ss -> {
						ScenarioResult pinnedInstance = ss.getPinned();
						if (pinnedInstance == null) {
							if (ss.getSelection().isEmpty()) {
								try (ServletOutputStream outputStream = res.getOutputStream()) {
									outputStream.println("[]");
								}
								return;
							}
							pinnedInstance = ss.getSelection().iterator().next();
						}
						if (pinnedInstance == null) {
							try (ServletOutputStream outputStream = res.getOutputStream()) {
								outputStream.println("[]");
							}
							return;
						}

						if (req instanceof HttpServletRequest r && r.getMethod().equals("PATCH")) {
							try (ServletOutputStream outputStream = res.getOutputStream()) {
								String json = handler.update(pinnedInstance.getScenarioDataProvider(), r);
								outputStream.print(json);

							} catch (final Exception e) {
								httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
								e.printStackTrace();
							}
						} else {

							try (ServletOutputStream outputStream = res.getOutputStream()) {
								String json = handler.getJSONData(pinnedInstance.getScenarioDataProvider(), req);
								outputStream.print(json);
							} catch (final Exception e) {
								httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
								e.printStackTrace();
							}
						}
					});
				}
			}
			res.flushBuffer();
		}
	}

}
