package nullptr2this.android.app.binder.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements ConfirmationDialogFragment.Callback {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.toaster = new Toaster(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button button = (Button) view.findViewById(R.id.button_fragment_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = ConfirmationDialogFragment.newInstance(
                          R.string.fragment_dialog_title
                        , R.string.fragment_dialog_message
                        , ConfirmationDialogFragment.BINDING_FRAGMENT_PARENT);
                dialog.show(getChildFragmentManager(), TAG_DIALOG_BOUND_TO_FRAGMENT);
            }
        });

        button = (Button) view.findViewById(R.id.button_activity_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = ConfirmationDialogFragment.newInstance(
                        R.string.activity_dialog_title
                        , R.string.activity_dialog_message
                        , ConfirmationDialogFragment.BINDING_ACTIVITY);
                dialog.show(getChildFragmentManager(), TAG_DIALOG_BOUND_TO_ACTIVITY);
            }
        });

        return view;
    }

    @Override
    public void onDialogConfirmed(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_confirmation_handled_in_fragment);
    }

    @Override
    public void onDialogRejected(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_rejection_handled_in_fragment);
    }

    @Override
    public void onDialogCancelled(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_cancellation_handled_in_fragment);
    }

    @Override
    public void onDialogDismissed(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_dismissal_handled_in_fragment);
    }

    private static final String TAG_DIALOG_BOUND_TO_ACTIVITY = "TAG_DIALOG_BOUND_TO_ACTIVITY";
    private static final String TAG_DIALOG_BOUND_TO_FRAGMENT = "TAG_DIALOG_BOUND_TO_FRAGMENT";

    private Toaster toaster;
}
