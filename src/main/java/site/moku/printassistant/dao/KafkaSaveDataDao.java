package site.moku.printassistant.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface KafkaSaveDataDao {

    public void insert(Map params) throws SQLException;

    public List<Map> qry();

}
