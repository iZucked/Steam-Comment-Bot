/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;

public class Version implements Comparable<Version> {
	private int major;
	private int minor;
	private int increment;
	private String qualifier;

	public Version() {

	}

	public Version(int major, int minor, int increment, String qualifier) {
		this.major = major;
		this.minor = minor;
		this.increment = increment;
		this.qualifier = qualifier;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	@Override
	public int compareTo(Version o) {

		int c = Integer.compare(getMajor(), o.getMajor());
		if (c == 0) {
			c = Integer.compare(getMinor(), o.getMinor());
		}
		if (c == 0) {
			c = Integer.compare(getIncrement(), o.getIncrement());
		}

		if (c == 0) {
			if (getQualifier().equals(o.getQualifier())) {
				c = 0;
			} else if ("RELEASE".equals(getQualifier())) {
				c = 1;
			} else {
				c = -1;
			}
		}

		return c;
	}

	public static Version fromString(String str) {
		Pattern p = Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)(\\.|\\-)(.+)"); // the pattern to search for
		Matcher m = p.matcher(str);

		// if we find a match, get the group
		if (m.find()) {
			// we're only looking for one group, so get it
			String majorStr = m.group(1);
			String minorStr = m.group(2);
			String incrementStr = m.group(3);
			String qualifierStr = m.group(5);

			Version v = new Version();
			v.setMajor(Integer.parseInt(majorStr));
			v.setMinor(Integer.parseInt(minorStr));
			v.setIncrement(Integer.parseInt(incrementStr));
			v.setQualifier(qualifierStr);

			return v;
			// print the group out for verification
//	      System.out.format("'%s'\n", theGroup);
		}

		throw new IllegalArgumentException("Invalid version string");
	}

	@Override
	public @NonNull String toString() {
		if ("SNAPSHOT".equals(qualifier)) {
			return String.format("%d.%d.%d-%s", major, minor, increment, qualifier);
		} else {
			return String.format("%d.%d.%d.%s", major, minor, increment, qualifier);
		}
	}

	public static void main(String[] args) {
		Version v = Version.fromString("1.2.4.blahblash");
		System.out.println(v);

	}
}
