import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Estudiante implements Runnable {
    private Semaphore estudiantesEsperando;
    private Semaphore monitor;
    private int id;

    public Estudiante(int id, Semaphore estudiantesEsperando, Semaphore monitor) {
        this.id = id;
        this.estudiantesEsperando = estudiantesEsperando;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("El estudiante " + id + " está programando.");
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 5000));

                System.out.println("El estudiante " + id + " necesita ayuda y va a la oficina del monitor.");

                if (estudiantesEsperando.tryAcquire()) {
                    System.out.println("El estudiante " + id + " se sienta en el corredor y espera.");
                    monitor.release();
                    estudiantesEsperando.acquire();
                    System.out.println("El estudiante " + id + " está siendo ayudado por el monitor.");
                } else {
                    System.out.println("No hay sillas disponibles. El estudiante " + id + " regresa a programar.");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
