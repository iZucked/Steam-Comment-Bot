package com.mmxlabs.lngdataserver.lng.importers.distances.ui;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class DistanceSanityCheckPage extends WizardPage {
	
	private DistancesSelectionPage distancePage;
	private Table table;
	private Composite container;

	protected DistanceSanityCheckPage(String pageName, DistancesSelectionPage distancesPage) {
		super(pageName);
		this.distancePage = distancesPage;
		setTitle("Route lines that will be lost");
	}

	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		
		table = new Table(container, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		setControl(table);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		table.removeAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		Map<ScenarioInstance, Map<RouteOption, List<RouteLine>>> lostDistances = distancePage.getLostDistances();
		String[] titles = { "Scenario", "Route Option", "Route" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}

		for (Entry<ScenarioInstance, Map<RouteOption, List<RouteLine>>> scenarioEntry : lostDistances.entrySet()) {
			TableItem scenarioItem = new TableItem(table, SWT.NONE);
			scenarioItem.setText(0, scenarioEntry.getKey().getName());
			for (Entry<RouteOption, List<RouteLine>> routeEntry : scenarioEntry.getValue().entrySet()) {
				TableItem routeItem = new TableItem(table, SWT.NONE);
				routeItem.setText(1, routeEntry.getKey().getName());

				for (RouteLine currentLine : routeEntry.getValue()) {
					TableItem lineItem = new TableItem(table, SWT.NONE);
					lineItem.setText(2, currentLine.getFrom().getName() + " -> " + currentLine.getTo().getName());
				}
			}
		}
		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}
		table.setSize(table.computeSize(SWT.DEFAULT, 200));

	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public boolean isPageComplete() {
		return true;
	}
}
