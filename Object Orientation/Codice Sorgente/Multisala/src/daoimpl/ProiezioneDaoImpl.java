package daoimpl;

import connection.Connessione;
import dao.ProiezioneDao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class ProiezioneDaoImpl implements ProiezioneDao {

    private Connessione conn = new Connessione();
    private RowMapperDao rmd = new RowMapperDao();

    @Override
    public void insertProiezione(String dataOdierna, String ora, String film, String sala, String prezzo) {
        String insertQuery = "INSERT INTO multisala.\"Proiezioni\"( \"Ora_Inizio\", \"Cod_Film\", \"Cod_Sala\", \"Prezzo\", \"Proiezione\", \"Day\") "
                + "VALUES ('" + ora + "', " + film + ", " + sala + "," + prezzo + ",true, '" + dataOdierna + "');";

        Connection con = conn.connect();

        try (Statement st = con.createStatement()) {
            st.execute(insertQuery);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
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
