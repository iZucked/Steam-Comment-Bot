package com.mmxlabs.jobcontroller.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceImporter {

	Map<String, Map<String, Integer>> distanceMap = new HashMap<String, Map<String, Integer>>();

	public void importDistances(InputStream is) throws IOException {

		List<String> ports = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		boolean firstElement = true;
		String line = br.readLine();
		while (line != null) {

			String[] elements = line.split(",");

			String A = null;
			for (int i = 0; i < elements.length; ++i) {
				String str = elements[i];
				String e = str.trim();

				if (firstElement) {
					ports.add(e);
				} else {

					if (i == 0) {
						// Port Name
						A = e;
					} else {
						// Distance element
						try {
							int distance = Integer.parseInt(e);
							putEntry(A, ports.get(i), distance);
						} catch (Exception nfe) {

						}

					}
				}

			}
			firstElement = false;
			line = br.readLine();
		}

		br.close();

	}

	public void putEntry(String A, String B, Integer D) {
		putEntryInt(A, B, D);
		putEntryInt(B, A, D);
	}

	void putEntryInt(String A, String B, Integer D) {
		Map<String, Integer> m;
		if (distanceMap.containsKey(A)) {
			m = distanceMap.get(A);
		} else {
			m = new HashMap<String, Integer>();
			distanceMap.put(A, m);
		}
		m.put(B, D);
	}

	public Integer getDistance(String A, String B) {
		if (distanceMap.containsKey(A)) {
			Map<String, Integer> m = distanceMap.get(A);
			if (m.containsKey(B)) {
				return m.get(B);
			}
		}
		return Integer.MAX_VALUE;
	}

}
