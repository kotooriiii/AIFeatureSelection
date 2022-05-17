package search;

import tree.FeatureSelectionTree;

import java.util.HashSet;
import java.util.Set;

public class BackwardSelectionSearch extends AbstractSearch
{
    public BackwardSelectionSearch(FeatureSelectionTree tree)
    {
        super(tree);
    }

    @Override
    public void nextFeature(FeatureSelectionTree.Node node)
    {
        //Use all operators to expand a node
        for (Integer feature : this.tree.getMaxFeatures())
        {
            if (!node.getFeatures().contains(feature))
                continue;

            HashSet<Integer> newSet = new HashSet<Integer>(node.getFeatures());
            newSet.remove(feature);

            //Add the child node and attach its parents, increase a move, and set the operator needed to move to this node.
            final FeatureSelectionTree.Node childAdded = this.tree.add(node, newSet);
            if (childAdded != null)
            {
                childAdded.addMove();
                this.tree.getFrontier().add(childAdded);
            }
        }
    }

    @Override
    public Set<Integer> getInitialSet()
    {
        HashSet<Integer> set =  new HashSet<Integer>(this.tree.getMaxFeatures());
        set.addAll(this.tree.getMaxFeatures());

        return set;
    }
}
