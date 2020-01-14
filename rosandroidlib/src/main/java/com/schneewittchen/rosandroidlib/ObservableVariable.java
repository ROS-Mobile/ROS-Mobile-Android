package com.schneewittchen.rosandroidlib;


public class ObservableVariable<T> {

    public T mVariable;
    private Listener<T> mListener;


    public ObservableVariable(T initValue){
        mVariable = initValue;
        mListener = null;
    }


    public void addObserver(Listener listener){
        mListener = listener;
    }

    public void removeObserver(){
        mListener = null;

    }

    public T get(){
        return mVariable;
    }

    public void set(T newValue){
        boolean hasChanged = !mVariable.equals(newValue);

        mVariable = newValue;

        if(hasChanged && mListener != null){
            mListener.onChanged(newValue);
        }
    }


    public interface Listener<T> {

        void onChanged(T newValue);
    }
}
