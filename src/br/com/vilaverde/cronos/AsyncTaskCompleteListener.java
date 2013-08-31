package br.com.vilaverde.cronos;

public interface AsyncTaskCompleteListener<T>
{

    public void onTaskComplete(T result);

}
