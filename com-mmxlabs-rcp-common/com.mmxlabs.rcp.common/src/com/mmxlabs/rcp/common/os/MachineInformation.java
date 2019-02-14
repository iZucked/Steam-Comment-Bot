/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;

public class MachineInformation extends PreferencePage implements IWorkbenchPreferencePage {

	private enum MachineFeatures {
		CPUName, CPUSpeed, NumberOfCores, NumberOfLogicalCores, NumberOfCPUS, // CPU
		PhysicalMemory // Memory
	}

	private static final Logger log = LoggerFactory.getLogger(MachineInformation.class);

	public MachineInformation() {
		super();
		setDescription("Machine Information");
	}

	@Override
	public void init(final IWorkbench workbench) {

	}

	@Override
	protected Control createContents(final Composite parent) {

		final Composite c = new Composite(parent, SWT.None);

		c.setLayout(new GridLayout(2, false));
		final Map<MachineFeatures, String> features = getMachineFeatures();
		final GridDataFactory dataFactory = GridDataFactory.swtDefaults().minSize(300, SWT.DEFAULT).hint(300, SWT.DEFAULT);
		{
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("CPU Name");

			final Text txt = new Text(c, SWT.READ_ONLY | SWT.RIGHT | SWT.WRAP);
			txt.setText(features.getOrDefault(MachineFeatures.CPUName, "Unknown"));
			txt.setLayoutData(dataFactory.create());
			txt.pack();
		}
		{
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("CPU Speed");

			final Text txt = new Text(c, SWT.READ_ONLY | SWT.RIGHT);
			txt.setText(features.getOrDefault(MachineFeatures.CPUSpeed, "Unknown"));
			txt.pack();
			txt.setLayoutData(dataFactory.create());
		}
		{
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("Number of cores:");

			final Text txt = new Text(c, SWT.READ_ONLY | SWT.RIGHT);
			txt.setText(features.getOrDefault(MachineFeatures.NumberOfCores, "Unknown"));
			txt.pack();
			txt.setLayoutData(dataFactory.create());
		}
		{
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("Number of logical cores:");

			final Text txt = new Text(c, SWT.READ_ONLY | SWT.RIGHT);
			txt.setText(features.getOrDefault(MachineFeatures.NumberOfLogicalCores, "Unknown"));
			txt.pack();
			txt.setLayoutData(dataFactory.create());
		}
		{
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("Physical memory:");

			final Text txt = new Text(c, SWT.READ_ONLY | SWT.RIGHT);
			txt.setText(features.getOrDefault(MachineFeatures.PhysicalMemory, "Unknown"));
			txt.pack();
			txt.setLayoutData(dataFactory.create());
		}

		return c;
	}

	private Map<MachineFeatures, String> getMachineFeatures() {

		final EnumMap<MachineFeatures, Set<Pair<Integer, String>>> features = new EnumMap<>(MachineFeatures.class);

		final String osname = System.getProperty("os.name");
		// FIXME: Support multiple physical cpus - we aim for a space separated list of
		// physical core attributes. However line order may not have physical id first.
		Integer physicalId = 0;
		if (osname != null) {
			if (osname.startsWith("Windows")) {
				{
					final List<String> lines = executeCommand("wmic", "CPU", "Get", "DeviceID,Name,MaxClockSpeed,NumberOfCores,NumberOfLogicalProcessors", "/Format:List");
					if (lines != null) {
						for (final String line : lines) {
							if (line.startsWith("DeviceID=")) {
								physicalId = Integer.parseInt(line.substring("DeviceID=CPU".length()));
							} else if (line.startsWith("NumberOfCores=")) {
								features.computeIfAbsent(MachineFeatures.NumberOfCores, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, line.substring("NumberOfCores=".length())));
							} else if (line.startsWith("NumberOfLogicalProcessors=")) {
								features.computeIfAbsent(MachineFeatures.NumberOfLogicalCores, (v) -> new HashSet<>())
										.add(new Pair<Integer, String>(physicalId, line.substring("NumberOfLogicalProcessors=".length())));
							} else if (line.startsWith("Name=")) {
								features.computeIfAbsent(MachineFeatures.CPUName, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, line.substring("Name=".length())));
								features.computeIfAbsent(MachineFeatures.CPUName, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, line.substring("Name=".length())));
							} else if (line.startsWith("MaxClockSpeed=")) {
								features.computeIfAbsent(MachineFeatures.CPUSpeed, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, line.substring("MaxClockSpeed=".length())));
							}
						}
					}
				}
				{
					final List<String> lines = executeCommand("wmic", "memphysical", "Get", "MaxCapacity", "/Format:List");
					if (lines != null) {
						for (final String line : lines) {
							if (line.startsWith("MaxCapacity=")) {
								features.computeIfAbsent(MachineFeatures.PhysicalMemory, (v) -> new HashSet<>()).add(new Pair<Integer, String>(0, line.substring("MaxCapacity=".length())));
							}
						}
					}
				}
			} else if (osname.startsWith("Linux")) {
				// Info for each logical(?) core, separated by empty lines.
				final List<String> lines = executeCommand("cat", "/proc/cpuinfo");
				if (lines != null) {
					for (final String line : lines) {
						if (line.startsWith("cpu cores")) {
							final String value = line.substring(line.indexOf(":") + 1).trim();
							features.computeIfAbsent(MachineFeatures.NumberOfCores, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, value));
						} else if (line.startsWith("siblings")) {
							final String value = line.substring(line.indexOf(":") + 1).trim();
							features.computeIfAbsent(MachineFeatures.NumberOfLogicalCores, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, value));
						} else if (line.startsWith("model name")) {
							final String value = line.substring(line.indexOf(":") + 1).trim();
							features.computeIfAbsent(MachineFeatures.CPUName, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, value));
						} else if (line.startsWith("cpu MHz")) {
							final String value = line.substring(line.indexOf(":") + 1).trim();
							features.computeIfAbsent(MachineFeatures.CPUSpeed, (v) -> new HashSet<>()).add(new Pair<Integer, String>(physicalId, value));
						}
					}
				}
			}
		}

		final Function<Set<Pair<Integer, String>>, String> f = (v) -> v.stream() //
				.sorted((a, b) -> Integer.compare(a.getFirst(), b.getFirst())) //
				.map(p -> p.getSecond()) //
				.collect(Collectors.joining(", "));

		return features.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), v -> f.apply(v.getValue())));
	}

	private @Nullable List<String> executeCommand(final String... command) {
		final ProcessBuilder pb = new ProcessBuilder(command);
		try {
			final Process proc = pb.start();

			final List<String> lines = new LinkedList<>();
			try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {

				String s = null;
				while ((s = stdInput.readLine()) != null) {
					lines.add(s);
				}
			}

			if (lines.isEmpty()) {
				try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {

					String s = null;
					while ((s = stdInput.readLine()) != null) {
						lines.add(s);
					}

				}
			}
			return lines;
		} catch (final IOException e) {
			// Ignore
		}
		return null;
	}
}
