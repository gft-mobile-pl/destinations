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

| Callback type | Destination type            | Arg | Usage                                                                                                                          |
|---------------|-----------------------------|:---:|--------------------------------------------------------------------------------------------------------------------------------|
| () -> Unit    | `withoutArgument()`         |  -  | `redirect(navController, destination)`                                                                                         |
| (T?) -> Unit  | `withoutArgument()`         |  -  | `redirectIgnoreArgument(navController, destination)`                                                                           |
| (T) -> Unit   | `withoutArgument()`         |  -  | `redirectIgnoreArgument(navController, destination)`                                                                           |
| () -> Unit    | `withOptionalArgument<T>()` | T?  | `redirectWithArgument(navController, destination, T?)`<br />e.g.<br />`redirectWithArgument(navController, destination, null)` |
| (T?) -> Unit  | `withOptionalArgument<T>()` | T?  | `redirect(navController, destination)`                                                                                         |
| (T) -> Unit   | `withOptionalArgument<T>()` | T?  | `redirect(navController, destination)`                                                                                         |
| (T?) -> Unit  | `withOptionalArgument<U>()` | U?  | `redirect(navController, destination) { T? -> U? }`                                                                            |
| (T) -> Unit   | `withOptionalArgument<U>()` | U?  | `redirect(navController, destination) { T -> U? }`                                                                             |
| () -> Unit    | `withArgument<T>()`         |  T  | `redirectWithArgument(navController, destination, T)`                                                                          |
| (T?) -> Unit  | `withArgument<T>()`         |  T  | `redirect(navController, destination) { T? -> T }`                                                                             |
| (T) -> Unit   | `withArgument<T>()`         |  T  | `redirect(navController, destination)`                                                                                         |
| (T?) -> Unit  | `withArgument<U>()`         |  U  | `redirect(navController, destination) { T? -> U }`                                                                             |
| (T) -> Unit   | `withArgument<U>()`         |  U  | `redirect(navController, destination) { T -> U }`                                                                              |

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
- In case of `Composables` (unlike the `Destinations`) using primitive types as arguments is fine as the name
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

## Screens

**Screen** is any view which is constructed and displayed directly by the `NavHost`. It could be a `@Composable` (in Compose graph) or a `Fragment` (in xml based graph).
In the context of **'Destinations library'** the screen is the view of a `Destination`.

In `Compose`:
- Navigation should be requested with callbacks:
  ```kotlin
  @Composable
  internal fun CardDetailsScreen(
      modifier: Modifier = Modifier,
      card: CardArgument,
      onNavigateToAccountDetails: () -> Unit,
      onNavigateToFreezeCard: (CardArgument) -> Unit,
      onNavigateToCancelCard: (CardArgument) -> Unit
  )
  ```
  - ⚠ Avoid using primitive types as arguments of the navigation callbacks. Lambdas' arguments have no name and may be ambiguous for other developers.
  - The name of the callback should suggest the target of the navigation.
  - Generally each callback should handle navigation to a single section or screen. However, there **might** be one exception to this rule - check the **Conditional navigation** chapter for more details.
- Although primitive types can be used for screens' arguments, the `Parcelables` are still considered a better choice.
  Reusing the same types of arguments as in the navigation layer may spare you arguments conversion.
- `Destinations` should never be used as navigation callbacks' arguments. Screens should not be aware which navigation library is used.
- You must never invoke `navController.navigate` from within the `Screen`.
- Generally you should never pass the `NavController` or `NavHostController` to any `Screen`. Even if you use `Scaffold` 
  with internal `NavHost` it is possible to extract `NavController` to navigation layer completely. 
  Check the `HomeScreen` in the sample project for an example. The `Scaffold` section of this README covers more details as well.
- You may pass (and probably have to pass) the `NavController` to view factories (check the `WidgetsFactory` in the sample project).
- Most of the screens should be declared as `internal`. Other modules should deal with **Sections** rather than with the screens.
  
## Sections

**Section** is a mixed set of **Screens** (sometimes only one) and navigation graphs which are logically connected. 
In technical terms it is just an extension method of `NavGraphBuilder` which adds composables, dialogs, graphs and other sections to the graph.

At first sections may remind sub-graphs, but:
- Sections add items to the graph, but unlike sub-graphs don't encapsulate them. 
  Nevertheless a section may encapsulate any number of `Destinations` which makes referenced screens unreachable from the outside of the section.  
- Even though sections are called in a hierarchical manner during graph creation, 
  the added items (screens, graphs) may be placed on the same level of the graph.
