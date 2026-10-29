# Mapu Backlog

A place to simply organise the ongoing and future work.

## MVP

The goal is a working app where a signed-in user picks a city and sees a map with my suggested places.

### Project set up
- [x] Restructure packages by feature (feature/<name>/data, domain, presentation and di) with shared code in core

- [x] Colour theme and typography

### Launch screen
- [x] Launch screen and VM - doesn't do anything for now

- [x] Set up Navigation graph

### Authentication

The login screen, it has a viewModel, a view and a repository. Users sign in with their Google account through Credential Manager, and the Google token is used to sign in to Firebase Auth. Firebase keeps the session, so returning users go straight to the Home screen.

- [x] Firebase set up - Firebase project, google-services.json and Firebase Auth. API key restricted to the Android app.

- [x] Login repository - signs in to Firebase with the Google token and checks if the user is logged in, through an AuthDataSource.

- [x] Hilt set up - inject the Login repository into the viewModels.

- [x] Login viewModel - sealed LoginUiState (Idle, Loading, LoginSuccess and Error) based on the Google and Firebase sign in results.

- [x] Login screen UI - Google sign in button, loading indicator and navigation to Home after login. Styled to match the design mock-ups.

- [x] Handle login errors - no Google account, sign in failed, Google Play services needs an update (dialog that opens the Play Store) and device not supported.

- [x] Accessibility - reviewed against WCAG AA (contrast, headings, screen reader announcements, scrolling with large text).

- [x] Portuguese translations

### Home screen
- [ ] Home - bottom navigation bar with 2 tabs: Guides and Profile. Navigating to a guide hides the bottom bar.

### Guides tab

A list of the available guides. It has a ViewModel, a guides repository (fetched from a local JSON file in the MVP) and a view.

- [ ] Guide model and repository - `Guide` (id, city, country, region, tagline, mapId, featured) read from `assets/guides.json`. Returns either the guides list or an error.

- [ ] ViewModel - sealed state for Loading, Loaded, Empty and Error, based on the repository result.

- [ ] Guides list UI - greeting, featured guide card and the list of all guides, following the mock-up.

- [ ] Guides list fetch error - error message with a retry button.

- [ ] Navigation to Guide details - passes only the guide id in the route.

### Guide details screen

Shows the city map with my suggested places. It has a ViewModel, a view and a repository that reads the guide's My Maps KML.

How the map works: I create one map per city in Google My Maps, shared as "anyone with the link", with one layer per category (Restaurants, Museums, Parks, ...). The app downloads the map as KML, turns it into its own `Place` models and draws the pins natively with MapLibre on an OpenFreeMap base map. This is free, needs no API key or billing account, and keeps the screens independent of where the data comes from.

- [ ] Map set up - add MapLibre and show the OpenFreeMap base map (Positron style) centred on the city, with the OpenStreetMap attribution.

- [ ] KML data source and parser - download `https://www.google.com/maps/d/kml?mid=<mapId>&forcekml=1` and parse each placemark into a `Place` (id, name, description, category, location, photo urls). The layer name gives the category. Unit tests with a sample KML file.

- [ ] Guide details repository and ViewModel - sealed state for Loading, Loaded and Error.

- [ ] Pins by category - custom pin icons in the app palette, e.g. a fork for restaurants, a monument for museums and a tree for parks.

- [ ] Place details - tapping a pin opens a bottom sheet with the name, description and photos.

- [ ] Open in Google Maps - button in the place bottom sheet that opens the place in Google Maps for directions (free, uses a link).

- [ ] Error state - message with a retry button when the map data can't be loaded.

### Profile tab
- [ ] Display user information - name, email and photo from the Firebase user.

- [ ] Sign out - signs out and goes back to the Login screen.

### Content
- [ ] Create the My Maps guides - at least a couple of cities, with layers per category, descriptions and photos. Add them to `guides.json`.

---

## Future Features

Ideas for after the MVP. Still to be defined.

- [x] Integrate Google Sign-In
- [ ] My maps tab - a third tab in the bottom bar with the user's saved guides, like favourites. Saved guides are stored per user in Firestore.
- [ ] Map styling - customise the OpenFreeMap style with the Vino palette, with a matching dark mode style.
- [ ] Guides from Firestore - move `guides.json` to Firestore, readable only by signed-in users, so the list can change without a new app release.
- [ ] Region filters - All, Europe, Americas and Asia chips on the Guides tab.
- [ ] Search guides
- [ ] Offline support - cache the guides and places, and look into Protomaps to store the map tiles offline.
- [ ] Custom maps - users copy pins from my guides into their own maps (e.g. "My London"). Stored per user in Firestore and drawn with the same map screen. Google My Maps has no API, so they can't be saved to the user's own My Maps.
- [ ] Paid guides - sell access per guide. Needs the places to move from public My Maps to Firestore, with security rules that only let users who bought a guide read it.
- [ ] Network error handling

---

## Bugs

- [ ] Login screen sometimes keeps loading after skipping Google's add account screen. Couldn't reproduce it yet, needs investigating.

---

## Technical Debt

- [x] Add Hilt for dependency injection
- [ ] The My Maps KML url isn't an official API and could change. If it stops working, bundle the KML files in the app or move the places to Firestore (only the data source changes).
- [ ] OpenFreeMap has no uptime guarantee. If it becomes a problem, self host the tiles (OpenFreeMap or Protomaps) or move to a paid provider by changing the style url.
