package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.DataType.Tables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.sql.DriverManager;
import java.sql.Statement;

public class Sqlite extends DataBase {
    public static final String FILE_NAME = "/sqlite.db";//sqllite文件的名称
    private File mPath;//数据库文件的地址

    private final Logger LOGGER;
    private Sqlite()//私有构造函数
    {
        LOGGER = LogManager.getLogger();//获取日志记录器
    }

    @Override
    protected void CheckAndFixDataBaseStructure() throws Exception {
        Statement stmt = this.mConn.createStatement();//创建查询
        stmt.execute(Tables.SqlLite.CREATE_TG_ACTION);
        stmt.execute(Tables.SqlLite.CREATE_TG_ACTION_MAP);
        stmt.execute(Tables.SqlLite.CREATE_TG_PLAYER_MAP);
        stmt.execute(Tables.SqlLite.CREATE_TG_ENTITY_MAP);
        stmt.execute(Tables.SqlLite.CREATE_TG_DIMENSION_MAP);
        stmt.execute(Tables.SqlLite.CREATE_TG_ITEM_MAP);
    }

    @Override
    protected void VerifyConnection() throws Exception {

    }

    @Override
    public void ConnectToDataBase() throws Exception {
        LOGGER.info("Connecting to sqlite.");
        if(!this.mPath.exists())//判断文件是否存在
        {
           this.mPath.createNewFile();//如果文件不存在的话就创建一个新的文件
        }

        Class.forName("org.sqlite.JDBC");//加载数据库的驱动
        this.mConn = DriverManager.getConnection(String.format("jdbc:sqlite:%s",this.mPath.getPath()));//打开数据库的连接
        CheckAndFixDataBaseStructure();

        FlushActionMap();//刷新对照表
        FlushPlayerMap();
        FlushDimensionMap();
        FlushItemMap();
        FlushEntityMap();

        this.insert_action = this.mConn.prepareStatement(Tables.INSERT_ACTION_STR);//设置预处理语句
        this.insert_action_map = this.mConn.prepareStatement(Tables.INSERT_ACTION_MAP_STR);
        this.insert_dimension_map = this.mConn.prepareStatement(Tables.INSERT_DIMENSION_MAP_STR);
        this.insert_entity_map = this.mConn.prepareStatement(Tables.INSERT_ENTITY_MAP_STR);
        this.insert_item_map = this.mConn.prepareStatement(Tables.INSERT_ITEM_MAP_STR);
        this.insert_player_map = this.mConn.prepareStatement(Tables.INSERT_PLAYER_MAP_STR);

        this.query_point_action_count = this.mConn.prepareStatement(Tables.QUERY_POINT_ACTION_COUNT);
        this.query_point_action = this.mConn.prepareStatement(Tables.QUERY_POINT_ACTION);
        this.Query_area_action_count = this.mConn.prepareStatement(Tables.QUERY_AREA_ACTION_COUNT);
        this.query_area_action = this.mConn.prepareStatement(Tables.QUERY_AREA_ACTION);

        LOGGER.info("Sqlite connected.");
    }

    public static Sqlite GetSqlite(File file_path)//工厂函数
    {
        Sqlite temp_obj = new Sqlite();
        temp_obj.mPath = file_path;//设置文件的地址

        return temp_obj;
    }
}
