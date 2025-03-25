import java.util.concurrent.Semaphore;

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
