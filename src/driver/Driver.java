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
        GUI gui = new GUI();
        gui.sendMenu();
    }
}
