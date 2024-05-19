package daoimpl;

import connection.Connessione;
import dao.SalaDao;
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
public class SalaDaoImpl implements SalaDao {

    private final Connessione conn = new Connessione();
    private final RowMapperDao rmd = new RowMapperDao();

    @Override
    /**
     * Metodo per la conversione di un ResulSet in un'ArrayList
     *
     * @param query La query sulla quale effettuare la ricerca
     * @return ResultSet convertito in ArrayList, ogni elemento della lista
     * contiene una mappa che replica la riga della tabella restituita dal DB
     * @throws SQLException
     */
    public ArrayList getListSale() {
        String query = "SELECT * FROM multisala.\"Sale\"";
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

    @Override
    /**
     * Metodo per la resituzione di un singolo elemento dal DB
     *
     * @param query La query da eseguire (può essere presente una sola colonna
     * della SELECT)
     * @return Elemento richiesto dalla query
     * @throws SQLException
     */
    public Object getSala(String id_proiezione) {
        String query = "SELECT \"Cod_Sala\" FROM multisala.\"Proiezioni\" WHERE \"ID_Proiezione\" = " + id_proiezione;
        Connection con = conn.connect();
        Object returnValue = new Object();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                returnValue = rs.getObject(1);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnValue;
    }
}
