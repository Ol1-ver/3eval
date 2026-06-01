import java.util.Random;

/**
 * Clase Pokemon: representa un Pokémon en combate.
 * Hereda de EntidadVideojuego y define comportamiento de ataque y movimiento.
 * 
 * Responsabilidades:
 * - Atacar al enemigo más cercano cuando está disponible
 * - Moverse aleatoriamente cuando no hay enemigos cercanos
 * - Gestionar su poder de ataque
 * 
 * @author Motor Pokémon
 * @version 1.0
 */
public class Pokemon extends EntidadVideojuego {
    /** Poder de ataque del Pokémon */
    private final int attack;

    /**
     * Constructor del Pokémon.
     * @param name nombre del Pokémon
     * @param type equipo ("Rojo" o "Azul")
     * @param x posición X inicial
     * @param y posición Y inicial
     * @param w ancho
     * @param h alto
     * @param health vida inicial
     * @param attack poder de ataque
     * @param image ruta a la imagen del Pokémon
     */
    public Pokemon(String name, String type, int x, int y, int w, int h, int health, int attack, String image) {
        super(name, type, x, y, w, h, health, image);
        this.attack = attack;
    }

    /**
     * Actualiza el comportamiento del Pokémon en cada tick.
     * Busca enemigos cercanos para atacar o se mueve aleatoriamente.
     * @param motor referencia al motor de juego
     */
    @Override
    public void update(MotorJuego motor) {
        // comportamiento sencillo: si hay un objetivo enemigo cercano, atacar
        Pokemon target = motor.findNearestEnemy(this);
        if (target != null) {
            System.out.println(getName() + " ataca a " + target.getName());
            target.takeDamage(this.attack);
            if (!target.isAlive()) {
                System.out.println(target.getName() + " ha sido derrotado y se elimina.");
                motor.removeEntity(target);
            }
        } else {
            // desplazamiento aleatorio para simular movimiento
            Random r = new Random();
            int dx = r.nextInt(3) - 1; // -1,0,1
            int dy = r.nextInt(3) - 1;
            setX(getX() + dx);
            setY(getY() + dy);
            System.out.println(getName() + " se mueve a (" + getX() + "," + getY() + ")");
        }
    }

    /**
     * Obtiene el poder de ataque del Pokémon.
     * @return daño que inflige este Pokémon
     */
    public int getAttack() { return attack; }
}
