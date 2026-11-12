# Mapu Backlog

A place to simply organise the ongoing and future work.

## MVP

The goal is a working app where a signed-in user picks a city and sees a map with my suggested places.

### Project set up
- [x] Restructure packages by feature (feature/<name>/data, domain, presentation and di) with shared code in core

- [x] Colour theme and typography

- [ ] CI with GitHub Actions - a workflow that runs the unit tests, lint and a debug build on every push and pull request, so problems show up before they reach main.

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
- [x] Home - bottom navigation bar with 2 tabs: Guides and Profile. The bar only shows on the tabs, so it's hidden on Login and Guide details. Navigation tests with Robolectric.

### Guides tab

A list of the available guides. It has a ViewModel, a guides repository (fetched from a local JSON file in the MVP) and a view.

The `Guide` model and the guides repository live in `core`, not in the Guides feature, because the Guide details screen and the future My maps tab need them too.

- [ ] Guide model and repository - `Guide` (id, city, country, region, tagline, mapId, featured) read from `assets/guides.json`. Returns either the guides list or an error.

- [ ] ViewModel - sealed state for Loading, Loaded, Empty and Error, based on the repository result.

- [ ] Guides list UI - greeting, featured guide card and the list of all guides, following the mock-up.

- [ ] Guides list fetch error - error message with a retry button.

- [ ] Navigation to Guide details - passes only the guide id in the route.

### Guide details screen

Shows the city map with my suggested places. It has a ViewModel, a view and a repository that reads the guide's My Maps KML.

How the map works: I create one map per city in Google My Maps, shared as "anyone with the link", with one layer per category (Restaurants, Museums, Parks, ...). The app downloads the map as KML, turns it into its own `Place` models and draws the pins natively with MapLibre on an OpenFreeMap base map. This is free, needs no API key or billing account, and keeps the screens independent of where the data comes from.

Built for later: the map screen shows any list of `Place`s, not only ones from My Maps. A curated guide, a user's saved map (My maps) and a guide created with AI should all open in the same map screen, only with a different data source. To keep that possible:
- `Place` and `PlaceCategory` live in `core/model`, with general categories (restaurants, cafés, museums, parks, viewpoints, beaches, hotels, ...) that any source can map to
- the map, pins and place bottom sheet don't know about KML or My Maps
- the KML code is only one data source behind the repository

- [ ] Map set up - add MapLibre and show the OpenFreeMap base map (Positron style) centred on the city, with the OpenStreetMap attribution.

- [ ] KML data source and parser - download `https://www.google.com/maps/d/kml?mid=<mapId>&forcekml=1` and parse each placemark into a `Place` (id, name, description, category, location, photo urls). The layer name gives the category. Unit tests with a sample KML file.

- [ ] Guide details repository and ViewModel - sealed state for Loading, Loaded and Error.

- [ ] Pins by category - custom pin icons in the app palette, e.g. a fork for restaurants, a monument for museums and a tree for parks.

- [ ] Place details - tapping a pin opens a bottom sheet with the name, description and photos.

- [ ] Open in Google Maps - button in the place bottom sheet that opens the place in Google Maps for directions (free, uses a link).

- [ ] Error state - message with a retry button when the map data can't be loaded.

### Profile tab

A simpler version of the Profile mock-up for the MVP. The rest of the mock-up is in Future Features.

- [x] Display user information - name, email and photo from the Firebase user, with the user's initials when there is no photo.

- [x] Sign out - signs out of Firebase, clears the saved Google sign in and goes back to the Login screen.

- [x] App version - shown below the Sign out button.

### Content
- [ ] Create the My Maps guides - at least a couple of cities, with layers per category, descriptions and photos. Add them to `guides.json`.

---

## Future Features

Ideas for after the MVP. Still to be defined.

The plan is to finish the MVP first (Guides and the map), then Firestore and My maps, then the AI guide creator, as each step builds on the one before.

