/**
 * Gestor de entradas del usuario: procesa comandos y acciones de juego.
 * 
 * Responsabilidades:
 * - Procesar comandos de estado (INICIAR, PAUSA, RESUMIR, GAMEOVER, QUICKSAVE)
 * - Simular pulsaciones de acción y gestos táctiles
 * - Registrar acciones realizadas
 * - Validar que el Pokémon puede actuar
 * 
 * @author Motor Pokémon
 * @version 1.0
 */
public class GestorEntradas {
    /** Referencia al motor de juego */
    private final MotorJuego motor;

    /**
     * Constructor del gestor de entradas.
     * @param motor referencia al motor de juego
     */
    public GestorEntradas(MotorJuego motor) { this.motor = motor; }

    /**
     * Procesa un comando de entrada.
     * Comandos soportados: INICIAR, PAUSA, RESUMIR, QUICKSAVE, GAMEOVER
     * @param cmd comando a procesar
     */
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

    /**
     * Simula un gesto de deslizamiento (swipe) del usuario.
     * @param direccion dirección del gesto (ej: "arriba", "abajo", "izquierda", "derecha")
     */
    public void gestoSwipe(String direccion) {
        System.out.println("[Input] gesto swipe: " + direccion);
        // podría mapearse a desplazamientos de entidades en futuras mejoras
    }

    /**
     * Simula una pulsación de acción sobre un Pokémon (ordenar ataque).
     * El Pokémon atacará al enemigo más cercano si existe.
     * @param p Pokémon objetivo
     */
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

    /**
     * Verifica si un Pokémon puede actuar (está vivo y el juego está en marcha).
     * @param p Pokémon a verificar
     * @return true si el Pokémon puede actuar, false en caso contrario
     */
    public boolean puedeActuar(Pokemon p) {
        return p != null && p.isAlive() && motor.getEstado() == MotorJuego.Estado.JUGANDO;
    }

    /** Última acción registrada por el usuario */
    private String ultimaAccion = "";
    
    /**
     * Obtiene la última acción registrada.
     * @return descripción de la última acción
     */
    public String getUltimaAccion() {
        return ultimaAccion;
    }
    
    /**
     * Registra una acción realizada por el usuario.
     * @param accion descripción de la acción
     */
    public void registrarAccion(String accion) {
        this.ultimaAccion = accion;
        System.out.println("[Acción registrada] " + accion);
    }
}
