package nullptr2this.android.app.binder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public class FragmentBinder<I> {

    public static final String EXTRA_BINDING_TARGET = "nullptr2this.android.app.binder.EXTRA_BINDING_TARGET";
    public static final String EXTRA_BINDING_CUSTOM_CLASS = "nullptr2this.android.app.binder.app.EXTRA_BINDING_CUSTOM_CLASS";

    public static final int BINDING_NONE = 0;
    public static final int BINDING_ACTIVITY = 1;
    public static final int BINDING_FRAGMENT_PARENT = 2;
    public static final int BINDING_FRAGMENT_TARGET = 3;
    public static final int BINDING_FRAGMENT_CUSTOM = 4;
    public static final int BINDING_DEFAULT = BINDING_NONE;

    public interface Binding<T> {
        void bind(T target);

        void unbind();
    }

    public abstract static class BindingStrategy<T> {

        public void onAttach() {
        }

        public void onCreate() {
        }

        public void onDestroy() {
        }

        public void onDetach() {
        }

        public BindingStrategy(@NonNull Fragment fragment, @NonNull Binding<T> binding, @NonNull Class<T> bindingType) {
            this.fragment = fragment;
            this.binding = binding;
            this.bindingType = bindingType;
        }

        protected final void bind(Object target) {
            try {
                binding.bind(bindingType.cast(target));
            } catch (ClassCastException exception) {
                throw new IllegalStateException(String.format("The binding target of type %s could not be cast to the required target type %s."
                        , target.getClass().getSimpleName(), bindingType.getSimpleName()), exception);
            }
        }

        protected final void unbind() {
            binding.unbind();
        }

        protected final Fragment fragment;
        private final Binding<T> binding;
        private final Class<T> bindingType;
    }

    public <F extends Fragment & Binding<I>> FragmentBinder(final F fragment, Class<I> bindingType) {
        this(fragment, fragment, bindingType);
    }

    public FragmentBinder(@NonNull Fragment fragment, @NonNull Binding<I> binding, @NonNull Class<I> bindingType) {
        this.fragment = fragment;
        this.binding = binding;
        this.bindingType = bindingType;
    }

    public static <T> Bundle createArguments(Class<? extends BindingStrategy<T>> customStrategy) {
        Bundle arguments = createArguments(BINDING_FRAGMENT_CUSTOM);
        arguments.putSerializable(EXTRA_BINDING_CUSTOM_CLASS, customStrategy);
        return arguments;
    }

    public static Bundle createArguments(int binding) {
        return populateArguments(new Bundle(), binding);
    }

    public static Bundle populateArguments(Bundle arguments, int binding) {
        if (null != arguments) {
            arguments.putInt(EXTRA_BINDING_TARGET, binding);
        }
        return arguments;
    }

    public static <T> Bundle populateArguments(Bundle arguments, Class<? extends BindingStrategy<T>> customStrategy) {
        Bundle args = new Bundle();
        if (null != arguments) {
            args = populateArguments(arguments, BINDING_FRAGMENT_CUSTOM);
            args.putSerializable(EXTRA_BINDING_CUSTOM_CLASS, customStrategy);
        }
        return args;
    }

    public void onAttach() {
        lazyBindingStrategy().onAttach();
    }

    public void onCreate() {
        lazyBindingStrategy().onCreate();
    }

    public void onDestroy() {
        lazyBindingStrategy().onDestroy();
    }

    public void onDetach() {
        lazyBindingStrategy().onDetach();
    }

    protected static class NoBindingStrategy<T> extends BindingStrategy<T> {
        public NoBindingStrategy(Fragment fragment, Binding<T> binding, Class<T> bindingType) {
            super(fragment, binding, bindingType);
        }
    }

    protected static class ActivityBindingStrategy<T> extends BindingStrategy<T> {

        public ActivityBindingStrategy(Fragment fragment, Binding<T> binding, Class<T> bindingType) {
            super(fragment, binding, bindingType);
        }

        @Override
        public void onAttach() {
            bind(fragment.getActivity());
        }

        @Override
        public void onDetach() {
            unbind();
        }
    }

    protected static class ParentFragmentBindingStrategy<T> extends BindingStrategy<T> {

        public ParentFragmentBindingStrategy(Fragment fragment, Binding<T> binding, Class<T> bindingType) {
            super(fragment, binding, bindingType);
        }

        @Override
        public void onCreate() {
            bind(fragment.getParentFragment());
        }

        @Override
        public void onDestroy() {
            unbind();
        }
    }

    protected static class TargetFragmentBindingStrategy<T> extends BindingStrategy<T> {

        public TargetFragmentBindingStrategy(Fragment fragment, Binding<T> binding, Class<T> bindingType) {
            super(fragment, binding, bindingType);
        }

        @Override
        public void onCreate() {
            bind(fragment.getTargetFragment());
        }

        @Override
        public void onDestroy() {
            unbind();
        }
    }

    private static <T> BindingStrategy<T> createBindingStrategy(Fragment fragment, Binding<T> binding, Class<T> bindingType) {

        Bundle arguments = fragment.getArguments();
        int bindingTarget = getBindingTargetArgument(arguments);

        switch (bindingTarget) {
            case BINDING_ACTIVITY:
                return new ActivityBindingStrategy<>(fragment, binding, bindingType);
            case BINDING_FRAGMENT_PARENT:
                return new ParentFragmentBindingStrategy<>(fragment, binding, bindingType);
            case BINDING_FRAGMENT_TARGET:
                return new TargetFragmentBindingStrategy<>(fragment, binding, bindingType);
            case BINDING_FRAGMENT_CUSTOM:
                return createCustomBindingStrategy(fragment, binding, bindingType);
            case BINDING_NONE:
            default:
                return new NoBindingStrategy<>(fragment, binding, bindingType);
        }
    }

    private static <T> BindingStrategy<T> createCustomBindingStrategy(Fragment fragment, Binding<T> binding, Class<T> bindingType) {
        try {
            Class<? extends BindingStrategy<T>> strategyClass = getCustomBindingStrategyArgument(fragment.getArguments());
            return strategyClass.getConstructor(Fragment.class, Binding.class, Class.class).newInstance(fragment, binding, bindingType);
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot instantiate custom binding strategy.", ex);
        }
    }

    private static int getBindingTargetArgument(Bundle bundle) {
        if (null != bundle) {
            return bundle.getInt(EXTRA_BINDING_TARGET, BINDING_DEFAULT);
        }
        return BINDING_DEFAULT;
    }

    private static <T> Class<? extends BindingStrategy<T>> getCustomBindingStrategyArgument(Bundle bundle) {
        if (null == bundle) {
            return null;
        }
        return (Class<? extends BindingStrategy<T>>) bundle.getSerializable(EXTRA_BINDING_CUSTOM_CLASS);
    }

    private BindingStrategy<I> lazyBindingStrategy() {
        if (null == bindingStrategy) {
            bindingStrategy = createBindingStrategy(fragment, binding, bindingType);
        }
        return bindingStrategy;
    }

    private final Fragment fragment;
    private final Binding<I> binding;
    private final Class<I> bindingType;
    private BindingStrategy<I> bindingStrategy;
}