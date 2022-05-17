package classifier;

import evaluation.AbstractEvaluation;
import instance.DataInstance;
import instance.DataInstanceManager;
import tree.FeatureSelectionTree;

import java.util.Set;

public abstract class AbstractClassifier
{
    protected Set<Integer> currentFeatures;
    protected DataInstanceManager manager;

    public AbstractClassifier(DataInstanceManager manager, FeatureSelectionTree tree)
    {
        this.manager = manager;
        this.currentFeatures = tree.findSolution().getFeatures();
    }

    public abstract void train();
    public abstract Integer test(DataInstance instance);

}
