package entity;

/**
 *
 * @author Vincenzo Giordano N86003039
 */
public class Sala {

    private int id;
    private String description;
    private String tecnologia;

    /**
     *
     */
    public Sala() {
        super();
    }

    /**
     *
     * @param id identificativo della sala
     * @param description descrizione della sala
     * @param tecnologia ci permette di capire di che tecnologie è composta, se imax o audio dolby
     */
    public Sala(int id, String description, String tecnologia) {
        this.id = id;
        this.description = description;
        this.tecnologia = tecnologia;
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
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getTecnologia() {
        return tecnologia;
    }

    /**
     *
     * @param tecnologia
     */
    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
}
