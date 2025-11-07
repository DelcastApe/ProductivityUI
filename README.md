# ProductivityUI (Android + Jetpack Compose)

ProductivityUI es una app minimalista de productividad pensada para usarse como **ventana flotante** en Meta Quest 3 (vía SideQuest) mientras compartes la pantalla del PC. Incluye **Quick Notes**, **To-Do List** y un **Calendar** simple, todo **persistido en local con Room** (sin internet).

<div align="center">
  <img src="docs/screenshot-1.png" alt="UI Screenshot" width="700"/>
</div>

## ✨ Características

- **Quick Notes**: crea y borra notas rápidas.
- **To-Do List**: agrega tareas, selección múltiple y marca como completadas.
- **Calendar**: selecciona día y agrega tareas por fecha.
- **Avatar**: selector de imagen local para el perfil (en memoria por ahora).
- **Persistencia local** con **Room** (SQLite).
- **UI** con **Jetpack Compose** + Material 3.
- Preparado para conectar a **Supabase** en el futuro.

## 🧱 Tech Stack

- Kotlin + Jetpack Compose (Material 3)
- Room (DAO/Entities/Database)
- ViewModel + StateFlow
- Coil (carga de imagen local)
- Gradle Kotlin DSL

## 🚀 Construir y ejecutar

**Requisitos**
- Android Studio (Giraffe/Koala o superior)
- JDK 17 (Embedded JDK de Android Studio recomendado)
- **minSdk 29**, **targetSdk 36**

**Pasos**
1. Clonar el repo:
   ```bash
   git clone https://github.com/DelcastApe/ProductivityUI.git
   cd ProductivityUI
