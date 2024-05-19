package connection;

import java.sql.*;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class Connessione {

    private final String database_connection_string = "jdbc:postgresql://localhost:5432/Multisala";

    private final String database_user_name = "postgres";

    private final String database_user_password = "admin";

    /**
     * 
     * Utilizzando il seguente codice ci connettiamo al database e ritorniamo l'oggetto connessione.
     *
     * @return Connection
     */
    public Connection connect() {

        Connection conn = null;

        try {

            conn = DriverManager.getConnection(database_connection_string, database_user_name, database_user_password);
            System.out.println("You are successfully connected to the PostgreSQL database server.");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return conn;
    }
}
