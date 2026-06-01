import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase principal de la aplicación: punto de entrada para la simulación del motor de juego Pokémon.
 * 
 * Responsabilidades:
 * - Inicializar el motor de juego y el gestor de entradas
 * - Crear 4 Pokémons (2 por equipo)
 * - Ejecutar el bucle de simulación con ticks periódicos
 * - Monitorear condiciones de fin de partida
 * 
 * @author Motor Pokémon
 * @version 1.0
 */
public class Main {
    /**
     * Método principal: inicia la simulación del motor de juego.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        MotorJuego motor = new MotorJuego();
        GestorEntradas gi = new GestorEntradas(motor);

        // Crear 4 pokemons: 2 en equipo Rojo, 2 en equipo Azul
        Pokemon r1 = new Pokemon("Charmy", "Rojo", 0, 0, 1,1, 30, 8, "charmy.png");
        Pokemon r2 = new Pokemon("Bulby", "Rojo", 1, 0, 1,1, 28, 7, "bulby.png");
        Pokemon b1 = new Pokemon("Splash", "Azul", 5, 5, 1,1, 26, 6, "splash.png");
        Pokemon b2 = new Pokemon("Leafy", "Azul", 6, 5, 1,1, 24, 5, "leafy.png");

        motor.addEntity(r1);
        motor.addEntity(r2);
        motor.addEntity(b1);
        motor.addEntity(b2);

        // iniciar partida
        gi.comando("INICIAR");

        // bucle simulado: usamos un scheduler para evitar bloqueos con Thread.sleep
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger tick = new AtomicInteger(0);

        Runnable task = () -> {
            int t = tick.getAndIncrement();
            System.out.println("\nTick " + t);
            motor.actualizar();

            // simular que el jugador pulsa acción con r1 cada 3 ticks
            if (t % 3 == 0 && motor.getEntities().contains(r1)) {
                gi.pulsarAccionSobre(r1);
            }

            // en el tick 2 hacemos un quicksave para demostrar el guardado
            if (t == 2) gi.comando("QUICKSAVE");

            // comprobar condición de victoria simple
            boolean anyRojo = motor.getEntities().stream().anyMatch(e -> e.getType().equals("Rojo"));
            boolean anyAzul = motor.getEntities().stream().anyMatch(e -> e.getType().equals("Azul"));
            if (!anyRojo || !anyAzul || t >= 9) {
                if (!anyRojo || !anyAzul) System.out.println("Un equipo ha sido eliminado. Forzando GAME OVER.");
                gi.comando("GAMEOVER");
                scheduler.shutdownNow();
                System.out.println("Simulación finalizada. Estado final: " + motor.getEstado());
            }
        };

        // ejecutar cada 300 ms
        scheduler.scheduleAtFixedRate(task, 0, 300, TimeUnit.MILLISECONDS);
    }
}
