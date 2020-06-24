/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.types.APortSet;

public class IncomeStatementByRegion extends AbstractIncomeStatement<String> {

	//Hard coded list until we have preferences done.
	String[] REGIONS = new String[] { "JKTC", Regions.EUROPE.name(), Regions.AMERICAS.name(), Regions.FAR_EAST_AND_MIDDLE_EAST.name(), Regions.OTHER.name() };
	Map<String, String> portToRegionMap = new HashMap<>();
	
	public IncomeStatementByRegion() {
		super("com.mmxlabs.lingo.reports.Reports_IncomeStatementByRegion");
	}

	private String[] getRegions() {	
		return REGIONS;
	}
	
	private Map<String,String> getPortToRegionMap(PortModel portModel) {
		if (this.portToRegionMap.isEmpty() && portModel != null) {
			//Compute.
			String[] regions = this.getRegions();
			
			for (String region : regions) {
				PortGroup regionPG = getPortGroupForRegion(portModel, region);
				
				if (regionPG != null) {
					for (APortSet port : regionPG.getContents()) {
						this.portToRegionMap.put(port.getName(), region);
					}
				}
			}
		}
		
		return this.portToRegionMap;
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
		if (dischargeSlot == null) {
			return Regions.OTHER.name();
		}
		final Port port = dischargeSlot.getPort();
		if (port != null && port.getName() != null) {
			final PortModel pm = (PortModel)port.eContainer();
			
			Map<String,String> portToRegion = getPortToRegionMap(pm);
			String region = portToRegion.get(port.getName());
			
			if (region != null) {
				return region;
			}		
		}
		
		return Regions.OTHER.name();
		
/*		final Location location = port.getLocation();
		if (location == null) {
			return Regions.OTHER;
		}
		if (location.getCountry() == null) {
			return Regions.OTHER;
		}
		final String country = location.getCountry().toLowerCase();
		switch (country) {
		case "china":
		case "china, peoples rep":
		case "taiwan":
		case "south korea":
		case "japan":
		case "australia":
		case "united arab emirates":
		case "u.a.e.":
		case "kuwait":
		case "pakistan":
		case "india":
			return Regions.FAR_EAST_AND_MIDDLE_EAST;
		case "spain":
		case "portugal":
		case "norway":
		case "united kingdom":
		case "turkey":
		case "greece":
		case "cyprus":
			return Regions.EUROPE;
		case "canada":
		case "chile":
		case "u.k.":
		case "u.s.a.":
		case "argentina":
		case "brazil":
			return Regions.AMERICAS;
		default:
			return Regions.OTHER;
		}*/
		
	}

	@Override
	protected Collection<String> getSubTypes() {
		//return Lists.newArrayList(Regions.values());
		return Lists.newArrayList(this.getRegions());
	}

	@Override
	protected int doCompare(final IncomeStatementData o1, final IncomeStatementData o2) {
		return ((Enum) o1.key).ordinal() - ((Enum) o2.key).ordinal();
	}

	@Override
	protected void resetData() {
		//Clean out port to region map, in case for some reason data has changed.
		this.portToRegionMap = new HashMap<>();
	}

	@Override
	protected String doGetColumnText(final IncomeStatementData data) {
		return data.key.toString();
	}
}