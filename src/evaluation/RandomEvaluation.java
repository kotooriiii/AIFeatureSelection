package evaluation;

import tree.FeatureSelectionTree;

public class RandomEvaluation<T> extends AbstractEvaluation<T>
{
    @Override
    public double getAccuracy(FeatureSelectionTree<T>.Node node)
    {
        return Math.random();
    }


}
