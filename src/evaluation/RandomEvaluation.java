package evaluation;

import tree.FeatureSelectionTree;

public class RandomEvaluation<T> extends AbstractEvaluation<T>
{
    @Override
    public double getAccuracy(FeatureSelectionTree<T>.Node node)
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
