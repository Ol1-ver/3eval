import java.util.*;

/**
 * Clase principal del motor de juego: gestiona el estado global, las entidades y la simulación.
 * 
 * Responsabilidades:
 * - Controlar el estado del juego (MENU, JUGANDO, PAUSA, GAMEOVER)
 * - Mantener la lista de entidades activas
 * - Coordinar la actualización periódica de entidades
 * - Gestionar el sistema de puntuación
 * - Buscar enemigos cercanos para ataques
 * 
 * @author Motor Pokémon
 * @version 1.0
 */
public class MotorJuego {
    /**
     * Enumeración de los posibles estados del juego.
     */
    public enum Estado { MENU, JUGANDO, PAUSA, GAMEOVER }

    /** Estado actual del motor de juego */
    private Estado estado = Estado.MENU;
    /** Lista de entidades activas en el juego */
    private final List<EntidadVideojuego> entidades = new ArrayList<>();
    /** Sistema de puntuación y logros */
    private final SistemaPuntuacion punt = new SistemaPuntuacion();

    /**
     * Obtiene el estado actual del motor.
     * @return estado actual
     */
    public Estado getEstado() { return estado; }
    
    /**
     * Establece el estado del motor y notifica el cambio.
     * @param e nuevo estado
     */
    public void setEstado(Estado e) { this.estado = e; System.out.println("Estado cambiado a: " + e); }

    /**
     * Cuenta el número de entidades vivas en el juego.
     * @return cantidad de entidades vivas
     */
    public int contarEntidadesVivas() {
        int c = 0;
        for (EntidadVideojuego e : entidades) if (e.isAlive()) c++;
        return c;
    }

    /**
     * Obtiene todas las entidades de un equipo específico.
     * @param team nombre del equipo (ej: "Rojo", "Azul")
     * @return lista de entidades del equipo
     */
    public List<EntidadVideojuego> getEntitiesByTeam(String team) {
        List<EntidadVideojuego> result = new ArrayList<>();
        for (EntidadVideojuego e : entidades) {
            if (team.equals(e.getType())) result.add(e);
        }
        return result;
    }

    /**
     * Añade una entidad al motor de juego.
     * @param e entidad a añadir
     */
    public void addEntity(EntidadVideojuego e) { entidades.add(e); System.out.println("Entidad añadida: " + e.getName()); }
    
    /**
     * Elimina una entidad del motor de juego.
     * @param e entidad a eliminar
     */
    public void removeEntity(EntidadVideojuego e) { entidades.remove(e); System.out.println("Entidad eliminada: " + e.getName()); }

    /**
     * Actualiza el estado del juego en cada tick.
     * Solo actualiza si el estado es JUGANDO.
     * Procesa eventos de muerte y registra logros.
     */
    public void actualizar() {
        if (estado != Estado.JUGANDO) {
            System.out.println("Motor en estado " + estado + ", no se actualiza.");
            return;
        }
    System.out.println("--- Ciclo de actualización: entidades=" + entidades.size() + " vivas=" + contarEntidadesVivas() + " ---");
        // Creamos copia para evitar ConcurrentModification
        List<EntidadVideojuego> snapshot = new ArrayList<>(entidades);
        for (EntidadVideojuego e : snapshot) {
            if (e.isAlive()) {
                e.update(this);
                // comprobación de logros por posición
                punt.checkPosition(e);
            } else {
                System.out.println(e.getName() + " sin vida, se elimina.");
                // registrar kill para el equipo contrario si corresponde
                if (e instanceof Pokemon p) {
                    // asumimos que el que murió aporta kills al otro equipo (simple)
                    String team = p.getType();
                    String other = "Rojo".equals(team) ? "Azul" : "Rojo";
                    punt.registerKill(other);
                }
                removeEntity(e);
            }
        }
    }

    /**
     * Genera un guardado rápido en formato JSON con el estado actual del juego.
     * @return cadena JSON con el estado del juego
     */
    public String quickSave() { return punt.quickSave(this); }

    /**
     * Obtiene el sistema de puntuación del motor.
     * @return sistema de puntuación
     */
    public SistemaPuntuacion getSistemaPuntuacion() { return punt; }

    /**
     * Busca el enemigo más cercano de un equipo rival.
     * @param source entidad fuente desde la que se busca
     * @return el Pokémon enemigo más cercano, o null si no hay
     */
    public Pokemon findNearestEnemy(EntidadVideojuego source) {
        if (!(source instanceof Pokemon)) return null;
        Pokemon psrc = (Pokemon) source;
        Pokemon nearest = null;
        double bestDist = Double.MAX_VALUE;
        for (EntidadVideojuego e : entidades) {
            if (e == source) continue;
            if (!(e instanceof Pokemon)) continue;
            Pokemon p = (Pokemon) e;
            if (p.getType().equals(psrc.getType())) continue; // mismo equipo
            double dx = p.getX() - source.getX();
            double dy = p.getY() - source.getY();
            double dist = Math.hypot(dx, dy);
            if (dist < bestDist) { bestDist = dist; nearest = p; }
        }
        return nearest;
    }

    /**
     * Obtiene una lista no modificable de todas las entidades.
     * @return lista de entidades inmutable
     */
    public List<EntidadVideojuego> getEntities() { return Collections.unmodifiableList(entidades); }
}
