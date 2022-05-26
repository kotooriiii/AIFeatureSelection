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
import java.util.ArrayList;
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

    private ArrayList<Long> classifierAverageTime;
    private ArrayList<Long> evaluationAverageTime;

    private long loadDataTime;


    public MachineLearningManager(File input, boolean isIdentifying, boolean isDebug)
    {
        this.dataInstanceManager = new DataInstanceManager(this);
        this.dataInstanceManager.load(input, isIdentifying);
        this.classifierAverageTime = new ArrayList<>();
        this.evaluationAverageTime = new ArrayList<>();

        this.tree = new FeatureSelectionTree(this, dataInstanceManager.getMaxFeatures());
        this.tree.setInformative(isDebug);
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

    public double getAverageClassifierTime()
    {
        long classifierTime = 0;
        for(int i = 0; i < classifierAverageTime.size(); i++)
        {
            classifierTime += classifierAverageTime.get(i);
        }

        return (double) classifierTime/classifierAverageTime.size();
    }

    public double getAverageEvaluationTime()
    {
        long evaluationTime = 0;
        for(int i = 0; i < evaluationAverageTime.size(); i++)
        {
            evaluationTime += evaluationAverageTime.get(i);
        }

        return (double) evaluationTime/evaluationAverageTime.size();
    }

    public ArrayList<Long> getClassifierAverageTimeList()
    {
        return classifierAverageTime;
    }

    public ArrayList<Long> getEvaluationAverageTimeList()
    {
        return evaluationAverageTime;
    }

    public void setLoadDataTime(long loadDataTime)
    {
        this.loadDataTime = loadDataTime;
    }

    public long getLoadDataTime()
    {
        return loadDataTime;
    }

    public void setDebug(boolean isDebug)
    {
        this.isDebug = isDebug;
    }

    public boolean isDebug()
    {
        return isDebug;
    }

    public String toStringTime(long millis)
    {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return  days + "d:" + hours % 24 + "h:" + minutes % 60 + "m:" + seconds % 60 + "s:" + millis % 1000  + "ms";
    }
}
