package search;

import tree.FeatureSelectionTree;

import java.util.HashSet;
import java.util.Set;

public class BackwardSelectionSearch<T> extends AbstractSearch<T>
{
    public BackwardSelectionSearch(FeatureSelectionTree<T> tree)
    {
        super(tree);
    }

    @Override
    public void nextFeature(FeatureSelectionTree<T>.Node node)
    {
        //Use all operators to expand a node
        for (T feature : this.tree.getMaxFeatures())
        {
            if (!node.getFeatures().contains(feature))
                continue;

            HashSet<T> newSet = new HashSet<T>(node.getFeatures());
            newSet.remove(feature);

            //Add the child node and attach its parents, increase a move, and set the operator needed to move to this node.
            final FeatureSelectionTree<T>.Node childAdded = this.tree.add(node, newSet);
            if (childAdded != null)
            {
                childAdded.addMove();
                this.tree.getFrontier().add(childAdded);
            }
        }
    }

    @Override
    public Set<T> getInitialSet()
    {
        HashSet<T> set =  new HashSet<T>(this.tree.getMaxFeatures());
        set.addAll(this.tree.getMaxFeatures());

        return set;
    }
}
