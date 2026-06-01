import java.util.Random;

/**
 * Clase Pokemon: representa un Pokémon en combate.
 * Hereda de EntidadVideojuego y define comportamiento de ataque.
 */
public class Pokemon extends EntidadVideojuego {
    private final int attack;

    public Pokemon(String name, String type, int x, int y, int w, int h, int health, int attack, String image) {
        super(name, type, x, y, w, h, health, image);
        this.attack = attack;
    }

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

    public int getAttack() { return attack; }
}
