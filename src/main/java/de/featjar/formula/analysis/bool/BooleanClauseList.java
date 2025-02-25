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
package de.featjar.formula.analysis.bool;

import de.featjar.base.computation.IComputation;
import de.featjar.base.data.Result;
import de.featjar.formula.analysis.VariableMap;
import de.featjar.formula.analysis.value.ValueClauseList;
import de.featjar.formula.structure.formula.IFormula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import java.util.Collection;

/**
 * A list of Boolean clauses.
 * Typically used to express a conjunctive normal form.
 * Compared to a {@link IFormula} in CNF (e.g., computed with
 * {@link ComputeCNFFormula}), a {@link ValueClauseList} is a more low-level representation.
 * A Boolean clause list only contains indices into a {@link VariableMap}, which links
 * a {@link BooleanClauseList} to the {@link de.featjar.formula.structure.term.value.Variable variables}
 * in the original {@link IFormula}.
 * TODO: more error checking for consistency of clauses with variables
 *
 * @author Sebastian Krieter
 * @author Elias Kuiter
 */
public class BooleanClauseList extends ABooleanAssignmentList<BooleanClause> {
    public BooleanClauseList() {}

    public BooleanClauseList(int size) {
        super(size);
    }

    public BooleanClauseList(Collection<? extends BooleanClause> clauses) {
        super(clauses);
    }

    public BooleanClauseList(BooleanClauseList other) {
        super(other);
    }

    @Override
    public Result<ValueClauseList> toValue(VariableMap variableMap) {
        return variableMap.toValue(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IComputation<ValueClauseList> toValue(IComputation<VariableMap> variableMap) {
        return (IComputation<ValueClauseList>) super.toValue(variableMap);
    }

    public String print() {
        return VariableMap.toAnonymousValue(this).get().print();
    }

    @Override
    public String toString() {
        return String.format("BooleanClauseList[%s]", print());
    }
}
