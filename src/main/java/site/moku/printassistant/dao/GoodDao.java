package site.moku.printassistant.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface GoodDao {
    public int sell(int id);

    public int sellWithVersion(Map params);

    public Map qryVersion(int id);
}
