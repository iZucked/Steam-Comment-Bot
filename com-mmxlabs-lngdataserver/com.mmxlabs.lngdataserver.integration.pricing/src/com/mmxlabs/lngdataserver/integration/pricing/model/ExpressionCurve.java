/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpressionCurve extends Curve {

    private String expression;

    @JsonCreator
    public ExpressionCurve(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "type", required = true) CurveType curveType,
            @JsonProperty(value = "description") String description,
            @JsonProperty(value = "unit") String unit,
            @JsonProperty(value = "currency") String currency,
            @JsonProperty(value = "expression") String expression
    ) {
        super(
                name,
                curveType,
                description,
                unit,
                currency
        );
        this.expression = expression;
    }

    public ExpressionCurve() {
        super();
        // jackson
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
