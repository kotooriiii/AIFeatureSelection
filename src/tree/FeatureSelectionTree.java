package tree;


import classifier.AbstractClassifier;
import classifier.KNNClassifier;
import evaluation.AbstractEvaluation;
import evaluation.RandomEvaluation;
import search.AbstractSearch;
import search.BackwardSelectionSearch;
import search.ForwardSelectionSearch;

import java.util.*;

public class FeatureSelectionTree<T>
{

    // The initial root node
    private Node root;

    //The algorithm function which decides how to calculate accuracy.
    private AbstractEvaluation<T> evaluation;
    private AbstractClassifier classifier; //todo document this
    private AbstractSearch search;

    //The list of nodes that are ready to expand/check if goal state.
    private Queue<Node> frontier;

    //The solution node. This node is important for us to know the path from initial to here.
    private Node solution = null;

    /**
     * The set (unique) of features for feature selection
     */
    private final Set<T> maxFeatures;
    private boolean isDebug = false;

    public class Node
    {
        //The set of features in this node
        private final Set<T> features;
        //The parent of this node
        private Node parent;
        //The list of children of this node
        private final List<Node> children;

        //The amount of moves that were needed to get to this state
        private int moves = 0;

        private final double errorSavedCost = Double.NEGATIVE_INFINITY;
        private double savedCost;

        /**
         * A constructor that creates a (child) node given you have a parent node and the state data.
         *
         * @param parent   The parent of *this* node. If this value is null, this becomes the root.
         * @param features The state data
         */
        public Node(Node parent, Set<T> features)
        {
            this.parent = parent;
            this.features = features;
            this.children = new ArrayList<Node>();
            savedCost = errorSavedCost;
        }

        /**
         * Private constructor for tree to define the root node
         *
         * @param features Initial set of features
         */
        private Node(Set<T> features)
        {
            this.features = features;
            this.children = new ArrayList<Node>();
            savedCost = errorSavedCost;
        }

        public void setSavedCost(double savedCost)
        {
            this.savedCost = savedCost;
        }

        public double getSavedCost()
        {
            return savedCost;
        }

        public boolean isSavedCostSet()
        {
            return this.savedCost != this.errorSavedCost;
        }

        /**
         * Gets the features of this node
         *
         * @return features of this node
         */
        public Set<T> getFeatures()
        {
            return this.features;
        }

        /**
         * Gets the parent of this node
         *
         * @return parent node
         */
        public Node getParent()
        {
            return parent;
        }

        public List<Node> getChildren()
        {
            return children;
        }

        /**
         * Gets the amount of moves needed to reach this state.
         *
         * @return a positive integer that represents the amount of moves needed to reach *this* node.
         */
        public int getMoves()
        {
            return moves;
        }

        /**
         * Adds a move to the counter.
         */
        public void addMove()
        {
            this.moves = getParent().getMoves() + 1;
        }

        /**
         * Checks if this node is the root node.
         *
         * @return true if the node is the root node, false otherwise
         */
        public boolean isRoot()
        {
            return parent == null;
        }

        /**
         * Checks if this node is te leaf node (no children).
         *
         * @return true if the node is a leaf node, false otherwise
         */
        public boolean isLeaf()
        {
            return children.size() == 0;
        }

