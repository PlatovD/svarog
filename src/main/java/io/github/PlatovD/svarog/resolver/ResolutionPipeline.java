package io.github.PlatovD.svarog.resolver;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.resolver.step.CycleDetectionStep;
import io.github.PlatovD.svarog.resolver.step.IndexingStep;
import io.github.PlatovD.svarog.resolver.step.ResolutionStep;
import io.github.PlatovD.svarog.resolver.step.ResolvingStep;
import io.github.PlatovD.svarog.resolver.step.SortingStep;

import java.util.ArrayList;
import java.util.List;

public final class ResolutionPipeline {

    private final List<ResolutionStep> steps;

    private ResolutionPipeline(List<ResolutionStep> steps) {
        this.steps = List.copyOf(steps);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ResolutionPipeline defaultPipeline() {
        return builder()
                .addStep(new IndexingStep())
                .addStep(new ResolvingStep())
                .addStep(new CycleDetectionStep())
                .addStep(new SortingStep())
                .build();
    }

    public ResolutionResult resolve(List<BeanDefinition> definitions) {
        ResolutionContext context = new ResolutionContext(definitions);
        for (ResolutionStep step : steps) {
            step.apply(context);
        }
        return new ResolutionResult(
                context.getCreationOrder(),
                context.getResolvedDependencies(),
                context.getByType(),
                context.getByName()
        );
    }

    public static final class Builder {

        private final List<ResolutionStep> steps = new ArrayList<>();

        public Builder addStep(ResolutionStep step) {
            steps.add(step);
            return this;
        }

        public ResolutionPipeline build() {
            return new ResolutionPipeline(steps);
        }
    }
}