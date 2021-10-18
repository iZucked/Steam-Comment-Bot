package com.mmxlabs.models.lng.adp.rateability.input;

import java.util.List;
import java.util.Optional;

public class Cargo {
	public String contract;
	public Optional<List<Integer>> minLoadDate = Optional.empty();
	public Optional<List<Integer>> maxLoadDate = Optional.empty();
	public Optional<List<Integer>> minDischargeDate = Optional.empty();
	public Optional<List<Integer>> maxDischargeDate = Optional.empty();
}
