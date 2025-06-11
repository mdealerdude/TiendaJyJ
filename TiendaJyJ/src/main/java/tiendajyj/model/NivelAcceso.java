package tiendajyj.model;

/**
 * Modelo para representar los niveles de acceso
 * @author MINEDUCYT
 */
public class NivelAcceso {
    private int id_nivel_acceso;
    private String nombre_nivel_acceso;
    
    // Constructors
    public NivelAcceso() {
    }
    
    public NivelAcceso(int id_nivel_acceso, String nombre_nivel_acceso) {
        this.id_nivel_acceso = id_nivel_acceso;
        this.nombre_nivel_acceso = nombre_nivel_acceso;
    }
    
    // Getters and Setters
    public int getId_nivel_acceso() {
        return id_nivel_acceso;
    }
    
    public void setId_nivel_acceso(int id_nivel_acceso) {
        this.id_nivel_acceso = id_nivel_acceso;
    }
    
    public String getNombre_nivel_acceso() {
        return nombre_nivel_acceso;
    }
    
    public void setNombre_nivel_acceso(String nombre_nivel_acceso) {
        this.nombre_nivel_acceso = nombre_nivel_acceso;
    }
    
    @Override
    public String toString() {
        return "NivelAcceso{" +
                "id_nivel_acceso=" + id_nivel_acceso +
                ", nombre_nivel_acceso='" + nombre_nivel_acceso + '\'' +
                '}';
    }
}