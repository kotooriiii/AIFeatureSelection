package search;

import tree.FeatureSelectionTree;

import java.util.Set;

public abstract class AbstractSearch<T>
{
    protected FeatureSelectionTree<T> tree;

    public AbstractSearch(FeatureSelectionTree<T> tree)
    {
        this.tree = tree;
    }

    public abstract void nextFeature(FeatureSelectionTree<T>.Node node);

    public abstract Set<T> getInitialSet();

}
