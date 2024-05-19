package entity;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class Proiezione {

    private int id;
    private String oraInizio;
    private int idFilm;
    private int idSala;
    private float prezzo;
    private boolean proiezione;
    private int bestOrario;

    /**
     *
     */
    public Proiezione() {
        super();
    }

    /**
     *
     * @param id identificativo della proiezione
     * @param oraInizio ora di inizio della proiezione
     * @param idFilm identificativo del film
     * @param idSala identificativo della sala
     * @param prezzo prezzo della proiezione
     * @param proiezione nominativo della proiezione
     * @param bestOrario orario di maggior affluenza
     */
    public Proiezione(int id, String oraInizio, int idFilm, int idSala, float prezzo, boolean proiezione, int bestOrario) {
        this.id = id;
        this.oraInizio = oraInizio;
        this.idFilm = idFilm;
        this.idSala = idSala;
        this.prezzo = prezzo;
        this.proiezione = proiezione;
        this.bestOrario = bestOrario;
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
    public String getOraInizio() {
        return oraInizio;
    }

    /**
     *
     * @param oraInizio
     */
    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    /**
     *
     * @return
     */
    public int getIdFilm() {
        return idFilm;
    }

    /**
     *
     * @param idFilm
     */
    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
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

    /**
     *
     * @return
     */
    public float getPrezzo() {
        return prezzo;
    }

    /**
     *
     * @param prezzo
     */
    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    /**
     *
     * @return
     */
    public boolean isProiezione() {
        return proiezione;
    }

    /**
     *
     * @param proiezione
     */
    public void setProiezione(boolean proiezione) {
        this.proiezione = proiezione;
    }


}
