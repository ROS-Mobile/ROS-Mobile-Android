package com.schneewittchen.rosandroid.widgets.button;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

public class ButtonData extends BaseData {

    public ButtonState state;

    public  ButtonData(){
        state = ButtonState.Idle;
    }

    public  ButtonData(ButtonState state) {
        this.state = state;
    }
}
