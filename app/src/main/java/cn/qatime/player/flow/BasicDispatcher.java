package cn.qatime.player.flow;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qatime.player.R;
import cn.qatime.player.activity.ExaminationIngActivity;
import flow.Dispatcher;
import flow.Flow;
import flow.Traversal;
import flow.TraversalCallback;

/**
 * @author luntify
 * @date 2017/12/11 13:19
 * @Description:
 */

public class BasicDispatcher implements Dispatcher {
    private final Activity activity;

    public BasicDispatcher(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
        Object destKey = traversal.destination.top();
        ViewGroup frame = (ViewGroup) activity.findViewById(R.id.basic_activity_frame);

        if (frame.getChildCount() > 0) {
            final View currentView = frame.getChildAt(0);

            // Save the outgoing view state.
            if (traversal.origin != null) {
                traversal.getState(traversal.origin.top()).save(currentView);
            }

            final Object currentKey = Flow.getKey(currentView);
            if (destKey.equals(currentKey)) {
                callback.onTraversalCompleted();
                return;
            }

            frame.removeAllViews();
        }

        @LayoutRes final int layout;
        if (destKey instanceof FlowExplainScreen) {
            layout = R.layout.flow_exam_explain;
        } else if (destKey instanceof FlowRadioScreen) {
            layout = R.layout.flow_radio_screen;
        } else {
            throw new AssertionError("Unrecognized screen " + destKey);
        }

        View incomingView = LayoutInflater.from(traversal.createContext(destKey, activity)) //
                .inflate(layout, frame, false);

        frame.addView(incomingView);
        traversal.getState(traversal.destination.top()).restore(incomingView);

        callback.onTraversalCompleted();
    }
}
