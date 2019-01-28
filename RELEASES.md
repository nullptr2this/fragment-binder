# 0.3

- BREAKING CHANGE - the default binding type has been switched from
BINDING_NONE to BINDING_CLOSEST_PARENT to provide a more reasonable
default for most use cases.  BINDING_NONE still exists for cases
where the fragment is used for display only.  This change also
provides a reasonable default scenario for fragments declared in
xml layouts.

# 0.2

- Adds a new binding type called BINDING_CLOSEST_PARENT.  This binding
type will cause the binder to search upward through the parent fragment
chain looking for an implementer of the bound type.  If a suitable
parent fragment is not found, the activity is used if it is of
the bound type.  Otherwise, IllegalStateException is thrown.

- BREAKING CHANGE - Removes several redefinitions of the
defined binding type constantsacross the fragment base class
implementations included with the library.

- BREAKING CHANGE - Removes the ListFragment implementation as ListView
and ListFragment have long since been somewhat deprecated in favor of
RecyclerView.

# 0.1
Initial public release.