package ru.sberbank.test.model;

import ru.sberbank.test.model.ints.ExecutionStatistics;

public class ExecutionStatisticsImp implements ExecutionStatistics {

    /**
     * минимальное время выполнения среди тасков
     * в миллисекундах
     */
    int minExecutionTimeInMs = 0;
    /**
     максимальное время выполнения среди тасков
     в миллисекундах
     */
    int maxExecutionTimeInMs = 0;
    /**
     * среднее арифметическое время выполнения
     * тасков в миллисекундах.
     */
    int averageExecutionTimeInMs = 0;

    public int getMinExecutionTimeInMs() {
        return minExecutionTimeInMs;
    }

    @Override
    public int getMaxExecutionTimeInMs() {
        return maxExecutionTimeInMs;
    }

    @Override
    public int getAverageExecutionTimeInMs() {
        return 0;
    }

    public void setMinExecutionTimeInMs(int minExecutionTimeInMs) {
        this.minExecutionTimeInMs = minExecutionTimeInMs;
    }

    public void setMaxExecutionTimeInMs(int maxExecutionTimeInMs) {
        this.maxExecutionTimeInMs = maxExecutionTimeInMs;
    }

    public void setAverageExecutionTimeInMs(int averageExecutionTimeInMs) {
        this.averageExecutionTimeInMs = averageExecutionTimeInMs;
    }
}
