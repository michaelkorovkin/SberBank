package ru.sberbank.test.model.ints;

public interface ExecutionStatistics {
    int getMinExecutionTimeInMs();
    int getMaxExecutionTimeInMs();
    int getAverageExecutionTimeInMs();
}
