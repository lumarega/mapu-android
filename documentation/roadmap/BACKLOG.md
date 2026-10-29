# Mapu Backlog

A place to simply organise the ongoing and future work.

## Next Up

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
- [ ] Home - Create a Home screen that will have 2 tabs: Guides and Profile

### Guides tab

Guides screen - a list of the available guides. It will have a ViewModel, a guides list repository (initially fetched from a local JSON file) and a view.

- [ ] ViewModel set up - with a view state flow for Loading, Loaded, Empty and Error.

- [ ] View set up - a compose view that renders according to the flow state from the viewModel. Initially with 4 simple one line text composables for Loading, Loaded, Empty and Error.

- [ ] Guides repository - the repository will fetch a local JSON file that has the details for all the guides available. Returns a flow with either the guides list if fetch successful or an error.

- [ ] Use the repository - update the VM so the state is based on the repository fetch result.

- [ ] Guides list UI 

- [ ] Guides list fetch error 


### Guides Details screen

Where the guide details will be. It will have a ViewModel, a view and a repository to fetch the data locally. There will also display an embedded google my maps.

- [ ] View Model set up - with a view state flow for Loading, Loaded, Empty and Error.

- [ ] Guide details repository - to fetch the data for the selected guide, it will come from a local JSON file for now. It will return a flow with either the data or an error.

- [ ] Guide details screen - based on the flow state either load the view details or the error screen

- [ ] Add Google My Map embedded view - in the Guide Details screen, an embedded google maps with pin locations will be displayed.


### Profile tab
- [ ] Display user information

- [ ] Sign out

---

## Future Features

- [x] Integrate Google Sign-In
- [ ] My Guides feature - a new tab in the Home screen where the saved guides will be shown.
- [ ] Implement Google Maps SDK
- [ ] Offline support
- [ ] Save favourite guides to the My Guides list
- [ ] Search guides
- [ ] Network error handling

---

## Bugs

- [ ] Login screen sometimes keeps loading after skipping Google's add account screen. Couldn't reproduce it yet, needs investigating.

---

## Technical Debt

- [x] Add Hilt for dependency injection