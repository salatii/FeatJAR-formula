/*
 * Copyright (C) 2023 Sebastian Krieter, Elias Kuiter
 *
 * This file is part of FeatJAR-formula.
 *
 * formula is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * formula is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with formula. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-formula> for further information.
 */
package de.featjar.formula.analysis;

import de.featjar.base.computation.Dependency;
import de.featjar.base.computation.IComputation;

/**
 * An analysis that can be passed a variable map.
 * Assumes that the implementing class can be cast to {@link IComputation}.
 */
public interface IVariableMapDependency {
    Dependency<VariableMap> getVariableMapDependency();

    /**
     * {@return a computation for the variable map used by this analysis}
     */
    default IComputation<VariableMap> getVariableMap() {
        return getVariableMapDependency().get((IComputation<?>) this);
    }

    /**
     * Sets the computation for the variable map assumed by this analysis.
     *
     * @param variableMap the variable map computation
     * @return this analysis
     */
    default IVariableMapDependency setVariableMap(IComputation<VariableMap> variableMap) {
        getVariableMapDependency().set((IComputation<?>) this, variableMap);
        return this;
    }
}
