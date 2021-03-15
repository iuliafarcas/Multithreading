public class Task implements Comparable <Task> {

    private int arrivalTime;
    private int finishTime;
    private int processingPeriod;
    private int id;

    public Task()
    {
        this.arrivalTime = 0;
        this.processingPeriod = 0;
    }

    public Task(int arrivalTime, int processingPeriod, int id)
    {
        this.arrivalTime = arrivalTime;
        this.processingPeriod = processingPeriod;
        this.finishTime = this.arrivalTime + this.processingPeriod;
        this.id = id;
    }

    public int getArrivalTime()
    {
        return this.arrivalTime;
    }

    public int getFinishTime()
    {
        return this.finishTime;
    }

    public int getProcessingPeriod()
    {
        return this.processingPeriod;
    }
    public int getId()
    {
        return id;
    }

    public void setProcessingPeriod(int period)
    {
        this.processingPeriod = period;
    }

    public void  setFinishTime(int time)
    {
        this.finishTime = time;
    }

    public String toString()
    {
        return " (" + id + "," + arrivalTime + "," + processingPeriod + ") ";
    }

    public  int compareTo(Task task)
    {
        if(this.getArrivalTime() < task.getArrivalTime())
            return -1;
        else if(this.getArrivalTime() == task.getArrivalTime())
            return 0;
        else return 1;
    }
}
