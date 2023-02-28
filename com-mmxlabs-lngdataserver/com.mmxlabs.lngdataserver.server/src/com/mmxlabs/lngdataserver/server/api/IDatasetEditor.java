package com.mmxlabs.lngdataserver.server.api;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface IDatasetEditor {

	String getEditorName();

	String getJSONData(IScenarioDataProvider sdp, ServletRequest request) throws Exception;

	String update(@NonNull IScenarioDataProvider scenarioDataProvider, HttpServletRequest r) throws Exception;
}
