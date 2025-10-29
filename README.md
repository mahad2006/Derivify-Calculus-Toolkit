<h1 align="center">Derivify: The Calculus Toolkit</h1>

<p align="center">
<a href="https://github.com/mahad2006/Derivify-Calculus-Toolkit/releases/latest"><img src="https://img.shields.io/github/v/release/mahad2006/Derivify-Calculus-Toolkit?style=for-the-badge&label=Latest%20Release"></a><a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge"></a><a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-UI-blue?style=for-the-badge&logo=android"></a><a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/Kotlin-100%25-orange?style=for-the-badge&logo=kotlin"></a>
</p>

<p align="center">
  A modern Android toolkit for mastering calculus — built with Kotlin, Jetpack Compose, and a custom symbolic math engine.
</p>

---

This project began as a final assignment for my second-semester **Multi-Variable Calculus** course. The requirement was to build something related to our curriculum, but I saw it as an opportunity to build a truly useful, high-quality tool for other students.

**Derivify** is the result: a native Android application designed to be an all-in-one toolkit for university-level calculus. It's not just a calculator; it's a learning companion built with a modern tech stack—100% Kotlin and Jetpack Compose.

## 📱 Application Showcase


| Calculator | Navigation Menu | Quiz Selection | Quiz in Progress |
| :---: | :---: | :---: | :---: |
| <img src="screenshots/calculator.png" alt="Calculator Screen" width="200"/> | <img src="screenshots/navigation.png" alt="Navigation Drawer" width="200"/> | <img src="screenshots/quiz_difficulty.png" alt="Quiz Difficulty Screen" width="200"/> | <img src="screenshots/quiz_question.png" alt="Quiz Question Screen" width="200"/> |

## Features

The app is built around two core ideas: providing powerful tools for solving problems and offering an interactive way to practice and test your knowledge.

### The Calculators
Derivify can handle a range of complex multi-variable calculus operations:

* **Partial Derivatives:** Solves partial derivatives for functions with multiple variables.
* **Gradients:** Instantly finds the gradient vector `∇f` of a function.
* **Directional Derivatives:** Calculates the rate of change in the direction of a given vector.

### The Calculus Quiz
To move beyond simple calculation, I built an interactive quiz feature with a large, university-level question bank. It’s designed to genuinely test your understanding, not just your memorization.

It features four difficulty levels:

* **Easy:** A review of foundational rules (Power Rule, Product Rule, etc.).
* **Medium:** A mix of chain rules, implicit differentiation, and inverse trig.
* **Hard:** Multi-variable topics like partial derivatives and gradients.
* **Nightmare:** A true challenge — mixed partials, directional derivatives, and more.

## 🏗️ How It's Built

This project was an exercise in building a modern, robust Android application from the ground up.

* **Language:** 100% **Kotlin**
* **User Interface:** **Jetpack Compose** with **Material 3** for a fully declarative, modern UI
* **Architecture:** **MVVM (Model-View-ViewModel)** for a clean separation between UI and logic
* **State Management:** Using `ViewModels` and `MutableState` for efficient UI reactivity

### Key Technical Feature: Custom Math Engine
The core of this application is a **custom-built symbolic differentiation engine**. Instead of relying on external libraries, this engine:
1.  **Parses** user-input functions into an Abstract Syntax Tree (AST).
2.  **Recursively applies** calculus rules (Power, Product, Chain Rule, etc.) to the tree.
3.  **Generates** an exact, analytical derivative, not a numerical approximation.

### Built With
<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Material%203-673AB7?style=for-the-badge&logo=materialdesign&logoColor=white"/>
  <img src="https://img.shields.io/badge/MVVM-FF6F00?style=for-the-badge&logo=architecture&logoColor=white"/>
</p>

## 📲 Download & Installation

The latest installable `.apk` file is available on the **Releases** page.

1.  Navigate to the [**Latest Release**](https://github.com/mahad2006/Derivify-Calculus-Toolkit/releases/latest)
2.  Under **Assets**, download the `app-release.apk` file
3.  Open it on your Android device (enable *Install from Unknown Sources* if prompted)

## 🛠️ Building from Source

Interested in building the app yourself or contributing?

1.  Clone the repository:
    ```bash
    git clone [https://github.com/mahad2006/Derivify-Calculus-Toolkit.git](https://github.com/mahad2006/Derivify-Calculus-Toolkit.git)
    ```
2.  Open the project in Android Studio (Hedgehog or newer).
3.  Let Gradle sync and build the project.
4.  Run on an emulator or a physical device.

## 🤝 Contributing

Contributions are welcome! If you have a feature request, bug report, or want to contribute code, please feel free to:
1.  Open an [issue](https://github.com/mahad2006/Derivify-Calculus-Toolkit/issues) to discuss the change.
2.  Fork the repository and create a new branch.
3.  Submit a pull request with your changes.

## About Me

My name is **Mahad** (`codewithmahad`), and I’m a software developer and student passionate about building practical, well-designed applications.
This project was a joy to build — combining my academic studies with my passion for mobile development.

<p align="center">
<a href="https://www.linkedin.com/in/codewithmahad"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/></a>
<a href="https://github.com/mahad2006"><img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white"/></a>
</p>

## 📜 License

This project is licensed under the **MIT License**.

---

<p align="center">
  <b>Developed with ❤️ by <a href="https://www.linkedin.com/in/codewithmahad">Shaikh Mahad</a></b> <br>
  <sub>© 2025 Derivify — All rights reserved.</sub>
</p>
