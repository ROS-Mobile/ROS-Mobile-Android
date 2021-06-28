package com.schneewittchen.rosandroid.widgets.laserscan;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberLayerViewHolder;
import com.schneewittchen.rosandroid.utility.Utils;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.Collections;
import java.util.List;

import sensor_msgs.LaserScan;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.05.21
 */
public class LaserScanDetailVH extends SubscriberLayerViewHolder implements TextView.OnEditorActionListener {

    private static final String TAG = LaserScanDetailVH.class.getSimpleName();
    private static final int MIN_POINT_SIZE = 1;
    private static final int MAX_POINT_SIZE = 50;

    private AlphaTileView areaTileView;
    private AlphaTileView pointsTileView;
    private EditText pointSizeEditText;
    private int pointsColor;
    private int areaColor;


    @Override
    protected void initView(View parentView) {
        this.pointsTileView = parentView.findViewById(R.id.pointsTileView);
        this.areaTileView = parentView.findViewById(R.id.areaTileView);
        this.pointSizeEditText = parentView.findViewById(R.id.pointSizeEditText);

        this.pointsTileView.setOnClickListener(v -> openColorChooser(pointsTileView));
        this.areaTileView.setOnClickListener(v -> openColorChooser(areaTileView));

        this.pointSizeEditText.setOnEditorActionListener(this);
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        LaserScanEntity scanEntity = (LaserScanEntity) entity;

        this.pointSizeEditText.setText(String.valueOf(scanEntity.pointSize));

        this.chooseColor(pointsTileView, scanEntity.pointsColor);
        this.chooseColor(areaTileView, scanEntity.areaColor);
    }

    private void openColorChooser(AlphaTileView tileView) {
        Log.i(TAG, "OPen stuff");

        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this.itemView.getContext())
                .setTitle("Choose a color")
                .setPositiveButton(R.string.ok, (ColorEnvelopeListener) (envelope, fromUser) -> {
                    chooseColor(tileView, envelope.getColor());
                    this.forceWidgetUpdate();
                })
                .setNegativeButton(R.string.cancel,
                        (dialogInterface, i) -> dialogInterface.dismiss());

        int initialColor = areaColor;

        if (tileView == pointsTileView) {
            initialColor = pointsColor;
        }

        builder.getColorPickerView().setInitialColor(initialColor);
        builder.show();
    }

    private void chooseColor(AlphaTileView tileView, int color) {
        if (tileView == pointsTileView) {
            this.pointsColor = color;
            this.pointsTileView.setBackgroundColor(color);

        } else if (tileView == areaTileView) {
            this.areaColor = color;
            this.areaTileView.setBackgroundColor(color);
        }
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        LaserScanEntity scanEntity = (LaserScanEntity) entity;
        scanEntity.pointsColor = pointsColor;
        scanEntity.areaColor = areaColor;

        int size = Integer.parseInt(pointSizeEditText.getText().toString());
        size = Math.max(MIN_POINT_SIZE, Math.min(MAX_POINT_SIZE, size));
        scanEntity.pointSize = size;
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(LaserScan._TYPE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_PREVIOUS:
                Utils.hideSoftKeyboard(v);
                v.clearFocus();
                this.forceWidgetUpdate();
                return true;
        }

        return false;
    }
}
