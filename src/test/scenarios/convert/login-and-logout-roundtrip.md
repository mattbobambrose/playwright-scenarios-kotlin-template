# Login And Logout Roundtrip

> Converted from `authenticated-flows-test-spec.md` by `/doc-to-scenarios`. Review before feeding into `/scenario-to-tests`.

**URL:** /login

Log in as the demo user, verify the authenticated header swap, then log out and verify the anonymous header is restored. Test 1 of this scenario is also referenced as a `**Prerequisite:**` by every other authenticated scenario, so it must remain the canonical login flow.

**Assert throughout:** No JavaScript console errors.

## Test 1: Login with valid credentials lands on `/` with the authenticated header

- **Action:** Open `/login`.
- **Expected:** The 'Log in' heading is visible inside the login section (`data-testid="login-section"`).
- **Action:** Enter 'demo' into the 'Username' field (`data-testid="input-username"`).
- **Action:** Enter 'demo' into the 'Password' field (`data-testid="input-password"`).
- **Action:** Click the 'Log in' submit button (`data-testid="login-submit"`).
- **Expected:** The URL becomes `/`.
- **Expected:** The 'My Shelves' link (`data-testid="nav-shelves"`) is visible in the header.
- **Expected:** The 'Orders' link (`data-testid="nav-orders"`) is visible in the header.
- **Expected:** The greeting (`data-testid="nav-user"`) reads 'Hi, demo'.
- **Expected:** The 'Log out' button (`data-testid="nav-logout"`) is visible in the header.
- **Expected:** The 'Log in' link (`data-testid="nav-login"`) is not present in the header.

## Test 2: Logout returns the user to the anonymous header and re-protects `/shelves`

- **Action:** Open `/login`.
- **Action:** Enter 'demo' into the 'Username' field (`data-testid="input-username"`).
- **Action:** Enter 'demo' into the 'Password' field (`data-testid="input-password"`).
- **Action:** Click the 'Log in' submit button (`data-testid="login-submit"`).
- **Action:** Click the 'Log out' button (`data-testid="nav-logout"`).
- **Expected:** The 'Log in' link (`data-testid="nav-login"`) is visible in the header.
- **Expected:** The 'My Shelves' link (`data-testid="nav-shelves"`) is not present in the header.
- **Expected:** The 'Orders' link (`data-testid="nav-orders"`) is not present in the header.
- **Expected:** The greeting (`data-testid="nav-user"`) is not present in the header.
- **Expected:** The 'Log out' button (`data-testid="nav-logout"`) is not present in the header.
- **Action:** Open `/shelves`.
- **Expected:** The URL ends at `/login` (with an optional `next=/shelves` query parameter).
