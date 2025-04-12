# Grocery List App

This repository contains the source code for a grocery list application built using modern Android development practices and technologies.

## Overview

This app allows users to:

* Add grocery items to a list.
* Store the grocery items locally using an SQLite database.

The project implements the following key Android concepts and libraries:

* **Room Persistence Library:** For providing an abstraction layer over SQLite, making database interactions easier and safer.
* **Model-View-ViewModel (MVVM):** A robust architectural pattern that separates the UI (View) from the data (Model) through a ViewModel, improving testability and maintainability.
* **Kotlin Coroutines:** For handling asynchronous operations in a concise and efficient way.
* **Koin:** A lightweight dependency injection framework for managing application dependencies.

## Project Structure

The project is structured following the MVVM pattern. Key directories and files you might find include:

* **`data/`:** Contains the data layer, including:
    * `database/`: Files related to the Room database (entities, DAOs, database instance).
    * `repository/`: Repository classes that abstract data access from different sources.
* **`ui/`:** Contains the UI layer (Activities, Fragments, Adapters) and their corresponding ViewModels.
* **`di/`:** Contains modules for Koin dependency injection.
* **`model/`:** Contains data classes representing the grocery items.
* **`viewmodel/`:** Contains the ViewModel classes that prepare and manage data for the UI.

## Technologies Used

* Android SDK
* Kotlin
* AndroidX Libraries
* Room Persistence Library
* ViewModel and LiveData (Android Architecture Components)
* Kotlin Coroutines
* Koin Dependency Injection

## Setup Instructions

To run this project on your local machine:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Gowtham4512l/grocery_list_app.git
    ```
2.  **Open the project in Android Studio.**
3.  **Build the project:** `Build` > `Make Project`
4.  **Run the application:** Select a target device (emulator or physical device) and click the `Run` button.
