/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.client.pricing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;


@Entity
@ApiModel(parent = Curve.class)
public class DataCurve extends Curve{

    @Embedded
    private List<CurvePoint> curve;

    @JsonCreator
    public DataCurve(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "type", required = true) CurveType curveType,
            @JsonProperty(value = "description") String description,
            @JsonProperty(value = "version") String version,
            @JsonProperty(value = "unit", required = true) String unit,
            @JsonProperty(value = "currency", required = true) String currency,
            @JsonProperty(value = "curve") List<CurvePoint> curve
    ) {
        super(
                name,
                curveType,
                description,
                version,
                unit,
                currency
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
