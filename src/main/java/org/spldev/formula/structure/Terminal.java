/* -----------------------------------------------------------------------------
 * Formula Lib - Library to represent and edit propositional formulas.
 * Copyright (C) 2021-2022  Sebastian Krieter
 * 
 * This file is part of Formula Lib.
 * 
 * Formula Lib is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Formula Lib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Formula Lib.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * See <https://github.com/skrieter/formula> for further information.
 * -----------------------------------------------------------------------------
 */
package org.spldev.formula.structure;

import java.util.*;

import org.spldev.formula.structure.atomic.literal.*;
import org.spldev.formula.structure.term.*;
import org.spldev.util.tree.structure.*;

/**
 * A propositional node that can be transformed into conjunctive normal form
 * (cnf).
 *
 * @author Sebastian Krieter
 */
public abstract class Terminal extends AbstractTerminal<Formula> implements Formula {

	@Override
	public void setVariableMap(VariableMap map) {
	}

	@Override
	public int hashCode() {
		return Objects.hash(getClass(), getName());
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		return equalsNode(other);
	}

	@Override
	public boolean equalsNode(Object other) {
		return (getClass() == other.getClass())
			&& Objects.equals(getName(), ((Terminal) other).getName());
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public List<? extends Term> getChildren() {
		return Collections.emptyList();
	}

}
