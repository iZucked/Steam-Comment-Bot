package com.mmxlabs.scheduler.optimiser.voyage;

public enum ExplicitIdleTime {
	CONTINGENCY, // Contingency matrix - extra idle between ports 
	MARKET_BUFFER, // Idle time before a load/discharge
	PURGE // Purge idle time. Pre-defined before a load event. It counted as idle for scheduling, but during evaluation it is split into a separate piece. 
}
