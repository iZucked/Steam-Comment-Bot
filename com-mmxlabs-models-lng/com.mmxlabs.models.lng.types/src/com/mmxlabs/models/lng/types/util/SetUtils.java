/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;

import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * Utility class to expand vessel sets and port sets.
 * @author hinton
 *
 */
public class SetUtils {
	
	/**
	 * Return true if any of the port sets in the given iterable expand to contain
	 * the given port
	 * @param portSets
	 * @param port
	 * @return
	 */
	public boolean contains(final Iterable<APortSet> portSets, final APort port) {
		return getPorts(portSets).contains(port);
	}
	
	/**
	 * True iff the given vessel sets expand to contain the given vessel
	 * @param vesselSets
	 * @param vessel
	 * @return
	 */
	public boolean contains(final Iterable<AVesselSet> vesselSets, final AVessel vessel) {
		return getVessels(vesselSets).contains(vessel);
	}
	
	/**
	 * Return a set containing each port in the given port sets
	 * @param portSets
	 * @return
	 */
	public static Set<APort> getPorts(final Iterable<APortSet> portSets) {
		final HashSet<APort> result = new HashSet<APort>();
		addPorts(portSets,result);
		return result;
	}
	
	/**
	 * Return a set containing each port in the given port set.
	 * @param portSet
	 * @return
	 */
	public static Set<APort> getPorts(final APortSet portSet) {
		final HashSet<APort> result = new HashSet<APort>();
		addPorts(portSet,result);
		return result;
	}
	
	public static void addPorts(final Iterable<APortSet> portSets, Collection<APort> output) {
		addPorts(portSets, output, new UniqueEList<APortSet>());
	}
	
	public static void addPorts(final Iterable<APortSet> portSets, Collection<APort> output, EList<APortSet> marks) {
		for (final APortSet set : portSets) addPorts(set, output, marks);
	}
	
	public static void addPorts(final APortSet portSet, Collection<APort> output, EList<APortSet> marks) {
		output.addAll(portSet.collect(marks));
	}
	
	public static void addPorts(final APortSet portSet, Collection<APort> output) {
		addPorts(portSet, output, new UniqueEList<APortSet>());
	}
	
	/**
	 * Return a set containing each vessel in the given vessel sets
	 * 
	 * @param portSets
	 * @return
	 */
	public static Set<AVessel> getVessels(final Iterable<AVesselSet> portSets) {
		final HashSet<AVessel> result = new HashSet<AVessel>();
		addVessels(portSets,result);
		return result;
	}
	
	/**
	 * return a set containing each vessel in the given vessel set.
	 * @param portSet
	 * @return
	 */
	public static Set<AVessel> getVessels(final AVesselSet portSet) {
		final HashSet<AVessel> result = new HashSet<AVessel>();
		addVessels(portSet,result);
		return result;
	}
	
	public static void addVessels(final Iterable<AVesselSet> portSets, Collection<AVessel> output) {
		addVessels(portSets, output, new UniqueEList<AVesselSet>());
	}
	
	public static void addVessels(final Iterable<AVesselSet> portSets, Collection<AVessel> output, EList<AVesselSet> marks) {
		for (final AVesselSet set : portSets) addVessels(set, output, marks);
	}
	
	public static void addVessels(final AVesselSet portSet, Collection<AVessel> output, EList<AVesselSet> marks) {
		output.addAll(portSet.collect(marks));
	}
	
	public static void addVessels(final AVesselSet portSet, Collection<AVessel> output) {
		addVessels(portSet, output, new UniqueEList<AVesselSet>());
	}
}
