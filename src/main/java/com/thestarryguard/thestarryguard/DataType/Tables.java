package com.thestarryguard.thestarryguard.DataType;

public class Tables {
    public class Mysql {
        public static final String CREATE_TG_ACTION = "CREATE TABLE `tg_action` (\n" +
                "  `player` int(10) NOT NULL,\n" +
                "  `action` int(10) NOT NULL,\n" +
                "  `target` int(10) NOT NULL,\n" +
                "  `time` int(10) NOT NULL,\n" +
                "  `x` int(10) NOT NULL,\n" +
                "  `y` int(10) NOT NULL,\n" +
                "  `z` int(10) NOT NULL\n" +
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
                "  `block` varchar(255) NOT NULL,\n" +
                "  `id` int(10) NOT NULL,\n" +
                "  PRIMARY KEY (`block`)\n" +
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
    }

    public class SqlLite {

    }
}
