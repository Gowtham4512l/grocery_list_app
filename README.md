# Grocery List App

## App Demo
https://github.com/user-attachments/assets/c37c8c96-4afb-485a-907a-2248e66b8556

This repository contains the source code for a grocery list application built using Android development practices and technologies.

## Overview

This app allows users to:

* Add grocery items to a list.
* Update quantities with plus/minus controls
* Delete items from the list
* Sort items by name or quantity
* Filter items with search functionality
* Choose between light, dark, or system theme

The project implements the following key Android concepts and libraries:

* **Room Persistence Library:** For providing an abstraction layer over SQLite, making database interactions easier and safer.
* **Model-View-ViewModel (MVVM):** A robust architectural pattern that separates the UI (View) from the data (Model) through a ViewModel, improving testability and maintainability.
* **Kotlin Coroutines:** For handling asynchronous operations in a concise and efficient way.
* **LiveData:** For building data objects that notify views when the underlying database changes.
* **Edge-to-Edge Display:** Proper handling of system insets and camera cutouts.

## Project Structure

The project is structured following the MVVM pattern. Key directories and files you might find include:

* **`data/`:** Contains the data layer, including:
    * `db/`: Files related to the Room database (entities, DAOs, database instance).
    * `repository/`: Repository classes that abstract data access.
* **`ui/`:** Contains the UI layer:
    * `shoppingList/`: Shopping activity, adapter, and related files.
    * `splash/`: Splash screen activity.
* **`utils/`:** Contains utility classes like theme management.

## Technologies Used

* Android SDK
* Kotlin
* AndroidX Libraries
* Room Persistence Library
* ViewModel and LiveData (Android Architecture Components)
* Kotlin Coroutines
* Material Design Components

## Setup Instructions

To run this project on your local machine:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Gowtham4512l/grocery_list_app.git
    ```
2.  **Open the project in Android Studio.**
3.  **Build the project:** `Build` > `Make Project`
4.  **Run the application:** Select a target device (emulator or physical device) and click the `Run` button.

## Features
* Local database storage with Room
* Sort by name (A-Z or Z-A)
* Sort by quantity (low-high or high-low)
* Search functionality
* Theme switching (light/dark/system)
* Material Design UI components
* Edge-to-edge display support
