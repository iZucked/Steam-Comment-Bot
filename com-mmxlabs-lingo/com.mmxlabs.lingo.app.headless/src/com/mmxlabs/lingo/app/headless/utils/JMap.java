package com.mmxlabs.lingo.app.headless.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

public class JMap {

	private Map<String, Pair<Object, Class<?>>> map;

	public JMap(Map<String, Object> map) {
		this.map = new HashMap<String, Pair<Object, Class<?>>>();
		setJMap(map);
		int ii = 0;
	}

	public Map<String, Pair<Object, Class<?>>> getJMap() {
		return map;
	}

	@SuppressWarnings("rawtypes")
	public void setJMap(Map<String, Object> map) {
		
		String periodPattern = "[0-9]{4}[-][0-9]{2}";
		String promptPattern = "[0-9]{4}[-][0-9]{2}[-][0-9]{2}";

		for (String key : map.keySet()) {
			
			Object value = map.get(key);
			
			@NonNull
			Class<? extends @NonNull Object> clazz = value.getClass();
			
			// Convert Longs to Ints
			if (clazz == Long.class) {
				clazz = Integer.class;
				value = (int) (long) value;
				this.map.put(key, new Pair<Object, Class<?>>(value, clazz));
			
			} else if (clazz == String.class){
				String stringValue = (String) value;
				String[] stringComponents = stringValue.split("-");
				
				if(stringValue.matches(periodPattern)){
					int year = Integer.parseInt(stringComponents[0]);
					int month = Integer.parseInt(stringComponents[1]);
					this.map.put(key, new Pair<Object,Class<?>>(YearMonth.of(year, month), YearMonth.class));
				} else if (stringValue.matches(promptPattern)){
					int year = Integer.parseInt(stringComponents[0]);
					int month = Integer.parseInt(stringComponents[1]);
					int day = Integer.parseInt(stringComponents[2]);
					this.map.put(key, new Pair<Object,Class<?>>(LocalDate.of(year, month, day), LocalDate.class));
				} else {
					this.map.put(key, new Pair<Object, Class<?>>(value, clazz));
				}
		} else {
			this.map.put(key, new Pair<Object, Class<?>>(value, clazz));
		}
	}
	}

	public Class<?> getClass(String key) {
		return map.get(key).getSecond();
	}

	public <T> T getValue(String key, Class<T> clazz) {
		return clazz.cast(map.get(key).getFirst());
	}

	public Set<String> getKeySet() {
		return map.keySet();
	}

	public Object testReturn(String key) {
		Object value = map.get(key).getFirst();
		Class<? extends @NonNull Object> clazz = map.get(key).getSecond();

		return clazz.cast(value);
	}

}
