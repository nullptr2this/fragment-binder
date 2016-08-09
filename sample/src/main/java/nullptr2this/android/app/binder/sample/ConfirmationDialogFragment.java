package nullptr2this.android.app.binder.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import nullptr2this.android.app.binder.BindingDialogFragment;
import nullptr2this.android.app.binder.FragmentBinder;
import nullptr2this.android.app.binder.FragmentBinder.BindingStrategy;

public class ConfirmationDialogFragment
        extends BindingDialogFragment<ConfirmationDialogFragment.Callback> {

    public interface Callback {

        void onDialogConfirmed(String tag, ConfirmationDialogFragment fragment);

        void onDialogRejected(String tag, ConfirmationDialogFragment fragment);

        void onDialogCancelled(String tag, ConfirmationDialogFragment fragment);

        void onDialogDismissed(String tag, ConfirmationDialogFragment fragment);
    }

    public ConfirmationDialogFragment() {
        super(Callback.class);
    }

    public static ConfirmationDialogFragment newInstance(int titleRes, int messageRes,
            Bundle bundle, Class<? extends BindingStrategy<Callback>> bindingStrategy) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle arguments = bundle == null ? new Bundle() : bundle;
        FragmentBinder.populateArguments(arguments, bindingStrategy);
        arguments.putInt(ARG_TITLE_RES, titleRes);
        arguments.putInt(ARG_MESSAGE_RES, messageRes);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static ConfirmationDialogFragment newInstance(int titleRes, int messageRes,
            int bindingMode) {

        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle arguments = createArguments(bindingMode);
        arguments.putInt(ARG_TITLE_RES, titleRes);
        arguments.putInt(ARG_MESSAGE_RES, messageRes);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static ConfirmationDialogFragment newInstance(int titleRes, int messageRes,
            int negativeRes, int bindingMode) {

        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle arguments = createArguments(bindingMode);
        arguments.putInt(ARG_TITLE_RES, titleRes);
        arguments.putInt(ARG_MESSAGE_RES, messageRes);
        arguments.putInt(ARG_NEGATIVE_TEXT_RES, negativeRes);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static ConfirmationDialogFragment newInstance(String title, String message,
            int bindingMode) {
        return newInstance(title, message, bindingMode, null);
    }

    public static ConfirmationDialogFragment newInstance(String title, String message,
            int bindingMode, Bundle extras) {

        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();

        Bundle arguments = createArguments(bindingMode);
        arguments.putString(ARG_TITLE_STRING, title);
        arguments.putString(ARG_MESSAGE_STRING, message);
        arguments.putParcelable(ARG_EXTRAS, extras);

        fragment.setArguments(arguments);

        return fragment;
    }

    public static ConfirmationDialogFragment newInstance(String title, String message,
            int negativeRes, int bindingMode) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle arguments = createArguments(bindingMode);
        arguments.putString(ARG_TITLE_STRING, title);
        arguments.putString(ARG_MESSAGE_STRING, message);
        arguments.putInt(ARG_NEGATIVE_TEXT_RES, negativeRes);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static ConfirmationDialogFragment newInstance(String title, String message,
            int positiveRes, int negativeRes, int bindingMode) {
        final ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        final Bundle arguments = createArguments(bindingMode);
        arguments.putString(ARG_TITLE_STRING, title);
        arguments.putString(ARG_MESSAGE_STRING, message);
        arguments.putInt(ARG_POSITIVE_TEXT_RES, positiveRes);
        arguments.putInt(ARG_NEGATIVE_TEXT_RES, negativeRes);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static ConfirmationDialogFragment newInstance(int titleRes, int messageRes,
            int positiveRes, int negativeRes, int bindingMode) {
        final ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        final Bundle arguments = createArguments(bindingMode);
        arguments.putInt(ARG_TITLE_RES, titleRes);
        arguments.putInt(ARG_MESSAGE_RES, messageRes);
        arguments.putInt(ARG_POSITIVE_TEXT_RES, positiveRes);
        arguments.putInt(ARG_NEGATIVE_TEXT_RES, negativeRes);
        fragment.setArguments(arguments);
        return fragment;
    }

    public Bundle getExtras() {
        Bundle args = getArguments();
        return (null == args) ? null :
                (Bundle) args.getParcelable(ARG_EXTRAS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        String message = "";
        String title = "";

        if (arguments.containsKey(ARG_TITLE_RES)) {
            title = getString(arguments.getInt(ARG_TITLE_RES, 0));
        }
        else if (arguments.containsKey(ARG_TITLE_STRING)) {
            title = arguments.getString(ARG_TITLE_STRING);
        }

        if (arguments.containsKey(ARG_MESSAGE_RES)) {
            message = getString(arguments.getInt(ARG_MESSAGE_RES, 0));
        }
        else if (arguments.containsKey(ARG_MESSAGE_STRING)) {
            message = arguments.getString(ARG_MESSAGE_STRING);
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        arguments.getInt(ARG_POSITIVE_TEXT_RES, R.string.yes_caps_leading),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isBound()) {
                                    getBinding().onDialogConfirmed(getTag(),
                                            ConfirmationDialogFragment.this);
                                }
                            }
                        })
                .setNegativeButton(
                        arguments.getInt(ARG_NEGATIVE_TEXT_RES, R.string.no_caps_leading),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isBound()) {
                                    getBinding().onDialogRejected(getTag(),
                                            ConfirmationDialogFragment.this);
                                }
                            }
                        })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (isBound()) {
                            getBinding()
                                    .onDialogCancelled(getTag(), ConfirmationDialogFragment.this);
                        }
                    }
                })
                .create();

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isBound()) {
            getBinding().onDialogDismissed(getTag(), ConfirmationDialogFragment.this);
        }
    }

    private static final String ARG_TITLE_RES = "ARG_TITLE_RES";
    private static final String ARG_MESSAGE_RES = "ARG_MESSAGE_RES";
    private static final String ARG_POSITIVE_TEXT_RES = "ARG_POSITIVE_TEXT_RES";
    private static final String ARG_NEGATIVE_TEXT_RES = "ARG_NEGATIVE_TEXT_RES";
    private static final String ARG_TITLE_STRING = "ARG_TITLE_STRING";
    private static final String ARG_MESSAGE_STRING = "ARG_MESSAGE_STRING";
    private static final String ARG_EXTRAS = "ARG_EXTRAS";
}
