/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class DataCurve extends Curve{

    private List<CurvePoint> curve;

    @JsonCreator
    public DataCurve(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "type", required = true) CurveType curveType,
            @JsonProperty(value = "description") String description,
            @JsonProperty(value = "unit", required = true) String unit,
            @JsonProperty(value = "currency", required = true) String currency,
            @JsonProperty(value = "curve") List<CurvePoint> curve,
            @JsonProperty(value = "marketIndex") String marketIndex
    ) {
        super(
                name,
                curveType,
                description,
                unit,
                currency,
                marketIndex
        );
        this.curve = curve;
    }

    public DataCurve() {
        super();
        // jackson
    }

    public List<CurvePoint> getCurve() {
        return curve;
    }

    public void setCurve(List<CurvePoint> curve) {
        this.curve = curve;
    }
}
