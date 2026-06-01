
/**
 * Clase abstracta base para todas las entidades del juego.
 * Define atributos y comportamientos comunes (posición, vida, tipo, etc.).
 * 
 * Responsabilidades:
 * - Representar una entidad en el mundo del juego
 * - Mantener estado de vida y posición
 * - Definir contrato para actualización periódica
 * 
 * @author Motor Pokémon
 * @version 1.0
 */
public abstract class EntidadVideojuego {
    /** Coordenada X de la entidad */
    private int x, y;
    /** Ancho y alto de la entidad */
    private final int w, h;
    /** Nombre identificador de la entidad */
    private final String name;
    /** Tipo/equipo de la entidad (ej: "Rojo", "Azul") */
    private final String type;
    /** Puntos de vida actuales */
    private int health;
    /** Ruta a la imagen/sprite de la entidad */
    private final String image;

    /**
     * Constructor de la entidad de videojuego.
     * @param name nombre identificador
     * @param type tipo/equipo
     * @param x posición X inicial
     * @param y posición Y inicial
     * @param w ancho
     * @param h alto
     * @param health vida inicial
     * @param image ruta a la imagen
     */
    public EntidadVideojuego(String name, String type, int x, int y, int w, int h, int health, String image) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.health = health;
        this.image = image;
    }

    /**
     * Actualiza el estado de la entidad en cada tick del juego.
     * Método abstracto que debe ser implementado por subclases.
     * @param motor referencia al motor de juego
     */
    public abstract void update(MotorJuego motor);

    /**
     * Obtiene el nombre de la entidad.
     * @return nombre
     */
    public String getName() { return name; }
    /** @return tipo/equipo de la entidad */
    public String getType() { return type; }
    /** @return coordenada X */
    public int getX() { return x; }
    /** @return coordenada Y */
    public int getY() { return y; }
    /** @param x nueva coordenada X */
    public void setX(int x) { this.x = x; }
    /** @param y nueva coordenada Y */
    public void setY(int y) { this.y = y; }
    /** @return ancho de la entidad */
    public int getW() { return w; }
    /** @return alto de la entidad */
    public int getH() { return h; }
    /** @return vida actual */
    public int getHealth() { return health; }
    /** @param health nueva vida */
    public void setHealth(int health) { this.health = health; }
    /** @return ruta de la imagen */
    public String getImage() { return image; }

    /**
     * Aplica daño a la entidad reduciendo su vida.
     * @param dmg cantidad de daño a aplicar
     */
    public void takeDamage(int dmg) {
        this.health -= dmg;
        System.out.println("[" + name + "] recibe " + dmg + " de daño. Vida=" + this.health);
    }

    /**
     * Verifica si la entidad está viva.
     * @return true si la vida es mayor a 0, false en caso contrario
     */
    public boolean isAlive() { return this.health > 0; }
}
