package nullptr2this.android.app.binder.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ConfirmationDialogFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment(), TAG_FRAGMENT_MAIN)
                    .commit();
        }

        toaster = new Toaster(this);
    }

    @Override
    public void onDialogConfirmed(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_confirmation_handled_in_activity);
    }

    @Override
    public void onDialogRejected(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_rejection_handled_in_activity);
    }

    @Override
    public void onDialogCancelled(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_cancellation_handled_in_activity);
    }

    @Override
    public void onDialogDismissed(String tag, ConfirmationDialogFragment fragment) {
        toaster.show(R.string.dialog_dismissal_handled_in_activity);
    }

    private static final String TAG_FRAGMENT_MAIN = "TAG_FRAGMENT_MAIN";

    private Toaster toaster;
}
