# Checkout — User Story

> Sample **user story document** for the bookstore demo's checkout flow, written
> to the conventions in [`TEST_DOC_GUIDE.md`](https://github.com/mattbobambrose/playwright-scenarios/blob/master/plugins/playwright-scenarios/TEST_DOC_GUIDE.md).
> Feed it to `/doc-to-scenarios` to generate `convert/` scenarios, then run
> `/review-scenario convert` and `/scenario-to-tests convert`. Its sibling,
> `checkout-test-spec.md`, covers the same checkout page exhaustively — element
> by element — whereas this document frames the flow as one user story with
> acceptance criteria. Replace both with your own documents once you've seen the
> pipeline work.

## User story

As a shopper who has added books to my cart, I want to enter my shipping and
payment details and place my order, so that I receive a confirmation number I
can reference later.

## Acceptance criteria

- **AC1** — Submitting the checkout form with valid shipping and payment details
  places the order and shows a confirmation page with an order number.
- **AC2** — Submitting the form with fields left blank keeps the shopper on the
  checkout page and shows a required-field message under each blank field.
- **AC3** — Submitting the form with payment details that are present but
  malformed keeps the shopper on the checkout page and explains what is wrong.

Each test below verifies one acceptance criterion.

## Scope

- In scope: the `/checkout` page and the order-confirmation page, exercised as
  the three behaviors in the acceptance criteria above. One shopper persona,
  desktop viewport.
- Out of scope: the catalog and cart steps that lead into checkout (covered by
  the `crawl/` and `record/` example scenarios), authenticated checkout, the
  browser's native email-format validation (see Architecture notes), mobile
  layouts, performance, accessibility, and payment-processor edge cases such as
  declined cards or 3-D Secure.

## Test data

The **Alex Rivera** fixture — one shopper, used by every test below:

| Field            | Value                            |
|------------------|----------------------------------|
| Full name        | Alex Rivera                      |
| Email            | alex.rivera@example.com          |
| Shipping address | 123 Main St, Portland, OR 97201  |
| Card number      | 4242424242424242                 |
| Expiry           | 12/30                            |
| CVC              | 123                              |

Reusing this persona across runs is safe: the demo assigns order numbers
sequentially on the server and places no uniqueness constraint on the shopper,
so there is no need for a unique email suffix or a cleanup step (guide Rule 14).

## Architecture notes

All selectors and message text below were verified against the running demo at
`http://localhost:8080`.

- **Cart precondition.** `/checkout` requires a non-empty cart. Visiting it with
  an empty cart redirects (HTTP 303) to `/cart`. Every test starts from a cart
  containing one copy of *A Quiet Revolution* (`/books/5`). To create that
  state: open `/books/5` and click the 'Add to Cart' button
  (`data-testid="add-to-cart"`), which lands on `/cart` with the book in it.
- **No iframes.** The card number, expiry, and CVC fields are plain inputs
  rendered directly on the checkout page — there is no Stripe or PayPal iframe.
- **Validation is server-side and per-field.** Clicking 'Place order' with a
  missing or malformed field submits the form; the server responds with
  HTTP 400 and re-renders `/checkout` with inline error messages. Only the
  fields that actually fail are flagged.
- **A failed submit logs a console error.** Because a failed validation is an
  HTTP 400 response, the browser records a "Failed to load resource: the server
  responded with a status of 400" console error. A flow-wide "no console
  errors" assertion therefore holds only on the happy path (Test 1); do **not**
  add one to Tests 2 and 3.
- **Email uses native browser validation.** The Email field is `type="email"`,
  so a malformed email is caught by the browser before the form submits — no
  server round-trip and no app-rendered message. The app's own error messages
  cover the empty/required case only.
- **The confirmation number is not deterministic.** A successful order performs
  a same-tab navigation to `/checkout/confirmation/ORD-####`, where `####`
  increments with every order placed. Assert the *pattern* (`ORD-` followed by
  digits), never a literal number.
- **Validation messages have no `data-testid`.** Each inline error renders as a
  `<div class="text-red-600">` element directly below its field; assert these
  by their visible text.

## Test 1: AC1 — a shopper places an order with valid details

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

Throughout this entire test, there should be no JavaScript console errors.

- Action: Open `/checkout`.
- Action: Enter 'Alex Rivera' into the 'Full name' field
  (`data-testid="input-name"`).
- Action: Enter 'alex.rivera@example.com' into the 'Email' field
  (`data-testid="input-email"`).
- Action: Enter '123 Main St, Portland, OR 97201' into the 'Shipping address'
  field (`data-testid="input-address"`).
- Action: Enter '4242424242424242' into the 'Card number' field
  (`data-testid="input-cardNumber"`).
- Action: Enter '12/30' into the 'Expiry (MM/YY)' field
  (`data-testid="input-cardExpiry"`).
- Action: Enter '123' into the 'CVC' field (`data-testid="input-cardCvc"`).
- Action: Click the 'Place order' button (`data-testid="place-order"`).
- Expected: A same-tab navigation (full page load) occurs to a URL matching
  `/checkout/confirmation/ORD-` followed by one or more digits.
- Expected: The heading 'Thank you for your order!' (`h1`) is visible.
- Expected: A line matching the pattern 'Your confirmation number is ORD-'
  followed by one or more digits is visible.
- Expected: The confirmation panel (`data-testid="confirmation"`) lists
  'A Quiet Revolution × 1' and the total '$16.99'.
- Expected: The 'Cart' link in the header shows a count of 0.

## Test 2: AC2 — an incomplete form guides the shopper to the blank fields

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

- Action: Open `/checkout`.
- Action: Without filling any field, click the 'Place order' button
  (`data-testid="place-order"`).
- Expected: The page stays on `/checkout` (no navigation to a confirmation URL).
- Expected: A red error reading 'Please enter your full name.' appears below the
  'Full name' field.
- Expected: A red error reading 'Email is required.' appears below the 'Email'
  field.
- Expected: A red error reading 'Shipping address is required.' appears below
  the 'Shipping address' field.
- Expected: A red error reading 'Card number is required.' appears below the
  'Card number' field.
- Expected: A red error reading 'Expiry is required.' appears below the
  'Expiry (MM/YY)' field.
- Expected: A red error reading 'CVC is required.' appears below the 'CVC' field.

## Test 3: AC3 — malformed payment details are caught before the order is placed

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

- Action: Open `/checkout`.
- Action: Fill every field with the Alex Rivera fixture values, except enter
  '1234' into the 'Card number' field (`data-testid="input-cardNumber"`).
- Action: Click the 'Place order' button (`data-testid="place-order"`).
- Expected: The page stays on `/checkout` (no navigation to a confirmation URL).
- Expected: A red error reading 'Card number must be 13–19 digits.' appears
  below the 'Card number' field.
