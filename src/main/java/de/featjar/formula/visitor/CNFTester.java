/*
 * Copyright (C) 2022 Sebastian Krieter, Elias Kuiter
 *
 * This file is part of formula.
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
package de.featjar.formula.visitor;

import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.structure.formula.connective.And;
import de.featjar.formula.structure.formula.connective.Or;
import de.featjar.formula.structure.formula.predicate.Predicate;

import java.util.List;

/**
 * Tests whether a formula is in conjunctive normal form.
 * The formula {@code new Literal("x")} is in CNF, but not in clausal CNF.
 * The formula {@code new And(new Or(new Literal("x")))} is in CNF and in clausal CNF.
 *
 * @author Sebastian Krieter
 */
public class CNFTester extends NormalFormTester {

    @Override
    public TraversalAction firstVisit(List<Formula> path) {
        final Formula formula = getCurrentNode(path);
        if (formula instanceof And) {
            return processLevelOne(path, formula, Or.class);
        } else if (formula instanceof Or) {
            return processLevelTwo(path, formula);
        } else if (formula instanceof Predicate) {
            return processLevelThree(path);
        } else {
            isNormalForm = false;
            isClausalNormalForm = false;
            return TraversalAction.SKIP_ALL;
        }
    }
}
