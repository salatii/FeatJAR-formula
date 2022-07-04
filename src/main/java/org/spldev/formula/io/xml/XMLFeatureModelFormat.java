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
package org.spldev.formula.io.xml;

import java.util.*;
import java.util.regex.*;

import org.spldev.formula.structure.*;
import org.spldev.formula.structure.atomic.literal.*;
import org.spldev.formula.structure.compound.*;
import org.spldev.util.io.format.*;
import org.w3c.dom.*;

/**
 * Parses feature model formulas from FeatureIDE XML files.
 *
 * @author Sebastian Krieter
 * @author Elias Kuiter
 */
public class XMLFeatureModelFormat extends AbstractXMLFeatureModelFormat<Formula, Literal, Boolean> {
	protected final List<Formula> constraints = new ArrayList<>();
	protected final VariableMap variableMap = new VariableMap();

	@Override
	public XMLFeatureModelFormat getInstance() {
		return new XMLFeatureModelFormat();
	}

	@Override
	public String getName() {
		return "FeatureIDE";
	}

	@Override
	public boolean supportsParse() {
		return true;
	}

	@Override
	protected Formula parseDocument(Document document) throws ParseException {
		final Element featureModelElement = getDocumentElement(document, FEATURE_MODEL);
		parseFeatureTree(getElement(featureModelElement, STRUCT));
		Optional<Element> constraintsElement = getOptionalElement(featureModelElement, CONSTRAINTS);
		if (constraintsElement.isPresent())
			parseConstraints(constraintsElement.get(), variableMap);
		if (constraints.isEmpty()) {
			return new And();
		} else {
			if (constraints.get(0).getChildren().isEmpty()) {
				constraints.set(0, new Or());
			}
		}
		return new And(constraints);
	}

	@Override
	protected void writeDocument(Formula object, Document doc) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Pattern getInputHeaderPattern() {
		return AbstractXMLFeatureModelFormat.inputHeaderPattern;
	}

	@Override
	protected Literal createFeatureLabel(String name, Literal parentFeatureLabel, boolean mandatory, boolean _abstract,
		boolean hidden)
		throws ParseException {
		if (variableMap.hasVariable(name)) {
			throw new ParseException("Duplicate feature name!");
		} else {
			variableMap.addBooleanVariable(name);
		}

		Literal literal = variableMap.createLiteral(name);
		if (parentFeatureLabel == null) {
			constraints.add(literal);
		} else {
			constraints.add(implies(literal, parentFeatureLabel));
			if (mandatory) {
				constraints.add(implies(parentFeatureLabel, literal));
			}
		}
		return literal;
	}

	@Override
	protected void addAndGroup(Literal featureLabel, List<Literal> childFeatureLabels) {
	}

	@Override
	protected void addOrGroup(Literal featureLabel, List<Literal> childFeatureLabels) {
		constraints.add(implies(featureLabel, childFeatureLabels));
	}

	@Override
	protected void addAlternativeGroup(Literal featureLabel, List<Literal> childFeatureLabels) {
		if (childFeatureLabels.size() == 1) {
			constraints.add(implies(featureLabel, childFeatureLabels.get(0)));
		} else {
			constraints.add(new And(implies(featureLabel, childFeatureLabels), atMostOne(childFeatureLabels)));
		}
	}

	@Override
	protected void addFeatureMetadata(Literal featureLabel, Element e) {
	}

	@Override
	protected Boolean createConstraintLabel() {
		return true;
	}

	@Override
	protected void addConstraint(Boolean constraintLabel, Formula formula) throws ParseException {
		constraints.add(formula);
	}

	@Override
	protected void addConstraintMetadata(Boolean constraintLabel, Element e) {
	}
}
