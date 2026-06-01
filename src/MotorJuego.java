import java.util.*;

public class MotorJuego {
    public enum Estado { MENU, JUGANDO, PAUSA, GAMEOVER }

    private Estado estado = Estado.MENU;
    private final List<EntidadVideojuego> entidades = new ArrayList<>();
    private final SistemaPuntuacion punt = new SistemaPuntuacion();

    public Estado getEstado() { return estado; }
    public void setEstado(Estado e) { this.estado = e; System.out.println("Estado cambiado a: " + e); }

    // Helper no invasivo: muestra número de entidades vivas
    public int contarEntidadesVivas() {
        int c = 0;
        for (EntidadVideojuego e : entidades) if (e.isAlive()) c++;
        return c;
    }

    // Nuevo método helper: obtener entidades de un equipo específico
    public List<EntidadVideojuego> getEntitiesByTeam(String team) {
        List<EntidadVideojuego> result = new ArrayList<>();
        for (EntidadVideojuego e : entidades) {
            if (team.equals(e.getType())) result.add(e);
        }
        return result;
    }

    public void addEntity(EntidadVideojuego e) { entidades.add(e); System.out.println("Entidad añadida: " + e.getName()); }
    public void removeEntity(EntidadVideojuego e) { entidades.remove(e); System.out.println("Entidad eliminada: " + e.getName()); }

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

    public String quickSave() { return punt.quickSave(this); }

    public SistemaPuntuacion getSistemaPuntuacion() { return punt; }

    // utilidad: buscar enemigo más cercano de distinto equipo (se usa propiedad type)
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

    public List<EntidadVideojuego> getEntities() { return Collections.unmodifiableList(entidades); }
}
