import java.util.*;

public class SistemaPuntuacion {
    private final Map<String,Integer> killsByTeam = new HashMap<>();
    private int score = 0;
    private final Set<String> unlocked = new HashSet<>();
    private final List<String> log = new ArrayList<>();

    public SistemaPuntuacion() {
        // inicializar equipos conocidos
        killsByTeam.put("Rojo", 0);
        killsByTeam.put("Azul", 0);
    }

    public void registerKill(String team) {
        killsByTeam.putIfAbsent(team, 0);
        killsByTeam.put(team, killsByTeam.get(team) + 1);
        score += 100;
        log.add("Kill registered for team=" + team);
        checkAchievements();
    }

    // Nuevo método: obtener puntuación actual
    public int getScore() {
        return score;
    }

    // Nuevo método: obtener kills de un equipo específico
    public int getKills(String team) {
        return killsByTeam.getOrDefault(team, 0);
    }

    public void checkPosition(EntidadVideojuego e) {
        // ejemplo: logro por llegar a x >= 10
        if (e.getX() >= 10) unlock("CU-COORD-10 Reached X=10 by " + e.getName());
    }

    private void unlock(String name) {
        if (unlocked.add(name)) {
            System.out.println("[Achievement unlocked] " + name);
            log.add("Achievement: " + name);
        }
    }

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

    public List<String> getLog() { return Collections.unmodifiableList(log); }
}
