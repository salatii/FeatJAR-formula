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
package de.featjar.formula.io;

import de.featjar.base.data.Result;
import de.featjar.base.io.format.IFormat;
import de.featjar.base.io.input.AInputMapper;
import de.featjar.formula.analysis.bool.*;
import java.util.List;

/**
 * Reads / Writes a list of configuration.
 *
 * @author Sebastian Krieter
 * @deprecated
 */
@Deprecated
public class ListFormat implements IFormat<ABooleanAssignmentList<?>> {

    @Override
    public Result<String> serialize(ABooleanAssignmentList<?> dnf) {
        final StringBuilder csv = new StringBuilder();
        csv.append("Configuration");
        final List<String> names = null; // dnf.getVariableMap().getVariableNames();
        for (final String name : names) {
            csv.append(';');
            csv.append(name);
        }
        csv.append('\n');
        int configurationIndex = 0;
        for (final ABooleanAssignment configuration : dnf.getAll()) {
            csv.append(configurationIndex++);
            final int[] literals = configuration.get();
            for (int literal : literals) {
                csv.append(';');
                csv.append(literal < 0 ? 0 : 1);
            }
            csv.append('\n');
        }
        return Result.of(csv.toString());
    }

    @Override
    public Result<ABooleanAssignmentList<?>> parse(AInputMapper inputMapper) {
        //        int lineNumber = 0;
        //        final BooleanSolutionList dnf = new BooleanSolutionList();
        //        final Iterator<String> iterator = inputMapper.get().getLineStream().iterator();
        //        try {
        //            {
        //                if (!iterator.hasNext()) {
        //                    return Result.empty(new ParseProblem("Empty file!", lineNumber, Severity.ERROR));
        //                }
        //                final String line = iterator.next();
        //                if (line.trim().isEmpty()) {
        //                    return Result.empty(new ParseProblem("Empty file!", lineNumber, Severity.ERROR));
        //                }
        //                final String[] names = line.split(";");
        //                final VariableMap map = VariableMap.empty();
        //                Arrays.asList(names).subList(1, names.length).forEach(map::add);
        //               // dnf.setVariableMap(map);
        //            }
        //
        //            while (iterator.hasNext()) {
        //                final String line = iterator.next();
        //                lineNumber++;
        //                final String[] split = line.split(";");
        //                if ((split.length - 1)
        //                        != dnf
        //                                .getVariableMap()
        //                                .getVariableNames()
        //                                .size()) {
        //                    return Result.empty(new ParseProblem(
        //                            "Number of selections does not match number of features!", lineNumber,
        // Severity.ERROR));
        //                }
        //                final int[] literals = new int
        //                        [dnf
        //                                .getVariableMap()
        //                                .getVariableNames()
        //                                .size()];
        //                for (int i = 1; i < split.length; i++) {
        //                    literals[i - 1] = split[i].equals("0") ? -i : i;
        //                }
        //                dnf.add(new BooleanSolution(literals, false));
        //            }
        //        } catch (final Exception e) {
        //            return Result.empty(new ParseProblem(e.getMessage(), lineNumber, Severity.ERROR));
        //        }
        //        return Result.of(dnf);
        return null;
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }

    @Override
    public boolean supportsSerialize() {
        return true;
    }

    @Override
    public boolean supportsParse() {
        return true;
    }

    @Override
    public String getName() {
        return "ConfigurationList";
    }
}
