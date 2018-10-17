package nullptr2this.android.app.binder;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import nullptr2this.android.app.binder.FragmentBinder.BindingStrategy;

public abstract class BindingDialogFragment<T> extends DialogFragment {

    public static final int BINDING_NONE = FragmentBinder.BINDING_NONE;
    public static final int BINDING_ACTIVITY = FragmentBinder.BINDING_ACTIVITY;
    public static final int BINDING_FRAGMENT_PARENT = FragmentBinder.BINDING_FRAGMENT_PARENT;
    public static final int BINDING_FRAGMENT_TARGET = FragmentBinder.BINDING_FRAGMENT_TARGET;

    protected BindingDialogFragment(Class<T> bindingType) {
        binder = new FragmentBinder<>(this, binding, bindingType);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        binder.onAttach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder.onCreate();
    }

    @Override
    public void onDestroy() {
        binder.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        binder.onDetach();
        super.onDetach();
    }

    protected boolean isBound() {
        return null != target;
    }

    protected T getBinding() {
        return target;
    }

    protected static Bundle createArguments(int binding) {
        return populateArguments(new Bundle(), binding);
    }

    protected static <T> Bundle createArguments(Class<? extends BindingStrategy<T>> bindingStrategy) {
        return FragmentBinder.createArguments(bindingStrategy);
    }

    protected static Bundle populateArguments(Bundle arguments, int binding) {
        return FragmentBinder.populateArguments(arguments, binding);
    }

    private final FragmentBinder<T> binder;
    private T target;

    private FragmentBinder.Binding<T> binding = new FragmentBinder.Binding<T>() {
        @Override
        public void bind(T target) {
            BindingDialogFragment.this.target = target;
        }

        @Override
        public void unbind() {
            target = null;
        }
    };
}
