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
package de.featjar.formula.analysis.value;

import de.featjar.base.computation.IComputation;
import de.featjar.base.tree.structure.ITree;
import de.featjar.formula.analysis.VariableMap;
import de.featjar.formula.analysis.bool.BooleanSolution;

public class ComputeValueRepresentationOfSolution
        extends AValueRepresentationComputation<BooleanSolution, ValueSolution> {
    public ComputeValueRepresentationOfSolution(
            IComputation<BooleanSolution> booleanRepresentation, IComputation<VariableMap> variableMap) {
        super(booleanRepresentation, variableMap);
    }

    @Override
    public ITree<IComputation<?>> cloneNode() {
        return new ComputeValueRepresentationOfSolution(getInput(), getVariableMap());
    }
}
