/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@name")
@JsonSubTypes({ @JsonSubTypes.Type(value = DataCurve.class, name = "DataCurve"), @JsonSubTypes.Type(value = ExpressionCurve.class, name = "ExpressionCurve") })

@ApiModel(discriminator = "@class", subTypes = { DataCurve.class, ExpressionCurve.class })
public abstract class Curve {
	@Id
	@JsonIgnore
	private ObjectId id;
	@ApiModelProperty(required = true)
	private String name;
	@ApiModelProperty(required = true)
	private CurveType type;
	private String description;
	private String unit;
	private String currency;

	protected Curve() {

	}

	protected Curve(String name, CurveType type, String description, String unit, String currency) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.unit = unit;
		this.currency = currency;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CurveType getType() {
		return type;
	}

	public void setType(CurveType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public static String encodedName(String name) {
		return name.replaceAll("\\.", "_");
	}
}
