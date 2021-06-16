package ru.sberbank.test.model;

public class TaskInfo {
    private Runnable task;
    private long startTime = -1;
    private long finishTime = -1;
    private boolean exceptionOnRun = false;

    public boolean isExceptionOnRun() {
        return exceptionOnRun;
    }

    public void setExceptionOnRun(boolean exceptionOnRun) {
        this.exceptionOnRun = exceptionOnRun;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }
}
