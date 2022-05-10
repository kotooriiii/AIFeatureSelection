package search;

import tree.FeatureSelectionTree;

import java.util.HashSet;
import java.util.Set;

public class ForwardSelectionSearch<T> extends AbstractSearch<T>
{
    public ForwardSelectionSearch(FeatureSelectionTree<T> tree)
    {
        super(tree);
    }

    @Override
    public void nextFeature(FeatureSelectionTree<T>.Node node)
    {
        //Use all operators to expand a node
        for (T feature : this.tree.getMaxFeatures())
        {

            //todo Implement search type (forward selection AND backwards selection)
            //default: this is forward selection
            //skip, we already have this feature
            if (node.getFeatures().contains(feature))
                continue;

            HashSet<T> newSet = new HashSet<T>(node.getFeatures());
            newSet.add(feature);
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
        return new HashSet<T>(this.tree.getMaxFeatures().size());
    }
}