- Section may not have a `Destination` assigned - this usually means that this section's only role is just adding "public" items to the graph.
- Section is just a graph builder. There is no notion of a section once the graph is built. 
  Whenever you navigate to a `Destination` which points to a section you navigate to some screen or graph added by the section.

Distributing the screens and graphs across the sections is not a trivial tasks. 
This is usually a good idea to start designing sections on the feature-module level.

### Module level sections

Creating sections on a module level is a multi-step process. First, try to identify standalone journeys (interaction flows) which do not depend on other journeys.
If there are no such journeys choose the one with the least amount of dependencies and leave some `TODO()`.
Once the first pack of journeys is complete, search for the journeys which depend on the journeys already created.
This is also the time you replaces `TODO()s` with references.
In the end create a special section which adds all the public sections of the module.

#### Journey sections

Typical standalone journey's section may look like this:
```kotlin
fun NavGraphBuilder.cancelCardSection(
  navController: NavController,
  onNavigateToNextAfterCardCancelled: () -> Unit,
  sectionDestination: DestinationWithRequiredArgument<CardArgument>
) {
  val cancelCardWarningDestination = Destination.withArgument<CardArgument>()
  val cancelCardConfirmationDestination = Destination.withArgument<CardArgument>()

  navigation(
    destination = sectionDestination,
    startDestination = cancelCardWarningDestination
  ) {
    composable(cancelCardWarningDestination) { arg ->
      CardCancellationWarning(
        card = arg,
        onNavigateToConfirmation = redirect(
          navController,
          cancelCardConfirmationDestination
        ) // You should rather use Session instead of re-passing card argument, but this example focuses on navigation only.
      )
    }

    composable(cancelCardConfirmationDestination) { arg ->
      CardCancellationConfirmation(
        card = arg,
        onNavigateToNextAfterCardCancelled = onNavigateToNextAfterCardCancelled,
        onNavigateToNextAfterCardCancellationAborted = { navController.popBackStack(sectionDestination, true) },
      )
    }
  }
}
```

- The visibility of the section should be:
  - `public` if the section is supposed to be **directly embedded** within sections defined in other module.
    > 💡 A section is **directly embedded** when it is a direct (first-level) child of other section. Section is **embedded indirectly**
         when it is a child of other section that is embedded.
  - `internal` if the section is supposed to be **embedded** only within the sections defined in the same module.
    > 💡 It still possible to navigate to `internal` section from a different module if a `public` `Destination` is assigned to it in the enclosing graph.
  - `private` if the section is supposed to be embedded only within the sections defined in the same file.
- You should not assign any specific `Destiantion` while declaring a session - you should expose a `sectionDestination` parameter instead.
  Thanks to this you will be able to embed such session privately into another section (this will be explained in more details in the next chapter).  
