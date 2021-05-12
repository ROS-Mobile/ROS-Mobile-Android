package com.schneewittchen.rosandroid.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;


import androidx.appcompat.widget.AppCompatTextView;

import java.util.Arrays;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.04.21
 */
public class CharacterWrapTextView extends AppCompatTextView {

    public CharacterWrapTextView(Context context) {
        super(context);
    }

    public CharacterWrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterWrapTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        getWrappedText(text.toString());
    }

    private String getWrappedText(String input) {
        int maxEms = getMaxLines();
        String output = "";
        List<String> parts = Arrays.asList(input.split("/"));
        parts.remove(0);

        String currentLine = "";
        int row = 0;

        for (String part: parts) {
            if (currentLine.length() + ("/" + part).length() > maxEms) {
                output += (row == 0? "" : "\n") +  currentLine;
                row++;
                currentLine = "";

            } else {
                currentLine += "/" + part;
            }
        }

        return output;
    }
}