package ru.sberbank.test.model.ints;

import ru.sberbank.test.model.TaskInfo;
import java.util.List;

public interface Context {
    void setCompletedTaskCount(int completedTaskCount);
    void setFailedTaskCount(int failedTaskCount);
    void setInterruptedTaskCount(int interruptedTaskCount);
    void setExecutionStatistics(ExecutionStatistics executionStatistics);
    int getCompletedTaskCount();
    int getFailedTaskCount();
    int getInterruptedTaskCount();
    void interrupt();
    boolean isFinished();
    void onFinish(Runnable callback);
    ExecutionStatistics getStatistics();

    void awaitTermination();
}
