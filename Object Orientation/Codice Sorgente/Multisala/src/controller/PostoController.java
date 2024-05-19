package controller;

import daoimpl.PostoDaoImpl;
import entity.Posto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class PostoController {

    private final PostoDaoImpl postoDao = new PostoDaoImpl();

    /**
     * Metodo per visualizzare posti disponibili
     *
     * @param idProiezione identificativo proiezione
     * @param dataOdierna identificativo per la data odierna
     * @return
     * @throws SQLException
     */
    public Vector<Posto> getPostiDisponibili(String idProiezione, String dataOdierna) throws SQLException {
        Vector<Posto> returnList = new Vector<>();
        ArrayList list = postoDao.getListPosti(idProiezione, dataOdierna);

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            HashMap map = (HashMap) iter.next();
            Posto i = new Posto((Integer) map.get("id_posti"), (Integer) map.get("numero"), map.get("lettera").toString(), (Integer) map.get("cod_sala"), (Integer) map.get("occupato"));
            returnList.add(i);
        }

        return returnList;
    }
}
