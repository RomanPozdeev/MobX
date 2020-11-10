[![Build Status](https://travis-ci.com/RomanPozdeev/MobX.svg?branch=master)](https://travis-ci.com/RomanPozdeev/MobX.svg?branch=master)
[![codecov](https://codecov.io/gh/RomanPozdeev/MobX/branch/master/graph/badge.svg)](https://codecov.io/gh/RomanPozdeev/MobX)
[![](https://jitpack.io/v/RomanPozdeev/MobX.svg)](https://jitpack.io/#RomanPozdeev/MobX)

# MobX for Kotlin

## Core concepts of MobX

### Observable
Observable is a mutable value that notifies about its changes.
```kotlin
class Task(val id: Int, title: String, description: String, done: Boolean = false) {
    var title by observable(title)

    var description by observable(description)

    var done by observable(done)
}
```

### Computed
Computed is a read-only value that is automatically kept in sync with other observables and computed.
Just like an observable, computed value notifies about its changes.
```kotlin
    val count by computed { todoList.size }
    val pending by computed { todoList.count { !it.done } }
    val done by computed { count - pending }
```

### Autorun
Autorun runs a reaction when its dependencies (observables and computed) are changed.
```kotlin
val disposable = autorun {
    containerView.title.text = task.title
    containerView.description.text = task.description
}
```
In this example logging runs immediately and then on any change of task title or task description. There are no needs to subscribe to these observables manually! Another example of reaction is UI updating.

### Action
Actions allow to mutate observables. Actions batch mutations, so a notifications will occur **only after an action has finished**.
```kotlin
fun clear() = action {
    todoList.clear()
}

or

fun clear() = action("clearAllTasks") {
    todoList.clear()
}
```
A string argument of action is a payload. It can be used for logging.

### Usage:
Please check out [app](app)

or

Add it in your root build.gradle at the end of repositories:

Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.RomanPozdeev:MobX:0.1'
}
```
## More
* https://mobx.js.org/the-gist-of-mobx.html - original MobX documentation
* https://github.com/mobxjs/mobx.dart - MobX for Dart/Flutter
* https://www.packtpub.com/web-development/mobx-quick-start-guide
* https://hackernoon.com/becoming-fully-reactive-an-in-depth-explanation-of-mobservable-55995262a254 - an in-depth explanation of MobX