- Most of the time a section should not expose the screens/sub-sections it embeds. It is enough to use locally defined  `Destinations` while embedding screens/sub-sections.
- Section may (but doesn't have to!) embrace all the screens/sub-sections within a single `navigation` graph. It has several advantages:
  - If you use any utility which prints the current navigation graph you will a get nice hierarchical representation of a navigation
    instead of flat list.
  - You may use any `Destination` you want for the first screen of the session. The `sectionDestination` will be assigned to the root `navigation` item.
    If you omit the `navigation` graph you would have to assign the `sectionDestination` to the first screen of the section.
  - You may easily experiment with a first screen of the section using the `startDestination` parameter instead of reassigning `Destinations` of the screens' back and forth.  
- The use of `NavController` within the section should be limited to three types of actions only:
  - Navigating to **screens/sub-sections** that are **embedded directly** in this section.
  - Navigating to other **sections** (not screens, not graphs) which are defined within the same feature module.
  - Collapsing the session when the journey is terminated (e.g. completed, aborted)
    > 💡 You may collapse a section with `navController.popBackStack(sectionDestination, true)`.
- You should use callbacks to request navigation to sections defined in other modules.
- There are two ways the journey may end:
  - the whole section collapses (this is generally the most common case),
  - a navigation callback is requested, e.g. check `onNavigateToNextAfterCardCancelled: () -> Unit`.

#### Embedding section within other section

Sometimes you need to control where a user is brought to after some journey is terminated, e.g.
1. User is on **Card details** screen.
2. User clicks **Cancel card** button.
3. A **Cancel card** journey (section) is started.
4. Journey completes successfully and card is cancelled.

At this point we need to remove the **Cancel card** journey from the backstack for sure, 
but at the same time we cannot go back to the **Card details** screen as the card is no longer valid. 
We should rather collapse the **Card details** section as well 
and show any screen that was shown before the user entered "**Card details**" section.

In order to implement such navigation both **Cancel card** journey and **Card details** section need to "cooperate":
1. **Cancel card** need to expose a callback invoked when card is cancelled:
    ```kotlin
    fun NavGraphBuilder.cancelCardSection(
        navController: NavController,
        onNavigateToNextAfterCardCancelled: () -> Unit, // <--- Callback invoked when a card is cancelled
        sectionDestination: DestinationWithRequiredArgument<CardArgument>
    ) { ... }
    ```
2. **Card details** section need to handle the callback:
    ```kotlin
    internal fun NavGraphBuilder.cardDetailsSection(
        navController: NavController,
        sectionDestination: DestinationWithRequiredArgument<CardArgument>,
        onNavigateToAccountDetails: () -> Unit
    ) {
        val cardDetailsDestination = Destination.withArgument<CardArgument>()
        val cancelCardDestination = Destination.withArgument<CardArgument>()
    
        navigation(
            destination = sectionDestination,
            startDestination = cardDetailsDestination
        ) {
            composable(cardDetailsDestination) { card ->
                CardDetails(
                    card = card,
                    onNavigateToAccountDetails = onNavigateToAccountDetails,
                    onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination),
                    onNavigateToCancelCard = redirect(navController, cancelCardDestination)
                )
            }
    
            cancelCardSection( // <--- embedded section
                navController = navController,
                onNavigateToNextAfterCardCancelled = { 
                    navController.popBackStack(destination = sectionDestination, inclusive = true) // <--- Collapse the 'Card details' section
                },
                sectionDestination = cancelCardDestination
            )
        }
    }
    ```
> 💡 Note that we don't have to handle a case when a card cancelling procedure is aborted - the "Cancel card" journey may simply collapse when this happens. 

#### Grouping module level sections together 

A good practice is to create a single **grouping method** which embeds all the sections defined within the module:

```kotlin
internal val CardsSummarySectionDestination = Destination.withoutArgument()
internal val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()
internal val FreezeCardSectionDestination = Destination.withArgument<CardArgument>()

fun NavGraphBuilder.cardFeatureSections(
    navController: NavController,
    onNavigateToAccountSummary: () -> Unit, // example of cross-feature navigation
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    cardsSummarySection(navController, CardsSummarySectionDestination, onNavigateToAccountSummary, onNavigateToAccountDetails)
    cardDetailsSection(navController, CardDetailsSectionDestination, onNavigateToAccountDetails)
    freezeCardSection(navController, FreezeCardSectionDestination)
}
```

- Such method serves several purposes:
  - It adds both `public` and `internal` sections to the graph.  
  - It assigns `Destination` to each added section.
  - It makes the construction of the application's graph simpler - it is enough for the main application module to call this single
  method to embed all the sections provided by the feature module.
  - Gathers callbacks used for cross-module navigation. 
- Consider the correct visibility of the `Destination` assigned to each section:
  - Use `public` if other modules may request navigation to the given section. 
    > 💡 It is always the app module that wires cross-module navigation. Each feature module may only request a navigation with the use of callback. Feature modules should never have access to `Destinations` defined by other feature modules.
  - Use `internal` if only the sections defined within the same module may navigate to the given section.
  - Use `private` if only the sections defined within the same file navigate to the given section.
  
> ⚠ <u>Not all sections defined in the module should be embedded by the grouping method</u>.<br /> 
> Some sections are supposed to be always embedded within a local context and you can't navigate to them
> using globally accessible `Destination`. An example would be `cancelCardSection` presented in the previous chapter.<br/>
> 
> Usually it is easy to spot such section as most of them provide a callback which is invoked when such 
> section is terminated and it is impossible to provide implementation of this callback in the **grouping method**.
> The other clue would be if a section is embedded many times. 
  

#### Meta sections

A section that embeds other sections, but not screens, graphs etc. is called **meta section**.
Such sections help to keep the structure of the navigation graph more readable 
by encapsulating sections used only in some part of the application. 
In fact the **grouping methods** described in the previous chapter are **meta sections**.

A good example of a meta section is `LoggedInSection` that you may find in the sample app:
```kotlin
fun NavGraphBuilder.loggedInSection(
    onNavigateToNextAfterLogout: () -> Unit,
    navController: NavHostController
) {
    navigation(
        destination = LoggedInSectionDestination,
        startDestination = HomeScreenDestination
    ) {
        homeScreenSection(
            sectionDestination = HomeScreenDestination,
            onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination),
            onNavigateBack = redirect(navController, LogoutPromptSectionDestination),

            // This callback is added just to demonstrate a very rare case of unnamed/context-less navigation.
            // (Generally such navigation should be avoided).
            onNavigationRequest = { request ->
                when (request) {
                    NavigateToAccountDetailsRequest -> navController.navigate(AccountDetailsDestination)
                }
            },

            navController = navController
        )

        accountFeatureSections(navController)

        cardFeatureSections(
            onNavigateToAccountSummary = redirect(navController, AccountSummaryDestination),
            onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination),
            navController = navController
        )

        logoutPromptSection(
            onNavigateToNextAfterLogout = onNavigateToNextAfterLogout,
            navController = navController
        )
    }
}
```
> ⚠ Every rule that applies to normal **sections** applies to **meta sections** as well.<br />
> 
> Nevertheless <u>you should refrain from navigating to sections that are not direct or indirect
> children of the current **meta section**</u>. You should use callbacks instead, e.g. examine the `onNavigateToNextAfterLogout` callback in the example above.

The top-most **meta section** is the `NavHost` declaration, e.g.
```kotlin
    val welcomeScreenDestination = Destination.withoutArgument()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = welcomeScreenDestination
    ) {
        composable(welcomeScreenDestination) {
            WelcomeScreen(
                onNavigateToNext = redirect(navController, LoginSectionDestination)
            )
        }

        loginSection(
            onNavigateToNextAfterSuccessfulLogin = redirect(navController, LoggedInSectionDestination, navOptions {
                popUpTo(welcomeScreenDestination.id) { inclusive = false }
            }),
            navController = navController
        )

        loggedInSection(
            onNavigateToNextAfterLogout = {
                navController.popBackStack(destination = welcomeScreenDestination, inclusive = false)
            },
            navController = navController
        )
    }
```

## Conditional navigation and pseudo-sections

Whenever a view requests navigation it simply invokes an appropriate callback exposed in its `@Composable` method.
The callback is implemented on the navigation layer where it maps the request to a `Destination`. 
The most important thing is that each callback handles navigation to one screen only.

However, this approach might be quite inconvenient in one very specific case. Imagine the following application's structure:
- There is a `LoggedInSection` which wires almost all other sections of the application.
- The first section of the `LoggedInSection` is the `HomeScreenSection`
- The first screen of the `HomeScreenSection` is `HomeScreen`
- `HomeScreen` contains its own `NavHost` to display tabs. There are 3 tabs:
  - `WidgetsScreen` which is dashboard that displays dynamic set of widgets.
  - `AccountSummaryScreen`
  - `PaymentCardsSummaryScreen`
- The set of widgets displayed on the `WidgetsScreen` is resolved dynamically based on many rules.
  Let's say the pool of the available widgets is 20 in size and we usually display 5 widgets at the time.
  Once the set of widgets is established the `WidgetsFactory` is used to create the views of the widgets.
  Each widget is defined in a different feature module and contains a few buttons. 
  Each button requests navigation to a different section defined in the same feature module as the widget.  

If we use the standard approach to navigation we would end up with something like this:
- Each widget exposes a few callback methods in order to request navigation.
- `WidgetsFactory` signature has to expose **a few dozens** of callbacks 
  as it has to be able to pass all the navigation requests from all the widgets (even if they are not displayed!).
- `HomeScreen` signature has to expose **a few dozens** of callbacks - it has to be able to pass all the navigation requests from `WidgetsFactory`.
- `HomeScreenSection` signature has to expose **a few dozens** of callbacks - it has to be able to pass all the navigation requests from `HomeScreen`.

> 💡 In fact you may shorten the list of intermediate screens/sections if you inject `WidgetsScreen` into the `HomeScreen`. 
> You may check the sample app for a complete solution.

You may guess that managing such `@Composables` tree would be a nightmare:
- a signature of each `@Composable` would take a few dozens of lines,
- adding/removing a widget would end up in changing many `@Composables`
- adding/removing a button to/from a widget would end up in changing many `@Composables`.
This problem is known as `property drilling` and in this case it is further exaggerated by the number of callbacks. 

There are at least two solutions to this problem:
- conditional navigation,
- pseudo-sections.

### Conditional navigation
> ⚠ Using conditional navigation should be limited to the very specific cases when standard navigation approach is <u>extremely</u> inconvenient.

1. Define navigation request parameter, e.g.
   ```kotlin
   data class NavigateToCardDetailsRequest(val card: CardArgument)
   ```
2. Expose navigation request in the view:
   ```kotlin
   @Composable
   fun CardsFeatureWidget(
     navController: NavController,
     onNavigateToCardDetails: (NavigateToCardDetailsRequest) -> Unit
   ) { ... }
   ```
3. Redirect all navigation requests to a single untyped navigation request on the higher level:
   ```kotlin
   object WidgetsFactory {
     @Composable
     fun CreateWidgets(
       navController: NavController,
       onNavigationRequest: (Any) -> Unit // <--- untyped navigation request
     ) {
       ...
       CardsFeatureWidget(navController = navController, onNavigateToCardDetails = onNavigationRequest) // <--- redirection to untyped navigation request
       ...
     }
   }
   ```
4. Pass the untyped navigation requests higher and higher in the composables tree:
   ```kotlin
   @Composable
   fun WidgetsScreen(
     onNavigationRequest: (Any) -> Unit,
     navController: NavController,
     modifier: Modifier = Modifier
   ) {
   
     ... 
   
     WidgetsFactory.CreateWidgets(
       navController = navController,
       onNavigationRequest = onNavigationRequest
     )
   }
   
   etc.
   ```
5. Handle navigation requests on the navigation layer:
   ```kotlin
   onNavigationRequest = { request ->
     when (request) {
       ...
       is NavigateToCardDetailsRequest -> navController.navigate(CardDetailsSectionDestination, request.card)
       ...   
     } 
   }
   ```

### Pseudo-sections

Some feature modules provide views (`@Composables`) that can be embedded directly into other views instead 
of being added to the navigation graph as screens. A good example of such views are widgets.

Usually these views request navigation only to the `Destinations` which are defined it the same module as the views are.
There is a possibility to satisfy these navigation requests by wrapping the views in the **pseudo-sections**. 
This special type of sections is intended to be embedded directly into views rather than into navigation graph.

```kotlin
@Composable
fun CardsFeatureWidget( // <--- pseudo section!
    navController: NavController
) {
    CardsFeatureWidget(
        onNavigateToCardDetails = redirect(navController, CardDetailsSectionDestination), // <--- same feature navigation request
    )
}
```
> 💡 Note that you should not add the `Section` suffix to the name of the pseudo section.<br />
> From the perspective of another modules it should be as indistinguishable from a "normal" view as possible.

As you can see the pseudo-section satisfies all the navigation request of the `CardsFeatureWidget` view. 
What is more, it can even navigate to `internal` `Destinations` (e.g. `CardDetailsSectionDestination`) as it is defined
in the same module as target screens.

Because pseudo-section is almost indistinguishable from a "normal" view it can be embedded directly into another view:
```kotlin
@Composable
fun SomeContainer(
    navController: NavController
) {
    ...
    CardsFeatureWidget(navController = navController)
    ...
}
```
> 💡 This is a rare case when you need to pass the `NavController` to view. 
> You may use dependency injection to remove the `navController` from the signature.

### Should I use 'Conditional navigation' or 'Pseudo-sections'?

- Use the **pseudo-section** if the view is defined in the same feature-module as the screens the view request navigation to.
- Use the **conditional navigation** if the view requests navigation to the screens defined in the different modules than the view.

You may use both **pseudo-section** and **conditional navigation** if it is required - check the `CardsFeatureWidget` in the sample app.

## Scaffold 

Please examine the sample application to find out how to use `Scaffold` properly with `Destinations`.
Pay special attention to `HomeScreen.kt` and `HomeScreenSection.kt`. 
You may note that:
- there is no need to pass `NavCotroller` to `Scaffold`
- `Scaffold` may but doesn't have to use `NavHost` (although it is very common)
- **all** the navigation code could be extracted from the `Scaffold` to the navigation layer.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedSection: State<HomeScreenSection>,
    onSectionSelected: (HomeScreenSection) -> Unit,
    sectionsNavHostBuilder: @Composable (modifier: Modifier) -> Unit
) {
    Scaffold(modifier = modifier, bottomBar = {
        NavigationBar(
            modifier = Modifier
                .height(72.dp)
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
        ) {
            HomeScreenSection.values().forEach { section ->
                NavigationBarItem(
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(section.icon, contentDescription = "")
                            Text(text = section.label) // You could use `label` property of NavigationBarItem but the selection marker embraces the icon only (!)
                        }
                    },
                    selected = selectedSection.value == section,
                    onClick = { onSectionSelected(section) }
                )
            }
        }
    }) { innerPadding ->
        sectionsNavHostBuilder(Modifier.padding(innerPadding))
    }
}
```



