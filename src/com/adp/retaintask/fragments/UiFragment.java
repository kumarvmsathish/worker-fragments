package com.adp.retaintask.fragments;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adp.retaintask.R;

/**
 * This is a fragment showing UI that will be updated from work done in the
 * retained fragment.
 */
public class UiFragment extends Fragment implements TaskFragment.TaskCallbacks {
  @SuppressWarnings("unused")
  private static final String TAG = UiFragment.class.getSimpleName();

  private Context mContext;
  private Resources mResources;
  private TaskFragment mTaskFragment;
  private ProgressBar mProgressBar;
  private Button mButton;
  private TextView mPercent;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mContext = activity.getApplicationContext();
    mResources = mContext.getResources();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    mProgressBar = (ProgressBar) view.findViewById(R.id.progress_horizontal);
    mButton = (Button) view.findViewById(R.id.task_button);
    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mTaskFragment.isRunning()) {
          mTaskFragment.cancel();
        } else {
          mTaskFragment.start();
        }
      }
    });
    mPercent = (TextView) view.findViewById(R.id.percent_progress);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (savedInstanceState != null) {
      mProgressBar.setProgress(savedInstanceState.getInt("current_progress"));
      mPercent.setText(savedInstanceState.getString("percent_progress"));
    }

    FragmentManager fm = getFragmentManager();
    mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");

    // If we haven't retained the worker fragment retained, then create it.
    if (mTaskFragment == null) {
      mTaskFragment = new TaskFragment();
      mTaskFragment.setTargetFragment(this, 0);
      fm.beginTransaction().add(mTaskFragment, "task").commit();
    }

    if (mTaskFragment.isRunning()) {
      mButton.setText(getString(R.string.cancel));
    } else {
      mButton.setText(getString(R.string.start));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("current_progress", mProgressBar.getProgress());
    outState.putString("percent_progress", mPercent.getText().toString());
  }

  /****************************/
  /***** CALLBACK METHODS *****/
  /****************************/

  @Override
  public void onPreExecute() {
    Toast.makeText(mContext, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(mResources.getString(R.string.cancel));
  }

  private static NumberFormat sFormatter = NumberFormat.getPercentInstance();
  static { sFormatter.setMinimumFractionDigits(1); }

  @Override
  public void onProgressUpdate(double percent) {
    int position = (int) (percent * mProgressBar.getMax());
    mProgressBar.setProgress(position);
    mPercent.setText(sFormatter.format(percent));
  }

  @Override
  public void onCancelled() {
    Toast.makeText(mContext, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(mResources.getString(R.string.start));
    mProgressBar.setProgress(0);
    mPercent.setText(mResources.getString(R.string.zero_percent));
  }

  @Override
  public void onPostExecute() {
    Toast.makeText(mContext, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(mResources.getString(R.string.start));
    mProgressBar.setProgress(mProgressBar.getMax());
    mPercent.setText(mResources.getString(R.string.one_hundred_percent));
  }
}
