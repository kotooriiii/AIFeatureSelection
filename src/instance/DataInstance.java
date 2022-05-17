package instance;

import java.util.HashMap;

public class DataInstance
{
    private Integer classId;
    private HashMap<Integer, Double> featuresId;

    public DataInstance(Integer classId)
    {
        this.classId = classId;
        this.featuresId = new HashMap<>();
    }

    public DataInstance()
    {
        this.classId = -1;
        this.featuresId = new HashMap<>();
    }

    public Double putFeatureData(Integer integer, Double duble)
    {
        return featuresId.put(integer, duble);
    }

    public Double getFeatureData(Integer integer)
    {
        return featuresId.get(integer);
    }

    public Integer getClassData()
    {
        return classId;
    }

    public void setClassId(Integer classId)
    {
        this.classId = classId;
    }

    public HashMap<Integer, Double> getFeaturesId()
    {
        return featuresId;
    }
}
