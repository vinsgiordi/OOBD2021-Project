package controller;

import daoimpl.SalaDaoImpl;
import entity.Option;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class SalaController {

    private final SalaDaoImpl salaDao = new SalaDaoImpl();

    /**
     *
     * @return @throws SQLException
     */
    public Vector<Option> getListaSale() throws SQLException {
        Vector<Option> returnList = new Vector<>();

        ArrayList list = salaDao.getListSale();

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            HashMap map = (HashMap) iter.next();
            Option i = new Option((Integer) map.get("ID_Sala"), map.get("Nome").toString());
            returnList.add(i);
        }

        return returnList;
    }

    /**
     *
     * @param id_proiezione
     * @return
     * @throws SQLException
     */
    public int getSalaProiezione(String id_proiezione) throws SQLException {
        int idSala = (int) salaDao.getSala(id_proiezione);

        return idSala;
    }
}
