/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.client.pricing.model;

import java.util.List;

public class BulkContainer {

    List<Curve> curves;

    public BulkContainer(){
        // jackson
    }

    public BulkContainer(List<Curve> curves){
        this.curves = curves;
    }

    public List<Curve> getCurves() {
        return curves;
    }

    public void setCurves(List<Curve> curves) {
        this.curves = curves;
    }
}
