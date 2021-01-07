/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;

/**
 * Utility class to make simple {@link IOptimiserInjectorService} implementations.
 * 
 * 
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public class OptimiserInjectorServiceMaker {

	private final Map<ModuleType, List<Function<Collection<@NonNull String>, Module>>> moduleMap = new EnumMap<>(ModuleType.class);
	private final Map<ModuleType, List<Function<Collection<@NonNull String>, Module>>> moduleOverrideMap = new EnumMap<>(ModuleType.class);

	public static OptimiserInjectorServiceMaker begin() {
		return new OptimiserInjectorServiceMaker();
	}

	public OptimiserInjectorServiceMaker withModule(final ModuleType moduleType, final Function<Collection<@NonNull String>, Module> moduleProvider) {
		moduleMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(moduleProvider);
		return this;
	}

	/**
	 * Create a new module to bind the instance type
	 * 
	 * @param moduleType
	 * @param clz
	 * @param object
	 * @return
	 */
	public <T> OptimiserInjectorServiceMaker withModuleOverrideBind(final ModuleType moduleType, final Class<T> clz, final @NonNull Class<? extends T> object) {
		moduleOverrideMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(hints -> new AbstractModule() {
			@Override
			protected void configure() {
				bind(clz).to(object);
			}
		});
		return this;
	}

	/**
	 * Create a new module to bind the instance type
	 * 
	 * @param moduleType
	 * @param clz
	 * @param object
	 * @return
	 */
	public <T> OptimiserInjectorServiceMaker withModuleBindInstance(final ModuleType moduleType, final Class<T> clz, final @NonNull T object) {
		moduleMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(hints -> new AbstractModule() {

			@Override
			protected void configure() {
				bind(clz).toInstance(object);
			}
		});
		return this;
	}

	/**
	 * Create a new module to bind the instance type
	 * 
	 * @param moduleType
	 * @param clz
	 * @param object
	 * @return
	 */
	public <T> OptimiserInjectorServiceMaker withModuleBindNamedInstance(final ModuleType moduleType, String named, final Class<T> clz, final @NonNull T object) {
		moduleMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(hints -> new AbstractModule() {

			@Override
			protected void configure() {
				bind(clz).annotatedWith(Names.named(named)).toInstance(object);
			}
		});
		return this;
	}

	/**
	 * Create a new module to bind the instance type
	 * 
	 * @param moduleType
	 * @param clz
	 * @param object
	 * @return
	 */
	public <T> OptimiserInjectorServiceMaker withModuleOverrideBindNamedInstance(final ModuleType moduleType, String named, final Class<T> clz, final @NonNull T object) {
		moduleOverrideMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(hints -> new AbstractModule() {

			@Override
			protected void configure() {
				bind(clz).annotatedWith(Names.named(named)).toInstance(object);
			}
		});
		return this;
	}

	/**
	 * Create a new module to bind the instance type
	 * 
	 * @param moduleType
	 * @param clz
	 * @param object
	 * @return
	 */
	public <T> OptimiserInjectorServiceMaker withModuleOverrideBindInstance(final ModuleType moduleType, final Class<T> clz, final @NonNull T object) {
		moduleOverrideMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(hints -> new AbstractModule() {

			@Override
			protected void configure() {
				bind(clz).toInstance(object);
			}
		});
		return this;
	}

	public OptimiserInjectorServiceMaker withModule(final ModuleType moduleType, final Module module) {
		moduleMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(hints -> module);
		return this;
	}

	public OptimiserInjectorServiceMaker withModuleOverride(final ModuleType moduleType, final Function<Collection<@NonNull String>, Module> moduleProvider) {
		moduleOverrideMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(moduleProvider);
		return this;
	}

	public OptimiserInjectorServiceMaker withModuleOverride(final ModuleType moduleType, final Module module) {
		moduleOverrideMap.computeIfAbsent(moduleType, t -> new LinkedList<>()).add(hints -> module);
		return this;
	}

	public ModuleBuilder makeModule() {
		return new ModuleBuilder(this);
	}

	public IOptimiserInjectorService make() {
		return new IOptimiserInjectorService() {

			@Override
			public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (moduleMap.containsKey(moduleType)) {
					final List<Function<Collection<@NonNull String>, Module>> modules = moduleMap.get(moduleType);
					if (modules.size() == 1) {
						return modules.get(0).apply(hints);
					} else {
						return new AbstractModule() {

							@Override
							protected void configure() {
								for (final Function<Collection<@NonNull String>, Module> m : modules) {
									install(m.apply(hints));
								}
							}
						};
					}
				}
				return null;
			}

			@Override
			public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (moduleOverrideMap.containsKey(moduleType)) {

					final List<Function<Collection<@NonNull String>, Module>> moduleProviders = moduleOverrideMap.get(moduleType);
					final List<@NonNull Module> modules = new LinkedList<>();
					for (final Function<Collection<@NonNull String>, Module> m : moduleProviders) {
						modules.add(m.apply(hints));
					}
					return modules;
				}
				return null;
			}
		};
	}

	public static class ModuleBuilder {
		private final List<Consumer<Binder>> configures = new LinkedList<>();
		private final OptimiserInjectorServiceMaker maker;

		private ModuleBuilder(final OptimiserInjectorServiceMaker maker) {
			this.maker = maker;
		}

		public ModuleBuilder with(final Consumer<Binder> statement) {
			configures.add(statement);

			return this;
		}

		public OptimiserInjectorServiceMaker buildOverride(final ModuleType moduleType) {
			final Module m = new AbstractModule() {

				@Override
				protected void configure() {
					configures.forEach(stmt -> stmt.accept(this.binder()));
				}
			};

			return maker.withModuleOverride(moduleType, m);
		}

		public OptimiserInjectorServiceMaker build(final ModuleType moduleType) {
			final Module m = new AbstractModule() {

				@Override
				protected void configure() {
					configures.forEach(stmt -> stmt.accept(this.binder()));
				}
			};

			return maker.withModule(moduleType, m);
		}

	}

	public @Nullable IOptimiserInjectorService make(@Nullable final IOptimiserInjectorService optimiserInjectorService) {

		final IOptimiserInjectorService local = make();

		if (optimiserInjectorService == null) {
			return local;
		}

		return new IOptimiserInjectorService() {
			@Override
			public @Nullable Module requestModule(final ModuleType moduleType, final Collection<String> hints) {
				final Module moduleA = local.requestModule(moduleType, hints);
				final Module moduleB = optimiserInjectorService.requestModule(moduleType, hints);
				if (moduleA == null) {
					return moduleB;
				} else if (moduleB == null) {
					return moduleA;
				} else {
					return Modules.combine(moduleA, moduleB);
				}
			}

			@Override
			public @Nullable List<Module> requestModuleOverrides(final ModuleType moduleType, final Collection<String> hints) {

				final List<Module> moduleA = local.requestModuleOverrides(moduleType, hints);
				final List<Module> moduleB = optimiserInjectorService.requestModuleOverrides(moduleType, hints);

				if (moduleA == null) {
					return moduleB;
				} else if (moduleB == null) {
					return moduleA;
				} else {
					final List<Module> newList = new LinkedList<>();
					newList.addAll(moduleA);
					newList.addAll(moduleB);
					return newList;
				}
			}
		};
	}
}
