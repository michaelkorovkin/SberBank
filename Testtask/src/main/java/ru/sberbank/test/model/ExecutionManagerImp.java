package ru.sberbank.test.model;

import ru.sberbank.test.model.ints.Context;
import ru.sberbank.test.model.ints.ExecutionManager;
import ru.sberbank.test.model.ints.ExecutionStatistics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionManagerImp implements ExecutionManager {
    private List<Runnable> tasksList;
    private List<TaskInfo> tasksInfo;
    private ru.sberbank.test.model.ints.Context context;
    public ExecutionManagerImp () {
        context = new ContextImp();
    }
    @Override
    /**
     * Метод execute принимает массив тасков, это задания которые ExecutionManager
     *  должен выполнять параллельно.
     * Метод execute – это неблокирующий метод, который сразу возвращает объект
     *  Contex
     */
    public Context execute(Runnable... tasks) {
        tasksList = Arrays.asList(tasks);
        tasksInfo = new ArrayList<TaskInfo>();
        Context context = new ContextImp();
        tasksList.stream().forEach(
                                           t -> {
                                               TaskInfo taskInfo = new TaskInfo();
                                               try {
                                                   taskInfo.setTask(t);
                                                   taskInfo.setStartTime(System.currentTimeMillis());
                                                   t.run();
                                                   taskInfo.setFinishTime(-1);
                                                   tasksInfo.add(taskInfo);
                                                   taskInfo.setExceptionOnRun(false);
                                               } catch (Exception e) {
                                                   taskInfo.setExceptionOnRun(true);
                                                   System.out.println("Error while start task: "+t.toString());;
                                               }
                                           }
                                   );

        context = computeContex(tasksInfo);
        return context;
    }

    public void interrupt (List<TaskInfo>tasks) {
        tasks.stream().filter(t -> ((Thread) t.getTask()).getState() == Thread.State.NEW).forEach(t -> ((Thread) t.getTask()).interrupt());
    }

    private boolean isFinished(List<TaskInfo> tasksInfo) {
        boolean ret = false;
        long terminatedTaskCount = tasksInfo.stream().filter(t -> ((Thread)t.getTask()).getState() == Thread.State.TERMINATED).count();
        return (terminatedTaskCount == tasksInfo.size());

    }
    private void onFinish(List<TaskInfo> tasksInfo, Runnable callback) {
        long terminatedTaskCount = tasksInfo.stream().filter(t -> ((Thread)t.getTask()).getState() != Thread.State.TERMINATED).count();
        if (terminatedTaskCount == tasksInfo.size()) {
            callback.run();
        }
    }
    /**
     * Метод вычисляет контекс (Context)
     * @param tasksInfo
     */
    private Context computeContex (List<TaskInfo> tasksInfo) {
        Context context = new ContextImp();
        ((ContextImp) context).setTasks(tasksInfo);
        /**
         * Выставляем время выполнения потока, если он закончил выполняться без исключений
         */
        tasksInfo.stream().filter(t -> t.isExceptionOnRun() == false).filter(t -> t.getFinishTime() == -1).filter(t -> ((Thread)t.getTask()).getState() == Thread.State.TERMINATED).
                forEach(t -> t.setFinishTime(System.currentTimeMillis()));

        context.setCompletedTaskCount((int) tasksInfo.stream().filter(t -> ((Thread)t.getTask()).getState() == Thread.State.TERMINATED).count());
        context.setFailedTaskCount((int) tasksInfo.stream().filter(t -> t.isExceptionOnRun() == true).count());
        context.setInterruptedTaskCount((int) tasksInfo.stream().filter(t -> ((Thread)t.getTask()).getState() != Thread.State.NEW).
                filter(t -> ((Thread)t.getTask()).getState() != Thread.State.TERMINATED).
                filter(t -> ((Thread)t.getTask()).getState() != Thread.State.RUNNABLE).count());

        context.setExecutionStatistics(computeExecutionStatistic(tasksInfo));
        return context;
    }

    /**
     * Метод вычисляет статистику выполнения
     * @param tasksInfo
     * @return
     */
    private ExecutionStatistics computeExecutionStatistic (List<TaskInfo> tasksInfo) {
        ExecutionStatistics executionStatistics = new ExecutionStatisticsImp();
        List <TaskInfo> noExceptionTasks = new ArrayList<>();
        noExceptionTasks = tasksInfo.stream().filter(t -> t.isExceptionOnRun() == false).filter(t -> (((Thread)t.getTask()).getState() == Thread.State.TERMINATED)).
                collect(Collectors.toList());
        Long taskCount = noExceptionTasks.stream().count();
        Long timeSumm =  noExceptionTasks.stream().
                map(t -> t.getFinishTime()-t.getStartTime()).reduce((x,y) -> x+y).get();
        try {
            ((ExecutionStatisticsImp) executionStatistics).setAverageExecutionTimeInMs((int) (timeSumm/taskCount));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((ExecutionStatisticsImp) executionStatistics).setMaxExecutionTimeInMs(Math.toIntExact(noExceptionTasks.stream().
                map(t -> t.getFinishTime() - t.getStartTime()).max(Long::compare).get()));
        ((ExecutionStatisticsImp) executionStatistics).setMinExecutionTimeInMs(Math.toIntExact(noExceptionTasks.stream().
                map(t -> t.getFinishTime() - t.getStartTime()).min(Long::compare).get()));

        return executionStatistics;
    }

}
