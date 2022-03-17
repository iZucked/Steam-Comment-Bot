/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.EList;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.preferences.PreferenceConstants;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scenario.service.ScenarioResult;

public class IncomeStatementByRegion extends AbstractIncomeStatement<String> {

	private static final List<String> DEFAULT_REGIONS = Arrays.asList("JKTC", Regions.EUROPE.name(), Regions.AMERICAS.name(), Regions.FAR_EAST_AND_MIDDLE_EAST.name(), Regions.OTHER.name());
	private Map<PortModel, Map<String, String>> portModelToPortToRegionMap = new HashMap<>();
	private String other = Regions.OTHER.name();
	
	public IncomeStatementByRegion() {
		super("com.mmxlabs.lingo.reports.Reports_IncomeStatementByRegion");
	}

	private List<String> getRegions() {	
	    IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		String regionsStr = prefs.get(PreferenceConstants.REPORT_REGIONS_LIST, "");
		if (!regionsStr.isBlank()) {
			String[] regions = regionsStr.replace("\r", "").split("\n");
			if (regions.length > 0) {
				if (Arrays.stream(regions).anyMatch(k -> k.equalsIgnoreCase(Regions.OTHER.name()))) {
					//Insure other string matches exactly.
					for (String region : regions) {
						if (region.equalsIgnoreCase(Regions.OTHER.name())) {
							this.other = region;
						}
					}
					return Arrays.asList(regions);
				}
				else {
					List<String> regionsList = new ArrayList<>();
					regionsList.addAll(Arrays.asList(regions));
					regionsList.add(Regions.OTHER.name());
					return regionsList;
				}
			}
		}
		return DEFAULT_REGIONS;
	}
	
	private Map<String,String> getPortToRegionMap(PortModel portModel) {
		return this.portModelToPortToRegionMap.get(portModel);
	}

	private void computePortToRegionMap(PortModel portModel) {
		//Compute.
		List<String> regions = this.getRegions();
		Map<String, String> portToRegionMap = new HashMap<>();
		
		for (String region : regions) {
			PortGroup regionPG = getPortGroupForRegion(portModel, region);
			
			if (regionPG != null) {
				for (APortSet port : SetUtils.getObjects(regionPG)) {
					portToRegionMap.put(port.getName().toLowerCase(), region);
				}
			}
		}
		
		this.portModelToPortToRegionMap.put(portModel, portToRegionMap);
	}
	
	protected IncomeStatementData getIncomeByMonth(final ScenarioResult scenarioResult, final Schedule schedule, final LineItems lineItem) {
		PortModel pm = ScenarioModelUtil.getPortModel(scenarioResult.getScenarioDataProvider());
		this.computePortToRegionMap(pm);
		return super.getIncomeByMonth(scenarioResult, schedule, lineItem);
	}

	private PortGroup getPortGroupForRegion(PortModel portModel, String region) {
		EList<PortGroup> pgs = portModel.getPortGroups();
		PortGroup regionPG = null;
		for (PortGroup pg : pgs) {
			if (region.equalsIgnoreCase(pg.getName())) {
				regionPG = pg;
			}
		}
		return regionPG;
	}
	
	@Override
	protected String getSubType(final DischargeSlot dischargeSlot) {	
		if (dischargeSlot != null) {
			final Port port = dischargeSlot.getPort();
			if (port != null && port.getName() != null) {
				final PortModel pm = (PortModel)port.eContainer();

				Map<String,String> portToRegion = getPortToRegionMap(pm);
				String region = portToRegion.get(port.getName().toLowerCase());

				if (region != null) {
					return region;
				}		
			}
		}
		return other;
	}

	@Override
	protected Collection<String> getSubTypes() {
		return Lists.newArrayList(this.getRegions());
	}

	@Override
	protected int doCompare(final IncomeStatementData o1, final IncomeStatementData o2) {
		if (o1.key instanceof Enum && o2.key instanceof Enum) {
			return ((Enum)o1.key).ordinal() - ((Enum)o2.key).ordinal();
		}
		else {
			String k1 = (String)o1.key;
			String k2 = (String)o2.key;
			return k1.compareTo(k2);
		}
	}

	@Override
	protected void resetData() {
		//Clean out port to region map, in case for some reason data has changed.
		this.portModelToPortToRegionMap = new HashMap<>();
	}

	@Override
	protected String doGetColumnText(final IncomeStatementData data) {
		return data.key.toString();
	}
}