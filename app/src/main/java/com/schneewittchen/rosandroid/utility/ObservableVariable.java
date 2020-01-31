package com.schneewittchen.rosandroid.utility;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class ObservableVariable<T> {

    public T mVariable;
    private Listener<T> mListener;


    public ObservableVariable(T initValue){
        mVariable = initValue;
        mListener = null;
    }


    public void addObserver(Listener<T> listener){
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
