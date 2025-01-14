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
package de.featjar.formula.transformer;

import de.featjar.base.computation.*;
import de.featjar.base.computation.Progress;
import de.featjar.base.data.Result;
import de.featjar.base.tree.structure.ITree;
import de.featjar.formula.structure.ExpressionKind;
import de.featjar.formula.structure.formula.IFormula;
import de.featjar.formula.structure.formula.FormulaNormalForm;
import de.featjar.formula.structure.formula.connective.And;
import de.featjar.formula.tester.NormalForms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Transforms a formula into strict conjunctive normal form.
 *
 * @author Sebastian Krieter
 * @author Elias Kuiter
 */
public class ComputeCNFFormula extends AComputation<IFormula> implements ITransformation<IFormula> {
    protected static final Dependency<IFormula> NNF_FORMULA = newRequiredDependency();
    protected static final Dependency<Boolean> IS_PLAISTED_GREENBAUM = newOptionalDependency(false);
    protected static final Dependency<Integer> MAXIMUM_NUMBER_OF_LITERALS = newOptionalDependency(Integer.MAX_VALUE); // be careful, this creates new variables that may clash on composition
    protected static final Dependency<Boolean> IS_PARALLEL = newOptionalDependency(false); // be careful, this does not guarantee determinism


    /**
     * Creates a new CNF formula computation.
     *
     * @param nnfFormula the input NNF formula computation
     */
    public ComputeCNFFormula(IComputation<IFormula> nnfFormula) {
        dependOn(NNF_FORMULA, IS_PLAISTED_GREENBAUM, MAXIMUM_NUMBER_OF_LITERALS, IS_PARALLEL);
        setInput(nnfFormula);
    }

    @Override
    public Dependency<IFormula> getInputDependency() {
        return NNF_FORMULA;
    }

    /**
     * {@return whether this computation uses the Plaisted-Greenbaum optimization}
     * If {@code true}, auxiliary variables are defined with {@link de.featjar.formula.structure.formula.connective.Implies} instead
     * of {@link de.featjar.formula.structure.formula.connective.BiImplies}, which yields smaller formulas that are not model-count-preserving.
     */
    public IComputation<Boolean> isPlaistedGreenbaum() {
        return getDependency(IS_PLAISTED_GREENBAUM);
    }

    /**
     * Sets whether this computation uses the Plaisted-Greenbaum optimization.
     *
     * @param isPlaistedGreenbaum whether this computation uses the Plaisted-Greenbaum optimization
     */
    public void setPlaistedGreenbaum(IComputation<Boolean> isPlaistedGreenbaum) {
        setDependency(IS_PLAISTED_GREENBAUM, isPlaistedGreenbaum);
    }

    /**
     * {@return the maximum number of literals available for distributive transformation}
     * When this number is exceeded for a constraint in the formula, it is instead transformed using the {@link TseitinTransformer}.
     */
    public IComputation<Integer> getMaximumNumberOfLiterals() {
        return getDependency(MAXIMUM_NUMBER_OF_LITERALS);
    }

    /**
     * Sets the maximum number of literals available for distributive transformation.
     *
     * @param maximumNumberOfLiterals the maximum number of literals
     */
    public void setMaximumNumberOfLiterals(IComputation<Integer> maximumNumberOfLiterals) {
        setDependency(MAXIMUM_NUMBER_OF_LITERALS, maximumNumberOfLiterals);
    }

    /**
     * Sets whether this computation introduces auxiliary variables.
     *
     * @param tseitin whether this computation introduces auxiliary variables
     */
    public void setTseitin(IComputation<Boolean> tseitin) {
        setDependency(MAXIMUM_NUMBER_OF_LITERALS,
                tseitin.mapResult(ComputeCNFFormula.class, "setTseitin", b -> b ? 0 : Integer.MAX_VALUE));
    }

    /**
     * {@return whether this computation is parallel}
     */
    public IComputation<Boolean> isParallel() {
        return getDependency(IS_PARALLEL);
    }

    /**
     * Sets whether this computation is parallel.
     *
     * @param isParallel whether this computation is parallel
     */
    public void setParallel(IComputation<Boolean> isParallel) {
        setDependency(IS_PARALLEL, isParallel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result<IFormula> compute(DependencyList dependencyList, Progress progress) {
        IFormula nnfFormula = dependencyList.get(NNF_FORMULA);
        boolean isPlaistedGreenbaum = dependencyList.get(IS_PLAISTED_GREENBAUM);
        int maximumNumberOfLiterals = dependencyList.get(MAXIMUM_NUMBER_OF_LITERALS);
        boolean isParallel = dependencyList.get(IS_PARALLEL);
        ExpressionKind.NNF.assertFor(nnfFormula);

        List<IFormula> clauseFormulas = isParallel ? Collections.synchronizedList(new ArrayList<>()) : new ArrayList<>();
        List<TseitinTransformer.Substitution> substitutions = isParallel ? Collections.synchronizedList(new ArrayList<>()) : new ArrayList<>();
        Consumer<IFormula> transformer = formula -> {
            transform(formula, clauseFormulas, substitutions, isPlaistedGreenbaum, maximumNumberOfLiterals);
            progress.incrementCurrentStep();
        };

        if (nnfFormula instanceof And) {
            List<IFormula> children = (List<IFormula>) nnfFormula.getChildren();
            progress.setTotalSteps(children.size());
            if (isParallel) {
                children.parallelStream().forEach(transformer);
            } else {
                children.forEach(transformer);
            }
        } else {
            progress.setTotalSteps(1);
            transformer.accept(nnfFormula);
        }

        TseitinTransformer.unify(substitutions);
        clauseFormulas.addAll(TseitinTransformer.getClauseFormulas(substitutions));
        return Result.of(NormalForms.normalToStrictNormalForm(new And(clauseFormulas), FormulaNormalForm.CNF));
    }

    @SuppressWarnings("unchecked")
    private void transform(IFormula formula, List<IFormula> clauseFormulas, List<TseitinTransformer.Substitution> substitutions, boolean isPlaistedGreenbaum, int maximumNumberOfLiterals) {
        if (formula.isStrictNormalForm(FormulaNormalForm.CNF)) {
            clauseFormulas.addAll((List<? extends IFormula>) formula.getChildren());
        } else if (formula.isNormalForm(FormulaNormalForm.CNF)) {
            clauseFormulas.addAll((List<? extends IFormula>) NormalForms.normalToStrictNormalForm(formula, FormulaNormalForm.CNF).getChildren());
        } else {
            Result<IFormula> transformationResult = distributiveTransform(formula,
                    new DistributiveTransformer.MaximumNumberOfLiteralsCancelPredicate(maximumNumberOfLiterals));
            if (transformationResult.isPresent()) {
                clauseFormulas.addAll((List<? extends IFormula>) transformationResult.get().getChildren());
                return;
            }
            substitutions.addAll(tseitinTransform(formula, isPlaistedGreenbaum));
        }
    }

    protected Result<IFormula> distributiveTransform(IFormula formula, DistributiveTransformer.ICancelPredicate cancelPredicate) {
        return new DistributiveTransformer(true, cancelPredicate).apply(formula);
    }

    protected List<TseitinTransformer.Substitution> tseitinTransform(IFormula formula, boolean isPlaistedGreenbaum) {
        return new TseitinTransformer(isPlaistedGreenbaum).apply(formula);
    }

    @Override
    public ITree<IComputation<?>> cloneNode() {
        return new ComputeCNFFormula(getInput());
    }
}
