package search;

import tree.FeatureSelectionTree;

import java.util.Set;

public abstract class AbstractSearch
{
    protected FeatureSelectionTree tree;

    public AbstractSearch(FeatureSelectionTree tree)
    {
        this.tree = tree;
    }

    public abstract void nextFeature(FeatureSelectionTree.Node node);

    public abstract Set<Integer> getInitialSet();

}
