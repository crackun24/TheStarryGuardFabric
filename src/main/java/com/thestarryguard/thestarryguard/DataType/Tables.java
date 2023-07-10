package com.thestarryguard.thestarryguard.DataType;

public class Tables {
    public class Mysql {
        public static final String CREATE_TG_ACTION = "CREATE TABLE `tg_action` (\n" +
                "  `player` int(10) NOT NULL,\n" +
                "  `action` int(10) NOT NULL,\n" +
                "  `target` int(10) NOT NULL,\n" +
                "  `time` int(10) NOT NULL,\n" +
                "  `data` varchar(255) DEFAULT NULL,\n" +
                "  `x` int(10) NOT NULL,\n" +
                "  `y` int(10) NOT NULL,\n" +
                "  `z` int(10) NOT NULL,\n" +
                "  `dimension` int(10) NOT NULL,\n" +
                "  KEY `location` (`x`,`y`,`z`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        public static final String CREATE_TG_ACTION_MAP = "CREATE TABLE `tg_action_map` (\n" +
                "  `action` varchar(255) NOT NULL,\n" +
                "  `id` int(10) NOT NULL,\n" +
                "  PRIMARY KEY (`action`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        public static final String CREATE_TG_ENTITY_MAP = "CREATE TABLE `tg_entity_map` (\n" +
                "  `entity` varchar(255) NOT NULL,\n" +
                "  `id` int(10) NOT NULL,\n" +
                "  PRIMARY KEY (`entity`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        public static final String CREATE_TG_ITEM_MAP = "CREATE TABLE `tg_item_map` (\n" +
                "  `item` varchar(255) NOT NULL,\n" +
                "  `id` int(10) NOT NULL,\n" +
                "  PRIMARY KEY (`item`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        public static final String CREATE_TG_PLAYER_MAP = "CREATE TABLE `tg_player_map` (\n" +
                "  `uuid` varchar(255) NOT NULL,\n" +
                "  `name` varchar(255) NOT NULL,\n" +
                "  `id` int(10) NOT NULL,\n" +
                "  PRIMARY KEY (`uuid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        public static final String CREATE_TG_DIMENSION_MAP = "CREATE TABLE `tg_dimension_map` (\n" +
                "  `dimension` varchar(255) NOT NULL,\n" +
                "  `id` int(10) NOT NULL,\n" +
                "  PRIMARY KEY (`dimension`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        public static final String INSERT_ACTION_STR = "INSERT INTO tg_action (player, action, target, time, data, x, y, z, dimension) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";//玩家行为预处理语句


        public static final String INSERT_ACTION_MAP_STR = "INSERT INTO tg_action_map (action, id) VALUES (?, ?)";
        public static final String INSERT_DIMENSION_MAP_STR = "INSERT INTO tg_dimension_map (dimension, id) VALUES (?, ?)";
        public static final String INSERT_ENTITY_MAP_STR = "INSERT INTO tg_entity_map (entity, id) VALUES (?, ?)";
        public static final String INSERT_ITEM_MAP_STR = "INSERT INTO tg_item_map (item, id) VALUES (?, ?)";
        public static final String INSERT_PLAYER_MAP_STR = "INSERT INTO tg_player_map (uuid, name, id) VALUES (?, ?, ?)";
        public static final String QUERY_POINT_ACTION = "SELECT * FROM tg_action WHERE x = ? AND y = ? AND z = ? AND dimension = ? ORDER BY time DESC LIMIT ?, ?";
        public static final String QUERY_POINT_ACTION_COUNT = "SELECT COUNT(*) AS count FROM tg_action WHERE x = ? AND y = ? AND z = ? AND dimension = ?";
    }


    public class SqlLite {

    }
}
