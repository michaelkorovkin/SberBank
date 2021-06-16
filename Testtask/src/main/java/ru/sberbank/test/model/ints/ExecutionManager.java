package ru.sberbank.test.model.ints;


public interface ExecutionManager  {
    Context execute(Runnable... tasks);
}
