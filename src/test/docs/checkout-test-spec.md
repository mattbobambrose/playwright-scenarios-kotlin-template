# Checkout — Test Specification

> Sample **test specification** for the bookstore demo's checkout flow, written
> to the conventions in [`TEST_DOC_GUIDE.md`](https://github.com/mattbobambrose/playwright-scenarios/blob/master/plugins/playwright-scenarios/TEST_DOC_GUIDE.md).
> Feed it to `/doc-to-scenarios` to generate `convert/` scenarios, then run
> `/review-scenario convert` and `/scenario-to-tests convert`. Its sibling,
> `checkout-user-story.md`, frames the same flow as one user story with
> acceptance criteria — whereas this document covers the checkout page
> exhaustively: every required element, every interaction, and the expected
> outcome of each. Replace both with your own documents once you've seen the
> pipeline work.

## Scope

- In scope: the `/checkout` page (the shipping and payment form plus the
  order-summary panel) and the order-confirmation page — every element that
  must render, every interaction a shopper can perform, and the outcome of each.
  One shopper persona, desktop viewport.
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
  Payment-field selectors target the page directly.
- **Validation is server-side and per-field.** Clicking 'Place order' with a
  missing or malformed field submits the form; the server responds with
  HTTP 400 and re-renders `/checkout` with inline error messages. Only the
  fields that actually fail are flagged.
- **A failed submit logs a console error.** Because a failed validation is an
  HTTP 400 response, the browser records a "Failed to load resource: the server
  responded with a status of 400" console error. A flow-wide "no console
  errors" assertion therefore holds only on the happy path (Test 2); do **not**
  add one to the validation tests (Tests 3-7).
- **Email uses native browser validation.** The Email field is `type="email"`,
  so a malformed email is caught by the browser before the form submits — no
  server round-trip and no app-rendered message. The app's own error messages
  cover the empty/required case only, which is why there is no "invalid email
  format" test here.
- **The confirmation number is not deterministic.** A successful order performs
  a same-tab navigation to `/checkout/confirmation/ORD-####`, where `####`
  increments with every order placed. Assert the *pattern* (`ORD-` followed by
  digits), never a literal number.
- **Validation messages have no `data-testid`.** Each inline error renders as a
  `<div class="text-red-600">` element directly below its field; assert these
  by their visible text.

## Test 1: The checkout page renders every required element

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

- Action: Open `/checkout`.
- Expected: The 'Checkout' heading (`h1`) is visible.
- Expected: A 'Full name' label and its field (`data-testid="input-name"`) are
  visible.
- Expected: An 'Email' label and its field (`data-testid="input-email"`) are
  visible.
- Expected: A 'Shipping address' label and its field
  (`data-testid="input-address"`) are visible.
- Expected: The 'Payment' heading (`h2`) is visible.
- Expected: A 'Card number' label and its field
  (`data-testid="input-cardNumber"`) are visible.
- Expected: An 'Expiry (MM/YY)' label and its field
  (`data-testid="input-cardExpiry"`) are visible.
- Expected: A 'CVC' label and its field (`data-testid="input-cardCvc"`) are
  visible.
- Expected: The 'Place order' button (`data-testid="place-order"`) is visible.
- Expected: The order-summary panel (`data-testid="checkout-summary"`) is
  visible and contains the 'Order summary' heading (`h2`).
- Expected: The order-summary panel shows the line item 'A Quiet Revolution × 1'
  and the line price '$16.99'.
- Expected: The order total (`data-testid="checkout-total"`) is visible and
  reads '$16.99'.
- Expected: No validation error message is visible on the freshly loaded page.

## Test 2: Placing an order with valid details reaches the confirmation page

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

## Test 3: Submitting an empty form flags every required field

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

## Test 4: Omitting a single field flags only that field

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

- Action: Open `/checkout`.
- Action: Enter 'Alex Rivera' into the 'Full name' field
  (`data-testid="input-name"`).
- Action: Leave the 'Email' field (`data-testid="input-email"`) empty.
- Action: Enter '123 Main St, Portland, OR 97201' into the 'Shipping address'
  field (`data-testid="input-address"`).
- Action: Enter '4242424242424242' into the 'Card number' field
  (`data-testid="input-cardNumber"`).
- Action: Enter '12/30' into the 'Expiry (MM/YY)' field
  (`data-testid="input-cardExpiry"`).
- Action: Enter '123' into the 'CVC' field (`data-testid="input-cardCvc"`).
- Action: Click the 'Place order' button (`data-testid="place-order"`).
- Expected: The page stays on `/checkout`.
- Expected: A red error reading 'Email is required.' appears below the 'Email'
  field.
- Expected: No error message appears below the 'Full name', 'Shipping address',
  'Card number', 'Expiry (MM/YY)', or 'CVC' fields.

## Test 5: A card number outside the 13-19 digit range is rejected

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

- Action: Open `/checkout`.
- Action: Fill every field with the Alex Rivera fixture values, except enter
  '1234' into the 'Card number' field (`data-testid="input-cardNumber"`).
- Action: Click the 'Place order' button (`data-testid="place-order"`).
- Expected: The page stays on `/checkout`.
- Expected: A red error reading 'Card number must be 13–19 digits.' appears
  below the 'Card number' field.

## Test 6: An invalid expiry is rejected

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

- Action: Open `/checkout`.
- Action: Fill every field with the Alex Rivera fixture values, except enter
  '99/99' into the 'Expiry (MM/YY)' field (`data-testid="input-cardExpiry"`).
- Action: Click the 'Place order' button (`data-testid="place-order"`).
- Expected: The page stays on `/checkout`.
- Expected: A red error reading 'Use MM/YY format.' appears below the
  'Expiry (MM/YY)' field.

## Test 7: A CVC that is not 3-4 digits is rejected

Preconditions:

- The cart contains one copy of *A Quiet Revolution* (see Architecture notes for
  the setup steps).

- Action: Open `/checkout`.
- Action: Fill every field with the Alex Rivera fixture values, except enter
  '1' into the 'CVC' field (`data-testid="input-cardCvc"`).
- Action: Click the 'Place order' button (`data-testid="place-order"`).
- Expected: The page stays on `/checkout`.
- Expected: A red error reading 'CVC must be 3 or 4 digits.' appears below the
  'CVC' field.
