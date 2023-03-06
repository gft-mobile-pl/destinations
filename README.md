# Destinations: type-safe multi-module navigation

[[_TOC_]]

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

# Usage

## Defining `Destinations`

`Destination` represents a screen or a section of an application a user can navigate to. Each `Destination` servers a role of both
an identifier of a navigation graph's node and screen's/section's argument definition.

There are three types od `Desintations`:

- `DestinationWithoutArgument`<br />
  This is the `Destination` that would be used the most in almost any project - note that most of the time you don't have to pass any
  arguments to your screens if you use `Session` mechanism.
  ```kotlin
  val HomeScreenDestination = Destination.withoutArgument()  
  ```
- `DestinationWithRequiredArgument<T : Parcelable>`
  This type of `Destination` is used usually used while embedding:
    - Standalone screens displaying data of specific items (e.g. credit card, a person)
    - Subgraphs which contains a journey related to some specific items. Generally the argument will be passed to the first screen of
      the subgraph - check the next point.
    - First screens of the subgraphs. It is only possible if the graph's argument and its first screen's argument are of the same type.
  ```kotlin
  @Parcelize
  data class CardArgument(val id: String) : Parcelable
  
  val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()  
  ```
  > ⚠ You may use `Serializables` or primitives as arguments as well, but this is discouraged. Check Best Practices section for more
  details.
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
Refer to the official documentation for more
details: https://developer.android.com/reference/kotlin/androidx/compose/ui/window/DialogProperties.

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
    defaultArgument = EditPasscodeArgument(EditPasscodeMode.DEFINE) // default value defined for the whole graph
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
        defaultArgument = EditPasscodeArgument(EditPasscodeMode.DEFINE) // default value defined for the particular screen
    ) { arg -> // non-null argument
        Introdcution(mode = arg.mode)
    }
    ...
}
```

The following table presents all valid combinations of `destination` and `startDestination`:

| Graph's `destination`           | `defaultArgument` in `navigation` | `startDestination`              | `defaultArgument` in `composable` |
|---------------------------------|:---------------------------------:|---------------------------------|:---------------------------------:|
| DestinationWithoutArgument      |                 ➖                 | DestinationWithoutArgument      |                 ➖                 |
| DestinationWithoutArgument      |                 ➖                 | DestinationWithOptionalArgument |                 ➖                 |
| DestinationWithoutArgument      |                 ➖                 | DestinationWithOptionalArgument |                 ➖                 |
| DestinationWithoutArgument      |                 ➖                 | DestinationWithOptionalArgument |                 ✔                 |
| DestinationWithoutArgument      |                 ✔                 | DestinationWithRequiredArgument |                 ➖                 |
| DestinationWithOptionalArgument |                 ➖                 | DestinationWithoutArgument      |                 ➖                 |
| DestinationWithOptionalArgument |                 ➖                 | DestinationWithOptionalArgument |                 ➖                 |
| DestinationWithOptionalArgument |                 ✔                 | DestinationWithOptionalArgument |                 ➖                 |
| DestinationWithOptionalArgument |                 ➖                 | DestinationWithOptionalArgument |                 ✔                 |
| DestinationWithOptionalArgument |                 ✔                 | DestinationWithOptionalArgument |        ✔ (will be ignored)        |
| DestinationWithRequiredArgument |                 ➖                 | DestinationWithoutArgument      |                 ➖                 |
| DestinationWithRequiredArgument |                 ➖                 | DestinationWithOptionalArgument |                 ➖                 |
| DestinationWithRequiredArgument |                 ➖                 | DestinationWithOptionalArgument |        ✔ (will be ignored)        |
| DestinationWithRequiredArgument |                 ➖                 | DestinationWithRequiredArgument |                 ➖                 |

> ⚠ Note: The types of the `destination` and `startDestination` arguments must match!

> ℹ The signature of the `NavGraphBuilder.navigation` method enforces correct redirection to the `startDestination` - if you make a
> mistake it will be caught on a compile-time.

> ℹ Default value may be defined for the whole graph or for a particular `startDestination`. If you are experimenting with
> the `startDestination` a lot
> a the moment it may be easier to define the default value in `navigation` so you don't have to repeat it for each screen just because
> that screen might serve a role of a `startDestination` at some point.

## Displaying the nav-graph

You need to use `com.gft.destinations.NavHost` to display the nav-graph. This is just an extended version
of `androidx.navigation.compose.NavHost`
which supports `Destinations` in addition to `routes`.

```kotlin
val navController: NavHostController = rememberNavController()

NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = WelcomeScreenDestination
) {
    ...
    composable(WelcomeScreenDestination) { ... }
    ...
    navigation(...) { ... }
    dialog(...) { ... }
    ...
}

```

## Redirections

Usually screens/`Composables` use callbacks to request navigation, e.g.
```kotlin
@Composable
internal fun CardDetails(
    modifier: Modifier = Modifier,
    card: CardArgument,
    onNavigateToAccountDetails: () -> Unit,
    onNavigateToFreezeCard: (CardArgument) -> Unit,
    onNavigateToCancelCard: (CardArgument) -> Unit
)
```
Then you need to provide implementations of these callbacks, e.g.
```kotlin
composable(cardDetailsDestination) { card ->
    CardDetails(
        card = card,
        onNavigateToAccountDetails = onNavigateToAccountDetails,
        onNavigateToFreezeCard = {arg ->
            navController.navigate(FreezeCardSectionDestination, arg)
        },
        onNavigateToCancelCard = { arg ->
            navController.navigate(cancelCardDestination, arg)    
        }
    )
}
```
However, you may achieve the same result writing less code with the help of `redirect` methods:
```kotlin
composable(cardDetailsDestination) { card ->
    CardDetails(
        card = card,
        onNavigateToAccountDetails = onNavigateToAccountDetails,
        onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination),
        onNavigateToCancelCard = redirect(navController, cancelCardDestination)
    )
}
```

With the `redirect` methods you may also:
- ignore any incoming argument, e.g. when navigating to `DestinationWithoutArgument` from a callback that accepts an argument (`(T) -> Unit`)
- provide a default argument, e.g. when navigating from `() -> Unit` callback to `DestinationWithOptionalArgument<T>` or `DestinationWithRequiredArgument<T>`
- map an argument (e.g. when navigating from `(String) -> Unit` callback to `DestinationWithRequiredArgument<CardArgument>`)
- provide custom `NavOptions`

The table below presents all valid redirections:

| Callback       | Callback<br />argument | Destination type                     | Destination<br />argument | Usage                                                                                                                         |
|----------------|:----------------------:|--------------------------------------|:-------------------------:|-------------------------------------------------------------------------------------------------------------------------------|
| `() -> Unit`   |           -            | `DestinationWithoutArgument`         |             -             | `= redirect(navController, destination)`                                                                                      |
| `(T?) -> Unit` |           T?           | `DestinationWithoutArgument`         |             -             | `= redirectIgnoreArgument(navController, destination)`                                                                        |
| `(T) -> Unit`  |           T            | `DestinationWithoutArgument`         |             -             | `= redirectIgnoreArgument(navController, destination)`                                                                        |
| `() -> Unit`   |           -            | `DestinationWithOptionalArgument<T>` |       T (optional)        | `= redirectWithArgument(navController, destination, T?)`<br />e.g. `= redirectWithArgument(navController, destination, null)` |
| `(T?) -> Unit` |           T?           | `DestinationWithOptionalArgument<T>` |       T (optional)        | `= redirect(navController, destination)`                                                                                      |
| `(T) -> Unit`  |           T            | `DestinationWithOptionalArgument<T>` |       T (optional)        | `= redirect(navController, destination)`                                                                                      |
| `(T?) -> Unit` |           T?           | `DestinationWithOptionalArgument<U>` |       U (optional)        | `= redirect(navController, destination) { T? -> U? }`                                                                         |
| `(T) -> Unit`  |           T            | `DestinationWithOptionalArgument<U>` |       U (optional)        | `= redirect(navController, destination) { T -> U? }`                                                                          |
| `() -> Unit`   |           -            | `DestinationWithRequiredArgument<T>` |             T             | `= redirectWithArgument(navController, destination, T)`                                                                       |
| `(T?) -> Unit` |           T?           | `DestinationWithRequiredArgument<T>` |             T             | `= redirect(navController, destination) { T? -> T }`                                                                          |
| `(T) -> Unit`  |           T            | `DestinationWithRequiredArgument<T>` |             T             | `= redirect(navController, destination)`                                                                                      |
| `(T?) -> Unit` |           T            | `DestinationWithRequiredArgument<U>` |             U             | `= redirect(navController, destination) { T? -> U }`                                                                          |
| `(T) -> Unit`  |           T            | `DestinationWithRequiredArgument<U>` |             U             | `= redirect(navController, destination) { T -> U }`                                                                           |

If at some point you need another type of redirection it probably means that you would like to "hack a system", write a temporary code or try to test something.
Remember that you still got the option to NOT use `redirections` and implement any navigation callback directly (check the beginning of this section).

# Best practices

This section covers the whole process of constructing navigation in a typical application.
It starts with guidelines on defining arguments, screens, then describes process of creating application graph and ends with solutions
to some common problems.

Although examples presented in this section rely on `Destinations` the presented principles and advices apply to all projects based on
Compose.

## Arguments

### What should/should not be passed as argument?

- ⚠ You should never pass sensitive data as argument. Arguments are stored inside `Bundles` 
  which are saved in plain text on the permanent storage each time activity (but not application process) is killed by the system.
  <br />
  Username, card number, account number etc. are all sensitive data as they can be easily linked to a particular persons
  or constitute to an authorization data required to perform certain operations (e.g. payment). 
  <br />
  On the other hand a card identifier may not be a sensitive data (depending on the implementation of the whole ecosystem)
  as this information cannot be used to obtain authorization data.
- It is advised not to pass around complex data objects when navigating, 
  but instead pass the minimum necessary information, such as a unique identifier or other form of ID, 
  as arguments when performing navigation actions. Complex objects should be stored as data in a single source of truth, such as the data layer.
- Usually only the first screen in the flow/section should require arguments. Subsequent screens of the flow should rather obtain data 
  from `Session` started by the first screen. 

### Arguments of `Destinations`
- ✔ Most of the time you should choose an object implementing `Parcelable` interface as an argument of the `Destination`, e.g.
  ```kotlin
  @Parcelize
  data class CardArgument(val id: String) : Parcelable
  ```
- Using `Serializable` objects is a good choice only if you communicate with non-Android, non-Kotlin systems based on Java,
  however even then you should rather convert models to `Parcelables` once entering the `ui` part of the application.
- ⚠ Using primitives as arguments of `Destination` is generally discouraged as primitives are nameless
  and usually do not provide enough context when used as generic parameters. Consider the following example:
  ```kotlin
  val AccountDetailsScreen: DestinationWithRequiredArgument<String>
  ```
  Can you guess what should be passed as argument? Account number in IBAN format? Internal account identifier? Customer identifier?
  Compare this to:
  ```kotlin
  @Parcelize
  data class AccountArgument(val id: String) : Parcelable

  val AccountDetailsScreen: DestinationWithRequiredArgument<AccountArgument>
  ```
- You may reuse the same argument type across many destinations, e.g. probably many `Destinations` inside a feature module focused 
  on a payment cards management require a card identifier which can be provided with:
  ```kotlin
  @Parcelize
  data class CardArgument(val id: String) : Parcelable
  ```
  You may declare such argument in the main navigation file of the module or in a file shared by navigation and ui layer of the module
  if you want to reuse the same argument in composables.
- If you need to provide multiple arguments to a `Destination` you may simply use a `data class`:
  ```kotlin
  @Parcelize
  data class AccountArgument(
    val accountId: String,
    val accountName: String,
    val accountLast4Digits: String 
  ) : Parcelable
  ```

### Arguments of screens/`Composables`
- 99% of the time the argument of a screen/`Composable` should be passed to view model directly:
  ```kotlin
  @Composable
  fun CardDetails(
    cardId: String,
    viewModel: MviViewModel<CardDetailsViewState, ViewEvent, NavigationEffect, ViewEffect> = koinViewModel<CardDetailsViewModel> { parametersOf(cardId) }
  )
  ```
- In case of `Composables` (unlike the `Destinations`) using primitive types as arguments is perfectly fine as the name
  of the argument provides necessary context:
  ```kotlin
  @Composable
  internal fun CardDetails(
    cardId: String,
    ...
  )
  ```
- You may use the same argument type for both `Composables` and `Destinations` especially if they are defined in the same feature module:
  ```kotlin
  @Parcelize
  data class CardArgument(val id: String) : Parcelable  
  
  ...
  
  val CardDetailsSection: DestinationWithRequiredArgument<CardArgument>  
  
  ...
  
  @Composable
  internal fun CardDetails(
    card: CardArgument,
    viewModel: MviViewModel<CardDetailsViewState, ViewEvent, NavigationEffect, ViewEffect> = koinViewModel<CardDetailsViewModel> { parametersOf(card.id) }
    ...
  )
  ```
  This may also pay off during redirection - you won't have to wrap/unwrap primitives into/out of `Parcelable` each time you pass argument from a view to navigation layer and vice versa.