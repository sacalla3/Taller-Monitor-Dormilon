import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class MonitorDormilon implements Runnable {
    private Semaphore estudiantesEsperando;
    private Semaphore monitor;
    private boolean ayudando = false;

    public MonitorDormilon(Semaphore estudiantesEsperando, Semaphore monitor) {
        this.estudiantesEsperando = estudiantesEsperando;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!ayudando) {
                    System.out.println("El monitor está durmiendo...");
                    monitor.acquire();
                }

                ayudando = true;
                System.out.println("El monitor está ayudando a un estudiante.");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));

                if (estudiantesEsperando.availablePermits() == 3) {
                    System.out.println("No hay más estudiantes esperando. El monitor vuelve a dormir.");
                    ayudando = false;
                } else {
                    estudiantesEsperando.release();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
