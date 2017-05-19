package com.mmxlabs.scheduler.optimiser.shared.port;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public final class DefaultPortProviderEditor implements IPortProviderEditor {

	private final List<IPort> ports = new ArrayList<>();
	private final List<IPort> readOnlyPorts = Collections.unmodifiableList(ports);

	private IPort anywhere = (@NonNull IPort) null;
	private final Map<String, IPort> portsByName = new HashMap<>();
	private final Map<String, IPort> portsByMMXID = new HashMap<>();

	@Override
	public int getNumberOfPorts() {
		return ports.size();
	}

	@Override
	public @NonNull List<@NonNull IPort> getAllPorts() {
		return readOnlyPorts;
	}

	@Override
	public @NonNull IPort getAnywherePort() {
		assert anywhere != null;
		return anywhere;
	}

	@Override
	public @Nullable IPort getPortForName(@NonNull final String name) {
		return portsByName.get(name);
	}

	@Override
	public @Nullable IPort getPortForMMXID(@NonNull final String mmxID) {
		return portsByMMXID.get(mmxID);
	}

	@Override
	public void setAnywherePort(@NonNull final IPort anywhere) {
		this.anywhere = anywhere;
		if (!ports.contains(anywhere)) {
			ports.add(0, anywhere);
		}
	}

	@Override
	public void addPort(@NonNull final IPort port, @NonNull final String name, @NonNull final String mmxID) {
		assert name != null;
		assert !portsByMMXID.containsKey(mmxID);
		assert !portsByName.containsKey(name);
		portsByMMXID.put(mmxID, port);
		portsByName.put(name, port);
		ports.add(port);
	}
}
