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
package de.featjar.formula.io.textual;

import de.featjar.base.data.Pair;
import java.util.Arrays;

/**
 * Symbols for a representation like in Java. These are inherently incomplete
 * and should only be used if absolutely necessary.
 *
 * @author Timo Günther
 * @author Sebastian Krieter
 */
public class JavaSymbols extends Symbols {

    public static final Symbols INSTANCE = new JavaSymbols();

    private JavaSymbols() {
        super(
                Arrays.asList(
                        new Pair<>(Operator.NOT, "!"),
                        new Pair<>(Operator.AND, "&&"),
                        new Pair<>(Operator.OR, "||"),
                        new Pair<>(Operator.BIIMPLIES, "==")),
                false);
    }
}
