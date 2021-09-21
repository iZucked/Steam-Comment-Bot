package com.mmxlabs.lingo.reports;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.Grid;

import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.CopyGridToJSONUtil;

public class ReportContentsGenerators {

	public static IReportContentsGenerator createContentsFor(ISelectedScenariosServiceListener selectedScenariosServiceListener, Grid grid) {
		
		return (pin, other, selectedObjects) -> {
			final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
			if (pin != null) {
				provider.addScenario(pin);
				provider.setPinnedScenarioInstance(pin);
			}
			if (other != null) {
				provider.addScenario(other);
			}
			// Request a blocking update ...
			selectedScenariosServiceListener.selectedDataProviderChanged(provider, true);
			
			// ... so the data is ready to be read here.
			final CopyGridToJSONUtil jsonUtil = new CopyGridToJSONUtil(grid, true);
			final String jsonContents = jsonUtil.convert();
			
			// ... so the data is ready to be read here.
			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(grid, false, true);
			final String htmlContents = util.convert();
			
			return ReportContents.make(htmlContents, jsonContents);
		};
	}
	public static IReportContentsGenerator createJSONFor(ISelectedScenariosServiceListener selectedScenariosServiceListener, Grid grid) {

		return (pin, other, selectedObjects) -> {
			final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
			if (pin != null) {
				provider.addScenario(pin);
				provider.setPinnedScenarioInstance(pin);
			}
			if (other != null) {
				provider.addScenario(other);
			}
			// Request a blocking update ...
			selectedScenariosServiceListener.selectedDataProviderChanged(provider, true);

			// ... so the data is ready to be read here.
			final CopyGridToJSONUtil jsonUtil = new CopyGridToJSONUtil(grid, true);
			final String jsonContents = jsonUtil.convert();
			return ReportContents.makeJSON(jsonContents);
		};
	}

	public static IReportContentsGenerator createHTMLFor(ISelectedScenariosServiceListener selectedScenariosServiceListener, Grid grid, boolean withColours) {
		return createHTMLFor(selectedScenariosServiceListener, grid, withColours, withColours, false, null);
	}

	public static IReportContentsGenerator createHTMLFor(ISelectedScenariosServiceListener selectedScenariosServiceListener, Grid grid, boolean withBackgroundColours, boolean withForegroundColours,
			boolean withRowHeaders, @Nullable Runnable action) {

		return (pin, other, selectedObjects) -> {

			final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
			if (pin != null) {
				provider.addScenario(pin);
				provider.setPinnedScenarioInstance(pin);
			}
			if (other != null) {
				provider.addScenario(other);
			}
			// Request a blocking update ...
			selectedScenariosServiceListener.selectedDataProviderChanged(provider, true);

			if (action != null) {
				action.run();
			}

			// ... so the data is ready to be read here.
			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(grid, withRowHeaders, true);
			util.setShowBackgroundColours(withBackgroundColours);
			util.setShowForegroundColours(withForegroundColours);
			final String contents = util.convert();
			return ReportContents.makeHTML(contents);
		};
	}
}
