package entity;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class Posto {

    private int id;
    private int numero;
    private String lettera;
    private int idSala;
    private int occupato;

    /**
     *
     */
    public Posto() {
        super();
    }

    /**
     *
     * @param id identificativo del posto
     * @param numero numero del posto
     * @param lettera lettera assegnata al posto
     * @param idSala identificativo della sala
     * @param occupato identificativo che ci dice se la sala è occupata o meno
     */
    public Posto(int id, int numero, String lettera, int idSala, int occupato) {
        this.id = id;
        this.numero = numero;
        this.lettera = lettera;
        this.idSala = idSala;
        this.occupato = occupato;
    }

    /**
     *
     * @return
     */
    public int getOccupato() {
        return occupato;
    }

    /**
     *
     * @param occupato
     */
    public void setOccupato(int occupato) {
        this.occupato = occupato;
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
    public int getNumero() {
        return numero;
    }

    /**
     *
     * @param numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     *
     * @return
     */
    public String getLettera() {
        return lettera;
    }

    /**
     *
     * @param lettera
     */
    public void setLettera(String lettera) {
        this.lettera = lettera;
    }

    /**
     *
     * @return
     */
    public int getIdSala() {
        return idSala;
    }

    /**
     *
     * @param idSala
     */
    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

}
