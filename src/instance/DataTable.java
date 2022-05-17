package instance;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.HashSet;

public class DataTable
{
    private HashMap<Integer, String> featureMap;
    private HashMap<Integer, String> classMap;

    private HashMap<String, Integer> features, classes;


    public DataTable()
    {
        featureMap = new HashMap<>();
        classMap = new HashMap<>();
        features = new HashMap<>();
        classes = new HashMap<>();
    }

    public Integer addFeature(String feature)
    {
        if(features.containsKey(feature))
        {
            return features.get(feature);
        }

        final int size = featureMap.size();
        featureMap.put(size, feature);
        features.put(feature, size);
        return size;
    }

    public Integer addClass(String clazz)
    {
        if(classes.containsKey(clazz))
        {
            return classes.get(clazz);
        }
        final int size = classMap.size();
        classMap.put(size, clazz);
        classes.put(clazz, size);
        return size;
    }

    public String getFeatureName(Integer integer)
    {
        return featureMap.get(integer);
    }

    public String getClassName(Integer integer)
    {
        return classMap.get(integer);
    }


}
