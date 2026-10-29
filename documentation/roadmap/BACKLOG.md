# Mapu Backlog

A place to simply organise the ongoing and future work.

## Next Up

### Launch screen
- [x] Launch screen and VM - doesn't do anything for now

- [x] Set up Navigation graph

### Authentication

The login screen, it will have a viewModel, a view and a repository. Initially it will fetch a boolean value locally and then navigate to the home screen if loggedIn is true or display an error if false.

- [ ] Login repository - saves and fetches a boolean value from DataSource (for now).

- [ ] Login viewModel - fetches from the Login repository and sets the view state.

- [ ] Login screen UI - a simple login screen that has a login success boolean flag and a login button.

- [ ] Handle login errors - if login failure display an error message

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

- [ ] Integrate Google Sign-In
- [ ] My Guides feature - a new tab in the Home screen where the saved guides will be shown.
- [ ] Implement Google Maps SDK
- [ ] Offline support
- [ ] Save favourite guides to the My Guides list
- [ ] Search guides
- [ ] Network error handling

---

## Bugs

- None

---

## Technical Debt

- [ ] Add Hilt for dependency injection later on