        /**
         * Returns a path from the initial node to this node.
         *
         * @return a String object that contains a path from initial node to this node.
         */
        @Override
        public String toString()
        {
            String path = "Path from Initial to Solution:\n\n";

            Node current = this;

            if (current.parent == null) //Return early if this is already the solution!
            {
                return path + "No operators needed.";
            }

            Stack<String> stack = new Stack<>(); //stack needed to reverse order from string. if queue, then this node to initial state.


            stack.push("State " + current.getMoves() + "\n" + current.features.toString() + "\n\n"); //Push to stack the operator and the current state

            Node parent = current.getParent(); //Get the parent of the current.

            //Loop, If has parent
            while (parent != null)
            {
                String part = "";

                String operatorName = "";


                if (parent.parent == null) //Initial State condition
                {
                    operatorName = "Initial State";
                } else //Loops middle nodes and grabs operator name and parent states
                {
                    operatorName = "State " + parent.getMoves();
                }

                part += operatorName + "\n" + parent.features.toString() + "\n\n";

                stack.push(part);


                parent = parent.getParent();
            }

            while (!stack.isEmpty()) //make a string from popping the stack
            {
                path += stack.pop();
            }

            return path;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj != null && obj instanceof FeatureSelectionTree.Node)
            {

                Node node = (Node) obj;

                for (T feature : this.getFeatures())
                {
                    if (!node.getFeatures().contains(feature))
                        return false;
                }

                for (T feature : node.getFeatures())
                {
                    if (!this.getFeatures().contains(feature))
                        return false;
                }

                return true;
            }
            return false;
        }
    }

    public FeatureSelectionTree(Set<T> maxFeatures)
    {
        this.maxFeatures = maxFeatures;

        this.evaluation = new RandomEvaluation<T>();
        this.classifier = new KNNClassifier(); //todo implement this
        this.search = new ForwardSelectionSearch<T>(this);

        this.root = new Node(this.search.getInitialSet());

        this.frontier = new PriorityQueue<Node>(evaluation);
        this.frontier.add(root);
    }


    /**
     * Finds the solution starting from the initial node (root).
     *
     * @return a Node object that represents the goal node, otherwise returns null if no solution was found.
     */
    public Node findSolution()
    {

        boolean isFirst = true;
        //Keep looping until the frontier is empty
        while (!frontier.isEmpty())
        {


            //Remove the next node in the frontier (remember this is a priority queue so our evaluation function already took care of this!)
            final Node poll = frontier.poll();
            frontier.clear(); //greedy algorithm, clear dont backtrack

            if (isFirst)
            {
                isFirst = false;
                if (isDebug)
                {
                    System.out.println("Default Rate Accuracy: " + format(evaluation.getAccuracy(poll)));
                }
            }

            this.search.nextFeature(poll);


            for (Node node : poll.getChildren())
            {
                if (isDebug)
                    System.out.println("Using feature(s) {" + node.getFeatures().toString() + "} accuracy is " + format(evaluation.getAccuracy(node)));

            }


            if(isDebug)
            System.out.println();

            if (evaluation.compare(this.frontier.peek(),  poll) > 0|| poll.getChildren().isEmpty())
            {
                if (isDebug)
                {
                    System.out.println("Accuracy decreased in all children.");
                    System.out.println("Search finished.");
                    System.out.println("The best feature subset was using feature(s) {" + poll.getFeatures().toString() + "} accuracy is " + format(evaluation.getAccuracy(poll)));
                }
                this.solution = poll;
                break;
            } else
            {
                if (isDebug)
                {
                    System.out.println("Accuracy increased in a child.");
                    System.out.println("Choosing child with best feature subset using feature(s) {" + this.frontier.peek().getFeatures().toString() + "} accuracy is " + format(evaluation.getAccuracy(this.frontier.peek())));
                }
            }

            if(isDebug)
                System.out.println();
        }
        return solution;
    }

    /**
     * Adds a child node to the supplied Node object.
     *
     * @param current The parent node
     * @param data    The new Set of features
     * @return
     */
    public final FeatureSelectionTree<T>.Node add(FeatureSelectionTree<T>.Node current, Set<T> data)
    {
        assert current != null;
        if (data == null)
            return null;

        FeatureSelectionTree<T>.Node child = new FeatureSelectionTree<T>.Node(current, data);
        current.getChildren().add(child);

        return child;
    }

    //Getters

    public Node getRoot()
    {
        return root;
    }

    public Node getSolution()
    {
        return solution;
    }

    public Queue<Node> getFrontier()
    {
        return frontier;
    }

    public AbstractEvaluation<T> getEvaluation()
    {
        return evaluation;
    }

    public AbstractClassifier getClassifier()
    {
        return classifier;
    }

    public Set<T> getMaxFeatures()
    {
        return maxFeatures;
    }

    public void setClassifier(AbstractClassifier<T> classifier)
    {
        this.classifier = classifier;
    }

    public void setSearch(AbstractSearch<T> search)
    {
        this.search = search;
        this.root = new Node(this.search.getInitialSet());
        this.solution = null;
        this.frontier.clear();
        this.frontier.add(root);

    }

    public void setEvaluation(AbstractEvaluation<T> evaluation)
    {
        this.evaluation = evaluation;
        this.solution = null;
        this.frontier = new PriorityQueue<Node>(evaluation); //todo implement this priority queue comparator
        this.frontier.add(root);
    }

    public void setDebug(boolean isDebug)
    {
        this.isDebug = isDebug;
    }

    public String format(double value)
    {
        return String.format("%.2f", value * 100) + "%";
    }
}
