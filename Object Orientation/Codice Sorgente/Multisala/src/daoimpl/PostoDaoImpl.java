package daoimpl;

import connection.Connessione;
import dao.PostoDao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class PostoDaoImpl implements PostoDao {
    
    private final Connessione conn = new Connessione();
    private final RowMapperDao rmd = new RowMapperDao();

    @Override
    /**
     * Metodo per la conversione di un ResultSet in un'ArrayList
     *
     * @param query La query sulla quale effettuare la ricerca
     * @return ResultSet convertito in ArrayList, ogni elemento della lista
     * contiene una mappa che replica la riga della tabella restituita dal DB
     * @throws SQLException
     */
    public ArrayList getListPosti(String idProiezione, String dataOdierna) {
        String query = "SELECT * FROM multisala.get_biglietti_disponibili(" + idProiezione + ",'" + dataOdierna + "')";
        ArrayList list = new ArrayList();
        Connection con = conn.connect();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
                HashMap row = new HashMap(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(row);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return list;
        }
    }
}
