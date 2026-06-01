import java.util.*;

/**
 * Sistema de puntuación y logros del juego.
 * 
 * Responsabilidades:
 * - Registrar kills de cada equipo
 * - Gestionar puntuación y logros desbloqueados
 * - Verificar condiciones de logros
 * - Generar guardados rápidos en formato JSON
 * - Mantener historial de eventos
 * 
 * @author Motor Pokémon
 * @version 1.0
 */
public class SistemaPuntuacion {
    /** Mapa de kills por equipo */
    private final Map<String,Integer> killsByTeam = new HashMap<>();
    /** Puntuación acumulada */
    private int score = 0;
    /** Conjunto de logros desbloqueados */
    private final Set<String> unlocked = new HashSet<>();
    /** Historial de eventos */
    private final List<String> log = new ArrayList<>();

    /**
     * Constructor del sistema de puntuación.
     * Inicializa los equipos conocidos (Rojo y Azul).
     */
    public SistemaPuntuacion() {
        // inicializar equipos conocidos
        killsByTeam.put("Rojo", 0);
        killsByTeam.put("Azul", 0);
    }

    /**
     * Registra una kill para un equipo específico.
     * Aumenta la puntuación y verifica logros.
     * @param team equipo que obtuvo la kill
     */
    public void registerKill(String team) {
        killsByTeam.putIfAbsent(team, 0);
        killsByTeam.put(team, killsByTeam.get(team) + 1);
        score += 100;
        log.add("Kill registered for team=" + team);
        checkAchievements();
    }

    /**
     * Obtiene la puntuación actual.
     * @return puntuación total
     */
    public int getScore() {
        return score;
    }

    /**
     * Obtiene el número de kills de un equipo específico.
     * @param team nombre del equipo
     * @return cantidad de kills del equipo
     */
    public int getKills(String team) {
        return killsByTeam.getOrDefault(team, 0);
    }

    /**
     * Verifica logros basados en la posición de una entidad.
     * @param e entidad a verificar
     */
    public void checkPosition(EntidadVideojuego e) {
        // ejemplo: logro por llegar a x >= 10
        if (e.getX() >= 10) unlock("CU-COORD-10 Reached X=10 by " + e.getName());
    }

    /**
     * Desbloquea un logro si no ha sido desbloqueado previamente.
     * @param name nombre del logro
     */
    private void unlock(String name) {
        if (unlocked.add(name)) {
            System.out.println("[Achievement unlocked] " + name);
            log.add("Achievement: " + name);
        }
    }

    /**
     * Verifica y desbloquea logros basados en el estado actual del juego.
     */
    private void checkAchievements() {
        // ejemplo simple: si un equipo tiene >=2 kills
        for (Map.Entry<String,Integer> en : killsByTeam.entrySet()) {
            if (en.getValue() >= 2) {
                unlock("Eliminator: " + en.getKey() + " reached 2 kills");
            }
        }
        // otro: first kill
        int totalKills = killsByTeam.values().stream().mapToInt(Integer::intValue).sum();
        if (totalKills >= 1) unlock("First Blood");
    }

    /**
     * Genera un guardado rápido del estado del juego en formato JSON.
     * Incluye estado, puntuación, kills y entidades.
     * @param motor referencia al motor de juego
     * @return cadena JSON con el estado del juego
     */
    public String quickSave(MotorJuego motor) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"estado\": \"").append(motor.getEstado()).append("\",\n");
        sb.append("  \"score\": ").append(score).append(",\n");
        sb.append("  \"kills\": {\n");
        int i=0;
        for (Map.Entry<String,Integer> en : killsByTeam.entrySet()) {
            sb.append("    \"").append(en.getKey()).append("\": ").append(en.getValue());
            if (++i < killsByTeam.size()) sb.append(",\n"); else sb.append("\n");
        }
        sb.append("  },\n");
        sb.append("  \"entities\": [\n");
        int j=0;
        for (EntidadVideojuego e : motor.getEntities()) {
            sb.append("    {\"name\":\"").append(e.getName()).append("\", \"type\":\"")
              .append(e.getType()).append("\", \"x\":").append(e.getX()).append(", \"y\":").append(e.getY())
              .append(", \"health\":").append(e.getHealth()).append("}");
            if (++j < motor.getEntities().size()) sb.append(",\n"); else sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Obtiene el historial de eventos del juego.
     * @return lista inmutable de eventos
     */
    public List<String> getLog() { return Collections.unmodifiableList(log); }
}
