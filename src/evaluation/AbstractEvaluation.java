package evaluation;

import tree.FeatureSelectionTree;

import java.util.Comparator;

/**
 * Every class must implement {@link AbstractEvaluation} in order to be able to be used as an evaluation function for feauture selection.
 */
public abstract class AbstractEvaluation<T> implements Comparator<FeatureSelectionTree<T>.Node>
{
    /**
     * This function returns the accuracy of the current node
     *
     * @return A value x, 0 <= x < 1. The higher the value x is, the higher the accuracy.
     */
    public abstract double getAccuracy(FeatureSelectionTree<T>.Node node);

    public int compare(FeatureSelectionTree<T>.Node o1, FeatureSelectionTree<T>.Node o2)
    {

        double accuracy1 = getAccuracy(o1);
        double accuracy2 = getAccuracy(o2);

        if(accuracy1 > accuracy2)
            return -1;
        else if (accuracy1 == accuracy2)
            return 0;
        else if (accuracy1 < accuracy2)
            return 1;

        //never occurs
        return 0;
    }
}
