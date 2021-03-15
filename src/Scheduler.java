import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.PriorityQueue;

public class Scheduler implements Runnable{

       public void run()
       {
            while(Simulation.time.get() <= Simulation.limit)
            try{
                {
                    if(endSimulation())//if all the clients have been processed
                    {
                        break;
                    }
                    else // we didn't reach the end of the simulation
                    {
                        System.out.println("Time: " + Simulation.time.get()+"\n");
                        try{
                            Simulation.fw.write("Time: " + Simulation.time.get()+"\n");
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    dispatchTask();
                    //print left clients not in queues
                    printClients();
                    //print the the queues
                    printQueues();
                    //replace dead queues
                    replace();
                    //start threads
                    startThreads();

                try{
                    Thread.sleep(1000);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                Simulation.time.addAndGet(1);

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
           try{
               System.out.println("//////////////");
               Simulation.fw.write("Simulation ended.\nAvg waiting time: "+Simulation.avgTime());
           }catch(Exception e)
           {
               e.printStackTrace();
           }
           try{
               Simulation.fw.close();
           }catch(Exception e)
           {
               e.printStackTrace();
           }

       }

        void dispatchTask()
        {
            if(Simulation.tasks.size() > 0)
            {
                for(Task task: Simulation.tasks)
                {
                    if(task.getArrivalTime() <= Simulation.time.get()) //client can be added to the queue
                    {
                        int min = min();
                        for(Server server: Simulation.servers)
                        {
                            if(server.getWaitingTime() == min)
                            {
                                server.addTask(task);
                                Simulation.tasks.remove(task);
                                break;
                            }
                        }
                    }
                    else break; //the simulation didn't reach the smallest arriving time yet;
                }
            }
        }

        public void replace()
        {
            for(int i = 0; i < Simulation.nbQueues; i++)
            {
               Thread thread;
               int c = 0;
                for(Thread t: Simulation.threads)
                {
                    if(t.getState().equals(Thread.State.TERMINATED))
                    {
                        Server s = Simulation.servers.get(c);
                        thread = new Thread(s, "Thread"+(c+1));
                        Simulation.threads.set(c,thread);
                    }
                    c++;
                }
            }
        }

        public int getID(String s)
        {
            int id = Integer.parseInt(s.substring(6,s.length()));
            return id;
        }
        public Server getServer(int id)
        {
            for(Server s: Simulation.servers)
            {
                if(s.getQueuenb() == id)
                    return s;
            }
            return null;
        }

        public void printClients()
        {
            try{
                if(Simulation.tasks.size() > 0)
                {
                    Simulation.fw.write("Waiting cilents: ");
                    System.out.println("Waiting clients");
                    for(Task t: Simulation.tasks)
                    {
                        System.out.print("("+t.getId()+","+t.getArrivalTime()+","+t.getProcessingPeriod()+")");
                        Simulation.fw.write("("+t.getId()+","+t.getArrivalTime()+","+t.getProcessingPeriod()+")");
                    }
                    System.out.println("");
                    Simulation.fw.write("\n");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void printQueues()
        {
            try{
                for(int i = 0; i < Simulation.nbQueues; i++)
                {
                    Server s = getServer(i+1);
                    if(s != null)
                    {
                        Simulation.fw.write("Queue "+(i+1)+":");
                        System.out.print("Queue "+(i+1)+":");
                        if(s.getNbclients() > 0)
                        {
                            for(Task t:s.queue )
                            {
                                System.out.print("("+t.getId()+","+t.getArrivalTime()+","+t.getProcessingPeriod()+") ");
                                Simulation.fw.write("("+t.getId()+","+t.getArrivalTime()+","+t.getProcessingPeriod()+") ");
                            }
                            System.out.println("");
                            Simulation.fw.write("\n");
                        }
                        else
                        {   System.out.println("closed");
                            Simulation.fw.write("closed");
                        Simulation.fw.write("\n");}
                    }
                }
                Simulation.fw.write("\n");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public int min()
        {
            int min=1000000;
            for(Server server: Simulation.servers)
            {
                if(server.getWaitingTime() < min)
                {
                    min = server.getWaitingTime();
                }
            }
            return min;
        }

        public boolean endSimulation()
        {
            if(Simulation.finishedTasks.size() >= Simulation.nbClients)
                return true;
            else return false;

        }

        public void startThreads()
        {
            for(Thread t: Simulation.threads)
            {
                if(t.getState().equals(Thread.State.NEW))
                {
                    int id = getID(t.getName());
                    if(getServer(id).queue.size() > 0)//the thread is not started but has clients
                    {
                        t.start();
                    }
                }
            }
        }






}
