package evaluation;

import classifier.AbstractClassifier;
import instance.DataInstanceManager;
import tree.FeatureSelectionTree;

public class RandomEvaluation extends AbstractEvaluation
{
    public RandomEvaluation(DataInstanceManager dataInstanceManager, AbstractClassifier classifier)
    {
        super(dataInstanceManager, classifier);
    }

    @Override
    public double getAccuracy(FeatureSelectionTree.Node node)
    {
        if (node.isSavedCostSet())
        {
            return node.getSavedCost();
        } else
        {
            node.setSavedCost(Math.random());
            return node.getSavedCost();
        }
    }


}
