package cn.qatime.player.flow;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qatime.player.R;
import cn.qatime.player.flow.screen.FlowAnswerScreen;
import cn.qatime.player.flow.screen.FlowExplainScreen;
import cn.qatime.player.flow.screen.FlowChooseScreen;
import cn.qatime.player.flow.screen.FlowRePostScreen;
import cn.qatime.player.flow.screen.FlowReadScreen;
import cn.qatime.player.flow.screen.FlowRecordScreen;
import cn.qatime.player.flow.view.FlowBaseLayout;
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
    private Object destKey;
    private View incomingView;

    public BasicDispatcher(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
        Object destKey = traversal.destination.top();
        ViewGroup frame = (ViewGroup) activity.findViewById(R.id.basic_activity_frame);

        if (frame.getChildCount() > 0) {
            final View currentView = frame.getChildAt(0);

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
        } else if (destKey instanceof FlowChooseScreen) {
            layout = R.layout.flow_choose_screen;
        } else if (destKey instanceof FlowAnswerScreen) {
            layout = R.layout.flow_answer_screen;
        } else if (destKey instanceof FlowRecordScreen) {
            layout = R.layout.flow_record_screen;
        } else if (destKey instanceof FlowRePostScreen) {
            layout = R.layout.flow_repost_screen;
        } else if (destKey instanceof FlowReadScreen) {
            layout = R.layout.flow_read_screen;
        } else {
            throw new AssertionError("Unrecognized screen " + destKey);
        }

        View incomingView = LayoutInflater.from(traversal.createContext(destKey, activity)) //
                .inflate(layout, frame, false);

        this.destKey = destKey;
        this.incomingView = incomingView;

        frame.addView(incomingView);
        traversal.getState(traversal.destination.top()).restore(incomingView);

        callback.onTraversalCompleted();
    }

    public void pause() {
        if (destKey instanceof FlowExplainScreen) {
        } else {
            FlowBaseLayout layout = incomingView.findViewById(R.id.base_layout);
            layout.pause();
        }
    }

    public void reStart() {
        if (destKey instanceof FlowExplainScreen) {
        } else {
            FlowBaseLayout layout = incomingView.findViewById(R.id.base_layout);
            layout.reStart();
        }
    }
}
