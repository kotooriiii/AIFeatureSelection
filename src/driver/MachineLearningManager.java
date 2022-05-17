package driver;

import classifier.AbstractClassifier;
import classifier.KNNClassifier;
import evaluation.AbstractEvaluation;
import evaluation.RandomEvaluation;
import instance.DataInstanceManager;
import search.AbstractSearch;
import search.ForwardSelectionSearch;
import tree.FeatureSelectionTree;

import java.io.File;
import java.util.PriorityQueue;

public class MachineLearningManager
{
    //The algorithm function which decides how to calculate accuracy.
    private AbstractEvaluation evaluation;
    private AbstractClassifier classifier; //todo document this
    private AbstractSearch search;
    private FeatureSelectionTree tree;
    private DataInstanceManager dataInstanceManager;
    private boolean isDebug = false;

    public MachineLearningManager(File input)
    {

        this.setDebug(true);
        this.dataInstanceManager = new DataInstanceManager();
        this.dataInstanceManager.load(input);

        this.tree = new FeatureSelectionTree(this, dataInstanceManager.getMaxFeatures());
    }

    public AbstractClassifier getClassifier()
    {
        return classifier;
    }

    public AbstractEvaluation getEvaluation()
    {
        return evaluation;
    }

    public AbstractSearch getSearch()
    {
        return search;
    }

    public FeatureSelectionTree getTree()
    {
        return tree;
    }

    public DataInstanceManager getDataInstanceManager()
    {
        return dataInstanceManager;
    }

    public void setTree(FeatureSelectionTree tree)
    {
        this.tree = tree;
    }

    public void setClassifier(AbstractClassifier classifier)
    {
        this.classifier = classifier;
    }

    public void setSearch(AbstractSearch search)
    {
        this.search = search;

    }

    public void setEvaluation(AbstractEvaluation evaluation)
    {
        this.evaluation = evaluation;

    }

    public void setDebug(boolean isDebug)
    {
        this.isDebug = isDebug;
    }

    public boolean isDebug()
    {
        return isDebug;
    }
}
