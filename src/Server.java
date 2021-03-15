import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private int waitingTime;
    public BlockingQueue<Task> queue;
    private int queuenb;
    private int nbclients;

    public Server(int queuenb)
    {
        this.queue = new LinkedBlockingQueue<Task>();
        this.waitingTime = 0;
        this.queuenb = queuenb;
        this.nbclients = 0;

    }

    public void run()
    {
        while(queue.size() > 0)
        {
            Task firstClient = queue.peek();
            int processTime = firstClient.getProcessingPeriod();
            firstClient.setProcessingPeriod((processTime-1));

            if(firstClient.getProcessingPeriod() <= 0)
            {
                waitingTime = waitingTime - processTime;
                Task t = queue.remove();
                nbclients--;
                Simulation.finishedTasks.add(t);
            }

            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    public void addTask(Task task)
    {
        waitingTime = waitingTime + task.getProcessingPeriod();
        task.setFinishTime(waitingTime);
        queue.add(task);
        nbclients = queue.size();
    }


    public int getQueuenb()
    {
        return queuenb;
    }

    public int getWaitingTime()
    {
        return this.waitingTime;
    }

    public int getNbclients()
    {
        return nbclients;
    }


    public String toString()
    {
        return queue + ";";
    }


}
