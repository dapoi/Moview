# Moview
This app made for demonstration latest technology with jetpack compose

## Highlights
- **Jetpack Compose UI** - Fully built using Jetpack Compose for a modern UI experience.
- **Modularization** - Separation of concerns with a structured module-based architecture.
- **Best Practices** - Implements MVVM, Clean Architecture, and other industry standards.

## Project Structure
```
project-root/
├── app/                # Main application module
├── core/common/        # Common utilities, components, and helpers
├── core/data/          # Data handling (repository, API, database)
│   ├── api/            # (Recommended) Separate module if you have multiple services
├── core/navigation/    # Navigation handling module
├── feature/home/       # Home feature module
├── feature/info/       # Info feature module
├── build-logic/        # Gradle convention plugins
```

## Prerequisites
- Android Studio Quail
- JDK 21+
- Gradle 9.5.1

## Available Technologies in the Template
- Hilt (Dependency Injection)
- Type Safe Jetpack Navigation
- Coroutine & Flow
- Retrofit & Moshi
- DataStore
- Room Database
