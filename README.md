# Mapu Android
Mapu (Map for You) is a travel guide application that helps users discover curated city guides built on top of Google Maps. Each guide contains recommendations for attractions, restaurants, cafés and other points of interest, allowing travellers to explore destinations through interactive, custom maps. 
 
## MVP scope

### Features
- Sign In screen integrated with Google Sign In
- Guides tab to browse available guide maps
- Open guide details and display a map with suggested places, with pins by category and place details
- Profile tab
- Support English and Portuguese localisation
- Support Dark mode
- Accessibility

## Technical Approach

- Kotlin-first development 
- Jetpack Compose
- Material 3
- MVVM + reactive and clean architecture
- Repository Pattern
- Hilt Dependency Injection
- Unit Tests
- Modular, scalable architecture
- Google Sign In for login
- MapLibre with OpenFreeMap for the maps, with places from Google My Maps (KML)
- Language support (Portuguese and English Localization)
- Build with Accessibility in mind (screen reader and design requirements)

## Design

Simple and elegant, with an editorial travel-guide feel. Theme lives in `core/designsystem/theme`.

### Colour palette (Vino)

| Role | Light | Dark | Use |
|---|---|---|---|
| Primary | Marsala `#7A2E3A` | `#E3A48F` | Logo, main actions, featured content |
| Secondary | Deep green `#2F4A3C` | `#A9CDB6` | Nature and map cues, selected states |
| Tertiary | Burnt orange `#B5541C` | `#F0A473` | Pins and highlights |
| Accent | Ochre `#B8953A` | `#D9BC6E` | Small badges only (not for text) |
| Background | `#FAF6F1` | `#171213` | Warm off-white / warm near-black |

Containers use warm clay and sand tints rather than pink, so the palette stays neutral. Dynamic colour is off so the brand palette always shows.

### Typography

- **Fraunces** (serif) for display, headline and large titles
- **Manrope** (sans-serif) for titles, body and labels

Both are bundled as variable fonts in `res/font` (SIL Open Font License).

### Mock-ups

| Login | Guides | Profile |
|---|---|---|
| <img src="documentation/design/login-light.png" width="240" alt="Login screen, light"> | <img src="documentation/design/guides-light.png" width="240" alt="Guides screen, light"> | <img src="documentation/design/profile-light.png" width="240" alt="Profile screen, light"> |
| <img src="documentation/design/login-dark.png" width="240" alt="Login screen, dark"> | <img src="documentation/design/guides-dark.png" width="240" alt="Guides screen, dark"> | <img src="documentation/design/profile-dark.png" width="240" alt="Profile screen, dark"> |

#### Login errors and alerts

Errors appear in a card above the Sign in button, so screen readers announce them before the retry action. When Google Play services is missing, the button is hidden because retrying can't help. When it needs updating, a dialog asks the user to update it.

| No Google account | Sign-in failed | Update Play services | Play services unavailable |
|---|---|---|---|
| <img src="documentation/design/login-no-account-light.png" width="180" alt="Login, no Google account error, light"> | <img src="documentation/design/login-failed-light.png" width="180" alt="Login, sign-in failed error, light"> | <img src="documentation/design/login-update-light.png" width="180" alt="Login, update Google Play services dialog, light"> | <img src="documentation/design/login-unavailable-light.png" width="180" alt="Login, Google Play services unavailable error, light"> |
| <img src="documentation/design/login-no-account-dark.png" width="180" alt="Login, no Google account error, dark"> | <img src="documentation/design/login-failed-dark.png" width="180" alt="Login, sign-in failed error, dark"> | <img src="documentation/design/login-update-dark.png" width="180" alt="Login, update Google Play services dialog, dark"> | <img src="documentation/design/login-unavailable-dark.png" width="180" alt="Login, Google Play services unavailable error, dark"> |

## Maps

Each city guide is a map I create in Google My Maps, with one layer per category (Restaurants, Museums, Parks, ...). The app downloads the map as KML, turns each pin into a `Place` and draws it natively with [MapLibre](https://maplibre.org) on an [OpenFreeMap](https://openfreemap.org) base map.

Why this approach:
- **Easy to edit** - places are managed in My Maps, so updating a guide doesn't need a new app release.
- **Native experience** - custom pins and colours, dark mode, accessibility and a bottom sheet with place details, which an embedded My Maps web view can't offer.
- **Free** - OpenFreeMap needs no API key or billing account. The Google Maps SDK needs a billing account, even though its native maps are free.
- **Easy to change** - screens only use the app's own `Guide` and `Place` models. Moving the places to a backend or changing the map provider only changes the data source or the map composable.

In the MVP, the guides list comes from a local JSON file, with the city details and the My Maps id for each guide.

## Architecture

<img width="800" height="500" alt="Mapu Flow Diagram" src="https://github.com/user-attachments/assets/4f6d2481-abc1-423e-814f-41b886f29859" />

## Roadmap

Ideas for after the MVP. Still to be defined.

- **My maps tab** - a list of the guides the user saved, like favourites, stored per user in Firestore.
- **Map styling** - customise the map style with the app colour palette, in light and dark mode.
- **Guides from Firestore** - move the guides list to Firestore, only readable by signed-in users, so it can change without a new release.
- **Custom maps** - users copy pins from the guides into their own maps (e.g. "My London"), which are saved in Firestore and drawn with the same map screen. Google My Maps has no API, so they can't be saved to the user's own My Maps account. Users can only copy places from the existing guides, not search for new ones, since Google charges for Places search.
- **Paid guides** - sell access per guide. This means moving the places from public My Maps to Firestore, with security rules so only users who bought a guide can read it.
- **Offline mode** - cache the guides and places, and store the map tiles offline (e.g. with Protomaps).
