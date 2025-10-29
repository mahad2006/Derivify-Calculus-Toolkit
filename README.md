
<h1 align="center">Derivify: The Calculus Toolkit</h1>

<p align="center">
  <a href="https://github.com/mahad2006/Derivify-Calculus-Toolkit/releases/latest">
    <img src="https://img.shields.io/github/v/release/mahad2006/Derivify-Calculus-Toolkit?style=for-the-badge&label=Latest%20Release">
  </a>
  <a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge">
  </a>
  <a href="https://developer.android.com/jetpack/compose">
    <img src="https://img.shields.io/badge/Jetpack%20Compose-UI-blue?style=for-the-badge&logo=android">
  </a>
  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-100%25-orange?style=for-the-badge&logo=kotlin">
  </a>
</p>

<p align="center">
  A modern Android toolkit for mastering calculus — built with Kotlin, Jetpack Compose, and a custom math engine.
</p>

---

This project began as a final assignment for my second-semester **Multi-Variable Calculus** course. The requirement was to build something related to our curriculum, but I saw it as an opportunity to build a truly useful, high-quality tool for other students.

**Derivify** is the result: a native Android application designed to be an all-in-one toolkit for university-level calculus.  
It's not just a calculator; it's a learning companion built with a modern tech stack—100% Kotlin and Jetpack Compose.

<br>
<hr>
<br>

## What It Does

The app is built around two core ideas: providing powerful tools for solving problems and offering an interactive way to practice and test your knowledge.

### The Calculators
Derivify can handle a range of complex multi-variable calculus operations:

* **Partial Derivatives:** Solves partial derivatives for functions with multiple variables.  
* **Gradients:** Instantly finds the gradient vector `∇f` of a function.  
* **Directional Derivatives:** Calculates the rate of change in the direction of a given vector.  

### The Calculus Quiz
To move beyond simple calculation, I built an interactive quiz feature with a large, university-level question bank.  
It’s designed to genuinely test your understanding, not just your memorization.

It features four difficulty levels:

* **Easy:** A review of foundational rules (Power Rule, Product Rule, etc.).  
* **Medium:** A mix of chain rules, implicit differentiation, and inverse trig.  
* **Hard:** Multi-variable topics like partial derivatives and gradients.  
* **Nightmare:** A true challenge — mixed partials, directional derivatives, and more.  

<br>
<hr>
<br>

## Application Screenshots

| Calculator | Navigation Menu | Quiz Selection | Quiz in Progress |
| :---: | :---: | :---: | :---: |
| <img src="screenshots/calculator.png" alt="Calculator Screen" width="200"/> | <img src="screenshots/navigation.png" alt="Navigation Drawer" width="200"/> | <img src="screenshots/quiz_difficulty.png" alt="Quiz Difficulty Screen" width="200"/> | <img src="screenshots/quiz_question.png" alt="Quiz Question Screen" width="200"/> |

<br>
<hr>
<br>

## 🏗️ How It's Built

This project was an exercise in building a modern, robust Android application from the ground up.

* **Language:** 100% **Kotlin**  
* **User Interface:** **Jetpack Compose** with **Material 3** for a fully declarative, modern UI  
* **Architecture:** **MVVM (Model-View-ViewModel)** for a clean separation between UI and logic  
* **State Management:** Using `ViewModels` and `MutableState` for efficient UI reactivity  
* **Math Engine:** Custom-built symbolic differentiation engine — parses user functions into expression trees and recursively applies calculus rules (Power, Product, Chain Rule) for exact analytical results  

<br>

### Built With

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Material%203-673AB7?style=for-the-badge&logo=materialdesign&logoColor=white"/>
  <img src="https://img.shields.io/badge/MVVM-FF6F00?style=for-the-badge&logo=architecture&logoColor=white"/>
</p>
<br>
<hr>
<br>

## Download & Installation

The latest installable `.apk` file is available on the **Releases** page.

1. Navigate to the [**Latest Release**](https://github.com/mahad2006/Derivify-Calculus-Toolkit/releases/latest)  
2. Under **Assets**, download the `app-release.apk` file  
3. Open it on your Android device (enable *Install from Unknown Sources* if prompted)  

<br>
<hr>
<br>

## What's Next: Future Roadmap

Derivify is still in active development.  
Next steps include expanding the toolkit with more advanced calculus utilities:

* **New Calculators:**
  * Maxima & Minima (Second Partial Derivative Test)
  * Linear Approximation & Tangent Planes
  * Vector Field Visualization
* **Step-by-Step Solutions:** Show *how* the derivative was calculated  
* **3D Graph Plotting:** Visualize the analyzed functions  

<br>
<hr>
<br>
## About Me

My name is **Mahad** (`codewithmahad`), and I’m a software developer and student passionate about building practical, well-designed applications.  
This project was a joy to build — combining my academic studies with my passion for mobile development.

* 🌐 **LinkedIn:** [www.linkedin.com/in/codewithmahad](https://www.linkedin.com/in/codewithmahad)
* 🧰 **GitHub:** [mahad2006](https://github.com/mahad2006)
<br>
<hr>
<br>

## 📜 License

This project is licensed under the **MIT License**.

---

<p align="center">
  <b>Developed with ❤️ by <a href="https://www.linkedin.com/in/codewithmahad">Shaikh Mahad</a></b> <br>
  <sub>© 2025 Derivify — All rights reserved.</sub>
</p>

