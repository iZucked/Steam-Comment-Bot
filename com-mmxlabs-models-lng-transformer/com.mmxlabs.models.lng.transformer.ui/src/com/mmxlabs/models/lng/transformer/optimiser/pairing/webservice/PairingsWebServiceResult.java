/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing.webservice;

public class PairingsWebServiceResult {
    public boolean isOptimised() {
        return optimised;
    }

    public void setOptimised(boolean optimised) {
        this.optimised = optimised;
    }

    private boolean optimised;

    public boolean[][] getPairings() {
        return pairings;
    }

    public void setPairings(boolean[][] pairings) {
        this.pairings = pairings;
    }

    private boolean[][] pairings;

}
