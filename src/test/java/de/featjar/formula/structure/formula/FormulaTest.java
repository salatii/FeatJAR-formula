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
package de.featjar.formula.structure.formula;

import static org.junit.jupiter.api.Assertions.*;

import de.featjar.formula.structure.Expressions;
import de.featjar.formula.structure.formula.connective.And;
import de.featjar.formula.structure.formula.connective.Implies;
import de.featjar.formula.structure.formula.connective.Or;
import de.featjar.formula.structure.formula.predicate.Literal;
import org.junit.jupiter.api.Test;

class FormulaTest {
    IFormula formula = new Implies(new Literal("a"), Expressions.False);
    IFormula cnf = new Or(new Literal(false, "a"), Expressions.False);
    IFormula dnf = new And(new Literal(false, "a"), Expressions.False);
    IFormula clausalCnf = new And(new Or(new Literal(false, "a"), Expressions.False));
    IFormula clausalDnf = new Or(new And(new Literal(false, "a"), Expressions.False));

    @Test
    void getType() {
        assertEquals(Boolean.class, formula.getType());
    }

    @Test
    void isCNF() {
        assertFalse(formula.isCNF());
        assertTrue(cnf.isCNF());
        assertTrue(clausalCnf.isCNF());
        assertTrue(dnf.isCNF());
        assertFalse(clausalDnf.isCNF());
    }

    @Test
    void isDNF() {
        assertFalse(formula.isDNF());
        assertTrue(cnf.isDNF());
        assertFalse(clausalCnf.isDNF());
        assertTrue(dnf.isDNF());
        assertTrue(clausalDnf.isDNF());
    }

    @Test
    void isClausalCNF() {
        assertFalse(formula.isStrictNormalForm(FormulaNormalForm.CNF));
        assertFalse(cnf.isStrictNormalForm(FormulaNormalForm.CNF));
        assertTrue(clausalCnf.isStrictNormalForm(FormulaNormalForm.CNF));
        assertFalse(dnf.isStrictNormalForm(FormulaNormalForm.CNF));
        assertFalse(clausalDnf.isStrictNormalForm(FormulaNormalForm.CNF));
    }

    @Test
    void isClausalDNF() {
        assertFalse(formula.isStrictNormalForm(FormulaNormalForm.DNF));
        assertFalse(cnf.isStrictNormalForm(FormulaNormalForm.DNF));
        assertFalse(clausalCnf.isStrictNormalForm(FormulaNormalForm.DNF));
        assertFalse(dnf.isStrictNormalForm(FormulaNormalForm.DNF));
        assertTrue(clausalDnf.isStrictNormalForm(FormulaNormalForm.DNF));
    }

    @Test
    void toCNF() {
        // TODO
    }

    @Test
    void toDNF() {
        // TODO
    }

    @Test
    void toClausalCNF() {
        // TODO
    }

    @Test
    void toClausalDNF() {
        // TODO
    }
}
