/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package superclases;

/**
 *
 * @author Ing. Luis Navarro
 */
public class Combo {
    private String campo;
    private String tabla;
    private String id;
    private String etiqueta;

    public Combo(String campo, String tabla, String id, String etiqueta) {
        this.campo = campo;
        this.tabla = tabla;
        this.id = id;
        this.etiqueta = etiqueta;
    }

    /**
     * @return the campo
     */
    public String getCampo() {
        return campo;
    }

    /**
     * @param campo the campo to set
     */
    public void setCampo(String campo) {
        this.campo = campo;
    }

    /**
     * @return the tabla
     */
    public String getTabla() {
        return tabla;
    }

    /**
     * @param tabla the tabla to set
     */
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the etiqueta
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * @param etiqueta the etiqueta to set
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return campo;
    }       

}
