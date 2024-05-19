package entity;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class Biglietto {

    private int id;
    private int idPosto;
    private int idProiezione;

    /**
     *
     */
    public Biglietto() {
        super();
    }

    /**
     *
     * @param id identificativo del biglietto
     * @param idPosto identificativo del posto
     * @param idProiezione identificativo della proiezione
     */
    public Biglietto(int id, int idPosto, int idProiezione) {
        this.id = id;
        this.idPosto = idPosto;
        this.idProiezione = idProiezione;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getIdPosto() {
        return idPosto;
    }

    /**
     *
     * @param idPosto
     */
    public void setIdPosto(int idPosto) {
        this.idPosto = idPosto;
    }

    /**
     *
     * @return
     */
    public int getIdProiezione() {
        return idProiezione;
    }

    /**
     *
     * @param idProiezione
     */
    public void setIdProiezione(int idProiezione) {
        this.idProiezione = idProiezione;
    }

}
