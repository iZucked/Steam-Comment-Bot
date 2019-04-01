/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.repository.csv.tests;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.lngdataserver.integration.distances.DefaultPortProvider;
import com.mmxlabs.models.lng.port.Port;

public class DefaultPortProviderTests {

	@Test
	public void testApproxName() throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		List<Port> samplePorts = new ArrayList<>();
		Port p1 = Mockito.mock(Port.class);
		when(p1.getName()).thenReturn("Skikda");
		samplePorts.add(p1);
		Port p2 = Mockito.mock(Port.class);
		when(p2.getName()).thenReturn("Arzew");
		samplePorts.add(p2);
		Port p3 = Mockito.mock(Port.class);
		when(p3.getName()).thenReturn("Soyo");
		samplePorts.add(p3);
		Port p4 = Mockito.mock(Port.class);
		when(p4.getName()).thenReturn("Senboku");
		samplePorts.add(p4);
		Port p5 = Mockito.mock(Port.class);
		when(p5.getName()).thenReturn("Yokkaichi");
		samplePorts.add(p5);
		Assertions.assertEquals("Skikda", DefaultPortProvider.staticGetPortByApproxName(samplePorts, "Skikda").getName());
		Assertions.assertEquals("Skikda", DefaultPortProvider.staticGetPortByApproxName(samplePorts, "Skikda").getName());
		Assertions.assertEquals("Arzew", DefaultPortProvider.staticGetPortByApproxName(samplePorts, "Arzev").getName());
	}

}
