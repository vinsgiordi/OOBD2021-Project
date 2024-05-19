package daoimpl;

import connection.Connessione;
import dao.FilmDao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class FilmDaoImpl implements FilmDao {

    private final Connessione conn = new Connessione();
    private final RowMapperDao rmd = new RowMapperDao();

    @Override
    public void insertFilm(String titolo, String durata, String dataInizio, String dataFine) {
        String insertQuery = "INSERT INTO multisala.\"Film\"( \"Titolo\", \"Durata\", \"Data_Inizio\", \"Data_Fine\", \"Attivo\") "
                + "VALUES ('" + titolo + "', " + durata + ", '" + dataInizio + "','" + dataFine + "',true);";

        Connection con = conn.connect();

        try (Statement st = con.createStatement()) {
            st.execute(insertQuery);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteFilm(String id_film) {
        String deleteQuery = "UPDATE multisala.\"Film\" SET \"Attivo\" = false WHERE \"ID_Film\" = " + id_film;

        Connection con = conn.connect();

        try (Statement st = con.createStatement()) {
            st.execute(deleteQuery);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    /**
     * Metodo per la conversione di un ResulSet in un'ArrayList
     *
     * @param query La query sulla quale effettuare la ricerca
     * @return ResultSet convertito in ArrayList, ogni elemento della lista
     * contiene una mappa che replica la riga della tabella restituita dal DB
     * @throws SQLException
     */
    public ArrayList getListFilm() {
        String query = "SELECT * FROM multisala.\"Film\"";
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
     * Metodo per la creazione di un DefaultTableModel tramite il ResultSet
     * restituito dal DB
     *
     * @param query La query sulla quale eseguire la ricerca sul DB
     * @param image eventuale immagine da settare sulla colonna indicata dal
     * parametro columnImage
     * @param columnImage indice della colonna sulla quale inserire l'immagine
     * @return ResultSet convertito in DefaultTableModel
     * @throws SQLException
     */
    public DefaultTableModel getDtm(String query, ImageIcon image, Integer columnImage) {
        DefaultTableModel result = new DefaultTableModel();
        Connection con = conn.connect();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            result = rmd.buildRowMapper(rs, true, image, columnImage);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return result;
        }

    }
}
