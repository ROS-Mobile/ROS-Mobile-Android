package com.schneewittchen.rosandroid.ui.fragments.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class LockableViewPager extends ViewPager {

  private boolean swipeable;


  public LockableViewPager(Context context) {
      super(context);
  }

  public LockableViewPager(Context context, AttributeSet attrs) {
      super(context, attrs);

      this.swipeable = false;
  }


  @Override
  public boolean onTouchEvent(MotionEvent event) {
      if (this.swipeable) {
          return super.onTouchEvent(event);
      }

      return false;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
      if (this.swipeable) {
          return super.onInterceptTouchEvent(event);
      }

      return false;
  }

  public void setSwipeable(boolean swipeable) {
      this.swipeable = swipeable;
  }
}