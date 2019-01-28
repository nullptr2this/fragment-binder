# fragment-binder
[![Release](https://jitpack.io/v/nullifythis/fragment-binder.svg)](https://jitpack.io/#nullifythis/fragment-binder)

NOTE: The API for this library is still under construction and is subject to breaking changes.

A small suite of classes allowing clients of a fragment class to control how a fragment acquires it's callback/delegate.

The Android Developers Guide for Fragments suggests ([here](https://developer.android.com/guide/components/fragments.html#CommunicatingWithActivity)) code like the following be used to wire up a fragment to communicate with it's containing Activity.

```java
public static class FragmentA extends ListFragment {
    OnArticleSelectedListener mListener;
    ...
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
    ...
}
```

The problem with this approach is that the Fragment implementation ends up knowing too much about it's surroundings and becomes less reusable as a result.  The approach forces the client to use the Fragment in a specific context, and will always have to route callbacks through the Activity.

The FragmentBinder implementation aims to provide a means for the client of a Fragment to instruct the Fragment via arguments as to how it should acquire a reference to it's callback rather than requiring the Fragment implementation to always assume the same target.

See the sample application for an example of a dialog fragment's callbacks being directed to the dialog's containing activity and fragment depending the arguments provided by the client.

# Download
Add the JitPack maven repository to your build file:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Add the library dependency:
```groovy
dependencies {
    implementation 'com.github.nullifythis:fragment-binder:0.1'
}
```
