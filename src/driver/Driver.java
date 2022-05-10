package driver;

import classifier.KNNClassifier;
import evaluation.RandomEvaluation;
import search.BackwardSelectionSearch;
import search.ForwardSelectionSearch;
import tree.FeatureSelectionTree;

import java.util.HashSet;
import java.util.Random;

public class Driver
{
    public static void main(String[] args)
    {

        final int MAX_FEATURES = 3;
        HashSet<Integer> maxFeatures = new HashSet<>();
        for (int i = 1; i <= MAX_FEATURES; i++)
            maxFeatures.add(i);

        FeatureSelectionTree<Integer> featureSelectionTree = new FeatureSelectionTree<>(maxFeatures);
        featureSelectionTree.setClassifier(new KNNClassifier<Integer>());
        featureSelectionTree.setEvaluation(new RandomEvaluation<Integer>());
        featureSelectionTree.setSearch(new ForwardSelectionSearch<>(featureSelectionTree));

        final FeatureSelectionTree<Integer>.Node solution = featureSelectionTree.findSolution();
        System.out.println(solution);
    }
}
