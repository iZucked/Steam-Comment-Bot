package com.mmxlabs.lngdataserver.server.api;

import javax.servlet.ServletRequest;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.ScenarioResult;

public interface IReportPublisher {

	String getReportName();

	String getJSONData(@NonNull ScenarioResult sr, @NonNull ServletRequest request) throws Exception;
}
