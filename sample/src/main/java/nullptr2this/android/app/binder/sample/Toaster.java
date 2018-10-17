package nullptr2this.android.app.binder.sample;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.widget.Toast;

public class Toaster {

    public Toaster(@NonNull Context context) {
        this.context = context;
    }

    public void show(@StringRes int toastText) {
        Toast.makeText(context, toastText, toastLength).show();
    }

    @NonNull
    private final Context context;
    private final int toastLength = Toast.LENGTH_SHORT;
}
