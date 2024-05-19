package daoimpl;

import connection.Connessione;
import dao.BigliettoDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import rowMapper.RowMapperDao;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class BigliettoDaoImpl implements BigliettoDao{

    private final Connessione conn = new Connessione();
    private final RowMapperDao rmd = new RowMapperDao();
    
    @Override
    public void insertBiglietto(String idPosto, String id_proiezione) {
        String insertQuery = "INSERT INTO multisala.\"Biglietti\" (\"Cod_Posto\",\"Cod_Proiezione\") VALUES (" + idPosto + "," + id_proiezione + ")";

        Connection con = conn.connect();

        try (Statement st = con.createStatement()) {
            st.execute(insertQuery);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
