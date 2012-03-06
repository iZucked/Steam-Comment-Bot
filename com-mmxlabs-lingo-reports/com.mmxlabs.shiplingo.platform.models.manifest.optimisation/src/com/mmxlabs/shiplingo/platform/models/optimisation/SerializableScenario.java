/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * A class which holds a scenario, but adds serialization knowhow
 * 
 * @author hinton
 * 
 */
public class SerializableScenario implements Serializable {
	public transient MMXRootObject scenario;

	public SerializableScenario() {
		this(null);
	}

	public SerializableScenario(final MMXRootObject scenario) {
		this.scenario = scenario;
	}

	private void writeObject(final ObjectOutputStream out) throws IOException {
		// serialize a copy of me into a new resource; necessarily flattens the scenario's containment stuff out.
		// this may cause some difficulties when integrating a solution back into an un-flattened scenario.
		final MMXRootObject copy = scenario.getSelfContainedCopy();

		final byte[] bytes = EMFUtils.serializeEObject(copy);
		out.defaultWriteObject();
		out.writeObject(bytes);
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final byte[] bytes = (byte[]) in.readObject();
		scenario = EMFUtils.deserialiseEObject(bytes, ScenarioPackage.eINSTANCE.getScenario());
	}
}
