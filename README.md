# Destinations: type-safe multi-module navigation

Currently Google suggests using `routes` to define destinations in Compose enabled projects. 
Unfortunately `routes` based navigation has many limitations which cannot be easily mitigated and is generally a step back 
when compared to `xml` based navigation.

`Destinations` library aims to deliver a similar set of features as `xml` based solution while avoiding the pitfalls of `routes`. 

| Aspect                                            | Destinations                                                                                                                             | Routes                                                                                                                                     |
|---------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Type-safety                                       | ✔ Yes, compile-time.                                                                                                                     | ❌ No, crash at runtime.                                                                                                                    |
| Supported types                                   | ✔ Primitives, Parcelables, Serializables.                                                                                                | ❌ Primitives only.                                                                                                                         |
| Data classes as arguments                         | ✔ Yes (if they are Parcelable or Serializable).                                                                                          | ❌ No.                                                                                                                                      |
| Any number of arguments                           | ✔ Yes.<br/>⚠ Multiple arguments must be grouped in a single Parcelable or Serializable.                                                  | ✔ Yes.                                                                                                                                     |
| Order of arguments is important                   | ✔ No. You can use named arguments in any order.                                                                                          | ❌ Yes if you use `/slash/{syntax}/`<br/>✔ No if you use `?query={syntax}`.                                                                 |
| Arguments description is required                 | ✔ No. Common data classes or primitives serve as arguments.                                                                              | ❌ You need to provide a `NamedNavArgument`  for each parameter. Parameters will be typed to `String` otherwise.                            |
| Nullable arguments supported                      | ✔ Yes. You simply use nullable field (by adding `?`).                                                                                    | ❌ No if you use `/slash/{syntax}/`<br/>✔ Yes if you use `?query={syntax}`.                                                                 |
| Defining conflicting destinations possible        | ✔ No. Identifiers are generated automatically and cannot repeat.                                                                         | ❌ Yes. `routes` are just `Strings` created manually and there will be a runtime conflict if e.g. two feature modules use the same `route`. |
| Encapsulating destination in a sub-graph possible | ✔ Yes.                                                                                                                                   | ❌ No. Even if you place a destination in a sub-graph you can navigate to it directly as all `routes` are global.                           |
| Controlling destinations visibility possible      | ✔ Yes. You simply control the visibility of `Destination` objects in Kotlin (you can't navigate to a `Destination` without a reference). | ❌ No. You can always navigate to any destination if you know the `String` defining the `route` as all `routes` are global.                 |
| Deeplinks supported                               | ✔ Not required. You may define a deeplink as usual and redirect it to a `Destination` using `Side Effect`.                               | ✔ Yes.                                                                                                                                     |
| Annotation processor required                     | ✔ No.                                                                                                                                    | ✔ No.                                                                                                                                      |
| Reflection used                                   | ✔ No.                                                                                                                                    | ✔ No.                                                                                                                                      |
| Amount of boilerplate                             | ✔ Minimalistic API.                                                                                                                      | ❌ A lot of boilreplate is required to define and use arguments.                                                                            |

## Defining `Destinations`

`Destination` represents a screen or a section of an application a user can navigate to. Each `Destination` servers a role of both 
an identifier of a navigation graph's node and screen's/section's argument definition.

There are three types od `Desintations`:
- `DestinationWithoutArgument`<br />
  This is the `Destination` that would be used the most in almost any project - note that most of the time you don't have to pass any arguments to your screens if you use `Session` mechanism.  
  ```kotlin
  val HomeScreenDestination = Destination.withoutArgument()  
  ```
- `DestinationWithRequiredArgument<T : Parcelable>`
  This type of `Destination` is used usually used while embedding:
  - Standalone screens displaying data of specific items (e.g. credit card, a person) 
  - Subgraphs which contains a journey related to some specific items. Generally the argument will be passed to the first screen of the subgraph - check the next point.
  - First screens of the subgraphs. It is only possible if the graph's argument and its first screen's argument are of the same type.
  ```kotlin
  @Parcelize
  data class CardArgument(val id: String) : Parcelable
  
  val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()  
  ```
  > ⚠ You may use `Serializables` or primitives as arguments as well, but this is discouraged. Check Best Practices section for more details. 
- `DestinationWithOptionalArgument`
  This type of `Destination` is very similar to `DestinationWithRequiredArgument` but it indicates that destination screen either:
  - does not require argument and can handle `null`
  - or does require argument but a default argument will be provided while placing the screen in the graph.
  <br /> 
  ```kotlin
  @Parcelize
  data class EditPasscodeArgument(val mode: EditPasscodeMode) : Parcelable
  
  val EditPasscodeSectionDestination = Destination.withOptionalArgument<EditPasscodeArgument>()
  ```
  > ℹ Check Best Practices section for examples. 
  
## Embedding screens and graphs
You may embed the same set of items as with `routes` using the following overloaded methods:
- `NavGraphBuilder.composable` to embed screens
- `NavGraphBuilder.navigation` to embed sub-graphs
- `NavGraphBuilder.dialog` to embed dialogs

### Composables

`Destination` without argument:
```kotlin
val HomeScreenDestination = Destination.withoutArgument()

...

composable(HomeScreenDestination) { // no argument
  HomeScreen()
}
```

`Destination` with required argument:
```kotlin
@Parcelize
data class CardArgument(val id: String) : Parcelable
val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()

...

composable(CardDetailsSectionDestination) { arg -> // non-null argument
  CardDetails(cardId = arg.id)
}
```

`Destination` with optional argument:
```kotlin
@Parcelize
data class EditPasscodeArgument(val mode: EditPasscodeMode) : Parcelable
val EditPasscodeIntroductionDestination = Destination.withOptionalArgument<EditPasscodeArgument>()

...

composable(EditPasscodeIntroductionDestination) { arg -> // nullable argument
  Introduction(mode = arg.mode ?: EditPasscodeMode.DEFINE)
}
```
You may specify default value for `DestinationWithOptionalArgument`:
```kotlin
composable(
  destination = EditPasscodeIntroductionDestination,
  defaultArgument = EditPasscodeArgument(EditPasscodeMode.DEFINE)
) { arg -> // non-nullable argument
  Introduction(mode = arg.mode)
}
```

### Dialogs

Embedding `dialogs` is the same as embedding `composables`. The only difference is additional parameter `dialogProperties` 
which you may use to define dialog behaviour.
Refer to the official documentation for more details: https://developer.android.com/reference/kotlin/androidx/compose/ui/window/DialogProperties.

```kotlin
    dialog(
        LogoutDialogDestination,
        dialogProperties = DialogProperties(dismissOnClickOutside = false)
    ) {
        LogoutDialog(...)
    }
```

### Subgraphs

Use `NavGraphBuilder.navigation` method to embed graph withing other graph. 

Whenever you build a graph you must define its `startDestination`. The `Destination` of the graph
must define its argument in a way compatible with `Destination` of the `startDestination` to ensure type safety, e.g.

```kotlin
@Parcelize
data class EditPasscodeArgument(val mode: EditPasscodeMode) : Parcelable

val EditPasscodeDestination = Destination.withOptionalArgument<EditPasscodeArgument>() // DestinationWithOptionalArgument
private val IntroductionDestination = Destination.withArgument<EditPasscodeArgument>() // DestinationWithRequiredArgument

...

navigation(
  destination = EditPasscodeDestination,
  startDestination = IntroductionDestination,
  defaultArgument = EditPasscodeMode.DEFINE // default value defined for the whole graph
) {
  composable(IntroductionDestination) { arg -> // non-null argument
    Introdcution(mode = arg.mode)
  }
  ...
}
```

alternatively you may define 

```kotlin
@Parcelize
data class EditPasscodeArgument(val mode: EditPasscodeMode) : Parcelable

val EditPasscodeDestination = Destination.withOptionalArgument<EditPasscodeArgument>() // DestinationWithOptionalArgument
private val IntroductionDestination = Destination.withOptionalArgument<EditPasscodeArgument>() // DestinationWithOptionalArgument

...

navigation(
  destination = EditPasscodeDestination,
  startDestination = IntroductionDestination
) {
  composable(
    destination = IntroductionDestination,
    defaultArgument = EditPasscodeMode.DEFINE // default value defined for a particular screen
  ) { arg -> // non-null argument
    Introdcution(mode = arg.mode)
  }
  ...
}
```


The following table presents all possible redirections from `destination` to `startDestination`:

| Graph's `destination`           | `defaultArgument` in `navigation` | `startDestination`              | `defaultArgument` in `composable` |
|---------------------------------|:---------------------------------:|---------------------------------|:---------------------------------:|
| DestinationWithoutArgument      |                 ✖                 | DestinationWithoutArgument      |                 ✖                 |
| DestinationWithoutArgument      |                 ✖                 | DestinationWithOptionalArgument |                 ✖                 |
| DestinationWithoutArgument      |                 ✔                 | DestinationWithOptionalArgument |                 ✖                 |
| DestinationWithoutArgument      |                 ✖                 | DestinationWithOptionalArgument |                 ✔                 |
| DestinationWithoutArgument      |                 ✔                 | DestinationWithRequiredArgument |                 ✖                 |
| DestinationWithOptionalArgument |                 ✖                 | DestinationWithoutArgument      |                 ✖                 |
| DestinationWithOptionalArgument |                 ✖                 | DestinationWithOptionalArgument |                 ✖                 |
| DestinationWithOptionalArgument |                 ✔                 | DestinationWithOptionalArgument |                 ✖                 |
| DestinationWithOptionalArgument |                 ✖                 | DestinationWithOptionalArgument |                 ✔                 |
| DestinationWithOptionalArgument |                 ✔                 | DestinationWithOptionalArgument |        ✔ (will be ignored)        |
| DestinationWithRequiredArgument |                 ✖                 | DestinationWithoutArgument      |                 ✖                 |
| DestinationWithRequiredArgument |                 ✖                 | DestinationWithOptionalArgument |                 ✖                 |
| DestinationWithRequiredArgument |                 ✖                 | DestinationWithOptionalArgument |        ✔ (will be ignored)        |
| DestinationWithRequiredArgument |                 ✖                 | DestinationWithRequiredArgument |                 ✖                 |
> ⚠ Note: The types of the `destination` and `startDestination` arguments must match!

> ℹ The signature of the `NavGraphBuilder.navigation` method enforces correct redirection to the `startDestination` - if you make a mistake it will be caught on a compile-time.  

> ℹ Default value may be defined for the whole graph or for a particular `startDestination`. If you are experimenting with the `startDestination` a lot
> a the moment it may be easier to define the default value in `navigation` so you don't have to repeat it for each screen just because that screen might serve a role of a `startDestination` at some point.


