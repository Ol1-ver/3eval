public class GestorEntradas {
    private final MotorJuego motor;

    public GestorEntradas(MotorJuego motor) { this.motor = motor; }

    public void comando(String cmd) {
        System.out.println("[Input] " + cmd);
        switch (cmd) {
            case "INICIAR" -> motor.setEstado(MotorJuego.Estado.JUGANDO);
            case "PAUSA" -> motor.setEstado(MotorJuego.Estado.PAUSA);
            case "RESUMIR" -> motor.setEstado(MotorJuego.Estado.JUGANDO);
            case "QUICKSAVE" -> {
                String save = motor.quickSave();
                System.out.println("[QuickSave]\n" + save);
            }
            case "GAMEOVER" -> motor.setEstado(MotorJuego.Estado.GAMEOVER);
            default -> System.out.println("Comando desconocido: " + cmd);
        }
    }

    // Helper para simular control táctil avanzado (no usado por defecto)
    public void gestoSwipe(String direccion) {
        System.out.println("[Input] gesto swipe: " + direccion);
        // podría mapearse a desplazamientos de entidades en futuras mejoras
    }

    // simula pulsación acción para un pokemon concreto (ataque dirigido a nearest)
    public void pulsarAccionSobre(Pokemon p) {
        Pokemon target = motor.findNearestEnemy(p);
        if (target != null) {
            System.out.println("Jugador ordena a " + p.getName() + " atacar a " + target.getName());
            target.takeDamage(p.getAttack());
            if (!target.isAlive()) motor.removeEntity(target);
        } else {
            System.out.println(p.getName() + " no tiene enemigos cercanos.");
        }
    }

    // Pequeña mejora: método seguro para evitar NullPointer
    public boolean puedeActuar(Pokemon p) {
        return p != null && p.isAlive() && motor.getEstado() == MotorJuego.Estado.JUGANDO;
    }

    // Nuevo: registrar última acción realizada
    private String ultimaAccion = "";
    
    public String getUltimaAccion() {
        return ultimaAccion;
    }
    
    public void registrarAccion(String accion) {
        this.ultimaAccion = accion;
        System.out.println("[Acción registrada] " + accion);
    }
