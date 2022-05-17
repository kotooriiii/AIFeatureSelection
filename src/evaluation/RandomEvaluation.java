package evaluation;

import tree.FeatureSelectionTree;

public class RandomEvaluation extends AbstractEvaluation
{
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
