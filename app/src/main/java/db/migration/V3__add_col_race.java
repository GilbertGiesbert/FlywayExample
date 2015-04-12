package db.migration;


import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V33__add_col_race implements JdbcMigration{

    @Override
    public void migrate(Connection connection) throws Exception {

        PreparedStatement statement = connection
                .prepareStatement("ALTER TABLE dogs ADD COLUMN race TEXT");
        statement.execute();
    }
}