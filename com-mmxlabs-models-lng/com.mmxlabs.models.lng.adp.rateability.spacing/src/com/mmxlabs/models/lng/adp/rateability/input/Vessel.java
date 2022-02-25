package com.mmxlabs.models.lng.adp.rateability.input;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Deprecated
public class Vessel {
	public String name;
	public Optional<String> contract = Optional.empty();
	public OptionalInt number = OptionalInt.empty();
	public Optional<List<Cargo>> cargoes = Optional.empty();
	public Optional<String> spotIndex = Optional.empty();
	public Optional<List<List<List<Integer>>>> availableDates = Optional.empty();
}