### Next: Firestore and My maps

Saving places per user is needed before the AI guide creator, as its results have to be saved somewhere.

- [x] Integrate Google Sign-In
- [ ] My maps tab - a tab in the bottom bar with the user's saved guides and maps. Stored per user in Firestore.
- [ ] Guides from Firestore - move `guides.json` to Firestore, readable only by signed-in users, so the list can change without a new app release.
- [ ] Custom maps - users copy pins from my guides into their own maps (e.g. "My London"). Stored per user in Firestore and drawn with the same map screen. Google My Maps has no API, so they can't be saved to the user's own My Maps.

### Idea in progress: AI guide creator

An idea to think about, refine and build after My maps. Nothing here is decided yet.

A new tab (e.g. "Create") where users answer a few questions about the trip they're planning, and an AI agent creates a guide tailored to them, with places to see, places to eat and places to stay. Users look at each suggestion, choose the ones they like, and those are saved as one of their maps in My maps, shown on the same map screen as the curated guides.

Possible questions (to be refined):
- Destination country and cities, or a "surprise me" option
- Number of days
- Activities they enjoy (beach, hiking, nature, museums, shopping, ...)
- Budget (e.g. $ to $$$)
- Hotel style (budget, mid range, resort, ...)
- Food style (restaurants, street food or a mix)

How it could work:
- The "agent" isn't a model trained from scratch. It's an existing AI model given instructions on how to plan a trip, a fixed format for its answers (so the app gets structured places, not free text), and tools such as web search so it suggests places that exist and are open.
- The app never calls the AI directly. A small backend checks the user is signed in, calls the AI and returns the results, so the API key isn't in the app (anyone can unpack an APK).
- AI models aren't reliable with exact locations, so the backend looks each suggested place up in a map database to get its real coordinates, and drops places it can't find.
- Creating a guide can take from 30 seconds to a couple of minutes, so the app needs a "creating your guide" screen. The backend could save the result to Firestore and the app listen for it.

Possible versions:
- [ ] v1 - questionnaire, suggestions as a list, user picks places, saved as a My map
- [ ] v2 - refine the guide by chatting with the agent (e.g. "more vegetarian places")
- [ ] v3 - day by day itinerary

Open questions:
- Billing - AI APIs charge per use (usually prepaid credit that can be capped). Firebase Cloud Functions needs a billing account, so the backend could use a free tier without a card instead (e.g. Cloudflare Workers).
- Questionnaire scope for v1.
- Which AI model, comparing quality and cost on a few test trips.
- Where to look up places for free (e.g. OpenStreetMap search) and its usage limits.
- Limits per user (e.g. a few guides a day), and whether it becomes a paid feature.

### Other ideas

- [ ] Map styling - customise the OpenFreeMap style with the Vino palette, with a matching dark mode style.
- [ ] Region filters - All, Europe, Americas and Asia chips on the Guides tab.
- [ ] Search guides
- [ ] Offline support - cache the guides and places, and look into Protomaps to store the map tiles offline.
- [ ] Paid guides - sell access per guide. Needs the places to move from public My Maps to Firestore, with security rules that only let users who bought a guide read it.
- [ ] Network error handling
- [ ] Full Profile screen - the rest of the Profile mock-up: a "Signed in with Google" label, Preferences (Language, Appearance and Accessibility) and About (Privacy policy and Terms of use).

---

## Bugs

- [ ] Login screen sometimes keeps loading after skipping Google's add account screen. Couldn't reproduce it yet, needs investigating.

---

## Technical Debt

- [x] Add Hilt for dependency injection
- [ ] The My Maps KML url isn't an official API and could change. If it stops working, bundle the KML files in the app or move the places to Firestore (only the data source changes).
- [ ] OpenFreeMap has no uptime guarantee. If it becomes a problem, self host the tiles (OpenFreeMap or Protomaps) or move to a paid provider by changing the style url.
