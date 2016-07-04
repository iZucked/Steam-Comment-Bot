/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.Pair;

public final class Options {
	private final String optionPrefix;
	private final Map<String, OptionParser> parsers;
	private final Map<String, Object> results;

	private final StringBuilder allHelp = new StringBuilder();

	public Options(final String optionPrefix) {
		this.optionPrefix = optionPrefix;
		this.parsers = new HashMap<String, OptionParser>();
		this.results = new HashMap<String, Object>();
	}

	public Options() {
		this("--");
	}

	public List<String> parseAndSet(final String[] args, final Object object) throws InvalidOptionException, InvalidArgumentException {
		final Map<String, Method> methodMap = new HashMap<String, Method>();
		final List<Pair<Integer, String>> names = new ArrayList<Pair<Integer, String>>();
		for (final Method m : object.getClass().getMethods()) {
			if (m.isAnnotationPresent(Option.class)) {
				final Option option = m.getAnnotation(Option.class);

				final String name = option.name().equals("") ? mangleName(m.getName()) : option.name();

				final boolean hasValue = option.defaultValue().equals(Option.EMPTY_DEFAULT_STRING_HACK) == false;

				final String value = option.defaultValue();

				final Class<?>[] parameters = m.getParameterTypes();
				if (parameters.length == 1) {
					OptionParser parser = null;
					if (parameters[0].equals(String.class)) {
						parser = hasValue ? new StringParser(value) : new StringParser();
					} else if (parameters[0].equals(int.class)) {
						parser = hasValue ? new IntegerParser(value) : new IntegerParser();
					} else if (parameters[0].equals(double.class)) {
						parser = hasValue ? new DoubleParser(value) : new DoubleParser();
					} else if (parameters[0].equals(boolean.class)) {
						parser = new NothingParser();
					} else if (parameters[0].equals(List.class)) {
						parser = new StringListParser(",", value);
					} else {
						throw new InvalidOptionException("Method " + m.getName() + " has argument type " + parameters[0].getSimpleName() + ", which cannot have a default value");
					}
					addOption(name, option.help() + " [" + parameters[0].getSimpleName() + "]", parser);
				} else {
					throw new InvalidOptionException("Method " + m.getName() + " has too many parameters!");
				}
				methodMap.put(name, m);
				names.add(new Pair<Integer, String>(option.order(), name));
			}
		}

		final List<String> r = parse(args);

		Collections.sort(names, new Comparator<Pair<Integer, String>>() {
			@Override
			public int compare(final Pair<Integer, String> arg0, final Pair<Integer, String> arg1) {
				final int x = arg0.getFirst().compareTo(arg1.getFirst());
				if (x == 0) {
					return arg0.getSecond().compareTo(arg1.getSecond());
				}
				return x;
			}

		});

		for (final Pair<Integer, String> p : names) {
			final Method method = methodMap.get(p.getSecond());
			try {
				method.invoke(object, getOption(p.getSecond()));
			} catch (final IllegalArgumentException e) {
				final InvalidArgumentException ex = new InvalidArgumentException("Wrong argument " + getOption(p.getSecond()));
				ex.setOption(p.getSecond());
				throw ex;
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("@Option used incorrectly for field " + method.getName());
			} catch (final InvocationTargetException e) {
				e.printStackTrace();

				throw new RuntimeException("This really should never happen");
			}
		}
		return r;
	}

	private String mangleName(String name) {
		if (name.startsWith("set")) {
			name = name.substring(3);
		}
		final StringBuilder result = new StringBuilder();

		for (final char b : name.toCharArray()) {
			if (Character.isUpperCase(b)) {
				if (!(result.length() == 0)) {
					result.append("-");
				}
				result.append(Character.toLowerCase(b));
			} else {
				result.append(b);
			}
		}
		return result.toString();
	}

	public void addOption(final String name, final OptionParser parser) {
		addOption(name, "undocumented", parser);
	}

	public void addOption(final String[] names, final OptionParser parser) {
		addOption(names, "undocumented", parser);
	}

	public void addOption(final String[] names, final String help, final OptionParser parser) {
		for (final String name : names) {
			parsers.put(name, parser);
		}
		if (help != null) {
			allHelp.append("\t --" + names[0] + " : " + help);
			if (parser.hasDefaultValue()) {
				allHelp.append(" (default value : " + parser.getDefaultValue() + ")");
			} else {
				allHelp.append(" (no default value)");
			}
			if (names.length > 1) {
				allHelp.append(" (synonyms : ");
				for (int x = 1; x < names.length; x++) {
					allHelp.append(names[x]);
					if (x > 1) {
						allHelp.append(", ");
					}
				}
				allHelp.append(")");
			}
			allHelp.append("\n");
		}
	}

	public String getHelp() {
		return allHelp.toString();
	}

	public void addOption(final String name, final String help, final OptionParser parser) {
		addOption(new String[] { name }, help, parser);
	}

	// TODO cleanup
	@SuppressWarnings("unchecked")
	public <T> T getOption(final String s) {
		if (results.containsKey(s)) {
			return (T) results.get(s);
		} else if (parsers.containsKey(s)) {
			if (parsers.get(s).hasDefaultValue()) {
				return (T) parsers.get(s).getDefaultValue();
			} else {
				return (T) results.get(s);
			}
		} else {
			return (T) results.get(s);
		}
	}

	public List<String> parse(final List<String> args) throws InvalidOptionException, InvalidArgumentException {
		final Iterator<String> it = args.iterator();
		final List<String> spare = new ArrayList<String>();
		while (it.hasNext()) {
			final String arg = it.next();
			if (arg.startsWith(optionPrefix)) {
				// split off the prefix and lookup the parser
				final String opt = arg.substring(optionPrefix.length());
				if (parsers.containsKey(opt)) {
					final OptionParser parser = parsers.get(opt);
					try {
						results.put(opt, parser.parse(opt, it));
					} catch (final InvalidArgumentException ex) {
						ex.setOption(opt);
						throw ex;
					}
				} else {
					throw new InvalidOptionException(arg);
				}
			} else {
				spare.add(arg);
				while (it.hasNext()) {
					spare.add(it.next());
				}
			}
		}
		return spare;
	}

	public List<String> parse(final String[] args) throws InvalidOptionException, InvalidArgumentException {
		return parse(Arrays.asList(args));
	}

	public boolean hasOption(final String s) {
		if (results.containsKey(s)) {
			return true;
		} else {
			return parsers.containsKey(s) && parsers.get(s).hasDefaultValue();
		}
	}

	private String quoteify(final Object o) {
		return "\"" + o.toString().replace("\"", "\\\"") + "\"";
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();

		sb.append("{");

		boolean b = false;
		for (final Map.Entry<String, Object> e : results.entrySet()) {
			if (b) {
				sb.append(", ");
			}
			b = true;
			sb.append("\n");
			sb.append("    ");
			sb.append(quoteify(e.getKey()));
			sb.append(":");
			sb.append(quoteify(e.getValue()));
		}

		sb.append("\n}");

		return sb.toString();
	}

}