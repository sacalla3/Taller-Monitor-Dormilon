import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class MonitorDormilon implements Runnable {
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

class Estudiante implements Runnable {
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

public class MonitorDormilonMain {
    public static void main(String[] args) {
        int numEstudiantes = 5; 
        int numSillas = 3;

        Semaphore estudiantesEsperando = new Semaphore(numSillas, true);
        Semaphore monitor = new Semaphore(0, true);

        Thread monitorThread = new Thread(new MonitorDormilon(estudiantesEsperando, monitor));
        monitorThread.start();

        for (int i = 1; i <= numEstudiantes; i++) {
            new Thread(new Estudiante(i, estudiantesEsperando, monitor)).start();
        }
    }
}
