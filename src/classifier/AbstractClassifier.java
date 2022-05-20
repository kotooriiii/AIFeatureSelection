package classifier;

import driver.MachineLearningManager;
import evaluation.AbstractEvaluation;
import instance.DataInstance;
import instance.DataInstanceManager;
import tree.FeatureSelectionTree;

import java.util.List;
import java.util.Set;

public abstract class AbstractClassifier
{
    protected Set<Integer> currentFeatures;
    protected MachineLearningManager manager;

    public AbstractClassifier(MachineLearningManager manager)
    {
        this.manager = manager;
    }

    public abstract void train(Set<Integer> currentFeatures, List<DataInstance> trainingInstances);
    public abstract Integer test(DataInstance instance);

}
