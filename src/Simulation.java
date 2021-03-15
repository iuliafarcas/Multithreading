import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {

    public static int limit;
    public static int minprocessTime;
    public static int maxprocessTime;
    public static int minarriveTime;
    public static int maxarriveTime;
    public static int nbQueues;
    public static int nbClients;
    public static AtomicInteger time = new AtomicInteger(0);

    public static FileWriter fw;
    public static ArrayList<Server> servers;
    public static PriorityBlockingQueue<Task> tasks;
    public static ArrayList<Thread> threads;
    public static ArrayList<Task> finishedTasks;

    public Simulation(String input, String output) throws IOException {
        //readfile
        readInput(input);
        fw = new FileWriter(output);
        tasks = new PriorityBlockingQueue<Task>(nbClients);
        servers = new ArrayList<Server>(nbQueues);
        threads = new ArrayList<Thread>(nbQueues);
        finishedTasks = new ArrayList<Task>();

        //generate the clients randomly
        genRandomClients(nbClients);
        Scheduler scheduler = new Scheduler();
        Thread mainThread = new Thread(scheduler);

        for(int i = 0; i < nbQueues; i++)
        {
            Server s = new Server(i+1);//, timer);
            servers.add(s);
            Thread t = new Thread(s, "Thread" + (i+1));
            threads.add(t);
        }

        mainThread.start();

    }

    public void genRandomClients(int nbClients)
    {
        Random rand = new Random();
        int n = 0;
        for(int i = 0; i < nbClients; i++)
        {
            int arrTime = rand.nextInt((maxarriveTime - minarriveTime) + 1)+ minarriveTime;
            int processTime = rand.nextInt((maxprocessTime - minprocessTime) + 1)+ minprocessTime;
            n++;
            tasks.add(new Task(arrTime, processTime, n));
        }

    }

    public static double avgTime() {
        double sum = 0;
        for(Task t : finishedTasks)
        {
            sum = sum + t.getFinishTime();
        }
        return sum/finishedTasks.size();
    }

    public void readInput(String input)
    {
        try{
            File f = new File(input);
            Scanner scanner = new Scanner(f);
            String inputdata = scanner.nextLine();
            nbClients = Integer.parseInt(inputdata);
            inputdata = scanner.nextLine();
            nbQueues = Integer.parseInt(inputdata);
            inputdata = scanner.nextLine();
            limit = Integer.parseInt(inputdata);
            inputdata = scanner.nextLine();
            String[] s = inputdata.split("\\,");
            minarriveTime = Integer.parseInt(s[0]);
            maxarriveTime = Integer.parseInt(s[1]);
            inputdata = scanner.nextLine();
            s = inputdata.split("\\,");
            minprocessTime = Integer.parseInt(s[0]);
            maxprocessTime = Integer.parseInt(s[1]);
            scanner.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Simulation simulation = new Simulation(args[0],args[1]);

    }

}
