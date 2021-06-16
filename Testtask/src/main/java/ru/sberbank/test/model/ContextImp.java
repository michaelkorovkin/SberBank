package ru.sberbank.test.model;

import ru.sberbank.test.model.ints.Context;
import ru.sberbank.test.model.ints.ExecutionStatistics;
import java.util.List;

public class ContextImp implements Context {
    private int completedTaskCount = 0;
    private int failedTaskCount = 0;
    private int interruptedTaskCount = 0;
    private ExecutionStatistics executionStatistics = new ExecutionStatisticsImp();
    List<TaskInfo> tasks;

    public List<TaskInfo> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskInfo> tasks) {
        this.tasks = tasks;
    }

    public void setCompletedTaskCount(int completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public void setFailedTaskCount(int failedTaskCount) {
        this.failedTaskCount = failedTaskCount;
    }

    public void setInterruptedTaskCount(int interruptedTaskCount) {
        this.interruptedTaskCount = interruptedTaskCount;
    }

    public void setExecutionStatistics(ExecutionStatistics executionStatistics) {
        this.executionStatistics = executionStatistics;
    }

    @Override
    /**
     *  возвращает количество тасков, которые на
     *  текущий момент успешно выполнились.
     */
    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    /**
     * Метод возвращает количество тасков, при выполнении
     *  которых произошел Exception
     * @return
     */
    @Override
    public int getFailedTaskCount() {
        return failedTaskCount;
    }

    @Override
    /**
     * Метод возвращает количество тасков, которые не
     *  были выполнены из-за отмены (вызовом предыдущего метода).
     */
    public int getInterruptedTaskCount() {
        return interruptedTaskCount;
    }

    @Override
    /**
     * Метод отменяет выполнения тасков, которые еще не начали выполняться.
     * !!!Что он тут делает?
     */
    public void interrupt() {
        tasks.stream().filter(t -> ((Thread)t.getTask()).getState() != Thread.State.RUNNABLE).forEach(t -> ((Thread) t.getTask()).interrupt());
    }

    @Override
    /**
     * Метод вернет true, если все таски были выполнены или отменены,
     *  false в противном случае.
     */
    public boolean isFinished() {
        long terminatedTask = tasks.stream().filter(t -> ((Thread)t.getTask()).getState() != Thread.State.TERMINATED).count();
        long interruptTask = tasks.stream().filter(t -> ((Thread)t.getTask()).isAlive()).count();
        return ((tasks.size() - terminatedTask - interruptTask)==0);
    }

    @Override
    /**
     * После завершения всех тасков должен выполниться
     *  callback (ровно 1 раз).
     */
    public void onFinish(Runnable callback) {
        long countTask = 0;
        long executedTasks = 0;
        countTask = tasks.stream().filter(t -> t.isExceptionOnRun() == false).count();
        executedTasks = tasks.stream().filter(t -> t.isExceptionOnRun() == false).filter(t -> ((Thread)t.getTask()).getState() == Thread.State.TERMINATED).count();
        if (countTask == executedTasks) {
            callback.run();
        }

    }

    @Override
    /**
     * Метод возвращает статистиску по времени выполнения задач.
     */
    public ExecutionStatistics getStatistics() {
        return executionStatistics;
    }

    /**
     *  Блокирует текущий поток, из которого произошел вызов,
     *  до тех пор пока не выполнятся все задачи.
     */
    @Override
    public void awaitTermination() {
        try {
            Thread.currentThread().wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
